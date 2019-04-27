package pl.polskieligi.dao;

import java.util.Collection;
import java.util.List;

import pl.polskieligi.model.Team;
import pl.polskieligi.model.TeamLeague;

public interface TeamLeagueDAO extends AbstractDAO<TeamLeague>{
	public void updateTeams(Long projectId,
			Collection<Long> teamIds);
	public List<Team> getTeams(Long projectId);
	public List<TeamLeague> getTeamLeagues(Long projectId);
	
	public TeamLeague findByProjectAndTeam(Long projectId, Long teamId);
}
