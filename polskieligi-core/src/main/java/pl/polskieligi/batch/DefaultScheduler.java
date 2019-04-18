package pl.polskieligi.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class DefaultScheduler {

	final static Logger log = Logger.getLogger(DefaultScheduler.class);

	@Autowired
	private JobLauncher launcher;

	private final Job job;

	public DefaultScheduler(Job job){
		this.job=job;
	}

	public void run() {
		log.info("Run "+job.getName());
		try {
			Map<String,JobParameter> parameters = new HashMap<String,JobParameter>();
			parameters.put("time",new JobParameter(System.currentTimeMillis()));
			JobParameters jobParameters =
					new JobParameters(parameters);
			launcher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
			log.error(e.getMessage(), e);
		} catch (JobRestartException e) {
			log.error(e.getMessage(), e);
		} catch (JobInstanceAlreadyCompleteException e) {
			log.error(e.getMessage(), e);
		} catch (JobParametersInvalidException e) {
			log.error(e.getMessage(), e);
		}
	}
}
