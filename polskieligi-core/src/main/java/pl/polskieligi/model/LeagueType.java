package pl.polskieligi.model;

import java.util.HashMap;
import java.util.Map;

public enum LeagueType {

	UNDEFINED(0, "Niezdefiniowana"),
	EKSTRAKLASA(1, "Ekstraklasa"),
	I_LIGA(2, "I liga"),
	II_LIGA(3, "II liga"),
	III_LIGA(4, "III liga"),
	IV_LIGA(5, "IV liga"),
	V_LIGA(6, "V liga"),
	LIGA_OKR(7, "Liga okręgowa"),
	KLASA_A(8, "Klasa A"),
	KLASA_B(9, "Klasa B"),
	KLASA_C(10, "Klasa C"),
	PUCHAR_POLSKI(11, "Puchar Polski");

	private final Integer id;
	private final String name;

	private static Map<Integer, LeagueType> idToNameMapping;

	private LeagueType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public static LeagueType getById(Integer id){
		if(idToNameMapping == null){
			initMapping();
		}
		return idToNameMapping.get(id);
	}

	public static LeagueType getByLeagueName(String leagueName) {
		if (leagueName != null)
			for (LeagueType s : values()) {
				if (leagueName.contains(s.getName()))
					return s;
			}
		return UNDEFINED;
	}

	private static void initMapping(){
		idToNameMapping = new HashMap<>();
		for(LeagueType s : values()){
			idToNameMapping.put(s.id, s);
		}
	}

	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
