package pl.polskieligi.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;
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
		return getMinutRetrieveQuery(team.getMinut_id());
	}
	@Override
	protected boolean updateData(Team source, Team target) {
		if (!StringUtils.isEmpty(source.getName()) && !source.getName().equals(target.getName())) {
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
