package pl.polskieligi.log.af;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.polskieligi.model.LeagueMatch;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportAFProjectLogic {

	private final static String TEAM1_NAME = "match_hometeam_name";
	private final static String TEAM2_NAME = "match_awayteam_name";
	private final static String TEAM1_SCORE = "match_hometeam_score";
	private final static String TEAM2_SCORE = "match_awayteam_score";
	private final static String MATCH_DATE = "match_date";
	private final static String MATCH_ID = "match_id";

	private final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

	private final static Logger log = Logger.getLogger(ImportAFProjectLogic.class);

	public void processLeague(JSONArray jsonArray){
		Long projectId = Long.valueOf(0);

		Set<String> teamNames = getTeamNames(jsonArray);
		Map<String, Long> teamMap = updateTeams(teamNames);
		for(JSONObject obj: (List<JSONObject>)jsonArray){
			LeagueMatch lm = parseMatch(obj, projectId, teamMap);
			System.out.println(lm);
		}
	}

	private LeagueMatch parseMatch(JSONObject obj, Long projectId, Map<String, Long> teamMap) {
		LeagueMatch lm = new LeagueMatch();
		lm.setProject_id(projectId);
		try {
			String idString = (String)obj.get(MATCH_ID);
			lm.setAf_id(Long.valueOf(idString));

			String dateString = (String)obj.get(MATCH_DATE);
			Date date = dateParser.parse(dateString);
			long time = date.getTime();
			lm.setMatch_date(new Timestamp(time));

			String t1 = (String)obj.get(TEAM1_NAME);
			lm.setMatchpart1(teamMap.get(t1));

			String t2 = (String)obj.get(TEAM2_NAME);
			lm.setMatchpart2(teamMap.get(t2));

			String ts1 = (String)obj.get(TEAM1_SCORE);
			lm.setMatchpart1_result(Float.valueOf(ts1));

			String ts2 = (String)obj.get(TEAM2_SCORE);
			lm.setMatchpart2_result(Float.valueOf(ts2));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			lm = null;
		}

		return lm;
	}

	private Set<String> getTeamNames(JSONArray jsonArray){
		Set<String> result = new HashSet<String>();
		for(JSONObject obj: (List<JSONObject>)jsonArray){
			result.add((String)obj.get(TEAM1_NAME));
			result.add((String)obj.get(TEAM2_NAME));
		}
		return result;
	}

	private Map<String, Long> updateTeams(Set<String> teamNames){
		Map<String, Long> result = new HashMap<String, Long>();
		for(String teamName: teamNames){
			Long teamId = Long.valueOf(0);
			result.put(teamName, teamId);
			System.out.println(teamName);
		}
		return result;
	}
}
