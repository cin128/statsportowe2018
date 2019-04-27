package pl.polskieligi.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.model.LeagueMatch;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class LeagueMatchDAOImpl extends AbstractDAOImpl<LeagueMatch> implements LeagueMatchDAO {
	public LeagueMatchDAOImpl() {
		super(LeagueMatch.class);
	}

	@Override
	protected TypedQuery<LeagueMatch> getRetrieveQuery(LeagueMatch match) {

		return getRetrieveQuery(match.getProject_id(), match.getRound().getId(), match.getMatchpart1().getId(),  match.getMatchpart2().getId());
	}
	
	protected TypedQuery<LeagueMatch> getRetrieveQuery(Long projectId, Long roundId, Long team1Id, Long team2Id) {
		TypedQuery<LeagueMatch> query = getEntityManager().createQuery(
				"SELECT lm from LeagueMatch lm where project_id = :project_id and round_id = :round and matchpart1_id = :matchpart1 and matchpart2_id = :matchpart2", LeagueMatch.class);
		query.setParameter("project_id", projectId);
		query.setParameter("round", roundId);
		query.setParameter("matchpart1", team1Id);
		query.setParameter("matchpart2", team2Id);
		return query;
	}

	@Override
	protected boolean updateData(LeagueMatch match, LeagueMatch oldMatch) {
		boolean updated = false;
		if (match.getCrowd() > 0) {
			oldMatch.setCrowd(match.getCrowd());
			updated = true;
		}
		if (match.getMatch_date() != null) {
			oldMatch.setMatch_date(match.getMatch_date());
			updated = true;
		}
		if (match.getCount_result()) {
			oldMatch.setCount_result(match.getCount_result());
			oldMatch.setPublished(match.getPublished());
			oldMatch.setMatchpart1_result(match.getMatchpart1_result());
			oldMatch.setMatchpart2_result(match.getMatchpart2_result());
			updated = true;
		}
		if (match.getMinut_id() > 0) {
			oldMatch.setMinut_id(match.getMinut_id());
			updated = true;
		}
		if (updated) {
			oldMatch.setModified(new Timestamp((new Date()).getTime()));
		}
		return updated;
	}

	public List<LeagueMatch> getMatchesByProjectId(Long projectId) {
		TypedQuery<LeagueMatch> query = getEntityManager().createQuery(
				"SELECT lm from LeagueMatch lm JOIN FETCH lm.round r where lm.project_id = :project_id and lm.published = :published order by r.matchcode desc, lm.match_date desc",
				LeagueMatch.class);
		query.setParameter("project_id", projectId);
		query.setParameter("published", true);
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<LeagueMatch> getMatchesByTeamSeason(Long teamId, Long seasonId) {
		Query query = getEntityManager().createNativeQuery(
				"SELECT lm.* from LeagueMatch lm JOIN Project p on p.id = lm.project_id where ( lm.matchpart1 = :teamId OR lm. matchpart2 = :teamId) AND p.season_id = :seasonId and lm.published = :published order by lm.match_date desc",
				LeagueMatch.class);
		query.setParameter("teamId", teamId);
		query.setParameter("seasonId", seasonId);
		query.setParameter("published", true);
		return query.getResultList();
	}

	@Override
	public LeagueMatch find(Long projectId, Long roundId, Long team1Id, Long team2Id) {
		TypedQuery<LeagueMatch> query = getRetrieveQuery( projectId,  roundId,  team1Id,  team2Id);
		List<LeagueMatch> result = query.getResultList();
		if(result.size()==1) {
			return result.get(0);
		}
		return null;
	}
}
