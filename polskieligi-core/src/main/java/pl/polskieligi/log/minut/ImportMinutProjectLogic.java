package pl.polskieligi.log.minut;

import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pl.polskieligi.dao.LeagueDAO;
import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.RoundDAO;
import pl.polskieligi.dao.SeasonDAO;
import pl.polskieligi.dao.TableDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.dto.ProjectInfo;
import pl.polskieligi.dto.TableRow;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.*;

@Component
@Transactional
public class ImportMinutProjectLogic {
	private static final String TEAM_ID = "id_klub=";
	private static final String AMP = "&amp;";

	final static Logger log = Logger.getLogger(ImportMinutProjectLogic.class);
	
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

	@Autowired
	TableDAO tableDAO;
	
	@Autowired
	ImportTeamLeaguePlayerLogic importTeamLeaguePlayerLogic;

	@Autowired
	ImportLeagueMatchLogic importLeagueMatchLogic;

	public ProjectInfo doImport(Integer projectMinutId) {
		log.info("Importing project id = " + projectMinutId);
		ProjectInfo pi = new ProjectInfo();
		java.util.Date startDate = new java.util.Date();

		try {
			Project leagueProject = projectDAO.retrieveByMinut(projectMinutId);
			if (leagueProject != null && (leagueProject.getArchive() && leagueProject.getPublished()
					|| leagueProject.getType() == Project.OTHER)) {
				log.info("Project alerady loaded id = " + projectMinutId);
				pi.setProject(leagueProject);
				pi.setSkipped(true);
			} else {
				log.debug("Start parsing... id = " + projectMinutId);

				if(leagueProject==null) {
					leagueProject = new Project();
					leagueProject.setMinut_id(projectMinutId);
				}
				
				pi.setProject(leagueProject);
				try {
					Document doc = Jsoup.connect(MinutUrlHelper.getProjectUrl(projectMinutId)).get();
					Integer year = parseProjectHeader(doc, projectMinutId, pi);
					leagueProject = pi.getProject();
					if (leagueProject == null || leagueProject.getId() == null) {
						throw new IllegalStateException("projecId==null!!!");
					}

					log.info("Processing project: " + leagueProject.getName());

					Map<String, Pair<Team, TeamLeague>> leagueTeamsPair = parseTeams(doc, leagueProject);
					Map<String, Team> leagueTeams = new HashMap<String, Team>();
					Map<Long, TeamLeague> teamLeagues = new HashMap<Long, TeamLeague>();
					for (Entry<String, Pair<Team, TeamLeague>> e : leagueTeamsPair.entrySet()) {
						leagueTeams.put(e.getKey(), e.getValue().getFirst());
						TeamLeague tl = e.getValue().getSecond();
						teamLeagues.put(tl.getTeam_id(), tl);
					}

					int teams_count = leagueTeams.size();
					pi.setTeams_count(teams_count);
					if (teams_count == 0) {
						leagueProject.setType(Project.OTHER);
					} else {
						leagueProject.setType(Project.REGULAR_LEAGUE);
					}
					List<Team> missingTeams = parseGames(doc, leagueProject, leagueTeams, teamLeagues, year, pi);
					if (teams_count == 0 && missingTeams.size() > 0) {
						updateTeamLeagues(missingTeams, leagueProject);
					}
					leagueProject = projectDAO.saveUpdate(leagueProject);
					updateStartPoints(leagueProject, teamLeagues);

					leagueProject.setImportStatus(ImportStatus.SUCCESS.getValue());

				} catch(SocketTimeoutException | NoRouteToHostException e){
					log.warn("Time out for: "+projectMinutId+" "+ e.getMessage());
					leagueProject.setImportStatus(ImportStatus.TIME_OUT.getValue());
				}
				if(leagueProject.getImportStatus()!=null && 
						(leagueProject.getImportStatus()==ImportStatus.SUCCESS.getValue() 
						|| leagueProject.getImportStatus()==ImportStatus.TIME_OUT.getValue())){
					projectDAO.update(leagueProject);
				} else {
					log.warn("Deleting: "+projectMinutId);
					projectDAO.delete(leagueProject);
				}

				pi.setProject(leagueProject);
			}
			java.util.Date endDate = new java.util.Date();
			long diff = endDate.getTime() - startDate.getTime();
			pi.setProcessingTime(diff);
			log.info("End processing id = " + projectMinutId + " time = " + (diff / 1000) + " sec");
		} catch (org.jsoup.HttpStatusException e) {
			pi.addMessage(e.getMessage() + " " + e.getUrl());
		} catch (SocketTimeoutException e) {
			pi.addMessage(e.getMessage());
			log.error(e.getMessage());
		} catch (Exception e) {
			pi.addMessage(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return pi;
	}

	@SuppressWarnings("deprecation")
	private Integer parseProjectHeader(Document doc, Integer projectMinutId, ProjectInfo pi) {
		Integer year = null;
		java.util.Date startDate = new java.util.Date();
		Elements titles = doc.select("title");
		Project leagueProject = null;
		for (Element title : titles) {
			String tmp = title.text();
			leagueProject = pi.getProject();
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
				league.setLeagueType(LeagueType.getByLeagueName(leagueName).getId());
				league.setRegion(Region.getRegionByProjectName(leagueName).getId());
				if(leagueName.contains("grupa: ")){
					league.setGroupName(leagueName.split("grupa: ")[1]);
				}
				league = leagueDAO.saveUpdate(league);
				log.debug("League " + leagueName + " saved id = " + league.getId());
				Season season = new Season();
				season.setName(sezon);
				season = seasonDAO.saveUpdate(season);
				log.debug("Season " + sezon   + " saved id = " + season.getId());
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
			log.info("Project saved " + leagueProject.getId());
			pi.setProject(leagueProject);
		}
		return year;
	}

	private Map<String, Pair<Team, TeamLeague>> parseTeams(Document doc, Project leagueProject) {
		Map<String, Pair<Team, TeamLeague>> leagueTeams = new HashMap<String, Pair<Team, TeamLeague>>();
		Elements druzyny = doc.select("a[href~=/skarb.php\\?id_klub=*]");
		for (Element druzyna : druzyny) {
			String tmp = druzyna.toString();
			String pkt = druzyna.parent().nextElementSibling().nextElementSibling().select("b").text();
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
			log.debug("Team " + druzyna.text() + "  saved " + t.getId());

			TeamLeague tl = new TeamLeague();
			tl.setProject_id(leagueProject.getId());
			tl.setTeam_id(t.getId());
			tl.setStartPoints(Integer.parseInt(pkt));
			tl = teamLeagueDAO.saveUpdate(tl);
			//importTeamLeaguePlayerLogic.doImport(tl.getId(), leagueProject.getSeason().getMinut_id(), t.getMinut_id());
			leagueTeams.put(t.getName(), Pair.of(t, tl));
			log.debug("TeamLeague saved " + tl.getId());
		}
		return leagueTeams;
	}
	
	private void updateTeamLeagues(List<Team> missingTeams, Project leagueProject) {
		for(Team team: missingTeams) {
			TeamLeague tl = new TeamLeague();
			tl.setProject_id(leagueProject.getId());
			tl.setTeam_id(team.getId());
			tl = teamLeagueDAO.saveUpdate(tl);
			log.debug("TeamLeague saved " + tl.getId());
		}
	}
	
	private List<Team> parseGames(Document doc, Project leagueProject, Map<String, Team> leagueTeams, Map<Long, TeamLeague> teamLeagues, Integer year, ProjectInfo pi) {
		List<Team> missingTeams = new ArrayList<Team>();
		int teams_count = leagueTeams.size();
		int matches_count = 0;
		int rounds_count = 0;
		Round round = null;
		Integer round_matchcode = 0;
		Elements kolejki = doc
				.select("table[class=main][width=600][border=0][cellspacing=0][cellpadding][align=center]");
		for (Element kolejka : kolejki) {
			Timestamp matchDate = new Timestamp(leagueProject
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
						checkTeam(t1, leagueTeams, missingTeams);						
						checkTeam(t2, leagueTeams, missingTeams);
						if (leagueTeams.containsKey(t1)
								&& leagueTeams.containsKey(t2)) {
							String resultValue = result.get(0)
									.text();
							LeagueMatch roundMatch = new LeagueMatch();
							roundMatch.setProject_id(leagueProject.getId());
							roundMatch.setRound(round);
							roundMatch.setMatch_date(matchDate);
							roundMatch.setMatchpart1(leagueTeams
									.get(t1));
							roundMatch.setMatchpart2(leagueTeams
									.get(t2));
							String minut_id = result.select("a[href]").attr("href");
							if(!StringUtil.isBlank(minut_id)) {
								roundMatch.setMinut_id(Integer.parseInt(minut_id.substring(18)));
							}
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
											.setMatchpart1_result(parseFloat(results[0]));
									roundMatch
											.setMatchpart2_result(parseFloat(results[1]));
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
				for(LeagueMatch lm: roundMatches) {
					matches_count++;
					LeagueMatch m = matchDAO.saveUpdate(lm);
					if(m.getCount_result() && m.getMinut_id()>0) {
						importLeagueMatchLogic.doImport(m);
					}
				}
			} else {
				Elements nowaKolejka = kolejka
						.select("td[colspan=3]>b>u");
				if (nowaKolejka.size() == 1) {
					String tmp = nowaKolejka.get(0).text();
					round_matchcode++;
					round = new Round();
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
					round.setProject_id(leagueProject.getId());
					round = roundDAO.saveUpdate(round);
					log.debug("Round saved " + round.getId());
					rounds_count++;
				}
			}
		}				
		if (rounds_count >= (teams_count - 1) * 2
				&& matches_count == teams_count * teams_count
						- teams_count) {
			leagueProject.setPublished(true);
		}
		pi.setMatches_count(matches_count);
		pi.setRounds_count(rounds_count);
		return missingTeams;
	}
	
	private void checkTeam(String teamName, Map<String, Team> leagueTeams, List<Team> missingTeams) {
		if(!StringUtils.isEmpty(teamName) && !leagueTeams.containsKey(teamName)) {
			Team team = teamDAO.retrieveTeamByName(teamName);
			if(team!=null) {
				leagueTeams.put(teamName, team);
				missingTeams.add(team);
			} else {
				log.warn("Team: " + teamName + " is missing!!!");				
			}
		}
	}
	
	private Float parseFloat(String value) {
		try {
			return Float.valueOf(value);
		} catch (NumberFormatException e) {
			log.warn("Value: " + value + " is not a number!!!");
		}
		return null;
	}

	private void updateStartPoints(Project leagueProject, Map<Long, TeamLeague> teamLeagues) {
		if (leagueProject.getType() == Project.REGULAR_LEAGUE) {
			for (TableRow row : tableDAO.getTableRowsSimple(leagueProject.getId())) {
				TeamLeague tl = teamLeagues.get(row.getTeam_id());
				if(tl==null){//drużyna wycofana z rozgrywek w trakcie sezonu
					tl = teamLeagueDAO.findByProjectAndTeam(leagueProject.getId(), row.getTeam_id());
				}

				tl.setStartPoints(tl.getStartPoints() - row.getPoints());
				teamLeagueDAO.update(tl);
			}
		}

	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setLeagueDAO(LeagueDAO leagueDAO) {
		this.leagueDAO = leagueDAO;
	}

	public void setSeasonDAO(SeasonDAO seasonDAO) {
		this.seasonDAO = seasonDAO;
	}

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}

	public void setTeamLeagueDAO(TeamLeagueDAO teamLeagueDAO) {
		this.teamLeagueDAO = teamLeagueDAO;
	}

	public void setMatchDAO(LeagueMatchDAO matchDAO) {
		this.matchDAO = matchDAO;
	}

	public void setRoundDAO(RoundDAO roundDAO) {
		this.roundDAO = roundDAO;
	}
}
