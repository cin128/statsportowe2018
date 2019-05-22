package pl.polskieligi.batch.config;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
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

	protected Job getJob(Integer defaultMaxValue, ImportLogic<T> importLogic,  BiConsumer<T, Integer> setObjectId, Function<T, Integer> getObjectId, Function<T, Integer> getImportStatus) {
		DefaultItemReader<T> importReader = getImportReader(defaultMaxValue, setObjectId);
		JpaPagingItemReader<T> updateReader = getUpdateReader();
		DefaultItemProcessor<T> processor = getProcessor(importLogic, getObjectId, getImportStatus);
		Step importStep = getImportStep(getImportStepName(), importReader, processor);
		Step updateStep = getUpdateStep(getUpdateStepName(), updateReader, processor);
		DefaultJobExecutionListener importJobExecutionListener = getImportJobExecutionListener(importReader);
		return getJob(getJobName(), importJobExecutionListener, importStep, updateStep);
	}


	protected Job getJob(String jobName, DefaultJobExecutionListener jobExecutionListener, Step... steps) {
		SimpleJobBuilder sjb = this.jobBuilderFactory.get(jobName).start(steps[0]);
		for(int i=1; i<steps.length; i++){
			sjb = sjb.next(steps[i]);
		}
		if(jobExecutionListener!=null) {
			return sjb.listener(jobExecutionListener).build();
		} else {
			return  sjb.build();
		}
	}

	protected Step getImportStep(String stepName, ItemReader<T> importReader, DefaultItemProcessor<T> processor) {
		return getStep(stepName, importReader, processor, false);
	}

	protected Step getUpdateStep(String stepName, ItemReader<T> importReader, DefaultItemProcessor<T> processor) {
		return getStep(stepName, importReader, processor, true);
	}

	private Step getStep(String stepName, ItemReader<T> importReader, DefaultItemProcessor<T> processor, boolean addStepListener) {
		SimpleStepBuilder<T, Object> sb = this.stepBuilderFactory.get(stepName)
				.<T, Object>chunk(1)
				.reader(importReader)
				.processor(processor)
				.writer(new EmptyWriter());
		if(addStepListener) {
			return sb.listener(new DefaultStepExecutionListener(getUpdateCountQuery(), genericDao)).build();
		} else {
			return sb.build();
		}
	}


	protected DefaultItemProcessor<T> getProcessor(ImportLogic<T> importLogic, Function<T, Integer> getObjectId, Function<T, Integer> getImportStatus) {
		return new DefaultItemProcessor<T>(getMaxPropertyName(), importLogic, getObjectId, getImportStatus, configDAO);
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

	protected abstract String getUpdateQueryWhereClause();

	protected String getUpdateQuery(){
		return "FROM "+getClazz().getSimpleName() +getUpdateQueryWhereClause();
	}

	protected String getUpdateCountQuery(){
		return "SELECT COUNT(*) "+getUpdateQuery();
	}

	protected abstract String getPrefix();

	protected String getJobName() {
		return getPrefix()+getClazz().getSimpleName()+"ImportJob";
	}

	protected String getImportStepName() {
		return getPrefix()+getClazz().getSimpleName()+"ImportStep";
	}

	protected String getUpdateStepName() {
		return getPrefix()+getClazz().getSimpleName()+"UpdateStep";
	}

}
