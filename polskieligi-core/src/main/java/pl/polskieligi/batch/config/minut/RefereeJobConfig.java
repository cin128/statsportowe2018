package pl.polskieligi.batch.config.minut;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Referee;

@Configuration
@EnableBatchProcessing
public class RefereeJobConfig extends AbstractMinutJobConfig<Referee> {

	@Override protected String getMaxPropertyName() {
		return Config.MINUT_REFEREE_MAX;
	}

	@Override protected Class<Referee> getClazz() {
		return Referee.class;
	}

	@Override protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (name IS NULL)";
	}

	@Bean
	public Job minutRefereeImportJob(@Value("${minut.referee.end}")Integer defaultMaxValue) {
		return getJob(defaultMaxValue);
	}

	@Bean
	public DefaultScheduler minutRefereeImportScheduler(@Qualifier("minutRefereeImportJob")Job job){
		return getScheduler(job);
	}
}
