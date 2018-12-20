package pl.polskieligi.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import pl.polskieligi.model.Project;

import javax.annotation.Resource;
import java.util.Properties;

public class ProjectImportReader implements ItemReader<Project> {
	private final static String MINUT_START = "minut.start.id";
	private final static String MINUT_END = "minut.end.id";

	private final Integer START = 9388;//68
	private final Integer END = 9390;//10528

	@Resource(name="applicationProperties")
	Properties properties;

	final static Logger log = Logger.getLogger(ProjectImportReader.class);

	private Integer index = START;
	@Override
	public Project read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		/*Integer.parseInt(properties.getProperty(MINUT_START, "68"));
		Integer.parseInt(properties.getProperty(MINUT_END, "10528"))*/;
		log.debug("Read: "+index);
		if(index<=END) {
			Project result = new Project();
			result.setMinut_id(index++);
			return result;	
		}
		return null;
	}
}
