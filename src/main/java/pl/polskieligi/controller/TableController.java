package pl.polskieligi.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.TableDAO;
import pl.polskieligi.dto.TableRow;

@Controller
public class TableController {

	final static Logger log = Logger.getLogger(TableController.class);

	@Autowired
	TableDAO tableDAO;
	
	@RequestMapping("/table")
	public ModelAndView showTable(Long projectId) {
		log.info("table start");
		List<TableRow> rows = tableDAO.getTableRows(projectId);
		ModelAndView mv = new ModelAndView("table", "rows", rows);
		return mv;		
	}
}
