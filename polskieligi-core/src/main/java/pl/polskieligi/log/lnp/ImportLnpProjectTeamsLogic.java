package pl.polskieligi.log.lnp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.AbstractDAO;
import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.log.distance.Distance;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Team;
import pl.polskieligi.model.TeamLeague;

@Component @Transactional 
public class ImportLnpProjectTeamsLogic extends AbstractImportLnpLogic<Project>{
	private static final Double AVG_DISTANCE_TRESHOLD = 6.0;

	final static Logger log = Logger.getLogger(ImportLnpProjectTeamsLogic.class);

	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	TeamLeagueDAO teamLeagueDAO;
	
	@Autowired
	TeamDAO teamDAO;

	private static final TeamsDistanceLogic tdl = new TeamsDistanceLogic();

	public ImportLnpProjectTeamsLogic() {
		super(Project.class);
	}

	@Override protected boolean deleteIfInvalid() {
		return false;
	}

	@Override
	protected ImportStatus process(Document doc, Project project) {
		ImportStatus result;
		Elements teams = doc.select("section>div[class=league-teams-list grid]>div[class=row]>a");
		List<TeamLeague> teamLeagueList = teamLeagueDAO.getTeamLeagues(project.getId());
		if(teams.size()==0 || teamLeagueList.size()==0) {
			log.error("Invalid number of teams: count: "+teamLeagueList.size()+" lnp count:"+teams.size());
			return ImportStatus.INVALID;
		} else {
			if (teams.size() != teamLeagueList.size()) {
				log.warn("Different numer of teams for project: " + project.getId() + " " + project.getName()+" count: "+teamLeagueList.size()+" lnp count:"+teams.size());
			}
			Set<LnpTeamDTO> lnpTeams = parseTeams(teams);
			List<Distance<LnpTeamDTO, TeamLeague>> distances = tdl.findMatchings(lnpTeams, teamLeagueList);


			Double avgDistance = Double.valueOf(distances.stream().mapToDouble(d -> d.getDistance()).sum())/distances.size();
			if(avgDistance<AVG_DISTANCE_TRESHOLD) {
				updateTeams(distances);
				result = ImportStatus.SUCCESS;
			} else {
				result = ImportStatus.INVALID;
			}

			log.info("Avg distance: "+avgDistance);
			return result;
		}
	}
	
	private Set<LnpTeamDTO> parseTeams(Elements teams){
		Set<LnpTeamDTO> result = new HashSet<LnpTeamDTO>();
		for (Element team: teams) {
			String href = team.attr("href");
			href = href.replace("https://www.laczynaspilka.pl/druzyna/", "");
			href = href.replace(".html", "");
			String[] split = href.split(",");
			String lnpName = split[0];
			Integer lnpId = Integer.valueOf(split[1]);
			String teamName = team.select("span[class=name]").get(0).text();
			result.add(new LnpTeamDTO(lnpName, lnpId, teamName));
		}
		return result;
	}
	

	
	private void updateTeams(List<Distance<LnpTeamDTO, TeamLeague>> distances){
		for(Distance<LnpTeamDTO, TeamLeague> d: distances) {
			TeamLeague tl = d.getPersObject();
			Team t = tl.getTeam();
			if(tl.getLnp_id()!=null && tl.getLnp_id()>0) {
				if(!tl.getLnp_id().equals(d.getWebObject().getLnpId())){
					log.error("Niejednoznaczne przypisanie: name:"+t.getName()+" lnpName:"+tl.getLnpName()+" "+d.getWebObject().getTeamName());
				}
			} else {
				tl.setLnp_id(d.getWebObject().getLnpId());
				tl.setLnpIdName(d.getWebObject().getLnpName());
				tl.setLnpName(d.getWebObject().getTeamName());
				teamLeagueDAO.update(tl);
			}
		}
	}

	@Override
	protected Project retrieveById(Integer id) {
		return projectDAO.retrieveByLnp(id);
	}

	@Override
	protected String getLink(Project p) {
		return LnpUrlHelper.getProjectTeamsUrl(p.getLnpIdName(), p.getLnp_id());
	}

	@Override
	protected AbstractDAO<Project> getDAO() {
		return projectDAO;
	}
}
