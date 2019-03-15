package pl.polskieligi.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.controller.TableController;
import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.TableDAO;
import pl.polskieligi.dto.TableRow;
import pl.polskieligi.log.comparator.TableRowAwayComparator;
import pl.polskieligi.log.comparator.TableRowHomeComparator;
import pl.polskieligi.model.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableSessionBean {

	final static Logger log = Logger.getLogger(TableSessionBean.class);

	@Autowired TableDAO tableDAO;

	@Autowired ProjectDAO projectDAO;

	private Long projectId = null;

	private List<TableRow> rows;
	private List<TableRow> rowsHome;
	private List<TableRow> rowsAway;
	private Project project;
	private String projectName;

	public void calculateTable(Long projectId) {
		log.info("calculateTable: "+projectId);
		if(this.projectId==null || this.projectId!=projectId){
			log.info("projectId changed. Old: "+this.projectId);
			this.projectId=projectId;
			rows = tableDAO.getTableRows(projectId);
			rowsHome = sortRowsHome(rows);
			rowsAway = sortRowsAway(rows);
			project = projectDAO.find(projectId);
			projectName = project.getName();
			log.info("projectName: "+projectName);
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
}
