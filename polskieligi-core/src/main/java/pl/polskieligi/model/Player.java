package pl.polskieligi.model;

import pl.polskieligi.log.DisplayUtils;

import javax.persistence.*;
import java.util.List;

@Entity @Table(indexes = {@Index(name = "IDX_PL_MINUT_ID", columnList = "minut_id", unique = false)})
public class Player extends Person {

	private String pseudo;
	private String position;
	private Float weight;
	private Float height;
	
	private Integer lnp_id;
	private String lnpIdName;
	
	@OneToMany
	@JoinColumn(name = "player_id")
	private List<TeamLeaguePlayer> teamleaguePlayers; 

	public Player() {
		pseudo = "";
		position = "";
		weight = new Float(0);
		height = new Float(0);
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public String getFullName() {
		return DisplayUtils.getPseudo(getName())+' '+DisplayUtils.getPseudo(getSurname());
	}


	@Override public String toString() {
		return getId() + " " + getMinut_id() + " " +getName() + " " +getSurname() + " " +getBirthDate() + " " +getBirthTown() + " " +getBirthCountry()+ " "
				+ getDeathDate()+ " " + getDeatTown()+ " " + getDeatCountry()+ " " + getNationality() + " " +position + " " + weight + " " + height;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public Integer getLnp_id() {
		return lnp_id;
	}

	public void setLnp_id(Integer lnp_id) {
		this.lnp_id = lnp_id;
	}

	public String getLnpIdName() {
		return lnpIdName;
	}

	public void setLnpIdName(String lnpIdName) {
		this.lnpIdName = lnpIdName;
	}

	public List<TeamLeaguePlayer> getTeamleaguePlayers() {
		return teamleaguePlayers;
	}
	
	
}
