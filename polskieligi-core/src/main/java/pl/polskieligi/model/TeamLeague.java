package pl.polskieligi.model;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_TL_PR_TE", columnList = "project_id,team_id", unique = false) })
public class TeamLeague implements LnpObject{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private Long project_id;

	private Long team_id;

	private Long club_id;
	
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
	
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name = "teamLeague_id")
	private Set<TeamLeaguePlayer> teamLeaguePlayers = new HashSet<>();

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

	public Long getClub_id() {
		return club_id;
	}

	public void setClub_id(Long club_id) {
		this.club_id = club_id;
	}

	public Set<TeamLeaguePlayer> getTeamLeaguePlayers() {
		return teamLeaguePlayers;
	}

	public void addPlayer(Long playerId) {
		TeamLeaguePlayer teamLeaguePlayer = new TeamLeaguePlayer();
		teamLeaguePlayer.setPlayer_id(playerId);
		teamLeaguePlayer.setTeamLeague_id(getId());
		this.teamLeaguePlayers.add(teamLeaguePlayer);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;

		if (!(obj instanceof TeamLeague))
			return false;

		if (obj == this)
			return true;

		return this.project_id.equals(((TeamLeague) obj).project_id)
				&&this.team_id.equals(((TeamLeague) obj).team_id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(project_id,team_id);
	}
}
