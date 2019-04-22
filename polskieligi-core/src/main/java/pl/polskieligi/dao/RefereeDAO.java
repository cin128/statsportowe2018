package pl.polskieligi.dao;

import pl.polskieligi.model.Referee;

public interface RefereeDAO extends AbstractDAO<Referee>{
	public Referee retrieveByMinut(Integer minutId);
}
