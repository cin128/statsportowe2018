package pl.polskieligi.model;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(indexes = { @Index(name = "IDX_SE_NAME", columnList = "name", unique = false) })
public class Season {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;

	public Season() {
		name = "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	private static Map<String, Integer> seasonNameToMinutId;
	static {
		seasonNameToMinutId = new HashMap<String, Integer>();
		seasonNameToMinutId.put("2018/19", 93);
		seasonNameToMinutId.put("2017/18", 91);
		seasonNameToMinutId.put("2016/17", 89);
		seasonNameToMinutId.put("2015/16", 87);
		seasonNameToMinutId.put("2014/15", 85);
		seasonNameToMinutId.put("2013/14", 83);
		seasonNameToMinutId.put("2012/13", 81);
		seasonNameToMinutId.put("2011/12", 79);
		seasonNameToMinutId.put("2010/11", 77);
		seasonNameToMinutId.put("2009/10", 75);
		seasonNameToMinutId.put("2008/09", 73);
		seasonNameToMinutId.put("2007/08", 71);
		seasonNameToMinutId.put("2006/07", 69);
		seasonNameToMinutId.put("2005/06", 67);
		seasonNameToMinutId.put("2004/05", 65);
		seasonNameToMinutId.put("2003/04", 63);
		seasonNameToMinutId.put("2002/03", 61);
		seasonNameToMinutId.put("2001/02", 59);
		seasonNameToMinutId.put("2000/01", 57);
		seasonNameToMinutId.put("1999/00", 55);
		seasonNameToMinutId.put("1998/99", 53);
		seasonNameToMinutId.put("1997/98", 51);
		seasonNameToMinutId.put("1996/97", 49);
		seasonNameToMinutId.put("1995/96", 47);
		seasonNameToMinutId.put("1994/95", 45);
		seasonNameToMinutId.put("1993/94", 43);
		seasonNameToMinutId.put("1992/93", 41);
		seasonNameToMinutId.put("1991/92", 39);
		seasonNameToMinutId.put("1990/91", 37);
		seasonNameToMinutId.put("1989/90", 35);
		seasonNameToMinutId.put("1988/89", 33);
		seasonNameToMinutId.put("1987/88", 31);
		seasonNameToMinutId.put("1986/87", 29);
		seasonNameToMinutId.put("1985/86", 27);
		seasonNameToMinutId.put("1984/85", 25);
		seasonNameToMinutId.put("1983/84", 23);
		seasonNameToMinutId.put("1982/83", 21);
		seasonNameToMinutId.put("1981/82", 19);
		seasonNameToMinutId.put("1980/81", 17);
		seasonNameToMinutId.put("1979/80", 15);
		seasonNameToMinutId.put("1978/79", 13);
		seasonNameToMinutId.put("1977/78", 11);
		seasonNameToMinutId.put("1976/77", 9);
		seasonNameToMinutId.put("1975/76", 7);
		seasonNameToMinutId.put("1974/75", 5);
		seasonNameToMinutId.put("1973/74", 3);
		seasonNameToMinutId.put("1972/73", 1);
		seasonNameToMinutId.put("1971/72", -1);
		seasonNameToMinutId.put("1970/71", -3);
		seasonNameToMinutId = Collections.unmodifiableMap(seasonNameToMinutId);
	}

	public Integer getMinut_id() {
		return seasonNameToMinutId.get(name);
	}
}
