package pl.polskieligi.batch.config;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.log.ImportLogic;

public abstract class AbstractJobConfig<T> {

	protected DefaultItemProcessor<T> getProcessor(ImportLogic<T> importLogic, Function<T, Integer> getObjectId, Function<T, Integer> getImportStatus) {
		return new DefaultItemProcessor<T>(getMaxPropertyName(), importLogic, getObjectId, getImportStatus);
	}


	protected DefaultItemReader<T> getImportReader(Integer defaultStartValue, BiConsumer<T, Integer> setObjectId) {
		return getImportReader(defaultStartValue, null, setObjectId);
	}
	
	protected DefaultItemReader<T> getImportReader(Integer defaultStartValue, Integer defaultEndValue, BiConsumer<T, Integer> setObjectId) {
		return new DefaultItemReader<T>(getMaxPropertyName(), defaultStartValue, defaultEndValue, setObjectId, getClazz());
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
}
