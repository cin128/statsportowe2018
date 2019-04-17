package pl.polskieligi.batch.config;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.log.minut.ImportMinutLogic;
import pl.polskieligi.model.MinutObject;

public abstract class AbstractJobConfig<T extends MinutObject> {

	protected DefaultItemProcessor<T> getProcessor(ImportMinutLogic<T> importLogic) {
		return new DefaultItemProcessor<T>(getMaxPropertyName(), importLogic);
	}

	protected DefaultItemReader<T> getImportReader(Integer defaultMaxValue) {
		return new DefaultItemReader<T>(getMaxPropertyName(), defaultMaxValue, getClazz());
	}

	protected DefaultJobExecutionListener getUpdateJobExecutionListener(DefaultItemReader<T> importReader) {
		return new DefaultJobExecutionListener(importReader);
	}

	protected abstract String getMaxPropertyName();

	protected abstract Class<T> getClazz();
}
