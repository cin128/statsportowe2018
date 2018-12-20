package pl.polskieligi.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import pl.polskieligi.model.Player;

import javax.annotation.Resource;
import java.util.Properties;

public class PlayerImportReader implements ItemReader<Player> {
		private final static String MINUT_START = "minut.start.id";
		private final static String MINUT_END = "minut.end.id";

		private final Integer START = 9388;//68
		private final Integer END = 9390;//10528

		@Resource(name="applicationProperties") Properties properties;

		final static Logger log = Logger.getLogger(pl.polskieligi.batch.PlayerImportReader.class);

		private Integer index = START;
		@Override
		public Player read() {
		/*Integer.parseInt(properties.getProperty(MINUT_START, "1"));
		Integer.parseInt(properties.getProperty(MINUT_END, "36211"))*/;
			log.debug("Read: "+index);
			if(index<=END) {
				Player result = new Player();
				result.setMinut_id(index++);
				return result;
			}
			return null;
		}
	}
