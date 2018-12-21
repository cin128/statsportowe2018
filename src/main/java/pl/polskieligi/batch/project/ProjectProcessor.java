package pl.polskieligi.batch.project;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.dto.ProjectInfo;
import pl.polskieligi.log.minut.ImportMinutProjectLogic;
import pl.polskieligi.model.Project;

public class ProjectProcessor implements ItemProcessor<Project, Object> {

	final static Logger log = Logger.getLogger(ProjectProcessor.class);

	@Autowired ImportMinutProjectLogic importProjectLogic;
	
	public Object process(Project p) throws Exception {
		log.info("Process: "+p.getMinut_id());
		ProjectInfo result = importProjectLogic.doImport(p.getMinut_id());
		return result;
	}
}
