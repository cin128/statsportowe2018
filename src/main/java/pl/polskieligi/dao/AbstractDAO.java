package pl.polskieligi.dao;

import java.util.List;

public interface AbstractDAO<T> {
	public T saveUpdate(T obj);
	
	public T find(Long id);

	public List<T> findAll() ;

	public void save(T entity);

	public void update(T entity);

	public void delete(T entity) ;

	public void deleteById(Long entityId) ;
}
