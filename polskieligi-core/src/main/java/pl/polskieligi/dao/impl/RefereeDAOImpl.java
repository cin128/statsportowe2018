package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.RefereeDAO;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.Referee;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class RefereeDAOImpl extends AbstractDAOImpl<Referee> implements RefereeDAO {
	public RefereeDAOImpl() {
		super(Referee.class);
	}

	@Override
	public Referee retrieveRefereeByMinut(Integer minutId) {
		Referee result = null;

		Query query = getEntityManager().createQuery("SELECT p from Referee p where minut_id = :minut_id");
		query.setParameter("minut_id", minutId);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked") List<Referee> referees = query.getResultList();
		for (Referee p : referees) {
			result = p;
		}

		return result;
	}


	@Override protected TypedQuery<Referee> getRetrieveQuery(Referee player) {
		TypedQuery<Referee> query = getEntityManager().createQuery("SELECT p from Referee p where minut_id = :minut_id", Referee.class);
		query.setParameter("minut_id", player.getMinut_id());
		return query;
	}
}
