package pl.polskieligi.batch.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.log.minut.ImportMinutRefereeLogic;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Referee;

@Configuration
public class RefereeJobConfig extends AbstractJobConfig<Referee>{
	@Override protected String getMaxPropertyName() {
		return Config.MINUT_REFEREE_MAX;
	}

	@Override protected Class<Referee> getClazz() {
		return Referee.class;
	}

	@Bean public DefaultItemProcessor<Referee> refereeProcessor(ImportMinutRefereeLogic importRefereeLogic) {
		return getProcessor(importRefereeLogic, Referee::getMinut_id);
	}

	@Bean public DefaultItemReader<Referee> refereeImportReader( @Value("${minut.referee.end}")Integer defaultMaxValue) {
		return getImportReader(defaultMaxValue);
	}

	@Bean public DefaultJobExecutionListener refereeImportJobExecutionListener(@Qualifier("refereeImportReader") DefaultItemReader<Referee> refereeImportReader) {
		return getImportJobExecutionListener(refereeImportReader);
	}

	@Bean
	public JpaPagingItemReader<Referee> refereeUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (name IS NULL)";
	}

	@Bean
	public DefaultScheduler refereeImportScheduler(@Qualifier("refereeImportJob")Job job){
		return getScheduler(job);
	}

	@Bean
	public DefaultScheduler refereeUpdateScheduler(@Qualifier("refereeUpdateJob")Job job){
		return getScheduler(job);
	}
}
