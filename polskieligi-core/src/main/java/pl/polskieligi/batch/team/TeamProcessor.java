package pl.polskieligi.batch.team;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.log.minut.ImportMinutTeamLogic;
import pl.polskieligi.model.Team;

public class TeamProcessor implements ItemProcessor<Team, Object> {

	final static Logger log = Logger.getLogger(TeamProcessor.class);

	@Autowired ImportMinutTeamLogic importTeamLogic;

	public Object process(Team t) {
		log.info("Process: " + t.getMinut_id());
		Team result = importTeamLogic.doImport(t.getMinut_id());
		return result;
	}
}
