package pl.polskieligi.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import pl.polskieligi.dto.ImportJob;

import java.util.*;

public abstract class AbstractImportController {

	@Autowired
	private JobLauncher launcher;

	protected ModelAndView importBatch(Job job) {
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
		pij.setProgress(Long.valueOf(0));
		pij.setProcessingTime(Long.valueOf(0));
		rows.add(pij);
		ModelAndView mv = new ModelAndView("views/importStatus", "rows", rows);
		return mv;
	}
}
