package pl.polskieligi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.TableDAO;
import pl.polskieligi.dto.TableRow;
import pl.polskieligi.log.comparator.TableRowAwayComparator;
import pl.polskieligi.log.comparator.TableRowHomeComparator;
import pl.polskieligi.model.Project;

@Controller
public class TableController {

	final static Logger log = Logger.getLogger(TableController.class);

	@Autowired
	TableDAO tableDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@RequestMapping("/table")
	public ModelAndView showTable(Long projectId) {
		log.info("table start");
		List<TableRow> rows = tableDAO.getTableRows(projectId);
		Project p = projectDAO.find(projectId);
		String title = p.getName();
		ModelAndView mv = new ModelAndView("thymeleaf/table");
		mv.addObject("project_name", title);
		mv.addObject("rows", rows);
		mv.addObject("rowsHome", sortRowsHome(rows));
		mv.addObject("rowsAway", sortRowsAway(rows));
		return mv;
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
}
