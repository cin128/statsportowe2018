package pl.polskieligi.controller.data.lnp;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.polskieligi.controller.data.AbstractImportController;
import pl.polskieligi.controller.data.minut.ImportPlayersBatchController;

@Controller
public class UpdateLnpProjectsBatchController extends AbstractImportController {

	final static Logger log = Logger.getLogger(ImportPlayersBatchController.class);

	@Autowired
	@Qualifier("lnpProjectUpdateJob") Job job;

	@RequestMapping("/updateLnpProjectsBatch")
	public ModelAndView updateLnpProjectsBatch() {
		log.info("updateLnpProjectsBatch start");
		return importBatch(job);
	}
}
