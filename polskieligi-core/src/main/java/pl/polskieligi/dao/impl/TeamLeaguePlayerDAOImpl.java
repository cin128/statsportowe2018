package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.TeamLeaguePlayerDAO;
import pl.polskieligi.model.TeamLeaguePlayer;

import javax.persistence.Query;

@Repository
@Transactional
public class TeamLeaguePlayerDAOImpl extends AbstractDAOImpl<TeamLeaguePlayer> implements TeamLeaguePlayerDAO {
	public TeamLeaguePlayerDAOImpl() {
		super(TeamLeaguePlayer.class);
	}
	@Override
	protected Query getRetrieveQuery(TeamLeaguePlayer tlp) {
		Query query = getEntityManager().createQuery("SELECT tlp from TeamLeaguePlayer tlp where player_id = :player_id AND teamLeague_id = :teamLeague_id");
		query.setParameter("player_id", tlp.getPlayer_id());
		query.setParameter("teamLeague_id", tlp.getTeamLeague_id());
		return query;
	}
}
