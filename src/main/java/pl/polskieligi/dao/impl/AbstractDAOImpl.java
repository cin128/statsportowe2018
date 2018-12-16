package pl.polskieligi.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.dao.AbstractDAO;

public abstract class AbstractDAOImpl<T> implements AbstractDAO<T> {
	@Autowired
	SessionFactory sessionFactory;

	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	private final Class<T> clazz;

    public AbstractDAOImpl(Class<T> clazz)
    {
        this.clazz = clazz;
    }
	
	@SuppressWarnings("unchecked")
	public T retrieveById(Long id) {
		Session session = getCurrentSession();
		return (T)session.get(clazz, id);		
	}
	
	public T saveUpdate(T obj) {
		Session session = getCurrentSession();
		Query query = getRetrieveQuery(obj);			
		T oldObj = null;
		@SuppressWarnings("unchecked")
		List<T> objs = query.list();
		for (T t : objs) {
			oldObj = t;
			updateData(obj, oldObj);			
			session.update(oldObj);
			obj = oldObj;
		}
		if (oldObj == null) {
			session.persist(obj);
		}
		session.flush();
		return obj;
	}
	
	protected abstract Query getRetrieveQuery(T obj);
	
	protected void updateData(T source, T target) {}
	
}
