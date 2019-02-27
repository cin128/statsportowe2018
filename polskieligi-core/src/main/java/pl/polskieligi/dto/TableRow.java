package pl.polskieligi.dto;

public class TableRow {
	private Integer sequence;
	private Long team_id;
	private String teamName;
	private Integer games;
	private Integer points;
	private Integer wins;
	private Integer draws;
	private Integer defeats;
	private Integer goalsScored;
	private Integer goalsAgainst;
	private Integer goalsScoredHome;
	private Integer goalsAgainstHome;
	private Integer goalsScoredAway;
	private Integer goalsAgainstAway;
	private TableRowMatch[] lastMatches;

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
	public Long getTeam_id() {
		return team_id;
	}

	public void setTeam_id(Long team_id) {
		this.team_id = team_id;
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Integer getGames() {
		return games;
	}

	public void setGames(Integer games) {
		this.games = games;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getDraws() {
		return draws;
	}

	public void setDraws(Integer draws) {
		this.draws = draws;
	}

	public Integer getDefeats() {
		return defeats;
	}

	public void setDefeats(Integer defeats) {
		this.defeats = defeats;
	}

	public Integer getGoalsScored() {
		return goalsScored;
	}

	public void setGoalsScored(Integer goalsScored) {
		this.goalsScored = goalsScored;
	}

	public Integer getGoalsAgainst() {
		return goalsAgainst;
	}

	public void setGoalsAgainst(Integer goalsAgainst) {
		this.goalsAgainst = goalsAgainst;
	}
	
	public Integer getGoalsScoredHome() {
		return goalsScoredHome;
	}

	public void setGoalsScoredHome(Integer goalsScoredHome) {
		this.goalsScoredHome = goalsScoredHome;
	}

	public Integer getGoalsAgainstHome() {
		return goalsAgainstHome;
	}

	public void setGoalsAgainstHome(Integer goalsAgainstHome) {
		this.goalsAgainstHome = goalsAgainstHome;
	}

	public Integer getGoalsScoredAway() {
		return goalsScoredAway;
	}

	public void setGoalsScoredAway(Integer goalsScoredAway) {
		this.goalsScoredAway = goalsScoredAway;
	}

	public Integer getGoalsAgainstAway() {
		return goalsAgainstAway;
	}

	public void setGoalsAgainstAway(Integer goalsAgainstAway) {
		this.goalsAgainstAway = goalsAgainstAway;
	}
	
	public TableRowMatch[] getLastMatches() {
		return lastMatches;
	}

	public void setLastMatches(TableRowMatch[] lastMatches) {
		this.lastMatches = lastMatches;
	}	
}
