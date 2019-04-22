package pl.polskieligi.dao;

import java.util.List;

import pl.polskieligi.dto.Scorer;
import pl.polskieligi.model.LeagueType;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Region;
import pl.polskieligi.model.Season;

public interface ProjectDAO extends AbstractDAO<Project>{
	public Project retrieveByMinut(Integer minutId);
	public Project retrieveByLnp(Integer lnpId);
	public Project getLastProjectForTeam(Integer teamId);
	public Long getOpenProjectsCount();
	public List<Project> findProjects(Long season, Integer leagueType, Integer region);
	public List<Scorer> retrieveScorers(Long projectId);
	public Project findProject(Season season, Region region, LeagueType lt, String groupName);
}
