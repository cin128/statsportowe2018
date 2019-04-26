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

	@Override protected TypedQuery<Referee> getRetrieveQuery(Referee ref) {
		return getMinutRetrieveQuery(ref.getMinut_id());
	}
}
