package pl.polskieligi.batch.config.lnp;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.AbstractImportLnpLogic;
import pl.polskieligi.model.LnpObject;

public abstract class AbstractLnpJobConfig <T extends LnpObject> extends AbstractJobConfig<T> {

	@Autowired
	private AbstractImportLnpLogic<T> importLnpLogic;

	protected Job getJob() {
		JpaPagingItemReader<T> updateReader = getUpdateReader();
		DefaultItemProcessor<T> processor = getProcessor(importLnpLogic, T::getLnp_id, T::getImportLnpStatus);
		Step updateStep = getUpdateStep(getUpdateStepName(), updateReader, processor);
		return getJob(getJobName(), null, updateStep);
	}

	protected Job getJob(Integer defaultMaxValue) {
		return getJob(defaultMaxValue, importLnpLogic, T::setLnp_id, T::getLnp_id, T::getImportLnpStatus);
	}

	@Override protected String getUpdateQueryWhereClause() {
		return " where lnp_id IS NOT NULL AND lnpIdName IS NOT NULL AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}

	@Override protected String getPrefix() {
		return "lnp";
	}
}
