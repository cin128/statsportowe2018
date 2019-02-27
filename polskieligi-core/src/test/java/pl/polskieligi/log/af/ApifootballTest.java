package pl.polskieligi.log.af;

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
			URL url = new URL("https://apifootball.com/api/?action=get_events&from=2018-07-01&to=2018-12-20&league_id=453&APIkey=12046c32d5f185c3d578edd891c5658763b50eca2be7d027f6c3346fafa8f4fd");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(url.openStream()));

			JSONParser jsonParser = new JSONParser();
			JSONArray jsonArray = (JSONArray) jsonParser.parse(in);

			ImportAFProjectLogic logic = new ImportAFProjectLogic();
			logic.processLeague(jsonArray);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
