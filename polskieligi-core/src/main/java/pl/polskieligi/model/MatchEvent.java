package pl.polskieligi.model;

import javax.persistence.*;

@Entity  @Table(indexes = {@Index(name = "IDX_ME_LMP", columnList = "leagueMatchplayer_id", unique = false)})
public class MatchEvent implements Comparable{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private Long leagueMatchplayer_id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="leagueMatchplayer_id",  insertable =  false, updatable = false)
	private LeagueMatchPlayer leagueMatchPlayer;

	private Integer time;

	private Integer type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLeagueMatchplayer_id() {
		return leagueMatchplayer_id;
	}

	public void setLeagueMatchplayer_id(Long leagueMatchplayer_id) {
		this.leagueMatchplayer_id = leagueMatchplayer_id;
	}

	public LeagueMatchPlayer getLeagueMatchPlayer() {
		return leagueMatchPlayer;
	}

	public void setLeagueMatchPlayer(LeagueMatchPlayer leagueMatchPlayer) {
		this.leagueMatchPlayer = leagueMatchPlayer;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public MatchEventType getType() {
		return MatchEventType.getById(type);
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override public int compareTo(Object o) {
		return this.getTime()-((MatchEvent)o).getTime();
	}
}
