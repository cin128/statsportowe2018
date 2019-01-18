package pl.polskieligi.batch.team;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;

import org.springframework.beans.factory.annotation.Value;
import pl.polskieligi.model.Team;

public class TeamImportReader implements ItemReader<Team> {
	final static Logger log = Logger.getLogger(TeamImportReader.class);

	@Value("${minut.team.start.id}")
	private Integer start;
	@Value("${minut.team.end.id}")
	private Integer end;
	private Integer index;

	@PostConstruct public void initService() {
		index = start;
	}

	@Override public Team read() {
		log.debug("Read: " + index);
		if (index <= end) {
			Team result = new Team();
			result.setMinut_id(index++);
			return result;
		}
		return null;
	}
}
