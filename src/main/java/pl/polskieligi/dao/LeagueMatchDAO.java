package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.LeagueMatch;

public interface LeagueMatchDAO extends AbstractDAO<LeagueMatch>{
	public int saveUpdate(List<LeagueMatch> roundMatches);
}
