package pl.polskieligi.model;

import java.util.HashMap;
import java.util.Map;

public enum LeagueType {

	UNDEFINED(0, null, "Niezdefiniowana"),
	EKSTRAKLASA(1, LeagueClass.SENIOR, "Ekstraklasa"),
	I_LIGA(2, LeagueClass.SENIOR, "I liga"),
	II_LIGA(3, LeagueClass.SENIOR, "II liga"),
	III_LIGA(4, LeagueClass.SENIOR, "III liga"),	
	V_LIGA(6, LeagueClass.SENIOR, "V liga", "PIĄTA LIGA"),
	IV_LIGA(5, LeagueClass.SENIOR, "IV liga"),
	LIGA_OKR(7, LeagueClass.SENIOR, "Liga okręgowa", "Klasa okręgowa", "KLASA OKREGOWA", " KL. O ", " KL O ", "Klasa Okr", "Okręgowa "),
	KLASA_A(8, LeagueClass.SENIOR, "Klasa A", "KL A"),
	KLASA_B(9, LeagueClass.SENIOR, "Klasa B", "KL B"),
	KLASA_C(10, LeagueClass.SENIOR, "Klasa C", "KL C"),
	PUCHAR_POLSKI(11, LeagueClass.SENIOR, "Puchar Polski"),
	EKSTRALIGA_KOBIET(12, LeagueClass.WOMAN, "Ekstraliga kobiet"),
	I_LIGA_KOBIET(13, LeagueClass.WOMAN, "I liga kobiet"),
	II_LIGA_KOBIET(14, LeagueClass.WOMAN, "II liga kobiet"),
	III_LIGA_KOBIET(15, LeagueClass.WOMAN, "III liga kobiet"),
	IV_LIGA_KOBIET(16, LeagueClass.WOMAN, "IV liga kobiet"),
	PUCHAR_POLSKI_KOBIET(17, LeagueClass.WOMAN, "Puchar Polski kobiet"),
	EKSTRAKLASA_FURSALU(18, LeagueClass.FUTSAL,"Ekstraklasa futsalu"),
	I_LIGA_FUTSALU(19, LeagueClass.FUTSAL, "I liga futsalu"),
	PUCHAR_POLSKI_W_FUTSALU(20, LeagueClass.FUTSAL, "Puchar Polski w futsalu"),
	CLJ(21, LeagueClass.JUNIOR, "Centralna Liga Juniorów"),
	CLJ17(22, LeagueClass.JUNIOR, "Centralna Liga Juniorów U-17"),
	//KLASA_OKR(23, "Klasa okręgowa"),
	LIGA_WOJ(24, LeagueClass.SENIOR, "Liga wojewódzka"),
	MLS(25, LeagueClass.SENIOR, "Mazowiecka Liga Seniorów"),
	LIGA_MOKR(26, LeagueClass.SENIOR, "Liga międzyokręgowa"),
	LMJS(27, LeagueClass.JUNIOR, "Liga Makroregionalna Juniorów Starszych"),
	LMJM(28, LeagueClass.JUNIOR, "Liga Makroregionalna Juniorów Młodszych"),	
	LM(29, LeagueClass.SENIOR, "Liga Mistrzów"),
	LE(30, LeagueClass.SENIOR, "Liga Europy"),	
	PUCHAR_UEFA(31, LeagueClass.SENIOR, "Puchar UEFA"),
	LIGA_NAR(32, LeagueClass.SENIOR, "Liga Narodów"),
	PZP(33, LeagueClass.SENIOR, "Puchar Zdobywców Pucharów"),
	SUPERPUCHAR(34, LeagueClass.SENIOR, "Superpuchar");
	
	private final Integer id;
	private final String[] names;
	private final LeagueClass leagueClass;

	private static Map<Integer, LeagueType> idToNameMapping;

	private LeagueType(Integer id, LeagueClass leagueClass, String ...names) {
		this.id = id;
		this.names = names;
		this.leagueClass = leagueClass;
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
			String uLeagueName = leagueName.toUpperCase();
			for (LeagueType s : values()) {
				for (String name : s.getNames()) {
					if (uLeagueName.contains(name.toUpperCase())) {
						result = s;
					}
				}
			}
			if(result==LeagueType.UNDEFINED && uLeagueName.startsWith("LO ")) {
				result = LIGA_OKR;
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
		return names[0];
	}
	
	private String[] getNames() {
		return names;
	}
	
	public String removeFromProjectName(String projectName) {
		String uProjectName = projectName.toUpperCase();
		for (String name : getNames()) {
			if (uProjectName.contains(name.toUpperCase())) {
				return uProjectName.replace(name.toUpperCase(), ""); 
			}
		}
		return uProjectName;
	}

	@Override
	public String toString() {
		return getNames()[0];
	}
	
}
