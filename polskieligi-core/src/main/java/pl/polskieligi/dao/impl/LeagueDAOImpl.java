package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueDAO;
import pl.polskieligi.model.League;

import javax.persistence.Query;

@Repository
@Transactional
public class LeagueDAOImpl extends AbstractDAOImpl<League> implements LeagueDAO {

	public LeagueDAOImpl() {
		super(League.class);
	}

	@Override
	protected Query getRetrieveQuery(League league) {
		Query query = getEntityManager().createQuery("SELECT l from League l where name = :name");
		query.setParameter("name", league.getName());
		return query;
	}
}
