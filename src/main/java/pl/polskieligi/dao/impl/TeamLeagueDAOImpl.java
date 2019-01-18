package pl.polskieligi.dao.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.Team;
import pl.polskieligi.model.TeamLeague;

import javax.persistence.Query;

@Repository
@Transactional
public class TeamLeagueDAOImpl  extends AbstractDAOImpl<TeamLeague> implements TeamLeagueDAO{
	public TeamLeagueDAOImpl() {
		super(TeamLeague.class);
	}
	
	@Override
	protected Query getRetrieveQuery(TeamLeague teamLeague) {
		Query query = getEntityManager()
				.createQuery("select tl from TeamLeague tl join tl.team t join tl.project p "
						+ "where t.id = :team_id and p.id = :project_id");
		query.setParameter("project_id", teamLeague.getProject().getId());
		query.setParameter("team_id", teamLeague.getTeam().getId());
		return query;
	}

	public void updateTeams(Long projectId,
			Collection<Long> teamIds) {

			Query query = getEntityManager()
					.createQuery("SELECT tl from TeamLeague tl where project_id = :project_id AND team_id NOT IN (:team_id)");
			query.setParameter("project_id", projectId);
			query.setParameter("team_id", teamIds);

			@SuppressWarnings("unchecked")
			List<TeamLeague> teamsToDelete = query.getResultList();
			for (TeamLeague tl : teamsToDelete) {							
				query = getEntityManager()
						.createQuery("SELECT m from Match m where project_id = :project_id AND (matchpart1 = :matchpart1 or matchpart2 = :matchpart2)");
				query.setParameter("project_id", projectId);
				query.setParameter("matchpart1", tl.getTeam().getId());
				query.setParameter("matchpart2", tl.getTeam().getId());

				@SuppressWarnings("unchecked")
				List<LeagueMatch> matchesToDelete = query.getResultList();
				for (LeagueMatch m : matchesToDelete) {
					getEntityManager().remove(m);
				}
				getEntityManager().remove(tl);
			}
			
	}
	
	@SuppressWarnings("unchecked")
	public List<Team> getTeams(Long projectId) {
		List<Team> result = null;

			Query query = getEntityManager().createQuery("from Team t where exists ( from TeamLeague tl join tl.team t1 join tl.project p1 where t.id = t1.id and p1.id = :project_id) order by t.name");
			query.setParameter("project_id", projectId);
			result = query.getResultList();
			
		return result;
	}
}
