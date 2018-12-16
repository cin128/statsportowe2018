package pl.polskieligi.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.MatchDAO;
import pl.polskieligi.model.Match;

@Repository
@Transactional
public class MatchDAOImpl extends AbstractDAOImpl<Match> implements MatchDAO {
	public MatchDAOImpl() {
		super(Match.class);
	}
	public Match saveUpdate(Match obj) {
		return null;//TODO
	}
	
	@Override
	protected Query getRetrieveQuery(Match obj) {
		// TODO Auto-generated method stub
		return null;
	}
	public int saveUpdate(List<Match> roundMatches) {
		int result = 0;
		Session session = getCurrentSession();
			for (Match match : roundMatches) {
				Query query = session
						.createQuery("from Match where project_id = :project_id and round_id = :round_id and matchpart1 = :matchpart1 and matchpart2 = :matchpart2");
				query.setParameter("project_id", match.getProject_id());
				query.setParameter("round_id", match.getRound_id());
				query.setParameter("matchpart1", match.getMatchpart1());
				query.setParameter("matchpart2", match.getMatchpart2());
				Match oldMatch = null;
				@SuppressWarnings("unchecked")
				List<Match> matches = query.list();
				for (Match m : matches) {
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
					session.update(oldMatch);
					result++;
				}
				if (oldMatch == null) {
					match.setModified(new Timestamp((new Date()).getTime()));
					Object o = session.save(match);
					if(o!=null){
						result++;
					}
				}
			}		
		return result;
	}
}
