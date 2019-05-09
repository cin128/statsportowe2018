package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.LeagueMatch;

public interface LeagueMatchDAO extends AbstractLnpDAO<LeagueMatch>{
	List<LeagueMatch> getMatchesByProjectId(Long projectId);
	List<LeagueMatch> getMatchesByTeamSeason(Long teamId, Long seasonId);
	LeagueMatch find(Long projectId, Long roundId, Long team1Id, Long team2Id);
}
