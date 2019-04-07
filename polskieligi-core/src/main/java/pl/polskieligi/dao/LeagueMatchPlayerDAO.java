package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.LeagueMatchPlayer;

public interface LeagueMatchPlayerDAO extends AbstractDAO<LeagueMatchPlayer>{
	List<LeagueMatchPlayer> getPlayerMatchesForSeason(Long playerId, Long seasonId);
	void updateTeamLeaguePlayers();
}
