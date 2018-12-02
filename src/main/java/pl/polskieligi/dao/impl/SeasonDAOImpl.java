package pl.polskieligi.dao.impl;

import java.util.List;

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

	public Long saveUpdate(Season season) {
		Long result = null;
		Session session = getCurrentSession();
		Query query = session.createQuery("from Season where name = :name");
		query.setParameter("name", season.getName());
		Season oldSeason = null;
		@SuppressWarnings("unchecked")
		List<Season> leagues = query.list();
		for (Season s : leagues) {
			oldSeason = s;
			result = s.getId();
		}
		if (oldSeason == null) {
			result = (Long) session.save(season);
		}
		return result;
	}
}
