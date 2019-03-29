package pl.polskieligi.dao;

import pl.polskieligi.model.Player;
import pl.polskieligi.model.Referee;

public interface RefereeDAO extends AbstractDAO<Referee>{
	public Referee retrieveRefereeByMinut(Integer minutId);
}
