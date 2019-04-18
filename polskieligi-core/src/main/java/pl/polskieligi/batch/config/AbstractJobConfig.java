package pl.polskieligi.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.log.minut.ImportMinutLogic;
import pl.polskieligi.model.MinutObject;

import javax.persistence.EntityManagerFactory;

public abstract class AbstractJobConfig<T extends MinutObject> {

	protected DefaultItemProcessor<T> getProcessor(ImportMinutLogic<T> importLogic) {
		return new DefaultItemProcessor<T>(getMaxPropertyName(), importLogic);
	}

	protected DefaultItemReader<T> getImportReader(Integer defaultMaxValue) {
		return new DefaultItemReader<T>(getMaxPropertyName(), defaultMaxValue, getClazz());
	}

	protected DefaultJobExecutionListener getImportJobExecutionListener(DefaultItemReader<T> importReader) {
		return new DefaultJobExecutionListener(importReader);
	}

	protected DefaultScheduler getScheduler(Job job){
		return new DefaultScheduler(job);
	}

	protected JpaPagingItemReader getUpdateReader(EntityManagerFactory entityManagerFactory){
		JpaPagingItemReader reader = new JpaPagingItemReader();
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString(getUpdateQuery());
		reader.setPageSize(1000);
		return reader;
	}

	protected abstract String getMaxPropertyName();

	protected abstract Class<T> getClazz();

	protected String getUpdateQuery(){
		return "FROM "+getClazz().getSimpleName() +" where importStatus = 1";
	}
}
