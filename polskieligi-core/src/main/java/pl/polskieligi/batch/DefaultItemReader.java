package pl.polskieligi.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.dao.ConfigDAO;
import pl.polskieligi.model.Config;
import pl.polskieligi.model.MinutObject;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class DefaultItemReader<T>  implements ItemReader<T> {

	final static Logger log = Logger.getLogger(DefaultItemReader.class);

	@Autowired
	private ConfigDAO configDAO;

	private Integer index;

	private final String propertyName;

	private final Integer defaultMaxValue;

	private final Class<T> clazz;

	private final BiConsumer<T, Integer> setObjectId;

	public DefaultItemReader(String propertyName, Integer defaultMaxValue, BiConsumer<T, Integer> setObjectId, Class<T> clazz){
		this.propertyName = propertyName;
		this.defaultMaxValue = defaultMaxValue;
		this.setObjectId = setObjectId;
		this.clazz = clazz;
	}

	public void initService(){
		Config conf = configDAO.findByName(propertyName);
		if(conf==null){
			conf = new Config();
			conf.setName(propertyName);
			conf.setValue(defaultMaxValue);
			configDAO.save(conf);
		}
		index = conf.getValue();
	}

	@Override public T read() {
		Integer end = 5+configDAO.findByName(propertyName).getValue();
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
