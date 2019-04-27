package pl.polskieligi.dao;

import pl.polskieligi.model.Round;

public interface RoundDAO extends AbstractDAO<Round>{
	Round findByProjectAndRound(Long projectId, Integer matchcode);
}
