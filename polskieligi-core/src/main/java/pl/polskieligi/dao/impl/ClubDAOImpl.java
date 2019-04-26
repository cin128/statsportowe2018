package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.ClubDAO;
import pl.polskieligi.model.Club;

import javax.persistence.TypedQuery;

@Repository
@Transactional
public class ClubDAOImpl extends AbstractDAOImpl<Club> implements ClubDAO {
	public ClubDAOImpl() {
		super(Club.class);
	}

	@Override protected TypedQuery<Club> getRetrieveQuery(Club obj) {
		return super.getLnpRetrieveQuery(obj.getLnp_id());
	}
}
