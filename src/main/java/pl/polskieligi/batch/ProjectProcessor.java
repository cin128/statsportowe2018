package pl.polskieligi.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.log.ImportProjectLogic;
import pl.polskieligi.model.Project;

public class ProjectProcessor implements ItemProcessor<Project, Object> {

	@Autowired
	ImportProjectLogic importProjectLogic;
	
	public Object process(Project p) throws Exception {
		String result = importProjectLogic.doImport(p.getMinut_id());		
		System.out.println(result);
		return null;
	}
}
