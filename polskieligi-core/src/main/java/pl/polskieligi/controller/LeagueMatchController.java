package pl.polskieligi.controller;

import java.util.ArrayList;
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
							if(me.getType().equals(MatchEventType.SCORE)) {
								team1Goals.add(me);
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
							if(me.getType().equals(MatchEventType.SCORE)) {
								team2Goals.add(me);
							}
						}
					}
				}
				mv.addObject("team1Players", team1Players);
				mv.addObject("team2Players", team2Players);
				mv.addObject("team1Subs", team1Subs);
				mv.addObject("team2Subs", team2Subs);
				mv.addObject("team1Goals", team1Goals);
				mv.addObject("team2Goals", team2Goals);
			}
		}
		return mv;
	}
}
