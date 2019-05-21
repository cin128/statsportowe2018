package pl.polskieligi.batch.config.lnp;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportLnpLeagueMatchLogic;
import pl.polskieligi.model.LeagueMatch;

import javax.persistence.EntityManagerFactory;

@Configuration
public class LnpLeagueMatchJobConfig extends AbstractLnpJobConfig<LeagueMatch> {
	@Override protected Class<LeagueMatch> getClazz() {
		return LeagueMatch.class;
	}

	@Bean
	public Job lnpLmUpdateJob() {
		return getJob();
	}

	protected String getUpdateQueryWhereClause() {
		return " where minut_id=0 AND lnp_id IS NOT NULL AND lnpIdName IS NOT NULL ";//TODO AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}
}
