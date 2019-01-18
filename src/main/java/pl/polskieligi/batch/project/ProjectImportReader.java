package pl.polskieligi.batch.project;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;

import org.springframework.beans.factory.annotation.Value;
import pl.polskieligi.model.Project;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

public class ProjectImportReader implements ItemReader<Project> {

	final static Logger log = Logger.getLogger(ProjectImportReader.class);
	@Value("${minut.project.start.id}")
	private Integer start;
	@Value("${minut.project.end.id}")
	private Integer end;
	private Integer index;

	@PostConstruct public void initService() {
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
