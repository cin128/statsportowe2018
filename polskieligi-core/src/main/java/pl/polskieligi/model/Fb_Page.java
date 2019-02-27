package pl.polskieligi.model;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Fb_Page {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private BigInteger page_id;
	
	@ManyToOne
	private Team team;

	public Fb_Page() {
		page_id = new BigInteger("0");
	}

	public Long getId() {
		return id;
	}

	public BigInteger getPage_id() {
		return page_id;
	}

	public void setPage_id(BigInteger page_id) {
		this.page_id = page_id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}	
}
