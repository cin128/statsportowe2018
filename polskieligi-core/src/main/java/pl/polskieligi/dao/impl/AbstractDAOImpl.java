package pl.polskieligi.dao.impl;

import pl.polskieligi.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

@Transactional
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
		TypedQuery<T> query = getRetrieveQuery(obj);
		T oldObj = null;
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

	protected TypedQuery<T> getRetrieveQuery(T obj){
		return null;
	};

	protected boolean updateData(T source, T target) {
		return false;
	}

	public T find(Long id) {
		return getEntityManager().find(clazz, id);
	}

	public List<T> findAll() {
		return getEntityManager().createQuery("from " + clazz.getName(), clazz).getResultList();
	}
	public Stream<T> streamAll() {
		return getEntityManager().createQuery("from " + clazz.getName(), clazz).getResultStream();
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
	
	public T retrieveByMinut(Integer minutId) {
		return retrieveById(getMinutRetrieveQuery(minutId));
	}
	
	public T retrieveByLnp(Integer lnpId) {
		return retrieveById(getLnpRetrieveQuery(lnpId));
	}

	private T retrieveById(TypedQuery<T> query) {
		T result = null;
		List<T> projects = query.getResultList();
		for (T p : projects) {
			result = p;
		}

		return result;
	}

	protected TypedQuery<T> getMinutRetrieveQuery(Integer id){
		return getRetrieveQuery(id, "minut_id");
	}

	protected TypedQuery<T> getLnpRetrieveQuery(Integer id){
		return getRetrieveQuery(id, "lnp_id");
	}

	private TypedQuery<T> getRetrieveQuery(Integer id, String idFieldName){
		TypedQuery<T> query = getEntityManager().createQuery("SELECT p from "+clazz.getSimpleName()+" p where "+idFieldName+" = :"+idFieldName, clazz);
		query.setParameter(idFieldName, id);
		query.setMaxResults(1);
		return query;
	}

}
