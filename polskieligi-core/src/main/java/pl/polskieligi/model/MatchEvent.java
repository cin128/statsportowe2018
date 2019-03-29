package pl.polskieligi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity  @Table(indexes = {@Index(name = "IDX_ME_LMP", columnList = "leagueMatchplayer_id", unique = false)})
public class MatchEvent {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private Long leagueMatchplayer_id;

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

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
