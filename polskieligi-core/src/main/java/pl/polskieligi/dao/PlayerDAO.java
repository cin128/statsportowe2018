package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.model.Player;

public interface PlayerDAO extends AbstractDAO<Player>{
	public Player retrieveByMinut(Integer minutId);
	public List<Player> findBySeasonAndTeam(Long seasonId, Long teamId);
}
