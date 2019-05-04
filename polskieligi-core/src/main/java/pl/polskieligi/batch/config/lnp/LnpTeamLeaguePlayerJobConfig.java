package pl.polskieligi.batch.config.lnp;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportLnpTeamLeaguePlayersLogic;
import pl.polskieligi.model.TeamLeague;

public class LnpTeamLeaguePlayerJobConfig extends AbstractJobConfig<TeamLeague> {
	@Override protected Class<TeamLeague> getClazz() {
		return TeamLeague.class;
	}

	@Bean
	public JpaPagingItemReader<TeamLeague> lnpTlClubUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	@Bean public DefaultItemProcessor<TeamLeague> lnpTlClubProcessor(ImportLnpTeamLeaguePlayersLogic importLnpTeamLeaguePlayersLogic) {
		return getProcessor(importLnpTeamLeaguePlayersLogic, TeamLeague::getLnp_id, TeamLeague::getImportLnpStatus);
	}

	protected String getUpdateQueryWhereClause() {
		return " where lnp_id IS NOT NULL AND lnpIdName IS NOT NULL AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}
}
