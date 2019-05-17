package pl.polskieligi.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.batch.*;
import pl.polskieligi.dao.ConfigDAO;
import pl.polskieligi.dao.GenericDAO;
import pl.polskieligi.log.ImportLogic;

import javax.persistence.EntityManagerFactory;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AbstractJobConfig<T> {

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ConfigDAO configDAO;

	@Autowired
	private GenericDAO genericDao;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	protected Job getJob(String jobName, DefaultJobExecutionListener jobExecutionListener, Step... steps) {
		SimpleJobBuilder sjb = this.jobBuilderFactory.get(jobName).start(steps[0]);
		for(int i=1; i<steps.length; i++){
			sjb = sjb.next(steps[i]);
		}
		return sjb.listener(jobExecutionListener)
				.build();
	}

	protected Step getStep(String stepName, ItemReader<T> importReader, DefaultItemProcessor<T> processor) {
		return this.stepBuilderFactory.get(stepName)
				.<T, Object>chunk(1)
				.reader(importReader)
				.processor(processor)
				.writer(new EmptyWriter())
				.build();
	}


	protected DefaultItemProcessor<T> getProcessor(ImportLogic<T> importLogic, Function<T, Integer> getObjectId, Function<T, Integer> getImportStatus) {
		return new DefaultItemProcessor<T>(getMaxPropertyName(), importLogic, getObjectId, getImportStatus);
	}

	protected DefaultItemReader<T> getImportReader(Integer defaultStartValue, BiConsumer<T, Integer> setObjectId) {
		return getImportReader(defaultStartValue, null, setObjectId);
	}
	
	protected DefaultItemReader<T> getImportReader(Integer defaultStartValue, Integer defaultEndValue, BiConsumer<T, Integer> setObjectId) {
		return new DefaultItemReader<T>(getMaxPropertyName(), defaultStartValue, defaultEndValue, setObjectId, getClazz(), configDAO);
	}

	protected DefaultJobExecutionListener getImportJobExecutionListener(DefaultItemReader<T> importReader) {
		return new DefaultJobExecutionListener(importReader);
	}

	protected DefaultScheduler getScheduler(Job job){
		return new DefaultScheduler(job);
	}

	protected JpaPagingItemReader<T> getUpdateReader(){
		JpaPagingItemReader<T> reader = new JpaPagingItemReader<T>();
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString(getUpdateQuery());
		reader.setPageSize(1000);
		return reader;
	}

	protected String getMaxPropertyName() {
		return null;
	}

	protected abstract Class<T> getClazz();

	protected String getUpdateQuery(){
		return "FROM "+getClazz().getSimpleName() +getUpdateQueryWhereClause();
	}
	
	protected String getUpdateQueryWhereClause() {
		return " where importStatus = 1";
	}

	protected String getJobName() {
		return getClazz().getSimpleName().toLowerCase()+"ImportJob";
	}

	protected String getImportStepName() {
		return getClazz().getSimpleName().toLowerCase()+"ImportStep";
	}

	protected String getUpdateStepName() {
		return getClazz().getSimpleName().toLowerCase()+"UpdateStep";
	}

}
