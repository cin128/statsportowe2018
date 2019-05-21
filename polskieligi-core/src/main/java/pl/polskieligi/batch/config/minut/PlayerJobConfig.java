package pl.polskieligi.batch.config.minut;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultScheduler;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Player;

@Configuration
@EnableBatchProcessing
public class PlayerJobConfig extends AbstractMinutJobConfig<Player> {

	@Override protected String getMaxPropertyName() {
		return Config.MINUT_PLAYER_MAX;
	}

	@Override protected Class<Player> getClazz() {
		return Player.class;
	}

	@Override protected String getUpdateQuery(){
		return super.getUpdateQuery()+" OR (name IS NULL)";
	}

	@Bean
	public Job minutPlayerImportJob(@Value("${minut.player.end}")Integer defaultMaxValue) {
		return getJob(defaultMaxValue);
	}

	@Bean
	public DefaultScheduler minutPlayerImportScheduler(@Qualifier("minutPlayerImportJob")Job job){
		return getScheduler(job);
	}
}
