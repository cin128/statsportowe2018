package pl.polskieligi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = { @Index(name = "IDX_LMP_PL_T_LM", columnList = "player_id,team_id,leagueMatch_id", unique = true) })
public class LeagueMatchPlayer{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private Long player_id;

	private Long team_id;

	private Long leagueMatch_id;

	private Integer minutIn;

	private Integer minutOut;

	private Integer number;

	private Boolean firstSquad;

	@OneToOne
	@JoinColumn(name="player_id", insertable =  false, updatable = false)
	private Player player;

	@OneToOne
	@JoinColumn(name="team_id", insertable =  false, updatable = false)
	private Team team;

	@OneToOne
	@JoinColumn(name="leagueMatch_id", insertable =  false, updatable = false)
	private LeagueMatch leagueMatch;

	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name="leagueMatchplayer_id")
	@OrderBy("time DESC")
	private List<MatchEvent> matchEvents = new ArrayList<MatchEvent>();

	@Transient
	private Substitution substitutionIn;

	@Transient
	private Substitution substitutionOut;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(Long player_id) {
		this.player_id = player_id;
	}

	public Long getLeagueMatch_id() {
		return leagueMatch_id;
	}

	public void setLeagueMatch_id(Long leagueMatch_id) {
		this.leagueMatch_id = leagueMatch_id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public LeagueMatch getLeagueMatch() {
		return leagueMatch;
	}

	public void setLeagueMatch(LeagueMatch leagueMatch) {
		this.leagueMatch = leagueMatch;
	}

	public Long getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Long team_id) {
		this.team_id = team_id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Integer getMinutIn() {
		return minutIn;
	}

	public void setMinutIn(Integer minutIn) {
		this.minutIn = minutIn;
	}

	public Integer getMinutOut() {
		return minutOut;
	}

	public void setMinutOut(Integer minutOut) {
		this.minutOut = minutOut;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<MatchEvent> getMatchEvents() {
		return matchEvents;
	}

	public void addMatchEvent(MatchEvent matchEvent) {
		this.matchEvents.add(matchEvent);
	}

	public Boolean getFirstSquad() {
		return firstSquad;
	}

	public void setFirstSquad(Boolean firstSquad) {
		this.firstSquad = firstSquad;
	}

	public Substitution getSubstitutionIn() {
		return substitutionIn;
	}

	public Substitution getSubstitutionOut() {
		return substitutionOut;
	}

	public void setSubstitutionIn(Substitution substitutionIn) {
		this.substitutionIn = substitutionIn;
	}

	public void setSubstitutionOut(Substitution substitutionOut) {
		this.substitutionOut = substitutionOut;
	}

	@Override public String toString() {
		return number+" "+minutIn+" "+minutOut;
	}
}
