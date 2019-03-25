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
	PUCHAR_POLSKI(11, "Puchar Polski"),
	EKSTRALIGA_KOBIET(12, "Ekstraliga kobiet"),
	I_LIGA_KOBIET(13, "I liga kobiet"),
	II_LIGA_KOBIET(14, "II liga kobiet"),
	III_LIGA_KOBIET(15, "III liga kobiet"),
	IV_LIGA_KOBIET(16, "IV liga kobiet"),
	PUCHAR_POLSKI_KOBIET(17, "Puchar Polski kobiet"),
	EKSTRAKLASA_FURSALU(18, "Ekstraklasa futsalu"),
	I_LIGA_FUTSALU(19, "I liga futsalu"),
	PUCHAR_POLSKI_W_FUTSALU(20, "Puchar Polski w futsalu"),
	CLJ(21, "Centralna Liga Juniorów"),
	CLJ17(22, "Centralna Liga Juniorów U-17"),
	KLASA_OKR(23, "Klasa okręgowa"),
	LIGA_WOJ(24, "Liga wojewódzka"),
	MLS(25, "Mazowiecka Liga Seniorów"),
	LIGA_MOKR(26, "Liga międzyokręgowa"),
	LMJS(27, "Liga Makroregionalna Juniorów Starszych"),
	LMJM(28, "Liga Makroregionalna Juniorów Młodszych"),	
	LM(29, "Liga Mistrzów"),
	LE(30, "Liga Europy"),	
	PUCHAR_UEFA(31, "Puchar UEFA"),
	LIGA_NAR(32, "Liga Narodów"),
	PZP(33, "Puchar Zdobywców Pucharów");
	
	
	
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
		LeagueType result = UNDEFINED;
		if (leagueName != null) {
			for (LeagueType s : values()) {
				if (leagueName.contains(s.getName()))
					result = s;
			}
		}
		return result;
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
