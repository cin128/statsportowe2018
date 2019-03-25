package pl.polskieligi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueDAO;

@Controller
public class UpdateController {
	
	@Autowired
	LeagueDAO leagueDAO;
	
	@RequestMapping("/update")
	public ModelAndView update() {
		leagueDAO.updateLeagueTypes();
		leagueDAO.updateRegions();
		return new ModelAndView("views/importStatus");
	}

}
