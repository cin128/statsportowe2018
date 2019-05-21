package pl.polskieligi.batch.config.minut;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.minut.AbstractImportMinutLogic;
import pl.polskieligi.model.MinutObject;

public abstract class AbstractMinutJobConfig<T extends MinutObject> extends AbstractJobConfig<T> {

	@Autowired
	private AbstractImportMinutLogic<T> importMinutLogic;

	protected Job getJob(Integer defaultMaxValue) {
		return getJob(defaultMaxValue, importMinutLogic, T::setMinut_id, T::getMinut_id, T::getImportStatus);
	}

	protected String getUpdateQueryWhereClause() {
		return " where importStatus = 1";
	}

	@Override protected String getPrefix() {
		return "minut";
	}
}
