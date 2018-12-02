package pl.polskieligi.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

@SuppressWarnings("rawtypes")
public class EmptyWriter implements ItemWriter {
	public void write(List items) throws Exception {		
	}
}
