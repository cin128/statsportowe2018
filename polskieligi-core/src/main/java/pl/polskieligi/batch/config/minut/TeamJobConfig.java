package pl.polskieligi.batch.config.minut;

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
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.minut.ImportMinutTeamLogic;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Team;

@Configuration
public class TeamJobConfig extends AbstractJobConfig<Team> {
	@Override protected String getMaxPropertyName() {
		return Config.MINUT_TEAM_MAX;
	}

	@Override protected Class<Team> getClazz() {
		return Team.class;
	}

	@Bean public DefaultItemProcessor<Team> teamProcessor(ImportMinutTeamLogic importTeamLogic) {
		return getProcessor(importTeamLogic, Team::getMinut_id, Team::getImportStatus);
	}

	@Bean public DefaultItemReader<Team> teamImportReader( @Value("${minut.team.end}")Integer defaultMaxValue) {
		return getImportReader(defaultMaxValue, Team::setMinut_id);
	}

	@Bean public DefaultJobExecutionListener teamImportJobExecutionListener(@Qualifier("teamImportReader") DefaultItemReader<Team> teamImportReader) {
		return getImportJobExecutionListener(teamImportReader);
	}

	@Bean
	public JpaPagingItemReader<Team> teamUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (name IS NULL)";
	}

	@Bean
	public DefaultScheduler teamImportScheduler(@Qualifier("teamImportJob")Job job){
		return getScheduler(job);
	}

	@Bean
	public DefaultScheduler teamUpdateScheduler(@Qualifier("teamUpdateJob")Job job){
		return getScheduler(job);
	}
}
