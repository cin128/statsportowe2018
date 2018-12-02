package pl.polskieligi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.log.ImportProjectLogic;

@Controller
public class ImportProjectController {

	@Autowired
	ImportProjectLogic importProjectLogic;
	
	@RequestMapping("/importProject")
	public ModelAndView importProject(String projectId) {
		String result = importProjectLogic.doImport(Integer.parseInt(projectId));
		ModelAndView mv = new ModelAndView("importProject", "result", result);
		return mv;		
	}
}
