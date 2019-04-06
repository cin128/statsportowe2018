package pl.polskieligi.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueMatchPlayerDAO;
import pl.polskieligi.model.LeagueMatchPlayer;

@Repository
@Transactional
public class LeagueMatchPlayerDAOImpl extends AbstractDAOImpl<LeagueMatchPlayer> implements LeagueMatchPlayerDAO {
	public LeagueMatchPlayerDAOImpl() {
		super(LeagueMatchPlayer.class);
	}
	@Override
	protected Query getRetrieveQuery(LeagueMatchPlayer obj) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<LeagueMatchPlayer> getPlayerMatchesForSeason(Long playerId, Long seasonId) {
		TypedQuery<LeagueMatchPlayer> query = getEntityManager()
				.createQuery("SELECT DISTINCT lmp from LeagueMatchPlayer lmp LEFT JOIN FETCH lmp.matchEvents me JOIN FETCH lmp.leagueMatch lm JOIN FETCH lm.matchpart1 t1 JOIN FETCH lm.matchpart2 t2 JOIN lm.project p JOIN p.season s where lmp.player_id = :playerId and s.id = :seasonId order by lm.match_date desc", LeagueMatchPlayer.class);
		query.setParameter("playerId", playerId);
		query.setParameter("seasonId", seasonId);
		return query.getResultList();
	}
}
