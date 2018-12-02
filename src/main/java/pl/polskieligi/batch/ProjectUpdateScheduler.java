package pl.polskieligi.batch;

import java.util.HashMap;
import java.util.Map;

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

public class ProjectUpdateScheduler {
	@Autowired
	@Qualifier("pojectUpdateJobLauncher")
	private JobLauncher launcher;
	
	@Autowired
	@Qualifier("projectUpdateJob")
	private Job job;

	private JobExecution execution;	

	public void run() {
		try {
			Map<String,JobParameter> parameters = new HashMap<String,JobParameter>();
			parameters.put("time",new JobParameter(System.currentTimeMillis()));
			JobParameters jobParameters =
					new JobParameters(parameters);
			execution = launcher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
		} catch (JobRestartException e) {
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}	
}
