package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.LeagueMatch;

public interface LeagueMatchDAO extends AbstractDAO<LeagueMatch>{
	public LeagueMatch saveUpdate(LeagueMatch roundMatch);
	public List<LeagueMatch> getMatchesByProjectId(Long projectId);
	public List<LeagueMatch> getMatchesByTeamSeason(Long teamId, Long seasonId);
	public LeagueMatch find(Long projectId, Long roundId, Long team1Id, Long team2Id);
}
