package pl.polskieligi.model;

import java.util.HashMap;
import java.util.Map;

public enum Region {

	UNDEFINED(0, "Niezdefiniowana", "Niezdefiniowana"),
	DOL(1, "Dolnośląski ZPN", "Dolnośląski", "dolnośląska", "Jelenia Góra", "Legnica", "Wałbrzych", "Wrocław", "Środa Śląska", "Trzebnica"),
	KUJ(2, "Kujawsko-Pomorski ZPN", "Kujawsko-Pomorski", "kujawsko-pomorska", "Bydgoszcz", "Toruń", "Włocławek"),
	LUE(3, "Lubelski ZPN", "Lubelski", "lubelska", "Biała Podlaska", "Chełm", "Lublin", "Zamość"),
	LUU(4, "Lubuski ZPN", "Lubuski", "lubuska", "Gorzów Wielkopolski", "Krosno Odrzańskie", "Nowa Sól", "Świebodzin", "Zielona Góra", "Żagań", "Żary"),
	LOD(5, "Łódzki ZPN", "Łódzki", "łódzka", "Łódź", "Piotrków Trybunalski", "Sieradz", "Skierniewice"),
	MAL(6, "Małopolski ZPN", "Małopolski", "małopolska", "Chrzanów", "Kraków", "Limanowa", "Myślenice", "Nowy Sącz", "Olkusz", "Oświęcim", "Podhale", "Tarnów", "Wadowice", "Wieliczka", "Gorlice"),
	MAZ(7, "Mazowiecki ZPN", "Mazowiecki", "mazowiecka", "Ciechanów", "Ostrołęka", "Płock", "Radom", "Siedlce", "Warszawa"),
	OPO(8, "Opolski ZPN", "Opolski", "opolska", "Opole"),
	POK(9, "Podkarpacki ZPN", "Podkarpacki", "podkarpacka" , "Dębica", "Jarosław", "Krosno", "Rzeszów", "Stalowa Wola", "Lubaczów", "Przemyśl", "Przeworsk"),
	POL(10, "Podlaski ZPN", "Podlaski", "podlaska"),
	POM(11, "Pomorski ZPN", "Pomorski", "pomorska", "Gdańsk", "Słupsk", "Malbork"),
	SLA(12, "Śląski ZPN", "Śląski", "śląska", "Bielsko-Biała", "Częstochowa", "Katowice", "Bytom", "Lubliniec", "Racibórz", "Rybnik", "Skoczów", "Sosnowiec", "Tychy", "Zabrze", "Żywiec"),
	SWI(13, "Świętokrzyski ZPN", "Świętokrzyski", "świętokrzyska", "Kielce", "Sandomierz"),
	WAR(14, "Warmińsko-Mazurski ZPN", "Warmińsko-Mazurski", "warmińsko-mazurska"),
	WIE(15, "Wielkopolski ZPN", "Wielkopolski", "wielkopolska", "Kalisz", "Konin", "Leszno", "Piła", "Poznań"),
	ZAC(16, "Zachodniopomorski ZPN", "Zachodniopomorski", "zachodniopomorska", "Koszalin", "Szczecin");

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