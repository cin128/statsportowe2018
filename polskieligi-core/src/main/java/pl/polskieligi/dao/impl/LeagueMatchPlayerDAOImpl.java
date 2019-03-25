package pl.polskieligi.dao.impl;

import javax.persistence.Query;

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
}
