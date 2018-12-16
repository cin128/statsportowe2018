package pl.polskieligi.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueDAO;
import pl.polskieligi.model.League;

@Repository
@Transactional
public class LeagueDAOImpl extends AbstractDAOImpl<League> implements LeagueDAO {

	public LeagueDAOImpl() {
		super(League.class);
	}

	@Override
	protected Query getRetrieveQuery(League league) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from League where name = :name");
		query.setParameter("name", league.getName());
		return query;
	}
}
