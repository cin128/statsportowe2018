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
		seasonNameToMinutId.put("2018/2019", 93);
		seasonNameToMinutId.put("2017/2018", 91);
		seasonNameToMinutId.put("2016/2017", 89);
		seasonNameToMinutId.put("2015/2016", 87);
		seasonNameToMinutId.put("2014/2015", 85);
		seasonNameToMinutId.put("2013/2014", 83);
		seasonNameToMinutId.put("2012/2013", 81);
		seasonNameToMinutId.put("2011/2012", 79);
		seasonNameToMinutId.put("2010/2011", 77);
		seasonNameToMinutId.put("2009/2010", 75);
		seasonNameToMinutId.put("2008/2009", 73);
		seasonNameToMinutId.put("2007/2008", 71);
		seasonNameToMinutId.put("2006/2007", 69);
		seasonNameToMinutId.put("2005/2006", 67);
		seasonNameToMinutId.put("2004/2005", 65);
		seasonNameToMinutId.put("2003/2004", 63);
		seasonNameToMinutId.put("2002/2003", 61);
		seasonNameToMinutId.put("2001/2002", 59);
		seasonNameToMinutId.put("2000/2001", 57);
		seasonNameToMinutId.put("1999/2000", 55);
		seasonNameToMinutId.put("1998/1999", 53);
		seasonNameToMinutId.put("1997/1998", 51);
		seasonNameToMinutId.put("1996/1997", 49);
		seasonNameToMinutId.put("1995/1996", 47);
		seasonNameToMinutId.put("1994/1995", 45);
		seasonNameToMinutId.put("1993/1994", 43);
		seasonNameToMinutId.put("1992/1993", 41);
		seasonNameToMinutId.put("1991/1992", 39);
		seasonNameToMinutId.put("1990/1991", 37);
		seasonNameToMinutId.put("1989/1990", 35);
		seasonNameToMinutId.put("1988/1989", 33);
		seasonNameToMinutId.put("1987/1988", 31);
		seasonNameToMinutId.put("1986/1987", 29);
		seasonNameToMinutId.put("1985/1986", 27);
		seasonNameToMinutId.put("1984/1985", 25);
		seasonNameToMinutId.put("1983/1984", 23);
		seasonNameToMinutId.put("1982/1983", 21);
		seasonNameToMinutId.put("1981/1982", 19);
		seasonNameToMinutId.put("1980/1981", 17);
		seasonNameToMinutId.put("1979/1980", 15);
		seasonNameToMinutId.put("1978/1979", 13);
		seasonNameToMinutId.put("1977/1978", 11);
		seasonNameToMinutId.put("1976/1977", 9);
		seasonNameToMinutId.put("1975/1976", 7);
		seasonNameToMinutId.put("1974/1975", 5);
		seasonNameToMinutId.put("1973/1974", 3);
		seasonNameToMinutId.put("1972/1973", 1);
		seasonNameToMinutId.put("1971/1972", -1);
		seasonNameToMinutId.put("1970/1971", -3);
		seasonNameToMinutId = Collections.unmodifiableMap(seasonNameToMinutId);
	}

	public Integer getMinut_id() {
		return seasonNameToMinutId.get(name);
	}
}
