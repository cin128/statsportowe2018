package pl.polskieligi.controller.data.lnp;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.controller.data.AbstractImportController;

@Controller
public class UpdateLnpProjectsBatchController extends AbstractImportController {
	@Autowired
	@Qualifier("lnpProjectUpdateJob") Job job;

	@RequestMapping("/updateLnpProjectsBatch")
	public ModelAndView updateLnpProjectsBatch() {
		return importBatch(job);
	}
}