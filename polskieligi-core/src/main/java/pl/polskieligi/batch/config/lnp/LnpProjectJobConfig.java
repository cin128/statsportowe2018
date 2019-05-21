package pl.polskieligi.batch.config.lnp;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportLnpProjectMatchesLogic;
import pl.polskieligi.model.Project;

@Configuration
public class LnpProjectJobConfig extends AbstractLnpJobConfig<Project> {

	@Override
	protected Class<Project> getClazz() {
		return Project.class;
	}

	@Bean
	public Job lnpProjectUpdateJob() {
		return getJob();
	}

}
