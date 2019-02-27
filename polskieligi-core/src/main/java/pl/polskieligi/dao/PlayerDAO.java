package pl.polskieligi.dao;

import pl.polskieligi.model.Player;

public interface PlayerDAO extends AbstractDAO<Player>{
	public Player retrievePlayerByMinut(Integer minutId);
}
