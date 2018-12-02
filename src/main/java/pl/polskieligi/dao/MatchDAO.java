package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.Match;

public interface MatchDAO extends AbstractDAO<Match>{
	public int saveUpdate(List<Match> roundMatches);
}
