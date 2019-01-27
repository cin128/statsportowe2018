package pl.polskieligi.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.TableDAO;
import pl.polskieligi.dto.TableRow;
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
		ModelAndView mv = new ModelAndView("table", "rows", rows);
		mv.addObject("project_name", title);
		return mv;		
	}
}
