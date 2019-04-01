package pl.polskieligi.model;

import javax.persistence.*;
import java.sql.Date;

@Entity @Table(indexes = {@Index(name = "IDX_PL_MINUT_ID", columnList = "minut_id", unique = false)})
public class Player {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer minut_id;

	private String name;
	private String surname;
	private Date birthDate;
	private String birthTown;
	private String birthCountry;
	private Date deathDate;
	private String deatTown;
	private String deatCountry;
	private String nationality;
	private String position;
	private Float weight;
	private Float height;

	private Integer importStatus;

	public Player() {
		minut_id = 0;
		name = "";
		surname = "";
		birthDate = null;
		birthTown = "";
		birthCountry = "";
		deathDate = null;
		deatTown = "";
		deatCountry = "";
		nationality = "";
		position = "";
		weight = new Float(0);
		height = new Float(0);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMinut_id() {
		return minut_id;
	}

	public void setMinut_id(Integer minut_id) {
		this.minut_id = minut_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthTown() {
		return birthTown;
	}

	public void setBirthTown(String birthTown) {
		this.birthTown = birthTown;
	}

	public String getBirthCountry() {
		return birthCountry;
	}

	public void setBirthCountry(String birthCountry) {
		this.birthCountry = birthCountry;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
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

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getDeatTown() {
		return deatTown;
	}

	public void setDeatTown(String deatTown) {
		this.deatTown = deatTown;
	}

	public String getDeatCountry() {
		return deatCountry;
	}

	public void setDeatCountry(String deatCountry) {
		this.deatCountry = deatCountry;
	}

	public Integer getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(Integer importStatus) {
		this.importStatus = importStatus;
	}

	public String getFullName() {
		return name+' '+surname;
	}

	@Override public String toString() {
		return id + " " + minut_id + " " +name + " " +surname + " " +birthDate + " " +birthTown + " " +birthCountry+ " "
				+ deathDate+ " " + deatTown+ " " + deatCountry+ " " + nationality + " " +position + " " + weight + " " + height;
	}
}
