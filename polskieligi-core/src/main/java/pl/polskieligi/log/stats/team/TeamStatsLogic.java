package pl.polskieligi.log.stats.team;

import pl.polskieligi.model.LeagueMatch;

import java.util.List;

public class TeamStatsLogic {
	public static TeamStatsDTO calculateTeamStats(List<LeagueMatch> leagueMatches, Long teamId) {
		TeamStatsDTO ts = new TeamStatsDTO();
		for(LeagueMatch lm: leagueMatches) {
			if(lm.getMatchpart1_id().equals(teamId)) {
				ts.gamesHome++;
				ts.goalsHome +=Math.round(lm.getMatchpart1_result());
				ts.goalsAgainstHome +=Math.round(lm.getMatchpart2_result());
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
				ts.goalsAgainstAway +=Math.round(lm.getMatchpart1_result());
				if(lm.getMatchpart1_result().equals(lm.getMatchpart2_result())) {
					ts.drawsAway++;
				} else if(lm.getMatchpart1_result()<lm.getMatchpart2_result()) {
					ts.winsAway++;
				} else {
					ts.lossesAway++;
				}
				if(lm.getMatchpart2_result()==0) {
					ts.failedToScoreAway++;
				}
				if(lm.getMatchpart1_result()==0) {
					ts.cleanSheetsAway++;
				}
			}
		}
		ts.games=ts.gamesHome+ts.gamesAway;
		ts.wins = ts.winsHome+ts.winsAway;
		ts.draws = ts.drawsHome+ts.drawsAway;
		ts.losses = ts.lossesHome+ts.lossesAway;
		ts.goals = ts.goalsHome+ts.goalsAway;
		ts.goalsAgainst = ts.goalsAgainstHome+ts.goalsAgainstAway;
		ts.failedToScore = ts.failedToScoreHome+ts.failedToScoreAway;
		ts.cleanSheets = ts.cleanSheetsHome+ts.cleanSheetsAway;

		return ts;
	}
}
