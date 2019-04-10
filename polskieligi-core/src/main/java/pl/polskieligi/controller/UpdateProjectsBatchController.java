package pl.polskieligi.controller;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UpdateProjectsBatchController extends AbstractImportController{

	final static Logger log = Logger.getLogger(UpdateProjectsBatchController.class);

	@Autowired
	@Qualifier("projectUpdateJob") Job job;

	@RequestMapping("/updateProjectsBatch")
	public ModelAndView updateProjectsBatch() {
		log.info("updateProjectsBatch start");
		return importBatch(job);
	}
}
