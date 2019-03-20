package pl.polskieligi.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.model.LeagueMatch;

import javax.persistence.Query;

@Repository
@Transactional
public class LeagueMatchDAOImpl extends AbstractDAOImpl<LeagueMatch> implements LeagueMatchDAO {
	public LeagueMatchDAOImpl() {
		super(LeagueMatch.class);
	}
	public LeagueMatch saveUpdate(LeagueMatch obj) {
		return null;//TODO
	}
	
	@Override
	protected Query getRetrieveQuery(LeagueMatch obj) {
		// TODO Auto-generated method stub
		return null;
	}
	public int saveUpdate(List<LeagueMatch> roundMatches) {
		int result = 0;
			for (LeagueMatch match : roundMatches) {
				Query query = getEntityManager()
						.createQuery("SELECT lm from LeagueMatch lm where project_id = :project_id and round = :round and matchpart1 = :matchpart1 and matchpart2 = :matchpart2");
				query.setParameter("project_id", match.getProject_id());
				query.setParameter("round", match.getRound());
				query.setParameter("matchpart1", match.getMatchpart1());
				query.setParameter("matchpart2", match.getMatchpart2());
				LeagueMatch oldMatch = null;
				@SuppressWarnings("unchecked")
				List<LeagueMatch> matches = query.getResultList();
				for (LeagueMatch m : matches) {
					oldMatch = m;
					if (match.getCrowd() > 0) {
						oldMatch.setCrowd(match.getCrowd());
					}
					if (match.getMatch_date() != null) {
						oldMatch.setMatch_date(match.getMatch_date());
					}
					if (match.getCount_result()) {
						oldMatch.setCount_result(match.getCount_result());
						oldMatch.setPublished(match.getPublished());
						oldMatch.setMatchpart1_result(match
								.getMatchpart1_result());
						oldMatch.setMatchpart2_result(match
								.getMatchpart2_result());
					}
					oldMatch.setModified(new Timestamp((new Date()).getTime()));
					getEntityManager().merge(oldMatch);
					result++;
				}
				if (oldMatch == null) {
					match.setModified(new Timestamp((new Date()).getTime()));
					getEntityManager().persist(match);
					result++;
				}
			}		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<LeagueMatch> getMatchesByProjectId(Long projectId) {
		Query query = getEntityManager()
				.createQuery("SELECT lm from LeagueMatch lm where project_id = :project_id and published = :published order by round.matchcode desc, match_date desc");
		query.setParameter("project_id", projectId);
		query.setParameter("published", true);
		return query.getResultList();
		
	}
}
