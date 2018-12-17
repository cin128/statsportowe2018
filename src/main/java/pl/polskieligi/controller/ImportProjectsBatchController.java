package pl.polskieligi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.polskieligi.dto.ProjectImportJob;

@Controller
public class ImportProjectsBatchController {

	final static Logger log = Logger.getLogger(ImportProjectsBatchController.class);

	@Autowired
	@Qualifier("pojectImportJobLauncher")
	private JobLauncher launcher;
	
	@Autowired
	@Qualifier("projectImportJob")
	Job job;
	
	@RequestMapping("/importProjectsBatch")
	public ModelAndView importProjectsBatch() {
		log.info("importProjectsBatch start");
		List<ProjectImportJob> rows = new ArrayList<ProjectImportJob>();
		JobParameter jp = new JobParameter(new Date());
		Map<String, JobParameter> map = new HashMap<String, JobParameter>();
		map.put("startDate", jp);
		JobParameters params= new JobParameters(map);
		JobExecution execution = null;
		try {
			execution = launcher.run(job, params);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			throw new RuntimeException(e);
		}
		ProjectImportJob pij = new ProjectImportJob();
		pij.setJobExecution(execution);
		pij.setProgress(Long.valueOf(0));
		pij.setProcessingTime(Long.valueOf(0));//TODO
		rows.add(pij);
		ModelAndView mv = new ModelAndView("updateProject", "rows", rows);
		return mv;
	}
}
