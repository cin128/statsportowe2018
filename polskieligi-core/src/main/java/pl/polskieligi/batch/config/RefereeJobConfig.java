package pl.polskieligi.batch.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.polskieligi.batch.DefaultItemProcessor;
import pl.polskieligi.batch.DefaultItemReader;
import pl.polskieligi.batch.DefaultJobExecutionListener;
import pl.polskieligi.log.minut.ImportMinutPlayerLogic;
import pl.polskieligi.log.minut.ImportMinutRefereeLogic;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.Referee;

@Configuration
public class RefereeJobConfig extends AbstractJobConfig<Referee>{
	@Override protected String getMaxPropertyName() {
		return Config.MINUT_REFEREE_MAX;
	}

	@Override protected Class<Referee> getClazz() {
		return Referee.class;
	}

	@Bean public DefaultItemProcessor refereeProcessor(ImportMinutRefereeLogic importRefereeLogic) {
		return getProcessor(importRefereeLogic);
	}

	@Bean public DefaultItemReader<Referee> refereeImportReader( @Value("${minut.referee.end}")Integer defaultMaxValue) {
		return getImportReader(defaultMaxValue);
	}

	@Bean public DefaultJobExecutionListener refereeUpdateJobExecutionListener(@Qualifier("refereeImportReader") DefaultItemReader refereeImportReader) {
		return getUpdateJobExecutionListener(refereeImportReader);
	}
}
