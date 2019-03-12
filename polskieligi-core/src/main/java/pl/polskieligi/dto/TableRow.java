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
	private Integer gamesHome;
	private Integer pointsHome;
	private Integer winsHome;
	private Integer drawsHome;
	private Integer defeatsHome;
	private Integer goalsScoredHome;
	private Integer goalsAgainstHome;
	private Integer gamesAway;
	private Integer pointsAway;
	private Integer winsAway;
	private Integer drawsAway;
	private Integer defeatsAway;
	private Integer goalsScoredAway;
	private Integer goalsAgainstAway;
	private TableRowMatch[] lastMatches;
	private TableRowMatch[] lastMatchesHome;
	private TableRowMatch[] lastMatchesAway;

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

	public Integer getGamesHome() {
		return gamesHome;
	}

	public void setGamesHome(Integer gamesHome) {
		this.gamesHome = gamesHome;
	}

	public Integer getPointsHome() {
		return pointsHome;
	}

	public void setPointsHome(Integer pointsHome) {
		this.pointsHome = pointsHome;
	}

	public Integer getWinsHome() {
		return winsHome;
	}

	public void setWinsHome(Integer winsHome) {
		this.winsHome = winsHome;
	}

	public Integer getDrawsHome() {
		return drawsHome;
	}

	public void setDrawsHome(Integer drawsHome) {
		this.drawsHome = drawsHome;
	}

	public Integer getDefeatsHome() {
		return defeatsHome;
	}

	public void setDefeatsHome(Integer defeatsHome) {
		this.defeatsHome = defeatsHome;
	}

	public Integer getGamesAway() {
		return gamesAway;
	}

	public void setGamesAway(Integer gamesAway) {
		this.gamesAway = gamesAway;
	}

	public Integer getPointsAway() {
		return pointsAway;
	}

	public void setPointsAway(Integer pointsAway) {
		this.pointsAway = pointsAway;
	}

	public Integer getWinsAway() {
		return winsAway;
	}

	public void setWinsAway(Integer winsAway) {
		this.winsAway = winsAway;
	}

	public Integer getDrawsAway() {
		return drawsAway;
	}

	public void setDrawsAway(Integer drawsAway) {
		this.drawsAway = drawsAway;
	}

	public Integer getDefeatsAway() {
		return defeatsAway;
	}

	public void setDefeatsAway(Integer defeatsAway) {
		this.defeatsAway = defeatsAway;
	}

	public TableRowMatch[] getLastMatchesHome() {
		return lastMatchesHome;
	}

	public void setLastMatchesHome(TableRowMatch[] lastMatchesHome) {
		this.lastMatchesHome = lastMatchesHome;
	}

	public TableRowMatch[] getLastMatchesAway() {
		return lastMatchesAway;
	}

	public void setLastMatchesAway(TableRowMatch[] lastMatchesAway) {
		this.lastMatchesAway = lastMatchesAway;
	}
}
