package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.GenericDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@Transactional
public class GenericDAOImpl implements GenericDAO{

	@PersistenceContext
	private EntityManager em;

	@Override public Long getCount(String query) {
		Query q = em.createQuery(query);
		return (Long) q.getSingleResult();
	}
}
