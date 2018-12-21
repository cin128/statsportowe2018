package pl.polskieligi.batch.player;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.log.minut.ImportMinutPlayerLogic;
import pl.polskieligi.model.Player;

public class PlayerProcessor implements ItemProcessor<Player, Object> {

	final static Logger log = Logger.getLogger(PlayerProcessor.class);

	@Autowired ImportMinutPlayerLogic importPlayerLogic;

	public Object process(Player p) {
		log.info("Process: " + p.getMinut_id());
		Player result = importPlayerLogic.doImport(p.getMinut_id());
		return result;
	}
}
