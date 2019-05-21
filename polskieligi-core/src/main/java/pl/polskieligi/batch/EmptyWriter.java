package pl.polskieligi.batch;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

@SuppressWarnings("rawtypes")
@Service
public class EmptyWriter implements ItemWriter {

	final static Logger log = Logger.getLogger(EmptyWriter.class);

	public void write(List items) {
		log.debug("Write: "+items);
	}
}
