package pl.polskieligi.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dto.Scorer;
import pl.polskieligi.model.League;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Season;
import pl.polskieligi.web.TableSessionBean;

import java.util.List;

@Controller
public class TableController {

	final static Logger log = Logger.getLogger(TableController.class);

	@Autowired
	private TableSessionBean tableSessionBean;

	@Autowired
	private ProjectDAO projectDAO;

	@RequestMapping("/table")
	public ModelAndView showTable(@RequestParam(value = "season", required = false) Long season,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "leagueType", required = false) Integer leagueType,
			@RequestParam(value = "region", required = false) Integer region) {
		log.info("showTable: season: "+season+", leagueType: "+leagueType+", region: "+region+", projectId: " + projectId);
		TableForm tf = new TableForm();
		tf.projectId=projectId;
		tf.leagueType=leagueType;
		tf.region=region;
		tf.season=season;
		ModelAndView mv = new ModelAndView("thymeleaf/table");
		if (projectId != null) {
			tableSessionBean.calculateTable(projectId);
			Project p = tableSessionBean.getProject();
			if(p!=null) {
				League l = p.getLeague();
				if(l!=null) {
					tf.leagueType = l.getLeagueType().getId();
					tf.region = l.getRegion().getId();
				}
				Season s = p.getSeason();
				if(s!=null) {
					tf.season = s.getId();
				}
				List<Scorer> scorers = projectDAO.retrieveScorers(p.getId());
				mv.addObject("scorers", scorers);
			} else {
				log.warn("Project not found: "+projectId);
			}
		}
		if (tf.season!=null && tf.leagueType!=null && tf.region!=null) {
			mv.addObject("projects", tableSessionBean.findProjects(tf.season, tf.leagueType, tf.region));
		}
		
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
		public Long season;
		
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

		public Long getSeason() {
			return season;
		}

		public void setSeason(Long season) {
			this.season = season;
		}
	}
}
