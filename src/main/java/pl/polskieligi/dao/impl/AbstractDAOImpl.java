package pl.polskieligi.dao.impl;

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
}
