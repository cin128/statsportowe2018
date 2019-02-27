package pl.polskieligi.model;

import javax.persistence.*;

@Entity
@Table(indexes = { @Index(name = "IDX_SE_NAME", columnList = "name", unique = false) })
public class Season {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;

	public Season() {
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
}
