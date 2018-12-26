package pl.polskieligi.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Team;

@Repository
@Transactional
public class TeamDAOImpl extends AbstractDAOImpl<Team> implements TeamDAO {
	public TeamDAOImpl() {
		super(Team.class);
	}

	@Override
	protected Query getRetrieveQuery(Team team) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Team where minut_id = :minut_id");
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
		List<Team> result = null;
		Session session = getCurrentSession();

		SQLQuery query = session.createSQLQuery(
				"SELECT t.* FROM  team_leagues tl join teams t on t.id = tl.team_id join projects p on p.id = tl.project_id where p.archive = :archive and p.type = :type and p.published = :published");
		query.addEntity(Team.class);
		query.setParameter("archive", false);
		query.setParameter("type", Project.REGULAR_LEAGUE);
		query.setParameter("published", true);
		result = query.list();

		return result;
	}
	
	@Override
	public Team retrieveTeamByMinut(Integer minutId) {
		Team result = null;
		Session session = getCurrentSession();

		Query query = session.createQuery("from Team where minut_id = :minut_id");
		query.setParameter("minut_id", minutId);
		@SuppressWarnings("unchecked") List<Team> teams = query.list();
		for (Team t : teams) {
			result = t;
		}

		return result;
	}

	@Override
	public Team retrieveTeamByName(String name) {
		Team result = null;
		Session session = getCurrentSession();

		Query query = session.createQuery("from Team where name = :name");
		query.setParameter("name", name);
		@SuppressWarnings("unchecked") List<Team> teams = query.list();
		for (Team t : teams) {
			result = t;
			break;
		}

		return result;
	}
}
