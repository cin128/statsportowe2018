package pl.polskieligi.batch.lmp;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.dao.TeamLeaguePlayerDAO;
import pl.polskieligi.model.LeagueMatchPlayer;
import pl.polskieligi.model.TeamLeague;
import pl.polskieligi.model.TeamLeaguePlayer;

public class LMPProcessor  implements ItemProcessor<LeagueMatchPlayer, Object>{

	@Autowired
	TeamLeagueDAO teamLeagueDAO;
	
	@Autowired
	TeamLeaguePlayerDAO teamLeaguePlayerDAO;
	
	@Override
	public Object process(LeagueMatchPlayer lmp) throws Exception {
		Long playerId = lmp.getPlayer_id();
		Long teamId = lmp.getTeam_id();
		Long projectId = lmp.getLeagueMatch().getProject_id();
		TeamLeague tl = teamLeagueDAO.findByProjectAndTeam(projectId, teamId);
		if(tl!=null) {
			TeamLeaguePlayer tlp = new TeamLeaguePlayer();	
			tlp.setTeamLeague_id(tl.getId());
			tlp.setPlayer_id(playerId);
			teamLeaguePlayerDAO.saveUpdate(tlp);
		}
		return lmp;
	}

}
