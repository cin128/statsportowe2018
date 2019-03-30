package pl.polskieligi.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.Team;

@Controller
public class TeamController {

	final static Logger log = Logger.getLogger(TeamController.class);

	@Autowired TeamDAO teamDAO;
	
	@Autowired LeagueMatchDAO matchDAO;

	@RequestMapping("/team")
	public ModelAndView showTeam(@RequestParam(value = "seasonId", required = false) Long seasonId,
			@RequestParam(value = "teamId", required = false) Long teamId) {
		log.info("showTeam: seasonId: " + seasonId + ", teamId: " + teamId);
		ModelAndView mv = new ModelAndView("thymeleaf/team");
		if(teamId!=null) {
			Team team = teamDAO.find(teamId);
			if(team!=null) {
				mv.addObject("team",team);
			}
			if(seasonId!=null) {
				List<LeagueMatch> leagueMatches = matchDAO.getMatchesByTeamSeason(teamId, seasonId);
				mv.addObject("leagueMatches", leagueMatches);
			}
		}
		return mv;
	}
}
