package pl.polskieligi.dao;

import pl.polskieligi.model.League;

public interface LeagueDAO extends AbstractDAO<League>{
	
	public void updateRegions();
	public void updateLeagueTypes();
	public void updateGroups();
}
