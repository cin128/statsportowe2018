package pl.polskieligi.log;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class TimestampParser {

	private static final Map<String, Integer> months;
	static {
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		aMap.put("stycznia", 0);
		aMap.put("lutego", 1);
		aMap.put("marca", 2);
		aMap.put("kwietnia", 3);
		aMap.put("maja", 4);
		aMap.put("czerwca", 5);
		aMap.put("lipca", 6);
		aMap.put("sierpnia", 7);
		aMap.put("września", 8);
		aMap.put("października", 9);
		aMap.put("listopada", 10);
		aMap.put("grudnia", 11);
		months = Collections.unmodifiableMap(aMap);
	}

	/*
	 * "21 lipca, 18:00"
	 */
	public static Timestamp parseTimestamp(Integer year, String substring) {
		if(year==null || substring==null){
			return null;
		}
		if (substring.trim().isEmpty()) {
			return null;
		}
		String[] tmp = substring.split(" ");

		Integer day = 0;
		if (tmp[0].contains("-")) {
			String[] tmp2 = tmp[0].split("-");
			day = Integer.parseInt(tmp2[0]);
		} else {
			day = Integer.parseInt(tmp[0]);
		}

		GregorianCalendar cal = null;
		if(tmp.length==2){
			Integer month = months.get(tmp[1]);
			cal = new GregorianCalendar(month > 5 ? year : year + 1, month, day);
		} else if (tmp.length == 3 && tmp[2].contains(":")) {
			Integer month = months
					.get(tmp[1].substring(0, tmp[1].length() - 1));
			String[] time = tmp[2].split(":");
			Integer hour = Integer.parseInt(time[0]);
			Integer minute = Integer.parseInt(time[1]);
			cal = new GregorianCalendar(month > 5 ? year : year + 1, month,
					day, hour, minute);
		} else if(tmp.length == 3&& tmp[1].contains("-")){
			String[] tmp2 = tmp[1].split("-");
			Integer month = months.get(tmp2[0]);
			cal = new GregorianCalendar(month > 5 ? year : year + 1, month, day);
		} else {
			return null;
		}
		return new Timestamp(cal.getTimeInMillis());
	}

	/*
	 * "22-23 marca" "30 listopada-1 grudnia"
	 */
	public static Date getRoundEnd(Integer year, String substring) {
		if(year==null || substring==null){
			return null;
		}
		int i = substring.indexOf("-");
		int day = -1;
		int month = -1;
		if (i < 0) {
			String[] tmp = substring.split(" ");
			day = Integer.parseInt(tmp[0]);
			month = months.get(tmp[1]);
		} else {
			String[] tmp = substring.split("-");
			tmp = tmp[1].split(" ");
			day = Integer.parseInt(tmp[0]);
			month = months.get(tmp[1]);
		}
		GregorianCalendar cal = new GregorianCalendar(month > 5 ? year
				: year + 1, month, day);
		return new Date(cal.getTimeInMillis());
	}

	public static Date getRoundStart(Integer year, String substring) {
		if(year==null || substring==null){
			return null;
		}
		int i = substring.indexOf("-");
		int day = -1;
		int month = -1;
		if (i < 0) {
			String[] tmp = substring.split(" ");
			day = Integer.parseInt(tmp[0]);
			month = months.get(tmp[1]);
		} else {
			String[] tmp = substring.split("-");
			if (tmp[0].length() > 3) {
				tmp = tmp[0].split(" ");
				day = Integer.parseInt(tmp[0]);
				month = months.get(tmp[1]);
			} else {
				day = Integer.parseInt(tmp[0]);
				tmp = tmp[1].split(" ");
				month = months.get(tmp[1]);
			}
		}
		GregorianCalendar cal = new GregorianCalendar(month > 5 ? year
				: year + 1, month, day);
		return new Date(cal.getTimeInMillis());
	}
}
