package pl.polskieligi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = { @Index(name = "IDX_TE_MINUT_ID", columnList = "minut_id", unique = false),
		 @Index(name = "IDX_TE_NAME", columnList = "name", unique = false)})
public class Team implements MinutObject{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String longName;
	
	private Integer minut_id;
	private Integer importStatus;

	@OneToMany
	@JoinColumn(name = "team_id")
	private List<TeamLeague> teamLeagues = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="club_id", insertable =  false, updatable = false)
	private Club club;

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
	
	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public List<TeamLeague> getTeamLeagues() {
		return teamLeagues;
	}
	
	@Override
	public String toString() {
		return getMinut_id() + " "+ name +" " + longName;
	}

	public Integer getMinut_id() {
		return minut_id;
	}

	public void setMinut_id(Integer minut_id) {
		this.minut_id = minut_id;
	}

	public Integer getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(Integer importStatus) {
		this.importStatus = importStatus;
	}
}
