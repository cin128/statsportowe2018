package pl.polskieligi.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import pl.polskieligi.model.Project;

public class ProjectImportReader implements ItemReader<Project> {
	private final Integer START = 9388;
	private final Integer END = 9390;
	
	private Integer index = START;
	@Override
	public Project read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if(index<=END) {
			Project result = new Project();
			result.setMinut_id(index++);
			return result;	
		}
		return null;
	}
}
