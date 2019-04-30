package pl.polskieligi.controller.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueMatchPlayerDAO;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.model.LeagueMatchPlayer;
import pl.polskieligi.model.Player;

@Controller
public class PlayerController {
	final static Logger log = Logger.getLogger(PlayerController.class);

	@Autowired PlayerDAO playerDAO;
	@Autowired LeagueMatchPlayerDAO leagueMatchPlayerDAO;
	
	@RequestMapping("/player")
	public ModelAndView showPlayer(@RequestParam(value = "seasonId", required = false) Long seasonId,
			@RequestParam(value = "playerId", required = false) Long playerId) {
		log.info("showPlayer: seasonId: " + seasonId + ", playerId: " + playerId);
		ModelAndView mv = new ModelAndView("thymeleaf/player");
		if(playerId!=null) {
			Player player = playerDAO.find(playerId);
			if(player!=null) {
				mv.addObject(player);
				if(seasonId!=null) {
					List<LeagueMatchPlayer> leagueMatches = leagueMatchPlayerDAO.getPlayerMatchesForSeason(playerId, seasonId);
					if(leagueMatches!=null && !leagueMatches.isEmpty()) {
						mv.addObject("leagueMatches", leagueMatches);
					}
				}
			}
		}
		
		return mv;
	}
}
