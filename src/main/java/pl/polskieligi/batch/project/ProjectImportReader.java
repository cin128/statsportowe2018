package pl.polskieligi.batch.project;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;

import pl.polskieligi.model.Project;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

public class ProjectImportReader implements ItemReader<Project> {
	private final static String MINUT_START = "minut.project.start.id";
	private final static String MINUT_END = "minut.project.end.id";

	@Resource(name = "applicationProperties") Properties properties;

	final static Logger log = Logger.getLogger(ProjectImportReader.class);

	private Integer start;
	private Integer end;
	private Integer index;

	@PostConstruct public void initService() {
		start = Integer.parseInt(properties.getProperty(MINUT_START, "68"));
		end = Integer.parseInt(properties.getProperty(MINUT_END, "10528"));
		index = start;
	}

	@Override public Project read() {
		log.debug("Read: " + index);
		if (index <= end) {
			Project result = new Project();
			result.setMinut_id(index++);
			return result;
		}
		return null;
	}
}
