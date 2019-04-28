package pl.polskieligi.log.minut;

import java.text.MessageFormat;

public class MinutUrlHelper {
	private static final String MINUT_URL = "http://www.90minut.pl";
	private static final String MINUT_PLAYER_URL_PATTERN = MINUT_URL+ "/kariera.php?id={0}";
	private static final String MINUT_REFEREE_URL_PATTERN = MINUT_URL+ "/sedzia.php?id={0}";
	private static final String MINUT_TEAM_URL_PATTERN = MINUT_URL+ "/skarb.php?id_klub={0}";
	private static final String MINUT_PROJECT_URL_PATTERN = MINUT_URL+"/liga/{0}/liga{1}.html";
	private static final String MINUT_MATCH_URL_PATTERN = MINUT_URL+ "/mecz.php?id_mecz={0}";
	

	public static String getPlayerUrl(Integer minut_id) {
		return MessageFormat.format(MINUT_PLAYER_URL_PATTERN, minut_id.toString());
	}
	
	public static String getRefereeUrl(Integer minut_id) {
		return MessageFormat.format(MINUT_REFEREE_URL_PATTERN, minut_id.toString());
	}
	
	public static String getTeamUrl(Integer minut_id) {
		return MessageFormat.format(MINUT_TEAM_URL_PATTERN, minut_id.toString());
	}
	
	public static String getProjectUrl(Integer projectMinutId){
		String index = "0";
		if(projectMinutId>=10000){
			index = "1";
		}
		return MessageFormat.format(MINUT_PROJECT_URL_PATTERN, index.toString(), projectMinutId.toString());
	}
	
	public static String getMatchUrl(Integer minut_id) {
		return MessageFormat.format(MINUT_MATCH_URL_PATTERN, minut_id.toString());
	}
}
