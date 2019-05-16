package pl.polskieligi.controller.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.log.stats.team.TeamStatsLogic;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.Team;

@Controller
public class TeamController {

	final static Logger log = Logger.getLogger(TeamController.class);

	@Autowired TeamDAO teamDAO;
	
	@Autowired PlayerDAO playerDAO;
	
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
				if(seasonId!=null) {
					mv.addObject("seasonId",seasonId);
					List<LeagueMatch> leagueMatches = matchDAO.getMatchesByTeamSeason(teamId, seasonId);
					mv.addObject("leagueMatches", leagueMatches);
					mv.addObject("teamStats", TeamStatsLogic.calculateTeamStats(leagueMatches, teamId));
					
					List<Player> players = playerDAO.findBySeasonAndTeam(seasonId, teamId);
					mv.addObject("players",players);	
				}
			}
		}
		return mv;
	}
}
