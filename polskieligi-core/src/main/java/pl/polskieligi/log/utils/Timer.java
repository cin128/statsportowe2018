package pl.polskieligi.log.utils;

import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.Date;

public class Timer {
	final static Logger log = Logger.getLogger(Timer.class);
	private Date start = new Date();
	public void stop(String desc){
		Date end = new Date();
		Duration d = Duration.ofMillis(end.getTime()-start.getTime());
		log.info("Time: "+desc+": "+d.toString());
	}
}
