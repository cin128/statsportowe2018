package pl.polskieligi.dao;

import pl.polskieligi.model.Substitution;

import java.util.List;

public interface SubstitutionDAO  extends AbstractDAO<Substitution>{
	List<Substitution> getSubstitutionsForLeagueMatch(Long leagueMatchId);
}
