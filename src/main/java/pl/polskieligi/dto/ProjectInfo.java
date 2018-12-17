package pl.polskieligi.dto;

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
		return getReport(false);
	}

	public String toHtml() {
		return getReport(true);
	}

	private String getReport(boolean html){
		StringBuilder report = new StringBuilder();
		String nl;
		if(html){
			nl="<br/>";
		} else {
			nl = "\n";
		}
		report.append(nl + "Project id = " + project.getId());
		report.append(nl + "Project minut id = " + project.getMinut_id());
		report.append(nl + "Archive = " + project.getArchive());
		report.append(nl + "Published = " + project.getPublished());
		report.append(nl + "Type = " + project.getType());
		if(skipped){
			report.append(nl + "Skipped");
		} else {
			report.append(nl + "League = " + (project.getLeague()!=null?project.getLeague().getName():null));
			report.append(nl + "Season = " + (project.getSeason()!=null?project.getSeason().getName():null));
			report.append(nl + "liczba dru�yn = " + teams_count);
			report.append(nl + "liczba mecz�w = " + matches_count);
			report.append(nl + "liczba kolejek = " + rounds_count);
			if(rounds_count >= (teams_count - 1) * 2
					&& matches_count == teams_count * teams_count
					- teams_count) {
				report.append(nl + "Ilo�� dru�yn/mecz�w jest niepoprawna");
			}
			report.append(nl + "czas trwania = " + processingTime / 1000 + " sec");
		}
		for(String m: messages){
			report.append(nl + m);
		}
		return report.toString();
	}
}
