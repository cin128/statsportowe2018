package pl.polskieligi.controller.data;

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
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dto.ImportJob;

public abstract class AbstractImportController {
	final static Logger log = Logger.getLogger(AbstractImportController.class);
	
	@Autowired
	private JobLauncher launcher;

	protected ModelAndView importBatch(Job job) {
		log.info(job.getName()+" start");
		List<ImportJob> rows = new ArrayList<ImportJob>();
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
		ImportJob pij = new ImportJob();
		pij.setJobExecution(execution);
		pij.setTotal(Long.valueOf(0));
		rows.add(pij);
		ModelAndView mv = new ModelAndView("views/importStatus", "rows", rows);
		return mv;
	}
}
