package pl.polskieligi.batch;

import java.util.function.Function;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.dao.ConfigDAO;
import pl.polskieligi.log.ImportLogic;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.MinutObject;

public class DefaultItemProcessor<T extends MinutObject> implements ItemProcessor<T, Object> {
	final static Logger log = Logger.getLogger(DefaultItemProcessor.class);

	@Autowired
	private ConfigDAO configDAO;

	private final String propertyName;

	private final ImportLogic<T> importLogic;
	
	private final Function<T, Integer> getObjectId;

	public DefaultItemProcessor(String propertyName, ImportLogic<T> importLogic, Function<T, Integer> getObjectId) {
		this.propertyName = propertyName;
		this.importLogic = importLogic;
		this.getObjectId = getObjectId;
	}

	@Override
	public Object process(T obj) {
		Object result = processInternal(obj);
		if(propertyName!=null && obj instanceof MinutObject){
			if(result!=null){
				MinutObject mo = (MinutObject)result;
				if(mo.getImportStatus()!=null && mo.getImportStatus()==ImportStatus.SUCCESS.getValue()) {
					Integer minut_id = mo.getMinut_id();
					if (minut_id != null && minut_id > 0) {
						Config conf = configDAO.findByName(propertyName);
						if (minut_id > conf.getValue()) {
							conf.setValue(minut_id);
							configDAO.update(conf);
						}
					}
				}
			}
		}
		return result;
	}


	private Object processInternal(T obj) {
		log.info("Process: " + obj.getMinut_id());
		T result = importLogic.doImport(getObjectId.apply(obj));
		return result;
	}
}
