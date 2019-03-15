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
import pl.polskieligi.web.TableSessionBean;

@Controller
public class TableController {

	final static Logger log = Logger.getLogger(TableController.class);

	@Autowired
	private TableSessionBean tableSessionBean;
	
	@RequestMapping("/table")
	public ModelAndView showTable(Long projectId) {
		log.info("showTable: " + projectId);
		tableSessionBean.calculateTable(projectId);
		ModelAndView mv = new ModelAndView("thymeleaf/table");
		mv.addObject("ts", tableSessionBean);
		return mv;
	}

}
