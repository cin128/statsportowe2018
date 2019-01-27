package pl.polskieligi.dao.impl;

import java.util.List;

import javax.persistence.Query;

import pl.polskieligi.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractDAOImpl<T> implements AbstractDAO<T> {

	@PersistenceContext
	private EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}

	private final Class<T> clazz;

	public AbstractDAOImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T saveUpdate(T obj) {
		Query query = getRetrieveQuery(obj);
		T oldObj = null;
		@SuppressWarnings("unchecked")
		List<T> objs = query.getResultList();
		for (T t : objs) {
			oldObj = t;
			if (updateData(obj, oldObj)) {
				getEntityManager().merge(oldObj);
			}
			obj = oldObj;
		}
		if (oldObj == null) {
			getEntityManager().persist(obj);
		}
		getEntityManager().flush();
		return obj;
	}

	protected abstract Query getRetrieveQuery(T obj);

	protected boolean updateData(T source, T target) {
		return false;
	}

	public T find(Long id) {
		return getEntityManager().find(clazz, id);
	}

	public List<T> findAll() {
		return getEntityManager().createQuery("from " + clazz.getName(), clazz).getResultList();
	}

	public void save(T entity) {
		getEntityManager().persist(entity);
	}

	public void update(T entity) {
		getEntityManager().merge(entity);
	}

	public void delete(T entity) {
		getEntityManager().remove(entity);
	}

	public void deleteById(Long entityId) {
		T entity = find(entityId);
		delete(entity);
	}

}
