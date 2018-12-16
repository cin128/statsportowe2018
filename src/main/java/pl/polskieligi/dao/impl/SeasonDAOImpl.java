package pl.polskieligi.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.SeasonDAO;
import pl.polskieligi.model.Season;

@Repository
@Transactional
public class SeasonDAOImpl extends AbstractDAOImpl<Season> implements SeasonDAO {
	public SeasonDAOImpl() {
		super(Season.class);
	}
	
@Override
	protected Query getRetrieveQuery(Season season) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Season where name = :name");
		query.setParameter("name", season.getName());
		return query;
	}
}
