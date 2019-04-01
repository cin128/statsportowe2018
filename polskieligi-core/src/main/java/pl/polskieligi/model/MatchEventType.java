package pl.polskieligi.model;

import java.util.HashMap;
import java.util.Map;

public enum MatchEventType {

	SCORE(1, "Gol"),
	SCORE_PANELTY(2, "Rzut karny"),
	PANELTY_MISSED(3, "Niestrzelony rzut karny"),
	SCORE_OWN(4, "Samobój"),
	YELLOW_CARD(5, "Żółta kartka"),
	RED_CARD(6, "Czerwona kartka");

	private final Integer id;
	private final String name;

	private static Map<Integer, MatchEventType> idToNameMapping;

	private MatchEventType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MatchEventType getById(Integer id){
		if(idToNameMapping == null){
			initMapping();
		}
		return idToNameMapping.get(id);
	}

	private static void initMapping(){
		idToNameMapping = new HashMap<>();
		for(MatchEventType s : values()){
			idToNameMapping.put(s.id, s);
		}
	}

	public Integer getId() {
		return id;
	}
}
