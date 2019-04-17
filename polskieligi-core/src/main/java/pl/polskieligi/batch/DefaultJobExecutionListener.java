package pl.polskieligi.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;


public class DefaultJobExecutionListener  implements JobExecutionListener {

	final static Logger log = Logger.getLogger(DefaultJobExecutionListener.class);

	private DefaultItemReader itemReader;

	public DefaultJobExecutionListener(DefaultItemReader itemReader){
		this.itemReader = itemReader;
	}

	public void beforeJob(JobExecution jobExecution) {
		log.debug("beforeJob: "+jobExecution);
		itemReader.initService();
	}

	public void afterJob(JobExecution jobExecution) {
		log.debug("afterJob: "+jobExecution);
	}
}
