package pl.polskieligi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.log.lnp.ImportLnpProjectMatchesLogic;
import pl.polskieligi.log.lnp.ImportLnpProjectTeamsLogic;
import pl.polskieligi.log.lnp.ImportProjectLogic;

@Controller
public class UpdateLnpController {
	final static Logger log = Logger.getLogger(UpdateLMPController.class);

	@Autowired
	ImportProjectLogic projectLogic;
	
	@Autowired
	ImportLnpProjectTeamsLogic importLnpProjectTeamsLogic;
	
	@Autowired
	ImportLnpProjectMatchesLogic importLnpProjectMatchesLogic;
	
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
	
	@RequestMapping("/updateLnpTeams2")
	public ModelAndView updateLnpTeams2() {
		log.info("updateLnpTeams start");
		importLnpProjectMatchesLogic.doImport(26967);
		return new ModelAndView("views/importStatus");
	}
}
