package pl.polskieligi.model;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_TL_PR_TE", columnList = "project_id,team_id", unique = false) })
public class TeamLeague {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private Long project_id;

	private Long team_id;

	@OneToOne
	@JoinColumn(name="project_id", insertable =  false, updatable = false)
	private Project project;

	@OneToOne
	@JoinColumn(name="team_id", insertable =  false, updatable = false)
	private Team team;
	
	private Integer startPoints;

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
}
