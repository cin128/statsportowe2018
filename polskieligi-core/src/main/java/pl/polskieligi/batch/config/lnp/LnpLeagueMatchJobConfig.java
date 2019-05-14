package pl.polskieligi.batch.config.lnp;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportLnpLeagueMatchLogic;
import pl.polskieligi.model.LeagueMatch;

import javax.persistence.EntityManagerFactory;

@Configuration
public class LnpLeagueMatchJobConfig extends AbstractJobConfig<LeagueMatch> {
	@Override protected Class<LeagueMatch> getClazz() {
		return LeagueMatch.class;
	}

	@Bean
	public JpaPagingItemReader<LeagueMatch> lnpLmUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	@Bean public DefaultItemProcessor<LeagueMatch> lnpLmProcessor(
			ImportLnpLeagueMatchLogic importLnpLeagueMatchPlayersLogic) {
		return getProcessor(importLnpLeagueMatchPlayersLogic, LeagueMatch::getLnp_id, LeagueMatch::getImportLnpStatus);
	}

	protected String getUpdateQueryWhereClause() {
		return " where minut_id=0 AND lnp_id IS NOT NULL AND lnpIdName IS NOT NULL AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}
}
