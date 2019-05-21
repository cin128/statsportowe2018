package pl.polskieligi.batch.config.minut;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Project;

@Configuration
public class ProjectJobConfig extends AbstractMinutJobConfig<Project> {

	@Override protected String getMaxPropertyName() {
		return Config.MINUT_PROJECT_MAX;
	}

	@Override protected Class<Project> getClazz() {
		return Project.class;
	}

	@Override protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (type = 1 AND archive = FALSE)";
	}

	@Bean
	public Job minutProjectImportJob(@Value("${minut.project.end}")Integer defaultMaxValue) {
		return getJob(defaultMaxValue);
	}

	@Bean
	public DefaultScheduler minutProjectImportScheduler(@Qualifier("minutProjectImportJob")Job job){
		return getScheduler(job);
	}

}
