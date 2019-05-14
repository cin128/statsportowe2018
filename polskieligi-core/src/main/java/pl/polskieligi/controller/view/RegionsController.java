package pl.polskieligi.controller.view;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegionsController {
	final static Logger log = Logger.getLogger(RegionsController.class);
	
	@RequestMapping("/regions")
	public ModelAndView showRegions() {
		log.info("showRegions");
		ModelAndView mv = new ModelAndView("thymeleaf/regions");
		return mv;
	}
}
