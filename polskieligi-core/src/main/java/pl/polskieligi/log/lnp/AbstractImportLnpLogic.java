package pl.polskieligi.log.lnp;

import pl.polskieligi.log.AbstractImportLogic;
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
}
