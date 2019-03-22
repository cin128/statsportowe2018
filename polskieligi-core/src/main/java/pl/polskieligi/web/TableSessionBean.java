package pl.polskieligi.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.SeasonDAO;
import pl.polskieligi.dao.TableDAO;
import pl.polskieligi.dto.TableRow;
import pl.polskieligi.log.comparator.TableRowAwayComparator;
import pl.polskieligi.log.comparator.TableRowHomeComparator;
import pl.polskieligi.model.*;

public class TableSessionBean {

	final static Logger log = Logger.getLogger(TableSessionBean.class);

	@Autowired TableDAO tableDAO;

	@Autowired ProjectDAO projectDAO;
	
	@Autowired LeagueMatchDAO leagueMatchDAO;

	@Autowired SeasonDAO seasonDAO;

	private Long projectId = null;

	private List<TableRow> rows;
	private List<TableRow> rowsHome;
	private List<TableRow> rowsAway;
	private Project project;
	private String projectName;
	private List<LeagueMatch> matches;

	public void calculateTable(Long projectId) {
		log.info("calculateTable: "+projectId);
		if(this.projectId==null || this.projectId!=projectId){
			log.info("projectId changed. Old: "+this.projectId);
			this.projectId=projectId;
			project = projectDAO.find(projectId);
			if(project!=null) {
				projectName = project.getName();
				matches = leagueMatchDAO.getMatchesByProjectId(projectId);
				if(project.getType()==Project.REGULAR_LEAGUE) {
					rows = tableDAO.getTableRows(projectId);
					rowsHome = sortRowsHome(rows);
					rowsAway = sortRowsAway(rows);
				}
			} else {
				log.error("Project not found: " + projectId);
			}
		}
	}

	private List<TableRow> sortRowsHome(List<TableRow> rows){
		List<TableRow> result = new ArrayList<TableRow>(rows);
		Collections.sort(result, new TableRowHomeComparator());
		return result;
	}

	private List<TableRow> sortRowsAway(List<TableRow> rows){
		List<TableRow> result = new ArrayList<TableRow>(rows);
		Collections.sort(result, new TableRowAwayComparator());
		return result;
	}

	public String getProjectName(){
		return projectName;
	}

	public List<TableRow> getRows() {
		return rows;
	}

	public List<TableRow> getRowsHome() {
		return rowsHome;
	}

	public List<TableRow> getRowsAway() {
		return rowsAway;
	}
	
	public List<LeagueMatch> getMatches(){
		return matches;
	}
	
	public Project getProject(){
		return project;
	}
	
	public List<Project> findProjects(Long season, Integer leagueType, Integer region){
		return projectDAO.findProjects(season, leagueType, region);
	}

	public List<Season> getAllSeasons(){
		return seasonDAO.findAll();
	}
}
