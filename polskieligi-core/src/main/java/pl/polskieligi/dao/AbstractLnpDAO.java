package pl.polskieligi.dao;

public interface AbstractLnpDAO<T> extends AbstractDAO<T>{
	T retrieveByLnp(Integer lnpId);
}
