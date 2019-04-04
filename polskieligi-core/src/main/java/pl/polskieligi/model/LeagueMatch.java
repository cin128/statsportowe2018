package pl.polskieligi.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_LM_PR_RO_MA", columnList = "project_id,round_id,matchpart1,matchpart2", unique = false) })
public class LeagueMatch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long match_id;
	private Integer minut_id;
	private Integer playground_id;
	private Timestamp match_date;
	
    @OneToOne
    @JoinColumn(name = "matchpart1")
    private Team matchpart1;
    
    @OneToOne
    @JoinColumn(name = "matchpart2")
    private Team matchpart2;

	private Float matchpart1_result;
	private Float matchpart2_result;
	private Long project_id;
	
	@OneToOne
	@JoinColumn(name="project_id", insertable =  false, updatable = false)
	private Project project;
	
	@OneToOne
    @JoinColumn(name = "round_id")
	private Round round;
	private Boolean count_result;
	private Boolean published;
	private Integer crowd;
	private String summary;
	private Timestamp modified;

	@OneToOne
	@JoinColumn(name = "referee_id")
	private Referee referee;

	private Long af_id;

	private Integer importStatus;

	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name="leagueMatch_id")
	private List<Substitution> substitutions = new ArrayList<Substitution>();

	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name="leagueMatch_id")
	@OrderBy("number ASC")
	private List<LeagueMatchPlayer> leagueMatchPlayers = new ArrayList<LeagueMatchPlayer>();

	public LeagueMatch() {
		minut_id = 0;
		playground_id = 0;
		match_date = new Timestamp(0);
		matchpart1_result = new Float(0);
		matchpart2_result = new Float(0);
		project_id = Long.valueOf(0);
		count_result = false;
		published = false;
		crowd = 0;
		summary = "";
		modified = new Timestamp(0);
		af_id = Long.valueOf(0);
	}

	public Long getMatch_id() {
		return match_id;
	}

	public void setMatch_id(Long match_id) {
		this.match_id = match_id;
	}

	public Integer getMinut_id() {
		return minut_id;
	}

	public void setMinut_id(Integer minut_id) {
		this.minut_id = minut_id;
	}

	public Integer getPlayground_id() {
		return playground_id;
	}

	public void setPlayground_id(Integer playground_id) {
		this.playground_id = playground_id;
	}

	public Timestamp getMatch_date() {
		return match_date;
	}

	public void setMatch_date(Timestamp match_date) {
		this.match_date = match_date;
	}

	public Team getMatchpart1() {
		return matchpart1;
	}

	public void setMatchpart1(Team matchpart1) {
		this.matchpart1 = matchpart1;
	}

	public Team getMatchpart2() {
		return matchpart2;
	}

	public void setMatchpart2(Team matchpart2) {
		this.matchpart2 = matchpart2;
	}

	public Float getMatchpart1_result() {
		return matchpart1_result;
	}

	public void setMatchpart1_result(Float matchpart1_result) {
		this.matchpart1_result = matchpart1_result;
	}

	public Float getMatchpart2_result() {
		return matchpart2_result;
	}

	public void setMatchpart2_result(Float matchpart2_result) {
		this.matchpart2_result = matchpart2_result;
	}

	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public Boolean getCount_result() {
		return count_result;
	}

	public void setCount_result(Boolean count_result) {
		this.count_result = count_result;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Integer getCrowd() {
		return crowd;
	}

	public void setCrowd(Integer crowd) {
		this.crowd = crowd;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public Long getAf_id() {
		return af_id;
	}

	public void setAf_id(Long af_id) {
		this.af_id = af_id;
	}

	public Referee getReferee() {
		return referee;
	}

	public void setReferee(Referee referee) {
		this.referee = referee;
	}

	public Integer getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(Integer importStatus) {
		this.importStatus = importStatus;
	}

	public List<Substitution> getSubstitutions() {
		return substitutions;
	}

	public void addSubstitutions(Substitution substitution) {
		this.substitutions.add(substitution);
	}

	public List<LeagueMatchPlayer> getLeagueMatchPlayers() {
		return leagueMatchPlayers;
	}

	public void addLeagueMatchPlayers(LeagueMatchPlayer leagueMatchPlayer) {
		this.leagueMatchPlayers.add(leagueMatchPlayer);
	}
	

	public Project getProject() {
		return project;
	}

	@Override public String toString() {
		return match_id+" " + match_date + " " + matchpart1 + " " + matchpart1_result + " - " + matchpart2_result + " " + matchpart2;
	}
}
