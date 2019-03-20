package pl.polskieligi.model;

import java.util.HashMap;
import java.util.Map;

public enum Region {

	UNDEFINED(0, "Niezdefiniowana", "Niezdefiniowana"),
	DOL(1, "Dolnośląski ZPN", "dolnośląska", "Jelenia Góra", "Legnica", "Wałbrzych", "Wrocław", "Środa Śląska", "Trzebnica"),
	KUJ(2, "Kujawsko-Pomorski ZPN", "kujawsko-pomorska", "Bydgoszcz", "Toruń", "Włocławek"),
	LUE(3, "Lubelski ZPN", "lubelska", "Biała Podlaska", "Chełm", "Lublin", "Zamość"),
	LUU(4, "Lubuski ZPN", "lubuska", "Gorzów Wielkopolski", "Krosno Odrzańskie", "Nowa Sól", "Świebodzin", "Zielona Góra", "Żagań", "Żary"),
	LOD(5, "Łódzki ZPN", "łódzka", "Łódź", "Piotrków Trybunalski", "Sieradz", "Skierniewice"),
	MAL(6, "Małopolski ZPN", "małopolska", "Chrzanów", "Kraków", "Limanowa", "Myślenice", "Nowy Sącz", "Olkusz", "Oświęcim", "Podhale", "Tarnów", "Wadowice", "Wieliczka", "Gorlice"),
	MAZ(7, "Mazowiecki ZPN", "mazowiecka", "Ciechanów", "Ostrołęka", "Płock", "Radom", "Siedlce", "Warszawa"),
	OPO(8, "Opolski ZPN", "opolska", "Opole"),
	POK(9, "Podkarpacki ZPN", "podkarpacka" , "Dębica", "Jarosław", "Krosno", "Rzeszów", "Stalowa Wola", "Lubaczów", "Przemyśl", "Przeworsk"),
	POL(10, "Podlaski ZPN", "podlaska"),
	POM(11, "Pomorski ZPN", "pomorska", "Gdańsk", "Słupsk", "Malbork"),
	SLA(12, "Śląski ZPN", "śląska", "Bielsko-Biała", "Częstochowa", "Katowice", "Bytom", "Lubliniec", "Racibórz", "Rybnik", "Skoczów", "Sosnowiec", "Tychy", "Zabrze", "Żywiec"),
	SWI(13, "Świętokrzyski ZPN", "świętokrzyska", "Kielce", "Sandomierz"),
	WAR(14, "Warmińsko-Mazurski ZPN", "warmińsko-mazurska"),
	WIE(15, "Wielkopolski ZPN", "wielkopolska", "Kalisz", "Konin", "Leszno", "Piła", "Poznań"),
	ZAC(16, "Zachodniopomorski ZPN", "zachodniopomorska", "Koszalin", "Szczecin");

	private final Integer id;
	private final String name;
	private final String[] groups;
	
	private static Map<Integer, Region> idToNameMapping;

	private Region(Integer id, String name, String... groups) {
		this.id = id;
		this.name = name;
		this.groups = groups;
	}

	public static Region getRegionByProjectName(String projectName) {
		Region result = UNDEFINED;
		if (projectName != null) {
			for (Region s : values()) {
				for (String g : s.groups) {
					if (projectName.contains(g)) {
						result = s;
					}
				}
			}
		}
		return result;
	}
	
	public static Region getById(Integer id){
		if(idToNameMapping == null){
			initMapping();
		}
		return idToNameMapping.get(id);
	}
	
	private static void initMapping(){
		idToNameMapping = new HashMap<>();
		for(Region s : values()){
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