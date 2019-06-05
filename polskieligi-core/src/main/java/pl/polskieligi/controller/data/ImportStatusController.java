package pl.polskieligi.controller.data;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.polskieligi.dto.ImportJob;

@Controller
public class ImportStatusController {

	final static Logger log = Logger.getLogger(ImportStatusController.class);

	@Autowired
	JobExplorer jobExplorer;

	@RequestMapping("/importStatus")
	public ModelAndView showUpdateInfo() {
		log.info("importStatus start");
		List<ImportJob> rows = new ArrayList<ImportJob>();
		for (String jobName : jobExplorer.getJobNames()) {

			List<JobInstance> list = jobExplorer.findJobInstancesByJobName(jobName, 0, 20);
			for (JobInstance jobInstance : list) {
				List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
				for (JobExecution jobExecution : jobExecutions) {
					ImportJob row = new ImportJob();
					row.setJobExecution(jobExecution);
					for(StepExecution se: jobExecution.getStepExecutions()){
						Long total = getTotal(se);
						if(total!=null){
							row.setTotal(total);
						}
					}

					Date endTime = jobExecution.getEndTime();
					if(endTime!=null) {
						Duration d = Duration.ofMillis(endTime.getTime()-jobExecution.getStartTime().getTime());
						String sd = LocalTime.MIDNIGHT.plus(d).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
						long days = d.toDays();
						row.setProcessingTime(days+"d "+sd);
					}
					rows.add(row);
				}
			}
		}
		Collections.sort(rows,(ImportJob o1, ImportJob o2) -> o1.getJobExecution().getId().intValue()-o2.getJobExecution().getId().intValue());
		
		ModelAndView mv = new ModelAndView("views/importStatus", "rows", rows);
		return mv;
	}
	
	private Long getTotal(StepExecution stepExecution) {
		if (stepExecution != null) {
			ExecutionContext ec = stepExecution.getExecutionContext();
			if (ec != null &&  ec.containsKey("jobComplete")) {
				return (Long)ec.get("jobComplete");
			} 
		} 
		return null;
	}
}
