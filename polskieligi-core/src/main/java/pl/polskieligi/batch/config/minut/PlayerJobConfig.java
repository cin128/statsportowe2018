package pl.polskieligi.batch.config.minut;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.batch.config.AbstractJobConfig;
import pl.polskieligi.log.minut.ImportMinutPlayerLogic;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Player;

@Configuration
public class PlayerJobConfig extends AbstractJobConfig<Player> {

	@Override protected String getMaxPropertyName() {
		return Config.MINUT_PLAYER_MAX;
	}

	@Override protected Class<Player> getClazz() {
		return Player.class;
	}

	@Bean public DefaultItemProcessor<Player> playerProcessor(ImportMinutPlayerLogic importPlayerLogic) {
		return getProcessor(importPlayerLogic, Player::getMinut_id, Player::getImportStatus);
	}

	@Bean public DefaultItemReader<Player> playerImportReader( @Value("${minut.player.end}")Integer defaultMaxValue) {
		return getImportReader(defaultMaxValue, Player::setMinut_id);
	}

	@Bean public DefaultJobExecutionListener playerImportJobExecutionListener(@Qualifier("playerImportReader") DefaultItemReader<Player> playerImportReader) {
		return getImportJobExecutionListener(playerImportReader);
	}

	@Bean
	public JpaPagingItemReader playerUpdateReader(EntityManagerFactory entityManagerFactory){
		return  getUpdateReader(entityManagerFactory);
	}

	protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (name IS NULL)";
	}

	@Bean
	public DefaultScheduler playerImportScheduler(@Qualifier("playerImportJob")Job job){
		return getScheduler(job);
	}

	@Bean
	public DefaultScheduler playerUpdateScheduler(@Qualifier("playerUpdateJob")Job job){
		return getScheduler(job);
	}
}
