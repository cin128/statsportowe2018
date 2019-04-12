package pl.polskieligi.model;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity  @Table(indexes = {@Index(name = "IDX_RE_MINUT_ID", columnList = "minut_id", unique = false)})
public class Referee extends Person{

	private String district;

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}
