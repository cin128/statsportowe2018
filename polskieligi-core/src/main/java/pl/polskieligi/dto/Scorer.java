package pl.polskieligi.dto;

import pl.polskieligi.log.DisplayUtils;

public class Scorer {
	private Long id;
	private String name;
	private String surname;
	private Long goals;

	public Scorer(Long id, String name, String surname, Long goals) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.goals = goals;
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Long getGoals() {
		return goals;
	}

	public void setGoals(Long goals) {
		this.goals = goals;
	}

	public String getFullName() {
		return DisplayUtils.getPseudo(name)+' '+DisplayUtils.getPseudo(surname);
	}
}
