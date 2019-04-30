package pl.polskieligi.controller.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueDAO;
import pl.polskieligi.dao.LeagueMatchPlayerDAO;

@Controller
public class UpdateController {
	
	@Autowired
	LeagueDAO leagueDAO;
	
	@Autowired
	LeagueMatchPlayerDAO leagueMatchPlayerDAO;
	
	@RequestMapping("/updateLeagueTypes")
	public ModelAndView updateLeagueTypes() {
		leagueDAO.updateLeagueTypes();
		return new ModelAndView("views/importStatus");
	}
	
	@RequestMapping("/updateRegions")
	public ModelAndView updateRegions() {
		leagueDAO.updateRegions();
		return new ModelAndView("views/importStatus");
	}
	
	@RequestMapping("/updateGroups")
	public ModelAndView updateGroups() {
		leagueDAO.updateGroups();
		return new ModelAndView("views/importStatus");
	}
	
	//@RequestMapping("/updateTeamLeaguePlayers")
	//WOLNO DZIALA
	public ModelAndView updateTeamLeaguePlayers() {
		leagueMatchPlayerDAO.updateTeamLeaguePlayers();
		return new ModelAndView("views/importStatus");
	}

}
