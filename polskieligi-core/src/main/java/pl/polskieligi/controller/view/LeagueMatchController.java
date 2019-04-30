package pl.polskieligi.controller.view;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.SubstitutionDAO;
import pl.polskieligi.model.*;

@Controller
public class LeagueMatchController {
	final static Logger log = Logger.getLogger(LeagueMatchController.class);
	
	@Autowired LeagueMatchDAO leagueMatchDAO;
	@Autowired SubstitutionDAO substitutionDAO;
	
	@RequestMapping("/leagueMatch")
	public ModelAndView showLeagueMatch(@RequestParam(value = "leagueMatchId", required = false) Long leagueMatchId) {
		log.info("showLeagueMatch: leagueMatchId: " + leagueMatchId);
		ModelAndView mv = new ModelAndView("thymeleaf/leagueMatch");
		if(leagueMatchId!=null) {
			LeagueMatch leagueMatch = leagueMatchDAO.find(leagueMatchId);
			if(leagueMatch!=null) {
				mv.addObject("leagueMatch", leagueMatch);
				Project p = leagueMatch.getProject();
				if(p!=null) {
					Season s = p.getSeason();
					if(s!=null) {
						mv.addObject("seasonId", s.getId());
					}
				}
				List<LeagueMatchPlayer> team1Players = new ArrayList<LeagueMatchPlayer>();
				List<LeagueMatchPlayer> team2Players = new ArrayList<LeagueMatchPlayer>();
				List<LeagueMatchPlayer> team1Subs = new ArrayList<LeagueMatchPlayer>();
				List<LeagueMatchPlayer> team2Subs = new ArrayList<LeagueMatchPlayer>();
				List<MatchEvent> team1Goals = new ArrayList<MatchEvent>();
				List<MatchEvent> team2Goals = new ArrayList<MatchEvent>();

				for(LeagueMatchPlayer lmp: leagueMatch.getLeagueMatchPlayers()) {
					if(lmp.getTeam_id().equals(leagueMatch.getMatchpart1().getId())) {
						if(lmp.getFirstSquad()) {
							team1Players.add(lmp);
						} else {
							team1Subs.add(lmp);
						}
						for(MatchEvent me: lmp.getMatchEvents()) {
							if(me.getType().equals(MatchEventType.SCORE) || me.getType().equals(MatchEventType.SCORE_PANELTY)) {
								team1Goals.add(me);
							} else if(me.getType().equals(MatchEventType.SCORE_OWN)) {
								team2Goals.add(me);
							}
						}
					}
					if(lmp.getTeam_id().equals(leagueMatch.getMatchpart2().getId())) {
						if(lmp.getFirstSquad()) {
							team2Players.add(lmp);
						} else {
							team2Subs.add(lmp);
						}
						for(MatchEvent me: lmp.getMatchEvents()) {
							if(me.getType().equals(MatchEventType.SCORE) || me.getType().equals(MatchEventType.SCORE_PANELTY)) {
								team2Goals.add(me);
							} else if(me.getType().equals(MatchEventType.SCORE_OWN)) {
								team1Goals.add(me);
							}
						}
					}
				}
				setSubstitutions(team1Players, team2Players, team1Subs, team2Subs, substitutionDAO.getSubstitutionsForLeagueMatch(leagueMatchId));

				mv.addObject("team1Players", team1Players);
				mv.addObject("team2Players", team2Players);
				mv.addObject("team1Subs", team1Subs);
				mv.addObject("team2Subs", team2Subs);
				Collections.sort(team1Goals);
				mv.addObject("team1Goals", team1Goals);
				Collections.sort(team2Goals);
				mv.addObject("team2Goals", team2Goals);
				mv.addObject("playerRows", Integer.max(team1Players.size(), team2Players.size()));
				mv.addObject("subRows", Integer.max(team1Subs.size(), team2Subs.size()));
				mv.addObject("goalRows", Integer.max(team1Goals.size(), team2Goals.size()));

			}
		}
		return mv;
	}

	private void setSubstitutions(List<LeagueMatchPlayer> team1Players, List<LeagueMatchPlayer> team2Players, List<LeagueMatchPlayer> team1Subs, List<LeagueMatchPlayer> team2Subs, List<Substitution> subs){
		Map<Long, Substitution> subsIn = new HashMap<Long, Substitution>();
		Map<Long, Substitution> subsOut = new HashMap<Long, Substitution>();
		for(Substitution s: subs){
			subsIn.put(s.getPlayerIn_id(), s);
			subsOut.put(s.getPlayerOut_id(), s);
		}
		setSubstitutionsOut(subsOut, team1Players);
		setSubstitutionsOut(subsOut, team2Players);
		setSubstitutionsIn(subsIn, team1Subs);
		setSubstitutionsIn(subsIn, team2Subs);
	}
	private void setSubstitutionsIn(Map<Long, Substitution> subsIn, List<LeagueMatchPlayer> teamSubs){
		for(LeagueMatchPlayer lmp: teamSubs){
			if(subsIn.containsKey(lmp.getPlayer_id())){
				lmp.setSubstitutionIn(subsIn.get(lmp.getPlayer_id()));
			}
		}
	}
	private void setSubstitutionsOut(Map<Long, Substitution> subsOut, List<LeagueMatchPlayer> teamPlayers){
		for(LeagueMatchPlayer lmp: teamPlayers){
			if(subsOut.containsKey(lmp.getPlayer_id())){
				lmp.setSubstitutionOut(subsOut.get(lmp.getPlayer_id()));
			}
		}
	}
}
