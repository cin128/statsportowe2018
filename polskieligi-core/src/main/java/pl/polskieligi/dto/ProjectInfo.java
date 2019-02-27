package pl.polskieligi.dto;

import pl.polskieligi.log.ReportGenerator;
import pl.polskieligi.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectInfo {
	private Project project;
	private boolean skipped = false;
	private List<String> messages = new ArrayList();
	private long processingTime = 0;
	private int matches_count = 0;
	private int rounds_count = 0;
	private int teams_count = 0;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public boolean isSkipped() {
		return skipped;
	}

	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}

	public void addMessage(String message){
		messages.add(message);
	}

	public List<String> getMessages() {
		return messages;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	public int getMatches_count() {
		return matches_count;
	}

	public void setMatches_count(int matches_count) {
		this.matches_count = matches_count;
	}

	public int getRounds_count() {
		return rounds_count;
	}

	public void setRounds_count(int rounds_count) {
		this.rounds_count = rounds_count;
	}

	public int getTeams_count() {
		return teams_count;
	}

	public void setTeams_count(int teams_count) {
		this.teams_count = teams_count;
	}

	@Override public String toString() {
		return ReportGenerator.getStringReport(this);
	}

	public String toHtml() {
		return ReportGenerator.getHtmlReport(this);
	}
}
