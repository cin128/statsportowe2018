package pl.polskieligi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dto.ProjectInfo;
import pl.polskieligi.log.ImportProjectLogic;

@Controller
public class ImportProjectController {

	final static Logger log = Logger.getLogger(ImportProjectController.class);

	@Autowired
	ImportProjectLogic importProjectLogic;
	
	@RequestMapping("/importProject")
	public ModelAndView importProject(String projectId) {
		log.info("importProject start. ProjectId = "+projectId);
		ProjectInfo result = importProjectLogic.doImport(Integer.parseInt(projectId));
		ModelAndView mv = new ModelAndView("importProject", "result", result.toHtml());
		return mv;		
	}
}
