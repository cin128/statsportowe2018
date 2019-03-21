package pl.polskieligi.model;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_PR_MINUT_ID", columnList = "minut_id", unique = false) })
public class Project {
	public final static Integer REGULAR_LEAGUE = 1;
	public final static Integer OTHER = 2;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Integer minut_id;
	private String name;
	
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private League league;
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private Season season;
	private Date start_date;
	private Boolean published;
	private Boolean archive;
	private Integer type;

	public Project() {
		minut_id = 0;
		name = "";
		start_date = new Date(0);
		published = false;
		archive = false;
		type = 0;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMinut_id() {
		return minut_id;
	}

	public void setMinut_id(Integer minut_id) {
		this.minut_id = minut_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return name;
	}
}
