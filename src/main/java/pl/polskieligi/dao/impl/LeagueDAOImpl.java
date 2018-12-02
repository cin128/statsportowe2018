package pl.polskieligi.dao.impl;

import java.util.List;

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

	public Long saveUpdate(League league) {
		Long result = null;
		Session session = getCurrentSession();
		Query query = session.createQuery("from League where name = :name");
		query.setParameter("name", league.getName());
		League oldLeague = null;
		@SuppressWarnings("unchecked")
		List<League> leagues = query.list();
		for (League l : leagues) {
			oldLeague = l;
			result = l.getId();
		}
		if (oldLeague == null) {
			result = (Long) session.save(league);
		}
		return result;
	}
}
