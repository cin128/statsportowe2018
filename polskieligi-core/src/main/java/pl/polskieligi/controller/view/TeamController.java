package pl.polskieligi.controller.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.Team;

@Controller
public class TeamController {

	final static Logger log = Logger.getLogger(TeamController.class);

	@Autowired TeamDAO teamDAO;
	
	@Autowired PlayerDAO playerDAO;
	
	@Autowired LeagueMatchDAO matchDAO;

	@RequestMapping("/team")
	public ModelAndView showTeam(@RequestParam(value = "seasonId", required = false) Long seasonId,
			@RequestParam(value = "teamId", required = false) Long teamId) {
		log.info("showTeam: seasonId: " + seasonId + ", teamId: " + teamId);
		ModelAndView mv = new ModelAndView("thymeleaf/team");
		if(teamId!=null) {
			Team team = teamDAO.find(teamId);
			if(team!=null) {
				mv.addObject("team",team);			
				if(seasonId!=null) {
					mv.addObject("seasonId",seasonId);
					List<LeagueMatch> leagueMatches = matchDAO.getMatchesByTeamSeason(teamId, seasonId);
					mv.addObject("leagueMatches", leagueMatches);
					mv.addObject("teamStats", calculateTeamStats(leagueMatches, teamId));
					
					List<Player> players = playerDAO.findBySeasonAndTeam(seasonId, teamId);
					mv.addObject("players",players);	
				}
			}
		}
		return mv;
	}
	
	private TeamStats calculateTeamStats(List<LeagueMatch> leagueMatches, Long teamId) {
		TeamStats ts = new TeamStats();
		for(LeagueMatch lm: leagueMatches) {
			if(lm.getMatchpart1_id().equals(teamId)) {
				ts.gamesHome++;
				ts.goalsHome +=Math.round(lm.getMatchpart1_result());
				if(lm.getMatchpart1_result().equals(lm.getMatchpart2_result())) {
					ts.drawsHome++;
				} else if(lm.getMatchpart1_result()>lm.getMatchpart2_result()) {
					ts.winsHome++;
				} else {
					ts.lossesHome++;			
				}
				if(lm.getMatchpart1_result()==0) {
					ts.failedToScoreHome++;
				}
				if(lm.getMatchpart2_result()==0) {
					ts.cleanSheetsHome++;
				}
				
			} else {
				ts.gamesAway++;
				ts.goalsAway +=Math.round(lm.getMatchpart2_result());
				if(lm.getMatchpart1_result().equals(lm.getMatchpart2_result())) {
					ts.drawsAway++;
				} else if(lm.getMatchpart1_result()<lm.getMatchpart2_result()) {
					ts.winsAway++;
				} else {
					ts.lossesHome++;			
				}
				if(lm.getMatchpart2_result()==0) {
					ts.failedToScoreAway++;
				}
				if(lm.getMatchpart2_result()==0) {
					ts.cleanSheetsAway++;
				}
			}
		}
		ts.games=ts.gamesHome+ts.gamesAway;
		ts.wins = ts.winsHome+ts.winsAway;
		ts.draws = ts.drawsHome+ts.drawsAway;
		ts.losses = ts.lossesHome+ts.lossesAway;
		ts.goals = ts.goalsHome+ts.goalsAway;
		ts.failedToScore = ts.failedToScoreHome+ts.failedToScoreAway;
		ts.cleanSheets = ts.cleanSheetsHome+ts.cleanSheetsAway;
		
		return ts;
	}
	
	private class TeamStats{
		Integer games = 0;
		Integer gamesHome = 0;
		Integer gamesAway = 0;
		Integer goals = 0;
		Integer goalsHome = 0;
		Integer goalsAway = 0;
		Integer wins = 0;
		Integer winsHome = 0;
		Integer winsAway = 0;
		Integer draws = 0;
		Integer drawsHome = 0;
		Integer drawsAway = 0;
		Integer losses = 0;
		Integer lossesHome = 0;
		Integer lossesAway = 0;
		Integer cleanSheets = 0;
		Integer cleanSheetsHome = 0;
		Integer cleanSheetsAway = 0;
		Integer failedToScore = 0;
		Integer failedToScoreHome = 0;
		Integer failedToScoreAway = 0;
		public Integer getGames() {
			return games;
		}
		public Integer getGamesHome() {
			return gamesHome;
		}
		public Integer getGamesAway() {
			return gamesAway;
		}
		public Integer getGoals() {
			return goals;
		}
		public Integer getGoalsHome() {
			return goalsHome;
		}
		public Integer getGoalsAway() {
			return goalsAway;
		}
		public Integer getWins() {
			return wins;
		}
		public Integer getWinsHome() {
			return winsHome;
		}
		public Integer getWinsAway() {
			return winsAway;
		}
		public Integer getDraws() {
			return draws;
		}
		public Integer getDrawsHome() {
			return drawsHome;
		}
		public Integer getDrawsAway() {
			return drawsAway;
		}
		public Integer getLosses() {
			return losses;
		}
		public Integer getLossesHome() {
			return lossesHome;
		}
		public Integer getLossesAway() {
			return lossesAway;
		}
		public Integer getCleanSheets() {
			return cleanSheets;
		}
		public Integer getCleanSheetsHome() {
			return cleanSheetsHome;
		}
		public Integer getCleanSheetsAway() {
			return cleanSheetsAway;
		}
		public Integer getFailedToScore() {
			return failedToScore;
		}
		public Integer getFailedToScoreHome() {
			return failedToScoreHome;
		}
		public Integer getFailedToScoreAway() {
			return failedToScoreAway;
		}
		
	}
}
