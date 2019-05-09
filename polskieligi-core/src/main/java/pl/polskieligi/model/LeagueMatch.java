package pl.polskieligi.model;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_LM_PR_RO_MA", columnList = "project_id,round_id,matchpart1,matchpart2", unique = false) })
public class LeagueMatch extends AbstractObject{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long match_id;
	private Integer playground_id;
	private Timestamp match_date;
	
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matchpart1")
    private Team matchpart1;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matchpart2")
    private Team matchpart2;
    
    @Column(name="matchpart1", insertable=false, updatable=false)
    private Long matchpart1_id;
    @Column(name="matchpart2", insertable=false, updatable=false)
    private Long matchpart2_id;
    
	private Float matchpart1_result;
	private Float matchpart2_result;
	private Long project_id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project_id", insertable =  false, updatable = false)
	private Project project;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id")
	private Round round;
	private Boolean count_result;
	private Boolean published;
	private Integer crowd;
	private String summary;
	private Timestamp modified;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "referee_id")
	private Referee referee;

	private Long af_id;
	
	private String location;

	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name="leagueMatch_id")
	private Set<Substitution> substitutions = new HashSet<Substitution>();

	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name="leagueMatch_id")
	private Set<LeagueMatchPlayer> leagueMatchPlayers = new HashSet<LeagueMatchPlayer>();

	public LeagueMatch() {
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

	public List<Substitution> getSubstitutions() {
		return substitutions.stream().collect(Collectors.toList());
	}

	public void addSubstitutions(Substitution substitution) {
		this.substitutions.add(substitution);
	}

	public List<LeagueMatchPlayer> getLeagueMatchPlayers() {
		return leagueMatchPlayers.stream().sorted(Comparator.comparing(LeagueMatchPlayer::getNumber)).collect(Collectors.toList());
	}

	public void addLeagueMatchPlayers(LeagueMatchPlayer leagueMatchPlayer) {
		if(leagueMatchPlayers.contains(leagueMatchPlayer)) {
			Optional<LeagueMatchPlayer> tlp = leagueMatchPlayers.stream().filter(p->p.equals(leagueMatchPlayer)).findFirst();
			tlp.ifPresent(p->
			{
				if(p.getNumber()==null) {
					p.setNumber(leagueMatchPlayer.getNumber());
				}
				if(p.getMinutIn()==0){
					p.setMinutIn(leagueMatchPlayer.getMinutIn());
				}
				if(p.getMinutOut()==0){
					p.setMinutOut(leagueMatchPlayer.getMinutOut());
				}
				if(!p.getFirstSquad()){
					p.setFirstSquad(leagueMatchPlayer.getFirstSquad());
				}
				List<MatchEvent> me = p.getMatchEvents();
				if(me.isEmpty()){
					p.addMatchEvents(leagueMatchPlayer.getMatchEvents());
				}
			});
		} else {
			this.leagueMatchPlayers.add(leagueMatchPlayer);
		}
	}
	

	public Project getProject() {
		return project;
	}
	

	public Long getMatchpart1_id() {
		return matchpart1_id;
	}

	public void setMatchpart1_id(Long matchpart1_id) {
		this.matchpart1_id = matchpart1_id;
	}

	public Long getMatchpart2_id() {
		return matchpart2_id;
	}

	public void setMatchpart2_id(Long matchpart2_id) {
		this.matchpart2_id = matchpart2_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override public String toString() {
		return match_id+" " + match_date + " " + matchpart1 + " " + matchpart1_result + " - " + matchpart2_result + " " + matchpart2;
	}
}
