package pl.polskieligi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Club{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	private Long lnpId;
	private String lnpIdName;
	
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

	public Long getLnpId() {
		return lnpId;
	}

	public void setLnpId(Long lnpId) {
		this.lnpId = lnpId;
	}

	public String getLnpIdName() {
		return lnpIdName;
	}

	public void setLnpIdName(String lnpIdName) {
		this.lnpIdName = lnpIdName;
	}
	
}
