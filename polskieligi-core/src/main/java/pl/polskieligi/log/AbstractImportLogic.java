package pl.polskieligi.log;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import pl.polskieligi.dao.AbstractDAO;

public abstract class AbstractImportLogic<T> implements ImportLogic<T>{
final static Logger log = Logger.getLogger(AbstractImportLogic.class);
		
	private final Class<T> clazz;
	
	public AbstractImportLogic(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public T doImport(Integer id) {
		T result = null;
		log.info("Importing id = " + id);
		java.util.Date startDate = new java.util.Date();

		try {
			T oldObj = retrieveById(id);
			if (oldObj != null && getImportStatus(oldObj)!=null && getImportStatus(oldObj)== ImportStatus.SUCCESS.getValue()) {
				log.info(getObjectName()+" alerady loaded id = "+id);
				result = oldObj;
			} else {
				log.debug("Start parsing... id = " + id);
				
				T obj;
				if(oldObj==null) {
					obj = clazz.newInstance();
					setObjectId(obj, id);
				} else {
					obj = oldObj;
				}				
				try {
					Document doc = Jsoup.connect(getLink(obj)).get();
					process(doc, obj);										
				} catch(SocketTimeoutException | NoRouteToHostException e){
					log.warn("Time out for: "+id+" "+ e.getMessage());
					setImportStatus(obj, ImportStatus.TIME_OUT.getValue());
				}
				if (getImportStatus(obj)==ImportStatus.TIME_OUT.getValue() || getImportStatus(obj)==ImportStatus.SUCCESS.getValue()) {
					obj = getDAO().saveUpdate(obj);
					log.info(getObjectName()+" saved: " + obj);
					result = obj;
				} else if(oldObj!=null){
					getDAO().delete(oldObj);
					log.info(getObjectName()+" deleted: " + oldObj);
					result = null;
				}
				java.util.Date endDate = new java.util.Date();
				long diff = endDate.getTime() - startDate.getTime();
				log.info("End processing id = " + id + " time = "+ (diff/1000) +" sec");					
			}
				
		} catch (IOException|InstantiationException|IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} 		
		
		return result;
	}
	
	protected String getObjectName() {
		return clazz.getSimpleName();
	}	
	
	protected abstract void process(Document doc, T obj);
	
	protected abstract T retrieveById(Integer id);
	
	protected abstract String getLink(T obj);
	
	protected abstract AbstractDAO<T> getDAO();	
	
	protected abstract Integer getImportStatus(T obj);
	
	protected abstract void setImportStatus(T obj, Integer importStatus);
	
	protected abstract void setObjectId(T obj, Integer id);
	
	
}
