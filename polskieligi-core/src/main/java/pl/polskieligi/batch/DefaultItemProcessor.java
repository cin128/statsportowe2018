package pl.polskieligi.batch;

import java.util.function.Function;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.dao.ConfigDAO;
import pl.polskieligi.log.ImportLogic;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.LnpObject;
import pl.polskieligi.model.MinutObject;

public class DefaultItemProcessor<T> implements ItemProcessor<T, Object> {
	final static Logger log = Logger.getLogger(DefaultItemProcessor.class);

	@Autowired
	private ConfigDAO configDAO;

	private final String propertyName;

	private final ImportLogic<T> importLogic;
	
	private final Function<T, Integer> getObjectId;

	private final Function<T, Integer> getImportStatus;

	public DefaultItemProcessor(String propertyName, ImportLogic<T> importLogic, Function<T, Integer> getObjectId, Function<T, Integer> getImportStatus) {
		this.propertyName = propertyName;
		this.importLogic = importLogic;
		this.getObjectId = getObjectId;
		this.getImportStatus = getImportStatus;
	}

	@Override
	public Object process(T obj) {
		T result = processInternal(obj);
		if(propertyName!=null && (result instanceof MinutObject || result instanceof LnpObject)){
			if(result!=null){
				Integer importStatus = getImportStatus.apply(result);
				if(importStatus!=null && importStatus==ImportStatus.SUCCESS.getValue()) {
					Integer id = getObjectId.apply(obj);
					if (id != null && id > 0) {
						Config conf = configDAO.findByName(propertyName);
						if (id > conf.getValue()) {
							conf.setValue(id);
							configDAO.update(conf);
						}
					}
				}
			}
		}
		return result;
	}


	private T processInternal(T obj) {
		log.info("Process: " + getObjectId.apply(obj));
		T result = importLogic.doImport(getObjectId.apply(obj));
		return result;
	}
}
