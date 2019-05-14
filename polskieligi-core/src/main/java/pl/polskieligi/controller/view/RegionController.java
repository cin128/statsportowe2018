package pl.polskieligi.controller.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.model.Project;

@Controller
public class RegionController {
	final static Logger log = Logger.getLogger(RegionsController.class);
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@RequestMapping("/region")
	public ModelAndView showRegion(Integer regionId) {
		log.info("showRegion");
		ModelAndView mv = new ModelAndView("thymeleaf/region");
		if(regionId!=null) {
			List<Project> projects = projectDAO.findProjects(new Long(49), regionId);
			mv.addObject("projects", projects);
		}
		return mv;
	}
}
