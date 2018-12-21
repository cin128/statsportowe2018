package pl.polskieligi.batch.player;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import pl.polskieligi.model.Player;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

public class PlayerImportReader implements ItemReader<Player> {
	private final static String MINUT_START = "minut.player.start.id";
	private final static String MINUT_END = "minut.player.end.id";

	@Resource(name = "applicationProperties") Properties properties;

	final static Logger log = Logger.getLogger(PlayerImportReader.class);

	private Integer start;
	private Integer end;
	private Integer index;

	@PostConstruct public void initService() {
		start = Integer.parseInt(properties.getProperty(MINUT_START, "1"));
		end = Integer.parseInt(properties.getProperty(MINUT_END, "36211"));
		index = start;
	}

	@Override public Player read() {
		log.debug("Read: " + index);
		if (index <= end) {
			Player result = new Player();
			result.setMinut_id(index++);
			return result;
		}
		return null;
	}
}
