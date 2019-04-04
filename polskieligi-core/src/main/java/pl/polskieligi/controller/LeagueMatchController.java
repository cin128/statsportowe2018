package pl.polskieligi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.LeagueMatchPlayer;
import pl.polskieligi.model.MatchEvent;
import pl.polskieligi.model.MatchEventType;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Season;

@Controller
public class LeagueMatchController {
	final static Logger log = Logger.getLogger(LeagueMatchController.class);
	
	@Autowired LeagueMatchDAO leagueMatchDAO;
	
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
}
