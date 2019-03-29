package pl.polskieligi.batch.referee;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.log.minut.ImportMinutRefereeLogic;
import pl.polskieligi.model.Referee;

public class RefereeProcessor  implements ItemProcessor<Referee, Object> {
	final static Logger log = Logger.getLogger(RefereeProcessor.class);

	@Autowired ImportMinutRefereeLogic importRefereeLogic;

	public Object process(Referee p) {
		log.info("Process: " + p.getMinut_id());
		Referee result = importRefereeLogic.doImport(p.getMinut_id());
		return result;
	}
}
