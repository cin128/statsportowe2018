package pl.polskieligi.batch.config.lnp;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.lnp.ImportClubLogic;
import pl.polskieligi.model.Club;
import pl.polskieligi.model.Config;

import javax.persistence.EntityManagerFactory;

@Configuration
public class LnpClubJobConfig extends AbstractLnpJobConfig<Club> {

	@Override protected String getMaxPropertyName() {
		return Config.LNP_CLUB_MAX;
	}

	@Override protected Class<Club> getClazz() {
		return Club.class;
	}

	@Bean
	public Job lnpClubImportJob(@Value("${lnp.club.start}")Integer defaultStartValue) {
		return getJob(defaultStartValue);
	}

	protected String getUpdateQueryWhereClause(){
		return super.getUpdateQueryWhereClause()+" OR (name IS NULL)";
	}
}
