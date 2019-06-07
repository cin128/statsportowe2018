package pl.polskieligi.log.minut;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pl.polskieligi.dao.AbstractDAO;
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
import pl.polskieligi.model.League;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.LeagueType;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Region;
import pl.polskieligi.model.Round;
import pl.polskieligi.model.Season;
import pl.polskieligi.model.Team;
import pl.polskieligi.model.TeamLeague;

@Component
@Transactional
public class ImportMinutProjectLogic extends AbstractImportMinutLogic<Project>{
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

	public ImportMinutProjectLogic() {
		super(Project.class);
	}

	@Override protected boolean isAlreadyLoaded(Project oldObj) {
		ProjectInfo pi = new ProjectInfo();
		boolean result = super.isAlreadyLoaded(oldObj) && (oldObj.getArchive() && oldObj.getPublished()
				|| oldObj.getType() == Project.OTHER)&& false;
		if(result){
			pi.setSkipped(true);
		}
		if(oldObj!=null){
			oldObj.setProjectInfo(pi);
			pi.setProject(oldObj);
		}
		return result;
	}

	@Override protected ImportStatus process(Document doc, Project leagueProject) {
		ProjectInfo pi =leagueProject.getProjectInfo();
		if(pi==null) {
			pi = new ProjectInfo();
			pi.setProject(leagueProject);
			leagueProject.setProjectInfo(pi);
		}
		
		String title = getTitle(doc);
		if(StringUtils.isEmpty(title)) {
			return ImportStatus.INVALID;
		}
		log.info("Processing project: " + title);
		leagueProject.setName(title);
		Season season = parseSeason(title);
		if(season==null){
			season = parseSeasonFromLink(doc);
		}
		leagueProject.setSeason(season);
		League league = parseLeague(title, season);
		leagueProject.setLeague(league);
		Date startDate = getStartDate(season);
		leagueProject.setStart_date(startDate);
		leagueProject.setArchive(isArchive(startDate));

		Map<String, Triple<Team, TeamLeague, Integer>> leagueTeamsPair = parseTeams(doc, leagueProject);
		Map<String, Team> leagueTeams = new HashMap<String, Team>();
		Map<Long, TeamLeague> teamLeagues = new HashMap<Long, TeamLeague>();
		Map<Long, Integer> points = new HashMap<Long, Integer>();
		for (Entry<String, Triple<Team, TeamLeague, Integer>> e : leagueTeamsPair.entrySet()) {
			leagueTeams.put(e.getKey(), e.getValue().getLeft());
			TeamLeague tl = e.getValue().getMiddle();
			teamLeagues.put(tl.getTeam_id(), tl);
			points.put(tl.getTeam_id(), e.getValue().getRight());
		}

		int teams_count = leagueTeams.size();
		pi.setTeams_count(teams_count);
		if (teams_count == 0) {
			leagueProject.setType(Project.OTHER);
		} else {
			leagueProject.setType(Project.REGULAR_LEAGUE);
		}
		List<Team> missingTeams = parseGames(doc, leagueProject, leagueTeams, teamLeagues);
		if (teams_count == 0 && missingTeams.size() > 0) {
			updateTeamLeagues(missingTeams, leagueProject);
		}
		leagueProject = projectDAO.saveUpdate(leagueProject);
		updateStartPoints(leagueProject, teamLeagues, points);

		return ImportStatus.SUCCESS;
	}
	
	private String getTitle(Document doc) {
		String result = doc.select("title").text();
		if ("404 Not Found".equals(result)) {
			result = null;
		}
		return result;
	}
	
	private Season parseSeason(String title) {
		String sezon = null;
		int index = title.indexOf("/");
		if (index < 5 || title.length() < 12) {
			if(title.length() > 4) {
				String tmp = title.substring(title.length()-4, title.length());
				if(tmp.matches("^\\d+$")) {
					sezon = tmp;
				}
			}
		} else {
			 sezon = title.substring(index - 4, index + 5);
		}
		if(sezon!=null) {
			Season season = new Season();
			season.setName(sezon);
			season = seasonDAO.saveUpdate(season);
			log.debug("Season " + sezon   + " saved id = " + season.getId());
			return season;
		}
		return null;
	}

	private Season parseSeasonFromLink(Document doc) {
		Elements links = doc.select("table[class=main][width=600][border=0][cellspacing=0][cellpadding][align=center]>tbody>tr>td>a[class=main][href]");
		for(Element link: links){
			String title = link.text();
			Season season = parseSeason(title);
			if(season!=null){
				return season;
			}
		}
		return null;
	}
	
	private League parseLeague(String title, Season season) {
		String leagueName = title;
		if(season!=null) {
			leagueName = leagueName.replaceAll(" " + season.getName(), "");
		}
		League league = new League();
		league.setName(leagueName);
		league.setLeagueType(LeagueType.getByLeagueName(leagueName).getId());
		league.setRegion(Region.getRegionByProjectName(leagueName).getId());
		if(leagueName.contains("grupa: ")){
			league.setGroupName(leagueName.split("grupa: ")[1]);
		}
		league = leagueDAO.saveUpdate(league);
		log.debug("League " + leagueName + " saved id = " + league.getId());
		return league;
	}
	
