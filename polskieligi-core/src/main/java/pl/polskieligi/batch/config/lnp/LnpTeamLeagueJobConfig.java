package pl.polskieligi.batch.config.lnp;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportLnpTeamLeagueLogic;
import pl.polskieligi.model.TeamLeague;

@Configuration
public class LnpTeamLeagueJobConfig extends AbstractLnpJobConfig<TeamLeague> {
	@Override protected Class<TeamLeague> getClazz() {
		return TeamLeague.class;
	}

	@Bean
	public Job lnpTlUpdateJob() {
		return getJob();
	}

	protected String getUpdateQueryWhereClause() {
		return " where lnp_id IS NOT NULL AND lnpIdName IS NOT NULL AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}
}
