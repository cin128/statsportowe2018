package pl.polskieligi.dao.impl;

import java.sql.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.model.Project;

@Repository
@Transactional
public class ProjectDAOImpl extends AbstractDAOImpl<Project> implements ProjectDAO {
	public ProjectDAOImpl() {
		super(Project.class);
	}

	public Project retrieveProjectByMinut(Integer minutId) {
		Project result = null;

		Query query = getEntityManager().createQuery("SELECT p from Project p where minut_id = :minut_id");
		query.setParameter("minut_id", minutId);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Project> projects = query.getResultList();
		for (Project p : projects) {
			result = p;
		}

		return result;
	}

	@Override
	protected Query getRetrieveQuery(Project leagueProject) {
		Query query = getEntityManager().createQuery(
				"SELECT p from Project p where minut_id = :minut_id");// or (league_id = :league_id and season_id = :season_id)
		query.setParameter("minut_id", leagueProject.getMinut_id());
		
//		query.setParameter("league_id", leagueProject.getLeague().getId());
//		query.setParameter("season_id", leagueProject.getSeason().getId());
		return query;
	}
	
	@Override
	protected boolean updateData(Project leagueProject, Project oldProject) {
		boolean result = false;
		if (leagueProject.getMinut_id() > 0 && leagueProject.getMinut_id()!=oldProject.getMinut_id()) {
			oldProject.setMinut_id(leagueProject.getMinut_id());
			result=true;
		}
		if (leagueProject.getLeague()!=null) {
			oldProject.setLeague(leagueProject.getLeague());
			result=true;
		}
		if (leagueProject.getSeason() !=null) {
			oldProject.setSeason(leagueProject.getSeason());
			result=true;
		}
		if (leagueProject.getName() != null && !leagueProject.getName().isEmpty() && !leagueProject.getName().equals(oldProject.getName())) {
			oldProject.setName(leagueProject.getName());
			result=true;
		}
		if (leagueProject.getStart_date() != null && !leagueProject.getStart_date().equals(new Date(0))) {
			oldProject.setStart_date(leagueProject.getStart_date());
			result=true;
		}
		if(oldProject.getArchive()!=leagueProject.getArchive()) {
			oldProject.setArchive(leagueProject.getArchive());
			result=true;
		}
		if (leagueProject.getType() > 0) {
			oldProject.setType(leagueProject.getType());
			result=true;
		}
		return result;
	}

	public Project getLastProjectForTeam(Integer teamId) {
		Project result = null;
		Query query = getEntityManager().createNativeQuery("SELECT p " + "FROM team_league tl "
				+ "LEFT JOIN project p ON p.id = tl.project_id "
				+ "WHERE tl.team_id = :team_id and p.published = :published and p.type = :type AND p.start_date = ( "
				+ "SELECT MAX( p2.start_date ) " + "FROM team_league tl2 "
				+ "LEFT JOIN project p2 ON p2.id = tl2.project_id "
				+ "WHERE tl2.team_id = tl.team_id and p2.published = :published and p2.type = :type )", Project.class);
		query.setParameter("team_id", teamId);
		query.setParameter("published", true);
		query.setParameter("type", Project.REGULAR_LEAGUE);
		query.setMaxResults(1);
		for (Object i : query.getResultList()) {
			result = (Project) i;
		}
		return result;
	}
	
	public Long getOpenProjectsCount() {
		Query query = getEntityManager().createQuery("SELECT COUNT(p) FROM Project p " );
		return (Long) query.getSingleResult();
	}

	@Override
	public List<Project> findProjects(Integer leagueType, Integer region) {
		TypedQuery<Project> query = getEntityManager().createQuery("SELECT p FROM Project p JOIN p.league l WHERE l.leagueType = :leagueType and l.region = :region", Project.class);
		query.setParameter("leagueType", leagueType);
		query.setParameter("region", region);
		return query.getResultList();
	}
}
