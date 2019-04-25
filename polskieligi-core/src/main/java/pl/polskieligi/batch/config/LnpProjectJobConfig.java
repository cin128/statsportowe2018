package pl.polskieligi.batch.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.log.lnp.ImportLnpTeamLeaguesLogic;
import pl.polskieligi.model.Project;

@Configuration
public class LnpProjectJobConfig extends AbstractJobConfig<Project>{

	@Override
	protected String getMaxPropertyName() {
		return null;
	}

	@Override
	protected Class<Project> getClazz() {
		return Project.class;
	}
	
	@Bean
	public JpaPagingItemReader<Project> lnpProjectUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}
	
	@Bean public DefaultItemProcessor<Project> lnpProjectUpdateProcessor(ImportLnpTeamLeaguesLogic importLnpTeamLeaguesLogic) {
		return getProcessor(importLnpTeamLeaguesLogic, Project::getLnp_id);
	}
	
	protected String getUpdateQueryWhereClause() {
		return " where lnp_id IS NOT NULL AND lnpIdName IS NOT NULL AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}
}
