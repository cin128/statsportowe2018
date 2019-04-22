package pl.polskieligi.batch.config;

import java.util.function.Function;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.log.ImportLogic;
import pl.polskieligi.model.MinutObject;

public abstract class AbstractJobConfig<T extends MinutObject> {

	protected DefaultItemProcessor<T> getProcessor(ImportLogic<T> importLogic, Function<T, Integer> getObjectId) {
		return new DefaultItemProcessor<T>(getMaxPropertyName(), importLogic, getObjectId);
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

	protected JpaPagingItemReader<T> getUpdateReader(EntityManagerFactory entityManagerFactory){
		JpaPagingItemReader<T> reader = new JpaPagingItemReader<T>();
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString(getUpdateQuery());
		reader.setPageSize(1000);
		return reader;
	}

	protected abstract String getMaxPropertyName();

	protected abstract Class<T> getClazz();

	protected String getUpdateQuery(){
		return "FROM "+getClazz().getSimpleName() +getUpdateQueryWhereClause();
	}
	
	protected String getUpdateQueryWhereClause() {
		return " where importStatus = 1";
	}
}
