package pl.polskieligi.batch.player;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import pl.polskieligi.model.Player;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

public class PlayerImportReader implements ItemReader<Player> {
	final static Logger log = Logger.getLogger(PlayerImportReader.class);

	@Value("${minut.player.start.id}")
	private Integer start;
	@Value("${minut.player.end.id}")
	private Integer end;
	private Integer index;

	@PostConstruct public void initService() {
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
