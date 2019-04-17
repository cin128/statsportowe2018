package pl.polskieligi.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultUpdateScheduler;

import javax.persistence.EntityManagerFactory;

@Configuration
public class ProjectJobConfig {

	@Bean
	public DefaultUpdateScheduler projectUpdateScheduler(@Qualifier("projectUpdateJob")Job job){
		return new DefaultUpdateScheduler(job);
	}

	@Bean
	public JpaPagingItemReader projectUpdateReader(EntityManagerFactory entityManagerFactory){
		JpaPagingItemReader reader = new JpaPagingItemReader();
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString("FROM Project where (type = 1 AND archive = FALSE) OR importStatus = 1");
		reader.setPageSize(1000);
		return reader;
	}
}
