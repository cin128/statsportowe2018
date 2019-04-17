package pl.polskieligi.model;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_CO_NAME", columnList = "name") })
public class Config {

	public static final String MINUT_PROJECT_MAX = "MINUT_PROJECT_MAX";
	public static final String MINUT_PLAYER_MAX = "MINUT_PLAYER_MAX";
	public static final String MINUT_REFEREE_MAX = "MINUT_REFEREE_MAX";
	public static final String MINUT_TEAM_MAX = "MINUT_TEAM_MAX";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
