package pl.polskieligi.controller.data.minut;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.controller.data.AbstractImportController;

@Controller
public class ImportTeamsBatchController extends AbstractImportController {

	final static Logger log = Logger.getLogger(ImportTeamsBatchController.class);

	@Autowired
	@Qualifier("teamImportJob") Job job;

	@RequestMapping("/importTeamsBatch")
	public ModelAndView importPlayersBatch() {
		log.info("importTeamsBatch start");
		return importBatch(job);
	}
}
