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
	protected Query getRetrieveQuery(TeamLeaguePlayer obj) {
		// TODO Auto-generated method stub
		return null;
	}
}
