package pl.polskieligi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Club implements LnpObject{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	private Integer lnp_id;
	private String lnpIdName;
	private Integer importLnpStatus;
	
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

	@Override public Integer getLnp_id() {
		return lnp_id;
	}

	@Override public void setLnp_id(Integer lnp_id) {
		this.lnp_id = lnp_id;
	}

	public String getLnpIdName() {
		return lnpIdName;
	}

	public void setLnpIdName(String lnpIdName) {
		this.lnpIdName = lnpIdName;
	}

	@Override public Integer getImportLnpStatus() {
		return importLnpStatus;
	}

	@Override public void setImportLnpStatus(Integer importLnpStatus) {
		this.importLnpStatus = importLnpStatus;
	}

	@Override public String toString() {
		return name;
	}
}
