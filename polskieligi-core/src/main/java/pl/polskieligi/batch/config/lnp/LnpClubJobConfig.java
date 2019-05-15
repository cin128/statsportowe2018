package pl.polskieligi.batch.config.lnp;

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
public class LnpClubJobConfig extends AbstractJobConfig<Club> {

	@Override protected String getMaxPropertyName() {
		return Config.LNP_CLUB_MAX;
	}

	@Override protected Class<Club> getClazz() {
		return Club.class;
	}

	@Bean public DefaultItemProcessor<Club> clubProcessor(ImportClubLogic importClubLogic) {
		return getProcessor(importClubLogic, Club::getLnp_id, Club::getImportLnpStatus);
	}

	@Bean public DefaultItemReader<Club> clubImportReader( @Value("${lnp.club.start}")Integer defaultStartValue, @Value("${lnp.club.end}")Integer defaultEndValue) {
		return getImportReader(defaultStartValue, defaultEndValue, Club::setLnp_id);
	}

	@Bean public DefaultJobExecutionListener clubImportJobExecutionListener(@Qualifier("clubImportReader") DefaultItemReader<Club> clubImportReader) {
		return getImportJobExecutionListener(clubImportReader);
	}

	@Bean
	public JpaPagingItemReader<Club> clubUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (name IS NULL)";
	}
}