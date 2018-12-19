package pl.polskieligi.model;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_TE_MINUT_ID", columnList = "minut_id", unique = false) })
public class Team {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Integer minut_id;
	private String name;
	private String short_name;
	private String middle_name;
	private String description;
	private Integer club_id;
	private Integer checked_out;
	private Timestamp checked_out_time;

	public Team() {
		minut_id = 0;
		name = "";
		short_name = "";
		middle_name = "";
		description = "";
		club_id = 0;
		checked_out = 0;
		checked_out_time = new Timestamp(0);
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

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getClub_id() {
		return club_id;
	}

	public void setClub_id(Integer club_id) {
		this.club_id = club_id;
	}

	public Integer getChecked_out() {
		return checked_out;
	}

	public void setChecked_out(Integer checked_out) {
		this.checked_out = checked_out;
	}

	public Timestamp getChecked_out_time() {
		return checked_out_time;
	}

	public void setChecked_out_time(Timestamp checked_out_time) {
		this.checked_out_time = checked_out_time;
	}	
}
