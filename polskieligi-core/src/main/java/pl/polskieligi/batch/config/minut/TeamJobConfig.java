package pl.polskieligi.batch.config.minut;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Team;

@Configuration
@EnableBatchProcessing
public class TeamJobConfig extends AbstractMinutJobConfig<Team> {
	@Override protected String getMaxPropertyName() {
		return Config.MINUT_TEAM_MAX;
	}

	@Override protected Class<Team> getClazz() {
		return Team.class;
	}

	@Override protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (name IS NULL)";
	}

	@Bean
	public Job teamImportJob(@Value("${minut.team.end}")Integer defaultMaxValue) {
		return getJob(defaultMaxValue);
	}

	@Bean
	public DefaultScheduler teamImportScheduler(@Qualifier("teamImportJob")Job job){
		return getScheduler(job);
	}
}
