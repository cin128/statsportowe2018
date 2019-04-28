package pl.polskieligi.log.minut;

import pl.polskieligi.log.AbstractImportLogic;
import pl.polskieligi.model.MinutObject;

public abstract class AbstractImportMinutLogic<T extends MinutObject> extends AbstractImportLogic<T>{
	
	public AbstractImportMinutLogic(Class<T> clazz) {
		super(clazz);
	}
	
	protected Integer getImportStatus(T obj) {
		return obj.getImportStatus();
	}
	
	protected void setImportStatus(T obj, Integer importStatus) {
		obj.setImportStatus(importStatus);
	}
	
	protected void setObjectId(T obj, Integer id) {
		obj.setMinut_id(id.intValue());
	}
	
}
