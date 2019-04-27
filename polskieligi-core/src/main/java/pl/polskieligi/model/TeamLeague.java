package pl.polskieligi.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_TL_PR_TE", columnList = "project_id,team_id", unique = false) })
public class TeamLeague implements LnpObject{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private Long project_id;

	private Long team_id;
	
	private Integer lnp_id;
	private String lnpIdName;
	private String lnpName;
	private Integer importLnpStatus;

	@OneToOne
	@JoinColumn(name="project_id", insertable =  false, updatable = false)
	private Project project;

	@OneToOne
	@JoinColumn(name="team_id", insertable =  false, updatable = false)
	private Team team;
	
	private Integer startPoints;
	
	@OneToMany
	@JoinColumn(name = "teamLeague_id")
	private List<TeamLeaguePlayer> teamLeaguePlayers = new ArrayList<>();

	public TeamLeague() {
		startPoints=0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	public Long getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Long team_id) {
		this.team_id = team_id;
	}

	public Project getProject() {
		return project;
	}

	public Team getTeam() {
		return team;
	}
	
	public Integer getStartPoints() {
		return startPoints;
	}

	public void setStartPoints(Integer startPoints) {
		this.startPoints = startPoints;
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

	public String getLnpName() {
		return lnpName;
	}

	public void setLnpName(String lnpName) {
		this.lnpName = lnpName;
	}

	public Integer getImportLnpStatus() {
		return importLnpStatus;
	}

	public void setImportLnpStatus(Integer importLnpStatus) {
		this.importLnpStatus = importLnpStatus;
	}

	public List<TeamLeaguePlayer> getTeamLeaguePlayers() {
		return teamLeaguePlayers;
	}
}
