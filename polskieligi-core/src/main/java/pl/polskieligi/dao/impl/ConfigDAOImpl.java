package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.ConfigDAO;
import pl.polskieligi.model.Config;

import javax.persistence.TypedQuery;

@Repository
@Transactional
public class ConfigDAOImpl  extends AbstractDAOImpl<Config> implements ConfigDAO {

	public ConfigDAOImpl() {
		super(Config.class);
	}

	@Override protected TypedQuery<Config> getRetrieveQuery(Config conf) {
		return getRetrieveQuery(conf.getName());
	}

	private TypedQuery<Config> getRetrieveQuery(String name) {
		TypedQuery<Config> query = getEntityManager().createQuery("SELECT l from Config l where name = :name", Config.class);
		query.setParameter("name", name);
		return query;
	}


	@Override public Config findByName(String name) {
		TypedQuery<Config> query = getRetrieveQuery(name);
		query.setMaxResults(1);
		for(Config c: query.getResultList()){
			return c;
		}
		return null;
	}
}
