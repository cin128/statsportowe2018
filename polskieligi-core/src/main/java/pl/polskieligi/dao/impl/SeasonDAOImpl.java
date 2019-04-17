package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.SeasonDAO;
import pl.polskieligi.model.Season;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class SeasonDAOImpl extends AbstractDAOImpl<Season> implements SeasonDAO {
	public SeasonDAOImpl() {
		super(Season.class);
	}
	
@Override
	protected TypedQuery<Season> getRetrieveQuery(Season season) {
		TypedQuery<Season> query = getEntityManager().createQuery("SELECT s from Season s where name = :name", Season.class);
		query.setParameter("name", season.getName());
		return query;
	}
}
