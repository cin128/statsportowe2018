package pl.polskieligi.log;

import pl.polskieligi.dto.ProjectInfo;

public class ReportGenerator {

	public static  String getStringReport(ProjectInfo pi) {
		return getReport(pi, false);
	}

	public static String getHtmlReport(ProjectInfo pi) {
		return getReport(pi, true);
	}

	private static String getReport(ProjectInfo pi, boolean html){
		StringBuilder report = new StringBuilder();
		String nl;
		if(html){
			nl="<br/>";
		} else {
			nl = "\n";
		}
		if(pi.getProject()!=null) {
			report.append(nl + "Project id = " + pi.getProject().getId());
			report.append(nl + "Project minut id = " + pi.getProject().getMinut_id());
			report.append(nl + "Archive = " + pi.getProject().getArchive());
			report.append(nl + "Published = " + pi.getProject().getPublished());
			report.append(nl + "Type = " + pi.getProject().getType());
			if (pi.isSkipped()) {
				report.append(nl + "Skipped");
			} else {
				report.append(nl + "League = " +
						(pi.getProject().getLeague() != null ? pi.getProject().getLeague().getName() : null));
				report.append(nl + "Season = " +
						(pi.getProject().getSeason() != null ? pi.getProject().getSeason().getName() : null));
				report.append(nl + "liczba dru�yn = " + pi.getTeams_count());
				report.append(nl + "liczba mecz�w = " + pi.getMatches_count());
				report.append(nl + "liczba kolejek = " + pi.getRounds_count());
				if (pi.getRounds_count() >= (pi.getTeams_count() - 1) * 2 &&
						pi.getMatches_count() == pi.getTeams_count() * pi.getTeams_count() - pi.getTeams_count()) {
					report.append(nl + "Ilo�� dru�yn/mecz�w jest poprawna");
				} else {
					report.append(nl + "Ilo�� dru�yn/mecz�w jest niepoprawna");
				}
				report.append(nl + "czas trwania = " + pi.getProcessingTime() / 1000 + " sec");
			}
		} else {
			report.append(nl + "Project = null");
		}
		for(String m: pi.getMessages()){
			report.append(nl + m);
		}
		return report.toString();
	}
}
