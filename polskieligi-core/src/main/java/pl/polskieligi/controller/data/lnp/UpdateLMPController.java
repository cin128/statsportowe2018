package pl.polskieligi.controller.data.lnp;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.polskieligi.controller.data.AbstractImportController;

@Controller
public class UpdateLMPController extends AbstractImportController {

	final static Logger log = Logger.getLogger(UpdateLMPController.class);

	@Autowired
	@Qualifier("LMPUpdateJob") Job job;
	
	@RequestMapping("/updateLMP")
	public ModelAndView UpdateLMP() {
		log.info("UpdateLMPBatch start");
		return importBatch(job);	
	}
}

