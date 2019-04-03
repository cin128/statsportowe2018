package pl.polskieligi.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity @Table(indexes = { @Index(name = "IDX_SU_LM", columnList = "leagueMatch_id", unique = false) })
public class Substitution implements Serializable {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private Long leagueMatch_id;

	private Long team_id;

	private Long playerIn_id;

	private Long playerOut_id;

	private Integer time;

	@OneToOne
	@JoinColumn(name="playerIn_id", insertable =  false, updatable = false)
	private Player playerIn;

	@OneToOne
	@JoinColumn(name="playerOut_id", insertable =  false, updatable = false)
	private Player playerOut;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLeagueMatch_id() {
		return leagueMatch_id;
	}

	public void setLeagueMatch_id(Long leagueMatch_id) {
		this.leagueMatch_id = leagueMatch_id;
	}

	public Long getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Long team_id) {
		this.team_id = team_id;
	}

	public Long getPlayerIn_id() {
		return playerIn_id;
	}

	public void setPlayerIn_id(Long playerIn_id) {
		this.playerIn_id = playerIn_id;
	}

	public Long getPlayerOut_id() {
		return playerOut_id;
	}

	public void setPlayerOut_id(Long playerOut_id) {
		this.playerOut_id = playerOut_id;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Player getPlayerIn() {
		return playerIn;
	}

	public Player getPlayerOut() {
		return playerOut;
	}

	public void setPlayerIn(Player playerIn) {
		this.playerIn = playerIn;
	}

	public void setPlayerOut(Player playerOut) {
		this.playerOut = playerOut;
	}
}
