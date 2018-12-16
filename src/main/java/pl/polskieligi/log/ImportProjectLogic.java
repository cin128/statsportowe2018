package pl.polskieligi.log;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueDAO;
import pl.polskieligi.dao.MatchDAO;
import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.RoundDAO;
import pl.polskieligi.dao.SeasonDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.model.League;
import pl.polskieligi.model.Match;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Round;
import pl.polskieligi.model.Season;
import pl.polskieligi.model.Team;
import pl.polskieligi.model.TeamLeague;

@Component
@Transactional
public class ImportProjectLogic {
	private static final String TEAM_ID = "id_klub=";
	private static final String AMP = "&amp;";
	private static final Map<String, Integer> months;
	static {
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		aMap.put("stycznia", 0);
		aMap.put("lutego", 1);
		aMap.put("marca", 2);
		aMap.put("kwietnia", 3);
		aMap.put("maja", 4);
		aMap.put("czerwca", 5);
		aMap.put("lipca", 6);
		aMap.put("sierpnia", 7);
		aMap.put("września", 8);
		aMap.put("października", 9);
		aMap.put("listopada", 10);
		aMap.put("grudnia", 11);
		months = Collections.unmodifiableMap(aMap);
	}

	final static Logger log = Logger.getLogger(ImportProjectLogic.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	LeagueDAO leagueDAO;
	
	@Autowired
	SeasonDAO seasonDAO;
	
	@Autowired
	TeamDAO teamDAO;
	
	@Autowired
	TeamLeagueDAO teamLeagueDAO;
	
	@Autowired
	MatchDAO matchDAO;
	
	@Autowired
	RoundDAO roundDAO;
	
	
	// public static void main(String[] args) {
	// System.out.println(parseTimestamp(2012, "21 lipca, 18:00"));
	// System.out.println(parseTimestamp(2012, "16 lutego, 15:30"));
	// System.out.println(Integer.parseInt("22 187".replace(" ", "")));
	// System.out.println("22 187".split("-")[0]);
	// System.out.println(getRoundStart(2012, "22-23 marca"));
	// System.out.println(getRoundEnd(2012, "22-23 marca"));
	// System.out.println(getRoundStart(2012, "30 listopada-1 grudnia"));
	// System.out.println(getRoundEnd(2012, "30 listopada-1 grudnia"));
	// }
	@SuppressWarnings("deprecation")
	public String doImport(Integer projectMinutId) {
		log.info("Importing project"+projectMinutId);
		java.util.Date startDate = new java.util.Date();
		StringBuilder report = new StringBuilder();
		Long projectId = null;
		Integer year = null;
		Map<String, Long> leagueTeams = new HashMap<String, Long>();
		Session session = sessionFactory.openSession();
		try {
			int matches_count = 0;
			int rounds_count = 0;
			int teams_count = 0;
			Project oldProject = projectDAO.retrieveProjectByMinut(projectMinutId);
			if (oldProject != null
					&& (oldProject.getArchive() && oldProject.getPublished() || oldProject
							.getType() == Project.OTHER)) {
				log.info("Project alerady loaded "+projectMinutId);		
				report.append("<br/>Project id = " + oldProject.getId());
				report.append("<br/>Project archive");
			} else
			{
				log.info("Start parsing... "+projectMinutId);
				Document doc = Jsoup.connect(
						"http://www.90minut.pl/liga/0/liga" + projectMinutId
								+ ".html").get();

				Elements titles = doc.select("title");
				Project leagueProject = null;
				for (Element title : titles) {
					String tmp = title.text();
					leagueProject = new Project();
					leagueProject.setMinut_id(projectMinutId);
					leagueProject.setName(tmp);
					int index = tmp.indexOf("/");
					if (index < 5 || tmp.length() < 12) {
						report.append("<br/>Wrong title: " + tmp);
					} else {
						String sezon = tmp.substring(index - 4, index + 5);
						year = Integer.parseInt(sezon.split("/")[0]);
						String leagueName = tmp.replaceAll(" " + sezon, "");
						report.append("<br/>" + leagueName + " - " + sezon);
						League league = new League();
						league.setName(leagueName);
						league = leagueDAO.saveUpdate(league);
						log.info("League " + leagueName + " saved " + league.getId());
						Season season = new Season();
						season.setName(sezon);
						season = seasonDAO.saveUpdate(season);
						log.info("Season " + sezon   + " saved " + season.getId());
						leagueProject.setLeague(league);
						leagueProject.setSeason(season);
						GregorianCalendar cal = new GregorianCalendar(year, 6,
								1);
						Date start_date = new Date(cal.getTimeInMillis());
						leagueProject.setStart_date(start_date);
						boolean archive = false;

						if (startDate.getYear() > start_date.getYear()) {
							if (startDate.getYear() == start_date.getYear() + 1) {
								if (startDate.getMonth() > 8) {
									archive = true;
								}
							} else {
								archive = true;
							}
						}
						leagueProject.setArchive(archive);
					}
					leagueProject = projectDAO.saveUpdate(leagueProject);
					projectId = leagueProject.getId();
					log.info("Project  saved " + projectId);
				}
				if (projectId == null) {
					throw new IllegalStateException("projecId==null!!!");
				}
				Elements druzyny = doc
						.select("a[href~=/skarb.php\\?id_klub=*]");
				for (Element druzyna : druzyny) {
					String tmp = druzyna.toString();
					int start = tmp.indexOf(TEAM_ID) + TEAM_ID.length();
					int end = tmp.indexOf(AMP);
					if (start < 0 || end < 0 || start >= end) {
						continue;
					}
					String teamId = tmp.substring(start, end);
					Team t = new Team();
					t.setName(druzyna.text());
					t.setMinut_id(Integer.parseInt(teamId));
					t = teamDAO.saveUpdate(t);
					log.info("Team "+druzyna.text()+"  saved " + t.getId());
					leagueTeams.put(t.getName(), t.getId());
					TeamLeague tl = new TeamLeague();
					tl.setProject(leagueProject);
					tl.setTeam(t);
					tl = teamLeagueDAO.saveUpdate(tl);
					log.info("TeamLeague saved " + tl.getId());
					teams_count++;					
				}
				Project persProject = projectDAO.retrieveProjectByMinut(projectMinutId);
				if (leagueTeams.size() == 0) {
					persProject.setType(Project.OTHER);
				} else {
					persProject.setType(Project.REGULAR_LEAGUE);
				}

				if (leagueTeams.size() > 0) {
					Long round_id = null;
					Integer round_matchcode = 0;
					Elements kolejki = doc
							.select("table[class=main][width=600][border=0][cellspacing=0][cellpadding][align=center]");
					for (Element kolejka : kolejki) {
						Timestamp matchDate = new Timestamp(persProject
								.getStart_date().getTime());
						Elements matches = kolejka.select("tr[align=left]");
						if (matches.size() > 0) {
							List<Match> roundMatches = new ArrayList<Match>();
							for (Element match : matches) {
								Elements teams = match
										.select("td[nowrap][valign=top][width=180]");
								Elements result = match
										.select("td[nowrap][valign=top][align=center][width=50]");
								Elements date = match
										.select("td[valign=top][nowrap][align=left][width=190]");
								if (teams.size() == 2 && result.size() == 1) {
									String t1 = teams.get(0).text();
									String t2 = teams.get(1).text();
									if (leagueTeams.containsKey(t1)
											&& leagueTeams.containsKey(t2)) {
										String resultValue = result.get(0)
												.text();
										Match roundMatch = new Match();
										roundMatch.setProject_id(projectId);
										roundMatch.setRound_id(round_id);
										roundMatch.setMatch_date(matchDate);
										roundMatch.setMatchpart1(leagueTeams
												.get(t1));
										roundMatch.setMatchpart2(leagueTeams
												.get(t2));
										if (resultValue.equals("-")) {
											roundMatch.setCount_result(false);
										} else {
											String[] results = resultValue
													.split("-");
											if (results.length == 2) {
												roundMatch
														.setCount_result(true);
												roundMatch.setPublished(true);
												roundMatch
														.setMatchpart1_result(Float
																.valueOf(results[0]));
												roundMatch
														.setMatchpart2_result(Float
																.valueOf(results[1]));
											}
										}
										if (date.size() == 1) {
											String tmp = date.get(0).text();
											if (tmp != null
													&& !tmp.trim().isEmpty()) {
												Timestamp ts = null;
												Integer crowd = 0;
												int index = tmp.indexOf("(");
												if (index == 0) {
													crowd = Integer
															.parseInt(tmp
																	.substring(
																			1,
																			tmp.length() - 1)
																	.replaceAll(
																			" ",
																			""));
												} else if (index > 0) {
													ts = parseTimestamp(year,
															tmp.substring(0,
																	index - 1));
													crowd = Integer
															.parseInt(tmp
																	.substring(
																			index + 1,
																			tmp.length() - 1)
																	.replaceAll(
																			" ",
																			""));
												} else {
													ts = parseTimestamp(year,
															tmp);
												}
												if (ts != null) {
													roundMatch
															.setMatch_date(ts);
												}
												if (crowd > 0) {
													roundMatch.setCrowd(crowd);
												}
											}
										}
										roundMatches.add(roundMatch);
									}
									Elements info = match
											.select("td[colspan=4]");
									if (info.size() > 0) {
										int size = roundMatches.size();
										if (size > 0) {
											roundMatches.get(size - 1)
													.setSummary(
															info.get(0).text());
										}
									}
								}
							}
							matches_count += matchDAO.saveUpdate(roundMatches);
						} else {
							Elements nowaKolejka = kolejka
									.select("td[colspan=3]");
							if (nowaKolejka.size() == 1) {
								String tmp = nowaKolejka.get(0).text();
								round_matchcode++;
								Round round = new Round();
								int i = tmp.indexOf("-");
								String roundName = tmp;
								if (i > 0 && tmp.length() - i > 2) {
									roundName = tmp.substring(0, i - 1);
									Date roundStart = getRoundStart(year,
											tmp.substring(i + 2));
									if (roundStart != null) {
										matchDate = new Timestamp(
												roundStart.getTime());
									}
									round.setRound_date_first(roundStart);
									round.setRound_date_last(getRoundEnd(year,
											tmp.substring(i + 2)));
								}
								round.setName(roundName.trim());
								round.setMatchcode(round_matchcode);
								round.setProject_id(projectId);
								round = roundDAO.saveUpdate(round);
								rounds_count++;
							}
						}
					}
					if (rounds_count >= (teams_count - 1) * 2
							&& matches_count == teams_count * teams_count
									- teams_count) {
						persProject.setPublished(true);
					} else {
						report.append("<br/> Ilo�� dru�yn/mecz�w jest niepoprawna = ");
					}
					teamLeagueDAO.updateTeams(projectId,
							leagueTeams.values());
				}
				projectDAO.saveUpdate(persProject);
				report.append("<br/>Project id = " + projectId);
				report.append("<br/> liczba dru�yn = " + teams_count);
				report.append("<br/> liczba mecz�w = " + matches_count);
				report.append("<br/> liczba kolejek = " + rounds_count);
			}
			java.util.Date endDate = new java.util.Date();
			long diff = endDate.getTime() - startDate.getTime();
			report.append("<br/> czas trwania = " + diff / 1000 + " sec");

		} catch ( org.jsoup.HttpStatusException e){
			report.append(e.getMessage()+" "+e.getUrl());
		} catch (Exception e) {
			report.append(e.getMessage());
			e.printStackTrace();
		} finally {
			session.flush();
			session.close();
		}
		return report.toString();
	}

	/*
	 * "21 lipca, 18:00"
	 */
	private static Timestamp parseTimestamp(Integer year, String substring) {
		if(year==null || substring==null){
			return null;
		}
		if (substring.trim().isEmpty()) {
			return null;
		}
		String[] tmp = substring.split(" ");

		Integer day = 0;
		if (tmp[0].contains("-")) {
			String[] tmp2 = tmp[0].split("-");
			day = Integer.parseInt(tmp2[0]);
		} else {
			day = Integer.parseInt(tmp[0]);
		}

		GregorianCalendar cal = null;
		if(tmp.length==2){
			Integer month = months.get(tmp[1]);
			cal = new GregorianCalendar(month > 5 ? year : year + 1, month, day);
		} else if (tmp.length == 3 && tmp[2].contains(":")) {
			Integer month = months
					.get(tmp[1].substring(0, tmp[1].length() - 1));
			String[] time = tmp[2].split(":");
			Integer hour = Integer.parseInt(time[0]);
			Integer minute = Integer.parseInt(time[1]);
			cal = new GregorianCalendar(month > 5 ? year : year + 1, month,
					day, hour, minute);
		} else if(tmp.length == 3&& tmp[1].contains("-")){
			String[] tmp2 = tmp[1].split("-");
			Integer month = months.get(tmp2[0]);
			cal = new GregorianCalendar(month > 5 ? year : year + 1, month, day);
		} else {
			return null;
		}
		return new Timestamp(cal.getTimeInMillis());
	}

	/*
	 * "22-23 marca" "30 listopada-1 grudnia"
	 */
	private static Date getRoundEnd(Integer year, String substring) {
		if(year==null || substring==null){
			return null;
		}
		int i = substring.indexOf("-");
		int day = -1;
		int month = -1;
		if (i < 0) {
			String[] tmp = substring.split(" ");
			day = Integer.parseInt(tmp[0]);
			month = months.get(tmp[1]);
		} else {
			String[] tmp = substring.split("-");
			tmp = tmp[1].split(" ");
			day = Integer.parseInt(tmp[0]);
			month = months.get(tmp[1]);
		}
		GregorianCalendar cal = new GregorianCalendar(month > 5 ? year
				: year + 1, month, day);
		return new Date(cal.getTimeInMillis());
	}

	private static Date getRoundStart(Integer year, String substring) {
		if(year==null || substring==null){
			return null;
		}
		int i = substring.indexOf("-");
		int day = -1;
		int month = -1;
		if (i < 0) {
			String[] tmp = substring.split(" ");
			day = Integer.parseInt(tmp[0]);
			month = months.get(tmp[1]);
		} else {
			String[] tmp = substring.split("-");
			if (tmp[0].length() > 3) {
				tmp = tmp[0].split(" ");
				day = Integer.parseInt(tmp[0]);
				month = months.get(tmp[1]);
			} else {
				day = Integer.parseInt(tmp[0]);
				tmp = tmp[1].split(" ");
				month = months.get(tmp[1]);
			}
		}
		GregorianCalendar cal = new GregorianCalendar(month > 5 ? year
				: year + 1, month, day);
		return new Date(cal.getTimeInMillis());
	}
}
