package pl.polskieligi.dao.impl;

import java.sql.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.RoundDAO;
import pl.polskieligi.model.Round;

import javax.persistence.Query;

@Repository
@Transactional
public class RoundDAOImpl extends AbstractDAOImpl<Round> implements RoundDAO {
	public RoundDAOImpl() {
		super(Round.class);
	}

	@Override
	protected Query getRetrieveQuery(Round round) {
		Query query = getEntityManager().createQuery("SELECT r from Round r where project_id = :project_id and matchcode = :matchcode");
		query.setParameter("project_id", round.getProject_id());
		query.setParameter("matchcode", round.getMatchcode());
		return query;
	}
	@Override
	protected boolean updateData(Round round, Round oldRound) {
		boolean result = false;
		if (round.getName() != null && !round.getName().isEmpty()) {
			oldRound.setName(round.getName());
			result = true;
		}
		if (round.getRound_date_first() != null && !round.getRound_date_first().equals(new Date(0))) {
			oldRound.setRound_date_first(round.getRound_date_first());
			result = true;
		}
		if (round.getRound_date_last() != null && !round.getRound_date_last().equals(new Date(0))) {
			oldRound.setRound_date_last(round.getRound_date_last());
			result = true;
		}
		return result;
	}
}
