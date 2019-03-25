package pl.polskieligi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(indexes = { @Index(name = "IDX_LMP_PL_LM", columnList = "player_id,leagueMatch_id", unique = true) })
public class LeagueMatchPlayer {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private Long player_id;

	private Long leagueMatch_id;

	@OneToOne
	@JoinColumn(name="player_id", insertable =  false, updatable = false)
	private Player player;

	@OneToOne
	@JoinColumn(name="leagueMatch_id", insertable =  false, updatable = false)
	private LeagueMatch leagueMatch;

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
	
	
}