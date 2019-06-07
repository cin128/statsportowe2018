package pl.polskieligi.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import pl.polskieligi.dto.ProjectInfo;

@Entity
@Table(indexes = { @Index(name = "IDX_PR_MINUT_ID", columnList = "minut_id", unique = false),
		@Index(name = "IDX_PR_LNP_ID", columnList = "lnp_id", unique = false)})
public class Project extends AbstractObject{
	public final static Integer REGULAR_LEAGUE = 1;
	public final static Integer OTHER = 2;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	private String name;
	
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private League league;
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private Season season;
	private Date start_date;
	private Boolean published;
	private Boolean archive;
	private Integer type;
	
	private String lnpName;
	
	@OneToMany(mappedBy="project")
	private Set<TeamLeague> teamLeagues = new HashSet<TeamLeague>();

	@OneToMany(mappedBy="project")
	private Set<LeagueMatch> leagueMatches = new HashSet<LeagueMatch>();

	@Transient
	private ProjectInfo projectInfo;

	public Project() {
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
	
	public Set<TeamLeague> getTeamLeagues(){
		return teamLeagues;
	}

	public String getLnpName() {
		return lnpName;
	}

	public void setLnpName(String lnpName) {
		this.lnpName = lnpName;
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

	@Override
	public String toString() {
		return name;
	}
}
