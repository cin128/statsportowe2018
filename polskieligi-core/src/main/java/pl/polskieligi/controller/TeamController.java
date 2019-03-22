package pl.polskieligi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.model.Team;

@Controller
public class TeamController {

	final static Logger log = Logger.getLogger(TeamController.class);

	@Autowired TeamDAO teamDAO;

	@RequestMapping("/team")
	public ModelAndView showTeam(@RequestParam(value = "season", required = false) Long season,
			@RequestParam(value = "teamId", required = false) Long teamId) {
		log.info("showTeam: season: " + season + ", teamId: " + teamId);
		ModelAndView mv = new ModelAndView("thymeleaf/team");
		if(teamId!=null) {
			Team team = teamDAO.find(teamId);
			if(team!=null) {
				mv.addObject(team);
			}
		}
		return mv;
	}
}
