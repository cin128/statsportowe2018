package pl.polskieligi.batch.project;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.dao.ProjectDAO;

public class ProjectUpdateJobExecutionListener implements JobExecutionListener {

	final static Logger log = Logger.getLogger(ProjectUpdateJobExecutionListener.class);

	@Autowired
	ProjectDAO projectDAO;
	
	public void beforeJob(JobExecution jobExecution) {
		log.debug("beforeJob: "+jobExecution);
		Double total = projectDAO.getOpenProjectsCount().doubleValue();
		jobExecution.getExecutionContext().put("jobComplete", total);		
	}

	public void afterJob(JobExecution jobExecution) {
		log.debug("afterJob: "+jobExecution);
	}

}
