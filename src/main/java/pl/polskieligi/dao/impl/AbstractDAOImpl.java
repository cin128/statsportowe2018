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

    public AbstractDAOImpl(Class<T> clazz)
    {
        this.clazz = clazz;
    }
	
	@SuppressWarnings("unchecked")
	public T retrieveById(Long id) {
		return (T) getEntityManager().find(clazz, id);
	}
	
	public T saveUpdate(T obj) {
		Query query = getRetrieveQuery(obj);			
		T oldObj = null;
		@SuppressWarnings("unchecked")
		List<T> objs = query.getResultList();
		for (T t : objs) {
			oldObj = t;
			if(updateData(obj, oldObj)) {
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
	
}
