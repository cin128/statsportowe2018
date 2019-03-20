package pl.polskieligi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.web.TableSessionBean;

@Controller
public class TableController {

	final static Logger log = Logger.getLogger(TableController.class);

	@Autowired
	private TableSessionBean tableSessionBean;

	@RequestMapping("/table")
	public ModelAndView showTable(@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "leagueType", required = false) Integer leagueType,
			@RequestParam(value = "region", required = false) Integer region) {
		log.info("showTable: " + projectId);
		log.info("leagueType: " + leagueType);
		log.info("region: " + region);
		TableForm tf = new TableForm();
		tf.projectId=projectId;
		tf.leagueType=leagueType;
		tf.region=region;
		ModelAndView mv = new ModelAndView("thymeleaf/table");
		if (projectId != null) {
			tableSessionBean.calculateTable(projectId);
			tf.leagueType=tableSessionBean.getLeagueType().getId();
			tf.region=tableSessionBean.getRegion().getId();
		}
		if (tf.leagueType!=null && tf.region!=null) {
			mv.addObject("projects", tableSessionBean.findProjects(leagueType, region));
		}
		log.info("leagueType: " + tf.leagueType);
		log.info("region: " + tf.region);
		
		mv.addObject("ts", tableSessionBean);
		mv.addObject("tableForm", tf);
		return mv;
	}

	@RequestMapping("/tableContent/{searchProject}")
	public ModelAndView showTableContent(@PathVariable("searchProject") Long projectId) {
		log.info("showTableContent: " + projectId);
		tableSessionBean.calculateTable(projectId);
		ModelAndView mv = new ModelAndView("thymeleaf/tableContent :: tableContent");
		mv.addObject("ts", tableSessionBean);
		return mv;
	}

	/*
	 * @RequestMapping("/fragments/general") public ModelAndView getHome() { return
	 * new ModelAndView("thymeleaf/fragments/general"); }
	 */

	
	public class TableForm{
		public Long projectId;
		public Integer leagueType;
		public Integer region;
		
		public Long getProjectId() {
			return projectId;
		}
		public void setProjectId(Long projectId) {
			this.projectId = projectId;
		}
		public Integer getLeagueType() {
			return leagueType;
		}
		public void setLeagueType(Integer leagueType) {
			this.leagueType = leagueType;
		}
		public Integer getRegion() {
			return region;
		}
		public void setRegion(Integer region) {
			this.region = region;
		}
		
		
	}
}
