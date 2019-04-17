package pl.polskieligi.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Team;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class TeamDAOImpl extends AbstractDAOImpl<Team> implements TeamDAO {
	public TeamDAOImpl() {
		super(Team.class);
	}

	@Override
	protected TypedQuery<Team> getRetrieveQuery(Team team) {
		TypedQuery<Team> query = getEntityManager().createQuery("SELECT t from Team t where minut_id = :minut_id", Team.class);
		query.setParameter("minut_id", team.getMinut_id());
		return query;
	}
	@Override
	protected boolean updateData(Team source, Team target) {
		if (source.getName() != null && !source.getName().isEmpty()) {
			target.setName(source.getName());
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<Team> getTeams() {
		Query query = getEntityManager().createNativeQuery(
				"SELECT t.* FROM team_league tl join team t on t.id = tl.team_id join project p on p.id = tl.project_id where p.archive = :archive and p.type = :type and p.published = :published", Team.class);
		query.setParameter("archive", false);
		query.setParameter("type", Project.REGULAR_LEAGUE);
		query.setParameter("published", true);

		return query.getResultList();
	}
	
	@Override
	public Team retrieveTeamByMinut(Integer minutId) {
		Team result = null;
		Query query = getEntityManager().createQuery("SELECT t from Team t where minut_id = :minut_id");
		query.setParameter("minut_id", minutId);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked") List<Team> teams = query.getResultList();
		for (Team t : teams) {
			result = t;
		}
		return result;
	}

	@Override
	public Team retrieveTeamByName(String name) {
		Team result = null;
		Query query = getEntityManager().createQuery("SELECT t from Team t where name = :name");
		query.setParameter("name", name);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked") List<Team> teams = query.getResultList();
		for (Team t : teams) {
			result = t;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getMatchingTeams(String name) {
		Query query = getEntityManager().createQuery("SELECT t from Team t where lower(name) like lower(concat('%', :name,'%'))", Team.class);
		query.setParameter("name", name);
		query.setMaxResults(3);
		return query.getResultList();
	}
}
