package pl.polskieligi.controller;

import org.apache.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.polskieligi.dto.ImportJob;

import java.util.*;

@Controller
public class ImportRefereesBatchController extends AbstractImportController{

	final static Logger log = Logger.getLogger(ImportRefereesBatchController.class);

	@Autowired
	@Qualifier("refereeImportJob") Job job;

	@RequestMapping("/importRefereesBatch")
	public ModelAndView importPlayersBatch() {
		log.info("importRefereesBatch start");
		return importBatch(job);
	}
}
