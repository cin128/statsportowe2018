package pl.polskieligi.log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class ApifootballTest {

	@Test public void test() {
		try {
			URL url = new URL("https://apifootball.com/api/?action=get_events&from=2016-10-30&to=2016-11-01&league_id=100&APIkey=12046c32d5f185c3d578edd891c5658763b50eca2be7d027f6c3346fafa8f4fd");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(url.openStream()));
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonArray = (JSONArray) jsonParser.parse(in);
			for(JSONObject obj: (List<JSONObject>)jsonArray){
				System.out.println(obj.toString());
				System.out.println(obj.get("match_hometeam_name")+" "+obj.get("match_awayteam_name"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
