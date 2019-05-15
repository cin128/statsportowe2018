package pl.polskieligi.batch.config.lnp;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportLnpProjectMatchesLogic;
import pl.polskieligi.model.Project;

@Configuration
public class LnpProjectJobConfig extends AbstractJobConfig<Project> {

	@Override
	protected Class<Project> getClazz() {
		return Project.class;
	}
	
	@Bean
	public JpaPagingItemReader<Project> lnpProjectUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}
	
	@Bean public DefaultItemProcessor<Project> lnpProjectUpdateProcessor(ImportLnpProjectMatchesLogic importLnpProjectMatchesLogic) {
		return getProcessor(importLnpProjectMatchesLogic, Project::getLnp_id, Project::getImportLnpStatus);
	}
	
	protected String getUpdateQueryWhereClause() {
		return " where lnp_id IS NOT NULL AND lnpIdName IS NOT NULL AND (importLnpStatus IS NULL OR importLnpStatus = 1)";
	}
}