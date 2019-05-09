package pl.polskieligi.dao;

import java.util.Collection;
import java.util.List;

import pl.polskieligi.model.Project;
import pl.polskieligi.model.Team;
import pl.polskieligi.model.TeamLeague;

public interface TeamLeagueDAO extends AbstractLnpDAO<TeamLeague>{
	void updateTeams(Long projectId,
			Collection<Long> teamIds);
	List<Team> getTeams(Long projectId);
	List<TeamLeague> getTeamLeagues(Long projectId);
	
	TeamLeague findByProjectAndTeam(Long projectId, Long teamId);
}
