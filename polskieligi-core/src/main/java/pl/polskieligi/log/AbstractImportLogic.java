package pl.polskieligi.log;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import pl.polskieligi.dao.AbstractDAO;

public abstract class AbstractImportLogic<T> implements ImportLogic<T> {
	final static Logger log = Logger.getLogger(AbstractImportLogic.class);

	private final Class<T> clazz;

	public AbstractImportLogic(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T doImport(Integer id) {
		T result = null;
		log.info("Importing " + getObjectName() + " id = " + id);
		java.util.Date startDate = new java.util.Date();

		try {
			T oldObj = retrieveById(id);
			if (isAlreadyLoaded(oldObj)) {
				log.info(getObjectName() + " alerady loaded id = " + id);
				result = oldObj;
			} else {
				log.debug("Start parsing... id = " + id);

				T obj;
				if (oldObj == null) {
					obj = clazz.newInstance();
					setObjectId(obj, id);
				} else {
					obj = oldObj;
				}
				try {
					Document doc = Jsoup.connect(getLink(obj)).get();
					ImportStatus status = process(doc, obj);
					setImportStatus(obj, status.getValue());
				} catch (SocketTimeoutException | NoRouteToHostException e) {
					log.warn("Time out for: " + id + " " + e.getMessage());
					setImportStatus(obj, ImportStatus.TIME_OUT.getValue());
				} catch (HttpStatusException e) {
					log.warn("Error for: " + id + " " + e.getMessage());
					setImportStatus(obj, ImportStatus.INVALID.getValue());
				}
				if (getImportStatus(obj) == ImportStatus.TIME_OUT.getValue()
						|| getImportStatus(obj) == ImportStatus.SUCCESS.getValue()) {
					obj = getDAO().saveUpdate(obj);
					log.info(getObjectName() + " saved: " + obj);
					result = obj;
				} else if (deleteIfInvalid() && oldObj != null) {
					getDAO().delete(oldObj);
					log.info(getObjectName() + " deleted: " + oldObj);
					result = null;
				}
				java.util.Date endDate = new java.util.Date();
				long diff = endDate.getTime() - startDate.getTime();
				log.info("End processing " + getObjectName() + " id = " + id + " time = " + (diff / 1000) + " sec");
			}

		} catch (IOException | InstantiationException | IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	protected boolean isAlreadyLoaded(T oldObj){
		return oldObj != null && getImportStatus(oldObj) != null
				&& getImportStatus(oldObj) == ImportStatus.SUCCESS.getValue();
	}

	protected boolean deleteIfInvalid() {
		return true;
	}

	protected String getObjectName() {
		return clazz.getSimpleName();
	}

	protected abstract ImportStatus process(Document doc, T obj);

	protected abstract T retrieveById(Integer id);

	protected abstract String getLink(T obj);

	protected abstract AbstractDAO<T> getDAO();

	protected abstract Integer getImportStatus(T obj);

	protected abstract void setImportStatus(T obj, Integer importStatus);

	protected abstract void setObjectId(T obj, Integer id);

}
