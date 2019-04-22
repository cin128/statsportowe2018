package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.Team;

public interface TeamDAO extends AbstractDAO<Team>{
	public List<Team> getTeams();
	public Team retrieveByMinut(Integer minutId);
	public Team retrieveTeamByName(String name);
	public List<Team> getMatchingTeams(String name);
}
