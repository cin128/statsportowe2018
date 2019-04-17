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
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class TeamLeagueDAOImpl  extends AbstractDAOImpl<TeamLeague> implements TeamLeagueDAO{
	public TeamLeagueDAOImpl() {
		super(TeamLeague.class);
	}
	
	@Override
	public TeamLeague findByProjectAndTeam(Long projectId, Long teamId) {
		Query query = getRetrieveQuery(projectId, teamId);
		List l = query.getResultList();
		if(l.size()>0) {
			return (TeamLeague)l.get(0);
		}
		return null;
	}
	
	@Override
	protected TypedQuery<TeamLeague> getRetrieveQuery(TeamLeague teamLeague) {
		return getRetrieveQuery(teamLeague.getProject_id(),  teamLeague.getTeam_id());
	}
	
	@Override
	protected boolean updateData(TeamLeague source, TeamLeague target) {
		boolean update = false;
		if(!source.getStartPoints().equals(target.getStartPoints())) {
			target.setStartPoints(source.getStartPoints());
			update = true;
		}
		return update;
	}
	
	protected TypedQuery<TeamLeague> getRetrieveQuery(Long projectId, Long teamId) {
		TypedQuery<TeamLeague> query = getEntityManager().createQuery(
				"select tl from TeamLeague tl " + "where tl.team_id = :team_id and tl.project_id = :project_id", TeamLeague.class);
		query.setParameter("project_id", projectId);
		query.setParameter("team_id", teamId);
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
				query.setParameter("matchpart1", tl.getTeam_id());
				query.setParameter("matchpart2", tl.getTeam_id());

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

			Query query = getEntityManager().createQuery("select t from Team t where exists (select tl from TeamLeague tl where t.id = tl.team_id and tl.project_id = :project_id) order by t.name");
			query.setParameter("project_id", projectId);
			result = query.getResultList();
			
		return result;
	}
}
