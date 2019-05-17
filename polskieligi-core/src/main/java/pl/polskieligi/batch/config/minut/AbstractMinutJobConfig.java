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
		DefaultItemReader<T> importReader = getImportReader(defaultMaxValue, T::setMinut_id);
		JpaPagingItemReader<T> updateReader = getUpdateReader();
		DefaultItemProcessor<T> processor = getProcessor(importMinutLogic, T::getMinut_id, T::getImportStatus);
		Step importStep = getStep(getImportStepName(), importReader, processor);
		Step updateStep = getStep(getUpdateStepName(), updateReader, processor);
		DefaultJobExecutionListener refereeImportJobExecutionListener = getImportJobExecutionListener(importReader);
		return getJob(getJobName(), refereeImportJobExecutionListener, importStep, updateStep);
	}
}
