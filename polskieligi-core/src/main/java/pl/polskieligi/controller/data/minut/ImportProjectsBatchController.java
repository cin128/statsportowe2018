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
public class ImportProjectsBatchController extends AbstractImportController {

	final static Logger log = Logger.getLogger(ImportProjectsBatchController.class);

	@Autowired
	@Qualifier("projectImportJob")
	Job job;

	@RequestMapping("/importProjectsBatch")
	public ModelAndView importPlayersBatch() {
		log.info("importProjectsBatch start");
		return importBatch(job);
	}
}
