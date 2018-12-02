package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.Team;

public interface TeamDAO extends AbstractDAO<Team>{
	public List<Team> getTeams();
}
