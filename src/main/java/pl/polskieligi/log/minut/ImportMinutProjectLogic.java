package pl.polskieligi.log.minut;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueDAO;
import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.RoundDAO;
import pl.polskieligi.dao.SeasonDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.dto.ProjectInfo;
import pl.polskieligi.model.League;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Round;
import pl.polskieligi.model.Season;
import pl.polskieligi.model.Team;
import pl.polskieligi.model.TeamLeague;

@Component
@Transactional
public class ImportMinutProjectLogic {
	private static final String TEAM_ID = "id_klub=";
	private static final String AMP = "&amp;";
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportMinutProjectLogic.class);
	
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
	LeagueMatchDAO matchDAO;
	
	@Autowired
	RoundDAO roundDAO;

	@SuppressWarnings("deprecation")
	public ProjectInfo doImport(Integer projectMinutId) {
		log.info("Importing project id = "+projectMinutId);
		ProjectInfo pi = new ProjectInfo();
		java.util.Date startDate = new java.util.Date();
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
				log.info("Project alerady loaded id = "+projectMinutId);
				pi.setProject(oldProject);
				pi.setSkipped(true);
			} else {
				log.debug("Start parsing... id = "+projectMinutId);
				Document doc = Jsoup.connect(get90minutLink(projectMinutId)).get();

				Elements titles = doc.select("title");
				Project leagueProject = null;
				for (Element title : titles) {
					String tmp = title.text();
					leagueProject = new Project();
					leagueProject.setMinut_id(projectMinutId);
					leagueProject.setName(tmp);
					int index = tmp.indexOf("/");
					if (index < 5 || tmp.length() < 12) {
						pi.addMessage("Wrong title: " + tmp);
					} else {
						String sezon = tmp.substring(index - 4, index + 5);
						year = Integer.parseInt(sezon.split("/")[0]);
						String leagueName = tmp.replaceAll(" " + sezon, "");
						League league = new League();
						league.setName(leagueName);
						league = leagueDAO.saveUpdate(league);
						log.info("League " + leagueName + " saved id = " + league.getId());
						Season season = new Season();
						season.setName(sezon);
						season = seasonDAO.saveUpdate(season);
						log.info("Season " + sezon   + " saved id = " + season.getId());
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
					log.info("Project saved " + projectId);
					pi.setProject(leagueProject);
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
					log.debug("Team "+druzyna.text()+"  saved " + t.getId());
					leagueTeams.put(t.getName(), t.getId());
					TeamLeague tl = new TeamLeague();
					tl.setProject(leagueProject);
					tl.setTeam(t);
					tl = teamLeagueDAO.saveUpdate(tl);

					log.debug("TeamLeague saved " + tl.getId());
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
							List<LeagueMatch> roundMatches = new ArrayList<LeagueMatch>();
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
										LeagueMatch roundMatch = new LeagueMatch();
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
													ts = TimestampParser.parseTimestamp(year,
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
													ts = TimestampParser.parseTimestamp(year,
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
										log.debug("Match: " + roundMatch.getMatchpart1() + " " + roundMatch.getMatchpart2());
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
									Date roundStart = TimestampParser.getRoundStart(year,
											tmp.substring(i + 2));
									if (roundStart != null) {
										matchDate = new Timestamp(
												roundStart.getTime());
									}
									round.setRound_date_first(roundStart);
									round.setRound_date_last(TimestampParser.getRoundEnd(year,
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
					}
					teamLeagueDAO.updateTeams(projectId,
							leagueTeams.values());
				}
				persProject = projectDAO.saveUpdate(persProject);
				pi.setProject(persProject);
				pi.setMatches_count(matches_count);
				pi.setRounds_count(rounds_count);
				pi.setTeams_count(teams_count);
			}
			java.util.Date endDate = new java.util.Date();
			long diff = endDate.getTime() - startDate.getTime();
			pi.setProcessingTime(diff);
			log.info("End processing id = " + projectMinutId + " time = "+ (diff/1000) +" sec");
		} catch ( org.jsoup.HttpStatusException e){
			pi.addMessage(e.getMessage()+" "+e.getUrl());
		} catch (Exception e) {
			pi.addMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			session.flush();
			session.close();
		}
		return pi;
	}

	private String get90minutLink(Integer projectMinutId){
		String index = "0";
		if(projectMinutId>=10000){
			index = "1";
		}
		return MINUT_URL+"/liga/"+index+"/liga" + projectMinutId + ".html";
	}
}
