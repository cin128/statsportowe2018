package pl.polskieligi.batch.team;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;

import pl.polskieligi.model.Team;

public class TeamImportReader implements ItemReader<Team> {
	private final static String MINUT_START = "minut.team.start.id";
	private final static String MINUT_END = "minut.team.end.id";

	@Resource(name = "applicationProperties") Properties properties;

	final static Logger log = Logger.getLogger(TeamImportReader.class);

	private Integer start;
	private Integer end;
	private Integer index;

	@PostConstruct public void initService() {
		start = Integer.parseInt(properties.getProperty(MINUT_START, "1"));
		end = Integer.parseInt(properties.getProperty(MINUT_END, "21967"));
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
