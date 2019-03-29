package pl.polskieligi.batch.referee;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import pl.polskieligi.model.Referee;

import javax.annotation.PostConstruct;

public class RefereeImportReader  implements ItemReader<Referee> {
	final static Logger log = Logger.getLogger(RefereeImportReader.class);

	@Value("${minut.referee.start}")
	private Integer start;
	@Value("${minut.referee.end}")
	private Integer end;
	private Integer index;

	@PostConstruct public void initService() {
		index = start;
	}

	@Override public Referee read() {
		log.debug("Read: " + index);
		if (index <= end) {
			Referee result = new Referee();
			result.setMinut_id(index++);
			return result;
		}
		return null;
	}
}