	private Date getStartDate(Season season) {
		if(season == null) {
			return new Date(0);
		}
		GregorianCalendar cal;
		if(season.getName().contains("/")) {
			Integer year =Integer.parseInt(season.getName().split("/")[0]);
			 cal = new GregorianCalendar(year, 6,
						1);
		} else {
			Integer year = Integer.parseInt(season.getName());
			 cal = new GregorianCalendar(year, 0,
						1);
		}
		return new Date(cal.getTimeInMillis());
	}
	
	private Boolean isArchive(Date date) {
		if(date==null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return cal.getTimeInMillis()>date.getTime();
	}

	private Map<String, Triple<Team, TeamLeague, Integer>> parseTeams(Document doc, Project leagueProject) {
		Map<String, Triple<Team, TeamLeague, Integer>> leagueTeams = new HashMap<String, Triple<Team, TeamLeague, Integer>>();
		Map<TeamLeague, TeamLeague> teamLeagueMap = leagueProject.getTeamLeagues().stream().collect(
				Collectors.toMap(x -> x,
						x -> x));
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

			TeamLeague pers = teamLeagueMap.get(tl);
			if(pers==null) {
				tl = teamLeagueDAO.saveUpdate(tl);
			} else {
				tl = pers;
			}
			//importTeamLeaguePlayerLogic.doImport(tl.getId(), leagueProject.getSeason().getMinut_id(), t.getMinut_id());
			leagueTeams.put(t.getName(), Triple.of(t, tl, Integer.parseInt(pkt)));
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
	
	private List<Team> parseGames(Document doc, Project leagueProject, Map<String, Team> leagueTeams, Map<Long, TeamLeague> teamLeagues) {
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

									parseExtraTime(match, roundMatch);
								}
							}
							parseMatchDetails(match, roundMatch, leagueProject.getStart_date());
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
						Date roundStart = TimestampParser.getRoundStart(leagueProject.getStart_date(),
								tmp.substring(i + 2));
						if (roundStart != null) {
							matchDate = new Timestamp(
									roundStart.getTime());
						}
						round.setRound_date_first(roundStart);
						round.setRound_date_last(TimestampParser.getRoundEnd(leagueProject.getStart_date(),
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
		leagueProject.getProjectInfo().setMatches_count(matches_count);
		leagueProject.getProjectInfo().setRounds_count(rounds_count);
		return missingTeams;
	}

	private void parseMatchDetails(Element match, LeagueMatch roundMatch, Date projectStartDate){
		Elements date = match
				.select("td[valign=top][nowrap][align=left][width=190]");
		if (date.size() == 1) {
			String tmp = date.get(0).text();
			if (tmp != null && !tmp.trim().isEmpty()) {
				Timestamp ts = null;
				Integer crowd = 0;
				int index = tmp.indexOf("(");
				if (index == 0) {
					crowd = Integer.parseInt(tmp.substring(1, tmp.length() - 1).replaceAll(" ", ""));
				} else if (index > 0) {
					ts = TimestampParser.parseTimestamp(projectStartDate, tmp.substring(0, index - 1));
					crowd = Integer.parseInt(tmp.substring(index + 1, tmp.length() - 1).replaceAll(" ", ""));
				} else {
					ts = TimestampParser.parseTimestamp(projectStartDate, tmp);
				}
				if (ts != null) {
					roundMatch.setMatch_date(ts);
				}
				if (crowd > 0) {
					roundMatch.setCrowd(crowd);
				}
			}
		}
	}

	private void parseExtraTime(Element match, LeagueMatch roundMatch){
		Element next = match.nextElementSibling();
		if(next!=null){
			String text = next.select("td[align=center][colspan=3]").text();
			if(!StringUtils.isEmpty(text)){
				if(text.contains("pd. ")){
					roundMatch.setExtra_time(true);
				}
				if(text.contains("k. ")){
					roundMatch.setPanelties(true);
					String res = text.split("k\\. ")[1];
					String[] results = res
							.split("-");
					if (results.length == 2) {
						roundMatch.setMatchpart1_panelties_result(parseInt(results[0]));
						roundMatch.setMatchpart2_panelties_result(parseInt(results[1]));
					}
				}
			}
		}
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

	private Integer parseInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			log.warn("Value: " + value + " is not a number!!!");
		}
		return null;
	}

	private void updateStartPoints(Project leagueProject, Map<Long, TeamLeague> teamLeagues, Map<Long, Integer> points) {
		if (leagueProject.getType() == Project.REGULAR_LEAGUE) {
			for (TableRow row : tableDAO.getTableRowsSimple(leagueProject.getId())) {
				TeamLeague tl = teamLeagues.get(row.getTeam_id());
				Integer pts = 0;
				if(tl==null){//drużyna wycofana z rozgrywek w trakcie sezonu
					tl = teamLeagueDAO.findByProjectAndTeam(leagueProject.getId(), row.getTeam_id());
				} else {
					pts = points.get(row.getTeam_id());
				}

				tl.setStartPoints(pts - row.getPoints());
				teamLeagueDAO.update(tl);
			}
		}

	}

	@Override
	protected String getLink(Project p) {
		return MinutUrlHelper.getProjectUrl(p.getMinut_id());
	}

	@Override
	protected Project retrieveById(Integer minutId) {
		return projectDAO.retrieveByMinut(minutId.intValue());
	}

	@Override
	protected AbstractDAO<Project> getDAO() {
		return projectDAO;
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
