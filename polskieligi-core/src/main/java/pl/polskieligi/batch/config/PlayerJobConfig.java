package pl.polskieligi.batch.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.log.minut.ImportMinutPlayerLogic;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Player;

@Configuration
public class PlayerJobConfig extends AbstractJobConfig<Player>{

	@Override protected String getMaxPropertyName() {
		return Config.MINUT_PLAYER_MAX;
	}

	@Override protected Class<Player> getClazz() {
		return Player.class;
	}

	@Bean public DefaultItemProcessor<Player> playerProcessor(ImportMinutPlayerLogic importPlayerLogic) {
		return getProcessor(importPlayerLogic);
	}

	@Bean public DefaultItemReader<Player> playerImportReader( @Value("${minut.player.end}")Integer defaultMaxValue) {
		return getImportReader(defaultMaxValue);
	}

	@Bean public DefaultJobExecutionListener playerUpdateJobExecutionListener(@Qualifier("playerImportReader") DefaultItemReader<Player> playerImportReader) {
		return getUpdateJobExecutionListener(playerImportReader);
	}
}
