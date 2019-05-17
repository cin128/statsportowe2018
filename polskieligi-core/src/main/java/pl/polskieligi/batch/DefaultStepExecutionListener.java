package pl.polskieligi.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import pl.polskieligi.dao.GenericDAO;

public class DefaultStepExecutionListener implements StepExecutionListener{
	final static Logger log = Logger.getLogger(DefaultStepExecutionListener.class);

	private final String query;

	private final GenericDAO dao;

	public DefaultStepExecutionListener(String query, GenericDAO dao){
		this.query = query;
		this.dao = dao;
	}

	@Override public void beforeStep(StepExecution stepExecution) {
		log.debug("beforeStep: "+stepExecution);
		Long total = dao.getCount(query);
		stepExecution.getExecutionContext().put("jobComplete", total);
	}

	@Override public ExitStatus afterStep(StepExecution stepExecution) {
		log.debug("afterStep: "+stepExecution);
		return null;
	}
}
