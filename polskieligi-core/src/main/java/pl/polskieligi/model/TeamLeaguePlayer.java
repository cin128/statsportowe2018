package pl.polskieligi.model;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_TLP_PL_TL", columnList = "player_id,teamLeague_id", unique = false) })
public class TeamLeaguePlayer {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private Long player_id;

	private Long teamLeague_id;

	@OneToOne
	@JoinColumn(name="player_id", insertable =  false, updatable = false)
	private Player player;

	@OneToOne
	@JoinColumn(name="teamLeague_id", insertable =  false, updatable = false)
	private TeamLeague teamLeague;

	public Long getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(Long player_id) {
		this.player_id = player_id;
	}

	public Long getTeamLeague_id() {
		return teamLeague_id;
	}

	public void setTeamLeague_id(Long teamLeague_id) {
		this.teamLeague_id = teamLeague_id;
	}

	public Player getPlayer() {
		return player;
	}

	public TeamLeague getTeamLeague() {
		return teamLeague;
	}
}
