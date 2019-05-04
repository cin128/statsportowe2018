package pl.polskieligi.log.lnp;

import java.text.MessageFormat;

public class LnpUrlHelper {
	private static final String LNP_URL = "https://www.laczynaspilka.pl";
	private static final String LNP_LOWER_URL_PATTERN = LNP_URL+"/league/get_lower?&zpn_id[]={0}&mode=league&season={1}";
	private static final String LNP_THIRD_URL_PATTERN = LNP_URL+"/league/get_third?&zpn_id[]={0}&mode=league&season={1}";
	private static final String LNP_CLUB_URL_PATTERN = LNP_URL+"/klub/x,{0}.html";	
	private static final String LNP_PROJECT_PATTERN = LNP_URL+"/rozgrywki/{0},{1}.html?round=0";
	private static final String LNP_PROJECT_TEAMS_PATTERN = LNP_URL+"/druzyny/{0},{1}.html";
	private static final String LNP_MATCH_PATTERN = LNP_URL+"/rozgrywki/mecz/{0},{1}.html";
	private static final String LNP_TEAM_LEAGUE_PATTERN = LNP_URL+"/druzyna/{0},{1}.html";
	private static final String LNP_PLAYER_PATTERN = LNP_URL+"/zawodnik/{0},{1}.html";
	
	
	
	public static String getThirdUrl(Integer zpn_id, String seasonName) {
		return MessageFormat.format(LNP_THIRD_URL_PATTERN, zpn_id.toString(), seasonName);
	}

	public static String getLowerUrl(Integer zpn_id, String seasonName) {
		return MessageFormat.format(LNP_LOWER_URL_PATTERN, zpn_id.toString(), seasonName);
	}
	
	public static String getClubUrl(Integer club_id) {
		return MessageFormat.format(LNP_CLUB_URL_PATTERN, club_id.toString());
	}
	
	public static String getProjectUrl(String lnpIdName, Integer lnpId) {
		return MessageFormat.format(LNP_PROJECT_PATTERN, lnpIdName, lnpId.toString());
	}
	
	public static String getProjectTeamsUrl(String lnpIdName, Integer lnpId) {
		return MessageFormat.format(LNP_PROJECT_TEAMS_PATTERN, lnpIdName, lnpId.toString());
	}
	
	public static String getMatchUrl(String lnpIdName, Integer lnpId) {
		return MessageFormat.format(LNP_MATCH_PATTERN, lnpIdName, lnpId.toString());
	}

	public static String getTeamLeagueUrl(String lnpIdName, Integer lnpId) {
		return MessageFormat.format(LNP_TEAM_LEAGUE_PATTERN, lnpIdName, lnpId.toString());
	}
	
	public static String getPlayerUrl(String lnpIdName, Integer lnpId) {
		return MessageFormat.format(LNP_PLAYER_PATTERN, lnpIdName, lnpId.toString());
	}
}
