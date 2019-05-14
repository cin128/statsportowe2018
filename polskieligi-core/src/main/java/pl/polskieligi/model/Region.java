package pl.polskieligi.model;

import java.util.HashMap;
import java.util.Map;

public enum Region {

	UNDEFINED(0, 0, "", "Niezdefiniowana", "Niezdefiniowana"),
	LUE(3, 7, "pl3", "Lubelski ZPN", "Lubelski", "lubelska", "Biała Podlaska", "Chełm", "Lublin", "Zamość"),//przed SLA, bo Lubliniec
	SLA(12, 18, "pl12", "Śląski ZPN", "Śląski", "śląska", "Bielsko-Biała", "Częstochowa", "Katowice", "Bytom", "Lubliniec", "Racibórz", "Rybnik", "Skoczów", "Sosnowiec", "Tychy", "Zabrze", "Żywiec"),//przed DOL
	DOL(1, 16, "pl1", "Dolnośląski ZPN", "Dolnośląski", "dolnośląska", "Jelenia Góra", "Legnica", "Wałbrzych", "Wrocław", "Środa Śląska", "Trzebnica", "Bolesławiec", "Kłodzko", "Lubań", "Oława", "Strzelin", "Zgorzelec", "Świdnica"),
	POM(11, 10, "pl11", "Pomorski ZPN", "Pomorski", "pomorska", "Gdańsk", "Słupsk", "Malbork", "Puck"),//przed ZAC, KUJ
	KUJ(2, 3, "pl2", "Kujawsko-Pomorski ZPN", "Kujawsko-Pomorski", "kujawsko-pomorska", "Bydgoszcz", "Toruń", "Włocławek"),
	LOD(5, 15, "pl5", "Łódzki ZPN", "Łódzki", "łódzka", "Łódź", "Piotrków Trybunalski", "Sieradz", "Skierniewice", "Brzeg"),	
	OPO(8, 12, "pl8", "Opolski ZPN", "Opolski", "opolska", "Opole", "Głubczyce", "Kluczbork", "Krapkowice", "Kędzierzyn-Koźle", "Nysa", "Olesno", "Prudnik"),//przed MAL, WIE
	MAL(6, 13, "pl6", "Małopolski ZPN", "Małopolski", "małopolska", "Chrzanów", "Kraków", "Limanowa", "Myślenice", "Nowy Sącz", "Olkusz", "Oświęcim", "Podhale", "Tarnów", "Wadowice", "Wieliczka", "Gorlice", "Bochnia", "Brzesko", "Miechów", "Nowy Targ", "Żabno"),
	MAZ(7, 1, "pl7", "Mazowiecki ZPN", "Mazowiecki", "mazowiecka", "Ciechanów", "Ostrołęka", "Płock", "Radom", "Siedlce", "Warszawa"),
	POK(9, 14, "pl9", "Podkarpacki ZPN", "Podkarpacki", "podkarpacka" , "Dębica", "Jarosław", "Krosno", "Rzeszów", "Stalowa Wola", "Lubaczów", "Przemyśl", "Przeworsk"),
	POL(10, 11, "pl10", "Podlaski ZPN", "Podlaski", "podlaska"),	
	SWI(13, 17, "pl13", "Świętokrzyski ZPN", "Świętokrzyski", "świętokrzyska", "Kielce", "Sandomierz"),
	WAR(14, 8, "pl14", "Warmińsko-Mazurski ZPN", "Warmińsko-Mazurski", "warmińsko-mazurska"),
	WIE(15, 2, "pl15", "Wielkopolski ZPN", "Wielkopolski", "wielkopolska", "Kalisz", "Konin", "Leszno", "Piła", "Poznań", "Kościan"),//przed LUU, bo Gorzów Wielkopolski
	LUU(4, 6, "pl4", "Lubuski ZPN", "Lubuski", "lubuska", "Gorzów Wielkopolski", "Krosno Odrzańskie", "Nowa Sól", "Świebodzin", "Zielona Góra", "Żagań", "Żary", "Drezdenko", "Słubice"),
	ZAC(16, 9, "pl16", "Zachodniopomorski ZPN", "Zachodniopomorski", "zachodniopomorska", "Koszalin", "Szczecin", "Białogard", "Darłowo", "Kamienna Góra", "Sławno", "Wałcz");

	private final Integer id;
	private final String name;
	private final String[] groups;
	private final Integer lnpId;
	private final String htmlClass;
	
	private static Map<Integer, Region> idToNameMapping;

	private Region(Integer id, Integer lnpId, String htmlClass, String name, String... groups) {
		this.id = id;
		this.lnpId = lnpId;
		this.htmlClass = htmlClass;
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
	
	public Integer getLnpId() {
		return lnpId;
	}

	public String getName() {
		return name;
	}
	
	public String getHtmlClass() {
		return htmlClass;
	}

	@Override
	public String toString() {
		return getName();
	}

}
