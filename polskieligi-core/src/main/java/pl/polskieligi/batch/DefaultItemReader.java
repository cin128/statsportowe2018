package pl.polskieligi.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.dao.ConfigDAO;
import pl.polskieligi.model.Config;
import java.util.function.BiConsumer;

public class DefaultItemReader<T>  implements ItemReader<T> {

	private final static Logger log = Logger.getLogger(DefaultItemReader.class);
	
	private final static int DEFAULT_TRESHOLD = 5;

	@Autowired
	private ConfigDAO configDAO;

	private Integer index;

	private final String propertyName;

	private final Integer defaultStartValue;
	
	private final Integer defaultEndValue;

	private final Class<T> clazz;

	private final BiConsumer<T, Integer> setObjectId;

	public DefaultItemReader(String propertyName, Integer defaultStartValue, BiConsumer<T, Integer> setObjectId, Class<T> clazz){		
		this(propertyName, defaultStartValue, null, setObjectId, clazz);

	}
	
	public DefaultItemReader(String propertyName, Integer defaultStartValue, Integer defaultEndValue, BiConsumer<T, Integer> setObjectId, Class<T> clazz){
		this.propertyName = propertyName;
		this.defaultStartValue = defaultStartValue;
		this.setObjectId = setObjectId;
		this.clazz = clazz;
		this.defaultEndValue = defaultEndValue;
	}

	public void initService(){
		Config conf = configDAO.findByName(propertyName);
		if(conf==null){
			conf = new Config();
			conf.setName(propertyName);
			conf.setValue(defaultStartValue);
			configDAO.save(conf);
		}
		index = conf.getValue();
	}

	@Override public T read() {
		
		Integer end = defaultEndValue;
		if(end==null) {
			end = configDAO.findByName(propertyName).getValue()+DEFAULT_TRESHOLD;
		}
		index++;
		if (index <= end) {
			return read(index);
		}
		return null;
	}

	private T read(Integer index){
		log.debug("Read: " + index);
		T result = null;
		try {
			result = clazz.newInstance();
		} catch (InstantiationException|IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}
		setObjectId.accept(result, index++);
		return result;
	}
}
