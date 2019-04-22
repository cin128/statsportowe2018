package pl.polskieligi.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Project;

import javax.persistence.EntityManagerFactory;

@Configuration
public class ProjectJobConfig extends AbstractJobConfig<Project>{

	@Override protected String getMaxPropertyName() {
		return Config.MINUT_PROJECT_MAX;
	}

	@Override protected Class<Project> getClazz() {
		return Project.class;
	}

	@Bean public DefaultItemReader<Project> projectImportReader( @Value("${minut.project.end}")Integer defaultMaxValue) {
		return getImportReader(defaultMaxValue);
	}

	@Bean public DefaultJobExecutionListener projectImportJobExecutionListener(@Qualifier("projectImportReader") DefaultItemReader<Project> projectImportReader) {
		return getImportJobExecutionListener(projectImportReader);
	}

	@Bean
	public JpaPagingItemReader<Project> projectUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (type = 1 AND archive = FALSE)";
	}

	@Bean
	public DefaultScheduler projectImportScheduler(@Qualifier("projectImportJob")Job job){
		return getScheduler(job);
	}

	@Bean
	public DefaultScheduler projectUpdateScheduler(@Qualifier("projectUpdateJob")Job job){
		return getScheduler(job);
	}
}
