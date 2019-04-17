package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.TeamLeaguePlayerDAO;
import pl.polskieligi.model.TeamLeaguePlayer;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class TeamLeaguePlayerDAOImpl extends AbstractDAOImpl<TeamLeaguePlayer> implements TeamLeaguePlayerDAO {
	public TeamLeaguePlayerDAOImpl() {
		super(TeamLeaguePlayer.class);
	}
	@Override
	protected TypedQuery<TeamLeaguePlayer> getRetrieveQuery(TeamLeaguePlayer tlp) {
		TypedQuery<TeamLeaguePlayer> query = getEntityManager().createQuery("SELECT tlp from TeamLeaguePlayer tlp where player_id = :player_id AND teamLeague_id = :teamLeague_id", TeamLeaguePlayer.class);
		query.setParameter("player_id", tlp.getPlayer_id());
		query.setParameter("teamLeague_id", tlp.getTeamLeague_id());
		return query;
	}
}
