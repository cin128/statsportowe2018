package pl.polskieligi.model;

public enum MatchEventType {

	SCORE(1, "Gol"),
	SCORE_PANELTY(2, "Rzut karny"),
	PANELTY_MISSED(3, "Niestrzelony rzut karny"),
	SCORE_OWN(4, "Samobój"),
	YELLOW_CARD(5, "Żółta kartka"),
	RED_CARD(6, "Czerwona kartka");

	private final Integer id;
	private final String name;

	private MatchEventType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}
}
