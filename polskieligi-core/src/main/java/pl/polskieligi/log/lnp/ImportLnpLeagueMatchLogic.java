package pl.polskieligi.log.lnp;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.*;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImportLnpLeagueMatchLogic extends AbstractImportLnpLogic<LeagueMatch>{
	final static Logger log = Logger.getLogger(ImportLnpLeagueMatchLogic.class);

	@Autowired
	private PlayerDAO playerDAO;

	@Autowired
	private LeagueMatchDAO matchDAO;

	public ImportLnpLeagueMatchLogic() {
		super(LeagueMatch.class);
	}

	@Override protected boolean isAlreadyLoaded(LeagueMatch oldObj) {
		return !super.isAlreadyLoaded(oldObj) || ( oldObj != null && oldObj.getImportStatus() != null
				&& oldObj.getImportStatus() == ImportStatus.SUCCESS.getValue());
	}

	@Override protected ImportStatus process(Document doc, LeagueMatch lm) {
		Elements teams = doc.select("div>section[class=report-teams-players grid]");

		List<LeagueMatchPlayer> players = parseSquads(teams, lm);
		Map<Integer, LeagueMatchPlayer> playersMap = players.stream().collect(
				Collectors.toMap(x->x.getPlayer().getLnp_id(), x->x));
		parseGoals(doc, lm, playersMap);
		parseMatchEvents(doc, lm, playersMap);
		playersMap.values().forEach(t->t.setLeagueMatch_id(lm.getMatch_id()));
		playersMap.values().forEach(t->lm.addLeagueMatchPlayers(t));
		return ImportStatus.SUCCESS;
	}

	private List<LeagueMatchPlayer> parseSquads(Elements teams, LeagueMatch obj){
		List<LeagueMatchPlayer> result = new LinkedList<LeagueMatchPlayer>();
		Elements team1 = teams.select("div[class=grid-24 grid-msw-48 grid-ms-48]");
		Elements team2 = teams.select("div[class=grid-24 grid-msw-48 grid-ms-48 text-right]");
		result.addAll(parseSquad(team1, obj.getMatchpart1_id()));
		result.addAll(parseSquad(team2, obj.getMatchpart2_id()));
		return result;
	}

	private List<LeagueMatchPlayer> parseSquad(Elements team, Long teamId){
		List<LeagueMatchPlayer> result = new LinkedList<LeagueMatchPlayer>();
		Elements squad = team.select("div[class*=report-players-list]");
		if(squad.size()>0){
			Element firstSquad = squad.get(0);
			result.addAll(parseSquad(firstSquad, true, teamId));
			if(squad.size()>1){
				Element subSquad = squad.get(1);
				result.addAll(parseSquad(subSquad, false, teamId));
			}
		}
		return result;
	}

	private List<LeagueMatchPlayer> parseSquad(Element squad, boolean isFirst, Long teamId){
		List<LeagueMatchPlayer> result = new LinkedList<LeagueMatchPlayer>();
		Elements players = squad.select("a[href]");
		for(Element player: players){
			String href = player.attr("href");
			Integer lnpId = LnpUrlHelper.getPlayerId(href);

			Integer nr =  Integer.parseInt(player.select("div[class=report-players-item cf]>div[class=player-nr]").first().text());
			LeagueMatchPlayer lmp = getLeagueMatchPlayer(lnpId, isFirst, nr, teamId);
			if(lmp!=null){
				result.add(lmp);
			}
		}
		return result;
	}

	private void parseMatchEvents(Document doc, LeagueMatch lm, Map<Integer, LeagueMatchPlayer> playersMap){

		Elements events = doc.select("div>section>div[class*=toggle-content report-half-content]>div[class*=report-tracking-action]");
		Substitution sub1 = null;
		Substitution sub2 = null;
		for(Element e: events){
			Integer time = getTime(e.select("div[class*=action-time]").text());
			String action = e.select("div[class*=action-icon]>i[class]").attr("class");
			String href = e.select("div[class*=action-name]>a[href]").attr("href");
			Integer playerId = LnpUrlHelper.getPlayerId(href);

			if(playersMap.containsKey(playerId)){
				if(playersMap.containsKey(playerId)) {
					LeagueMatchPlayer lmp = playersMap.get(playerId);
					switch (action) {
						case "i-report-ball":
							// skip
							//lmp.addMatchEvent(MatchEventType.SCORE, time);
							break;
						case "i-yellow-card":
							lmp.addMatchEvent(MatchEventType.YELLOW_CARD, time);
							break;
						case "i-red-card":
							lmp.addMatchEvent(MatchEventType.RED_CARD, time);
							lmp.setMinutOut(time);
							break;
						case "i-report-arrow-down":
							lmp.setMinutOut(time);
							Substitution subOut = getSub(lm, lmp, sub1, sub2);
							if (subOut == null) {
								subOut = createSub(lm.getMatch_id(), lmp.getTeam_id(), time);
								subOut.setPlayerOut_id(lmp.getPlayer_id());
								if (isHome(lm, lmp)) {
									sub1 = subOut;
								}
								if (isAway(lm, lmp)) {
									sub2 = subOut;
								}
							} else {
								if (subOut.getTime().equals(time) && subOut.getPlayerOut_id() == null) {
									subOut.setPlayerOut_id(lmp.getPlayer_id());
									lm.addSubstitutions(subOut);
									if (isHome(lm, lmp)) {
										sub1 = null;
									}
									if (isAway(lm, lmp)) {
										sub2 = null;
									}
								}
							}

							break;
						case "i-report-arrow-up":
							lmp.setMinutIn(time);
							if (lmp.getMinutOut().equals(0)) {
								lmp.setMinutOut(90);
							}

							Substitution subIn = getSub(lm, lmp, sub1, sub2);
							if (subIn == null) {
								subIn = createSub(lm.getMatch_id(), lmp.getTeam_id(), time);
								subIn.setPlayerIn_id(lmp.getPlayer_id());
								if (isHome(lm, lmp)) {
									sub1 = subIn;
								}
								if (isAway(lm, lmp)) {
									sub2 = subIn;
								}
							} else {
								if (subIn.getTime().equals(time) && subIn.getPlayerIn_id() == null) {
									subIn.setPlayerIn_id(lmp.getPlayer_id());
									lm.addSubstitutions(subIn);
									if (isHome(lm, lmp)) {
										sub1 = null;
									}
									if (isAway(lm, lmp)) {
										sub2 = null;
									}
								}
							}
							break;
						default:
							log.error("Unknown action: " + action);
					}
				}
			} else {
				log.error("Player not found: "+playerId);
			}
		}
	}

	private void parseGoals(Document doc, LeagueMatch lm, Map<Integer, LeagueMatchPlayer> playersMap){
		Elements goals = doc.select("div>section[class=report-players-goals grid]");
		Elements goalsHome = goals.select("div[class=grid-20 grid-space-0 grid-mt-18 text-right]");
		Elements goalsAway = goals.select("div[class=grid-20 grid-mt-18]");
		parseGoals(goalsHome, playersMap, true, lm.getMatchpart1_id());
		parseGoals(goalsAway, playersMap, false, lm.getMatchpart2_id());
	}

	private void parseGoals(Elements doc, Map<Integer, LeagueMatchPlayer> playersMap, boolean home, Long teamId){
		Elements goals = doc.select("div[class=row]>a[href]");
		for(Element g: goals){
			String href = g.attr("href");
			Integer playerId = LnpUrlHelper.getPlayerId(href);
			LeagueMatchPlayer lmp = getLeagueMatchPlayer(playerId, playersMap, teamId);
			if(lmp!=null){
				String text = g.select("span").text();
				boolean ownGoal = text.contains("(samobójcza)");
				if(ownGoal){
					text = text.replace("(samobójcza)", "");
				}
				String[] split = text.trim().split(" ");
				Integer time;
				if(home){
					time = getTime(split[split.length-1]);
				} else {
					time = getTime(split[0]);
				}
				if(ownGoal) {
					lmp.addMatchEvent(MatchEventType.SCORE_OWN, time);
				} else {
					lmp.addMatchEvent(MatchEventType.SCORE, time);
				}
			}
		}
	}

	private LeagueMatchPlayer getLeagueMatchPlayer(Integer playerId,  Map<Integer, LeagueMatchPlayer> playersMap, Long teamId){
		LeagueMatchPlayer result;
		if(playersMap.containsKey(playerId)) {
			result = playersMap.get(playerId);
		} else {
			result = getLeagueMatchPlayer(playerId, false, null, teamId);
			if(result!=null){
				playersMap.put(playerId, result);
			}
		}
		return result;
	}

	private LeagueMatchPlayer getLeagueMatchPlayer(Integer playerId, boolean isFirst, Integer nr, Long teamId){
		LeagueMatchPlayer lmp = null;
		Player p = playerDAO.retrieveByLnp(playerId);
		if(p==null){
			log.error("Player not found: "+playerId);
		} else {
			lmp = new LeagueMatchPlayer();
			lmp.setPlayer_id(p.getId());
			lmp.setPlayer(p);
			lmp.setFirstSquad(isFirst);
			lmp.setNumber(nr);
			lmp.setTeam_id(teamId);
			if(isFirst){
				lmp.setMinutIn(1);
				lmp.setMinutOut(90);
			} else {
				lmp.setMinutIn(0);
				lmp.setMinutOut(0);
			}
		}
		return lmp;
	}

	private Integer getTime(String text){
		String tmp = text.replace("'", "").replace(" min.", "").trim();
		if(tmp.contains("+")){
			tmp = tmp.split("\\+")[0];
		}
		return Integer.parseInt(tmp);
	}

	private Substitution createSub(Long lmId, Long mId, Integer time){
		Substitution sub = new Substitution();
		sub.setTime(time);
		sub.setTeam_id(mId);
		sub.setLeagueMatch_id(lmId);
		return sub;
	}

	private Substitution getSub(LeagueMatch lm, LeagueMatchPlayer lmp, Substitution sub1, Substitution sub2){
		Substitution sub = null;
		if(isHome(lm, lmp)){
			sub = sub1;
		}
		if(isAway(lm, lmp)){
			sub = sub2;
		}
		return sub;
	}

	private boolean isHome(LeagueMatch lm, LeagueMatchPlayer lmp){
		return lm.getMatchpart1_id().equals(lmp.getTeam_id());
	}

	private boolean isAway(LeagueMatch lm, LeagueMatchPlayer lmp){
		return lm.getMatchpart2_id().equals(lmp.getTeam_id());
	}

	@Override protected String getLink(LeagueMatch obj) {
		return LnpUrlHelper.getMatchUrl(obj.getLnpIdName(), obj.getLnp_id());
	}

	@Override protected AbstractLnpDAO<LeagueMatch> getDAO() {
		return matchDAO;
	}
}
