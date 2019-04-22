package pl.polskieligi.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractObject implements MinutObject, LnpObject{
	
	private Integer minut_id;
	private Integer lnp_id;
	private String lnpIdName;
	private Integer importStatus;
	private Integer importLnpStatus;
	
	public AbstractObject() {
		minut_id = 0;
		lnp_id = 0;
	}
	
	public Integer getMinut_id() {
		return minut_id;
	}

	public void setMinut_id(Integer minut_id) {
		this.minut_id = minut_id;
	}
	
	public Integer getLnp_id() {
		return lnp_id;
	}

	public void setLnp_id(Integer lnp_id) {
		this.lnp_id = lnp_id;
	}

	public String getLnpIdName() {
		return lnpIdName;
	}

	public void setLnpIdName(String lnpIdName) {
		this.lnpIdName = lnpIdName;
	}

	public Integer getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(Integer importStatus) {
		this.importStatus = importStatus;
	}

	public Integer getImportLnpStatus() {
		return importLnpStatus;
	}

	public void setImportLnpStatus(Integer importLnpStatus) {
		this.importLnpStatus = importLnpStatus;
	}
}
