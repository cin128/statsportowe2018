package pl.polskieligi.batch.config.lnp;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportLnpTeamLeagueClubLogic;
import pl.polskieligi.model.TeamLeague;

import javax.persistence.EntityManagerFactory;

@Configuration
public class LnpTeamLeagueClubJobConfig extends AbstractJobConfig<TeamLeague> {

	@Override protected Class<TeamLeague> getClazz() {
		return TeamLeague.class;
	}

	@Bean
	public JpaPagingItemReader<TeamLeague> lnpTlClubUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	@Bean public DefaultItemProcessor<TeamLeague> lnpTlClubProcessor(ImportLnpTeamLeagueClubLogic importLnpTeamLeagueClubLogic) {
		return getProcessor(importLnpTeamLeagueClubLogic, TeamLeague::getLnp_id, TeamLeague::getImportLnpStatus);
	}

	protected String getUpdateQueryWhereClause() {
		return " where lnp_id IS NOT NULL AND lnpIdName IS NOT NULL AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}
}
