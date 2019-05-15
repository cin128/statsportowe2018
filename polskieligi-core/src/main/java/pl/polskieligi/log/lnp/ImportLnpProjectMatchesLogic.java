package pl.polskieligi.log.lnp;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polskieligi.dao.*;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.log.distance.Distance;
import pl.polskieligi.model.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Transactional
public class ImportLnpProjectMatchesLogic extends AbstractImportLnpLogic<Project> {

	private static final Double AVG_DISTANCE_TRESHOLD = 1.0;

	final static Logger log = Logger.getLogger(ImportLnpProjectMatchesLogic.class);

	private final SimpleDateFormat matchDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");

	@Autowired
	ProjectDAO projectDAO;

	@Autowired
	TeamLeagueDAO teamLeagueDAO;

	@Autowired
	TeamDAO teamDAO;

	@Autowired
	RoundDAO roundDAO;

	@Autowired
	LeagueMatchDAO leagueMatchDAO;

	private static final TeamsDistanceLogic tdl = new TeamsDistanceLogic();

	public ImportLnpProjectMatchesLogic() {
		super(Project.class);
	}
	
	public void processMainLeagues() {
		process("ekstraklasa", 1, new Long(9730), false);	
		process("trzecia-liga", 25879, new Long(9731), true);//I Liga
		process("trzecia-liga", 25883, new Long(9732), true);//II Liga
		process("trzecia-liga", 25885, new Long(9808), true);//I
		process("trzecia-liga", 25884, new Long(9807), true);//II
		process("trzecia-liga", 25886, new Long(9810), true);//III
		process("trzecia-liga", 25888, new Long(9809), true);//IV
	}

	private void process(String lnpIdName, Integer lnp_id, Long projectId, boolean setLnpId) {
		try {			
			Document doc = Jsoup.connect(LnpUrlHelper.getProjectUrl(lnpIdName, lnp_id)).get();
			Project p = projectDAO.find(projectId);
			if(setLnpId) {
				p.setLnp_id(lnp_id);
				p.setLnpIdName(lnpIdName);
			}
			process(doc, p);
		} catch (IOException e) {
			log.error(e);
		}

	}
	
	@Override
	protected ImportStatus process(Document doc, Project project) {
		try {
			List<LnpMatch> lnpMatches = parseMatches(doc);
			Set<LnpTeamDTO> lnpTeams = getLnpTeams(lnpMatches);

			Set<TeamLeague> teamLeagueList = teamLeagueDAO.getTeamLeagues(project.getId());
			if (lnpTeams.size() == 0 || teamLeagueList.size() == 0) {
				log.error("Invalid number of teams: count: " + teamLeagueList.size() + " lnp count:" + lnpTeams.size());
				return ImportStatus.INVALID;
			} else {
				if (lnpTeams.size() != teamLeagueList.size()) {
					log.warn("Different numer of teams for project: " + project.getId() + " " + project.getName()
							+ " count: " + teamLeagueList.size() + " lnp count:" + lnpTeams.size());
				}
				List<Distance<LnpTeamDTO, TeamLeague>> minDistances = tdl.findMatchings(lnpTeams, teamLeagueList);
				//minDistances.stream().forEach(d->System.out.println(d.getPersObject().getTeam().getName()+" = "+d.getWebObject().getTeamName()+" "+d.getDistance()));
				Double avgDistance = Double.valueOf(minDistances.stream().mapToDouble(d -> d.getDistance()).sum())
						/ minDistances.size();
				ImportStatus result;
				if (avgDistance < AVG_DISTANCE_TRESHOLD) {
					Map<Integer, Long> teamsIds = updateTeams(minDistances);
					updateMatches(lnpMatches, teamsIds, project.getId());
					result = ImportStatus.SUCCESS;
				} else {
					result = ImportStatus.INVALID;
				}

				log.info("Avg distance: " + avgDistance);
				return result;
			}
		} catch (Exception e) {
			log.error(e);
			return ImportStatus.INVALID;
		}
	}

	@SuppressWarnings("deprecation")
	private void updateMatches(List<LnpMatch> lnpMatches, Map<Integer, Long> teamsIds, Long projectId) {
		for (LnpMatch m : lnpMatches) {
			if (teamsIds.containsKey(m.team1.getLnpId()) && teamsIds.containsKey(m.team2.getLnpId())) {
				Long team1 = teamsIds.get(m.team1.getLnpId());
				Long team2 = teamsIds.get(m.team2.getLnpId());
				Integer matchcode = Integer.parseInt(m.roundName.split(" ")[0]);

				Round round = roundDAO.findByProjectAndRound(projectId, matchcode);
				if (round != null) {
					LeagueMatch lm = leagueMatchDAO.find(projectId, round.getId(), team1, team2);
					if (lm != null) {
						lm.setLnpIdName(m.lnpteam1 + "," + m.lnpteam2);
						lm.setLnp_id(m.matchId);
						lm.setLocation(m.spot);
						if (lm.getMatch_date().getHours() == 0) {
							lm.setMatch_date(new Timestamp(m.date.getTime()));
						}
						if (lm.getMatchpart1_result() == null) {
							lm.setMatchpart1_result(new Float(m.team1goals));
						}
						if (lm.getMatchpart2_result() == null) {
							lm.setMatchpart2_result(new Float(m.team2goals));
						}

						leagueMatchDAO.update(lm);
					} else {
						log.warn("Match not found: " + projectId + " " + round.getId() + " " + team1 + " " + team2);
					}
				} else {
					log.warn("Round not found: " + matchcode + " " + projectId);
				}
			}
		}
	}

