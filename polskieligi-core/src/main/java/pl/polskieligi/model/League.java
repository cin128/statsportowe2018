package pl.polskieligi.model;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_LE_NAME", columnList = "name", unique = false) })
public class League {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer leagueType;
	private Integer region;
	private String groupName;

	public League() {
		name = "";
	}

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

	public LeagueType getLeagueType() {
		return LeagueType.getById(leagueType);
	}

	public void setLeagueType(Integer leagueType) {
		this.leagueType = leagueType;
	}

	public Region getRegion() {
		return Region.getById(region);
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString(){
		return "id="+id+", name="+name+", type="+getLeagueType();
	}
}
