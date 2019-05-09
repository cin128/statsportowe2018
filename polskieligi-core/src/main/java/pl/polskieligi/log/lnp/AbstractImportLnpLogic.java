package pl.polskieligi.log.lnp;

import pl.polskieligi.dao.AbstractDAO;
import pl.polskieligi.dao.AbstractLnpDAO;
import pl.polskieligi.log.AbstractImportLogic;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.LnpObject;

public abstract class AbstractImportLnpLogic<T extends LnpObject>  extends AbstractImportLogic<T>{	
	
	public AbstractImportLnpLogic(Class<T> clazz) {
		super(clazz);
	}
	
	protected Integer getImportStatus(T obj) {
		return obj.getImportLnpStatus();
	}
	
	protected void setImportStatus(T obj, Integer importStatus) {
		obj.setImportLnpStatus(importStatus);
	}
	
	protected void setObjectId(T obj, Integer id) {
		obj.setLnp_id(id);
	}

	protected abstract AbstractLnpDAO<T> getDAO();

	protected T retrieveById(Integer id) {
		return getDAO().retrieveByLnp(id);
	}

	@Override
	protected boolean deleteIfInvalid() {
		return false;
	}
}