	private Set<LnpTeamDTO> getLnpTeams(List<LnpMatch> lnpMatches) {
		return lnpMatches.stream().<LnpTeamDTO>flatMap(m -> Stream.of(m.team1, m.team2)).collect(Collectors.toSet());
	}

	private List<LnpMatch> parseMatches(Document doc) {
		List<LnpMatch> matches = new ArrayList<LnpMatch>();
		Elements games = doc.select("section[class*=season__games]>article[class=season__game grid]");
		for (Element g : games) {
			Element dateElem = g.select("div[class*=season__game-data]").get(0);
			Date date = parseDate(dateElem);

			Element teams = g.select("div[class*=season__game-name]").get(0);
			Element team1 = teams.select("a[class=team]").get(0);
			LnpTeamDTO t1 = parseTeam(team1);
			Element team2 = teams.select("a[class=team versus]").get(0);
			LnpTeamDTO t2 = parseTeam(team2);
			Integer t1g = null;
			Integer t2g = null;
			Elements sc = teams.select("span[class=score]");
			if (sc.size() == 1) {
				String score = sc.get(0).text();
				if (!StringUtils.isEmpty(score)) {
					String[] split = score.split(":");
					t1g = Integer.parseInt(split[0]);
					t2g = Integer.parseInt(split[1]);
				}
			}
			String spot = null;
			Elements sp = teams.select("span[class=spot]");
			if (sp.size() == 1) {
				spot = sp.get(0).text();
			}

			Element details = g.select("div[class*=season__game-action]").get(0);
			String roundName = details.select("div[class=event]").html().split("<br")[0].trim();
			Elements md = details.select("a[class=action][href]");

			Integer matchId = null;
			String lnpteam1 = null;
			String lnpteam2 = null;
			if (md.size() == 1) {
				String href = md.get(0).attr("href");
				String[] split = href.replace("https://www.laczynaspilka.pl/rozgrywki/mecz/", "").replace(".html", "")
						.split(",");
				lnpteam1 = split[0];
				lnpteam2 = split[1];
				matchId = Integer.parseInt(split[2]);

			}
			matches.add(new LnpMatch(date, t1, t2, t1g, t2g, spot, roundName, matchId, lnpteam1, lnpteam2));
		}
		return matches;
	}

	private LnpTeamDTO parseTeam(Element team) {
		String href = team.attr("href");
		href = href.replace("https://www.laczynaspilka.pl/druzyna/", "");
		href = href.replace(".html", "");
		String[] split = href.split(",");
		String lnpName = split[0];
		Integer lnpId = Integer.valueOf(split[1]);
		String teamName = team.text();
		return new LnpTeamDTO(lnpName, lnpId, teamName);
	}

	private Date parseDate(Element dateElem) {
		String day = dateElem.select("span[class=day]").text();
		String month = dateElem.select("span[class=month]").text();
		String hour = dateElem.select("span[class=hour]").text();
		StringBuilder sb = new StringBuilder();
		sb.append(day);
		sb.append("/");
		sb.append(month);
		sb.append(" ");
		sb.append(hour);

		try {
			return matchDateFormat.parse(sb.toString());
		} catch (ParseException e) {
			log.error(e);
		}
		return null;
	}

	private Map<Integer, Long> updateTeams(List<Distance<LnpTeamDTO, TeamLeague>> distances) {
		Map<Integer, Long> result = new HashMap<Integer, Long>();
		for (Distance<LnpTeamDTO, TeamLeague> d : distances) {
			TeamLeague tl = d.getPersObject();
			Team t = tl.getTeam();
			if (tl.getLnp_id() != null && tl.getLnp_id() > 0) {
				if (!tl.getLnp_id().equals(d.getWebObject().getLnpId())) {
					log.warn("Niejednoznaczne przypisanie: name:" + t.getName() + " lnpName:" + tl.getLnpName() + " "
							+ d.getWebObject().getTeamName());
				}
			}
			tl.setLnp_id(d.getWebObject().getLnpId());
			tl.setLnpIdName(d.getWebObject().getLnpName());
			tl.setLnpName(d.getWebObject().getTeamName());
			teamLeagueDAO.update(tl);

			result.put(d.getWebObject().getLnpId(), d.getPersObject().getTeam_id());
		}
		return result;
	}

	@Override
	protected String getLink(Project p) {
		return LnpUrlHelper.getProjectUrl(p.getLnpIdName(), p.getLnp_id());
	}

	@Override
	protected AbstractLnpDAO<Project> getDAO() {
		return projectDAO;
	}

	private class LnpMatch {
		final Date date;
		final LnpTeamDTO team1;
		final LnpTeamDTO team2;
		final Integer team1goals;
		final Integer team2goals;
		final String spot;
		final String roundName;
		final Integer matchId;
		final String lnpteam1;
		final String lnpteam2;

		LnpMatch(Date date, LnpTeamDTO team1, LnpTeamDTO team2, Integer team1goals, Integer team2goals, String spot,
				String roundName, Integer matchId, String lnpteam1, String lnpteam2) {
			this.date = date;
			this.team1 = team1;
			this.team2 = team2;
			this.team1goals = team1goals;
			this.team2goals = team2goals;
			this.spot = spot;
			this.roundName = roundName;
			this.matchId = matchId;
			this.lnpteam1 = lnpteam1;
			this.lnpteam2 = lnpteam2;

		}

	}
}
