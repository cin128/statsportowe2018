package pl.polskieligi.controller.data.lnp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.controller.data.minut.UpdateLMPController;
import pl.polskieligi.log.lnp.*;

@Controller
public class UpdateLnpController {
	final static Logger log = Logger.getLogger(UpdateLMPController.class);

	@Autowired
	ImportProjectLogic projectLogic;
	
	@Autowired
	ImportLnpProjectTeamsLogic importLnpProjectTeamsLogic;
	
	@Autowired
	ImportLnpProjectMatchesLogic importLnpProjectMatchesLogic;
	
	@Autowired ImportLnpTeamLeagueLogic importLnpTeamLeagueLogic;

	@Autowired ImportLnpLeagueMatchLogic importLnpLeagueMatchLogic;
	
	@RequestMapping("/updateLnp")
	public ModelAndView updateLnp() {
		log.info("updateLnp start");
		projectLogic.assignLnpIdToProjects();
		return new ModelAndView("views/importStatus");
	}
	
	@RequestMapping("/updateLnpTeams")
	public ModelAndView updateLnpTeams() {
		log.info("updateLnpTeams start");
		importLnpProjectTeamsLogic.doImport(25929);
		return new ModelAndView("views/importStatus");
	}
	
	@RequestMapping("/updateLnpMainLeagues")
	public ModelAndView updateLnpMainLeagues() {
		log.info("updateLnpMainLeagues start");
		importLnpProjectMatchesLogic.processMainLeagues();
		return new ModelAndView("views/importStatus");
	}

	@RequestMapping("/updateLnpProjects")
	public ModelAndView updateLnpProjects() {
		log.info("updateLnpMainLeagues start");
		importLnpProjectMatchesLogic.doImport(25873);
		return new ModelAndView("views/importStatus");
	}
	
	@RequestMapping("/updateLnpPlayers")
	public ModelAndView updateLnpPlayers() {
		log.info("updateLnpPlayers start");
		importLnpTeamLeagueLogic.doImport(257049);
		return new ModelAndView("views/importStatus");
	}

	@RequestMapping("/updateLnpMatch")
	public ModelAndView updateLnpMatch() {
		log.info("updateLnpMatch start");
		importLnpLeagueMatchLogic.doImport(2137483);
		return new ModelAndView("views/importStatus");
	}
}
