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
	
	private static final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
	
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
			List<LnpTeam> lnpTeams = parseTeams(teams);
			List<Distance> distances = findMatchingTeam(lnpTeams, teamLeagueList);


			Double avgDistance = Double.valueOf(distances.stream().mapToDouble(d -> d.distance).sum())/distances.size();
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
	
	private List<LnpTeam> parseTeams(Elements teams){
		List<LnpTeam> result = new ArrayList<LnpTeam>();
		for (Element team: teams) {
			String href = team.attr("href");
			href = href.replace("https://www.laczynaspilka.pl/druzyna/", "");
			href = href.replace(".html", "");
			String[] split = href.split(",");
			String lnpName = split[0];
			Integer lnpId = Integer.valueOf(split[1]);
			String teamName = team.select("span[class=name]").get(0).text();
			result.add(new LnpTeam(lnpName, lnpId, teamName));
		}
		return result;
	}
	
	private List<Distance> findMatchingTeam(List<LnpTeam> lnpTeams, List<TeamLeague> teamLeagueList) {
		List<Distance> distances = calculateDistances(lnpTeams, teamLeagueList);
		Set<Long> processedTeams = new HashSet<Long>();
		Set<Integer> processedLnpTeams = new HashSet<Integer>();
		
		
		List<Distance> result = new ArrayList<Distance>();
		Double currentDistance = Double.MIN_VALUE;
		List<Distance> currentDistances = new ArrayList<Distance>();
		for(Distance d: distances) {
			if(currentDistance<d.distance) {
				for(Distance cd: currentDistances) {
					processedTeams.add(cd.teamLeague.getId());
					processedLnpTeams.add(cd.lnpTeam.lnpId);
					long countLnpTeam = currentDistances.stream().filter(a->a.lnpTeam.lnpId.equals(cd.lnpTeam.lnpId)).count();
					long countTeam = currentDistances.stream().filter(a->a.teamLeague.getId().equals(cd.teamLeague.getId())).count();
					if(countLnpTeam==1 && countTeam==1) {
						result.add(cd);
					}
				}
											
				currentDistance = d.distance;
				currentDistances.clear();				
			}
			if(!processedTeams.contains(d.teamLeague.getId()) && !processedLnpTeams.contains(d.lnpTeam.lnpId)) {
				currentDistances.add(d);			
			}
		}
		if(currentDistances.size()>0) {
			for(Distance cd: currentDistances) {
				long countLnpTeam = currentDistances.stream().filter(a->a.lnpTeam.lnpId.equals(cd.lnpTeam.lnpId)).count();
				long countTeam = currentDistances.stream().filter(a->a.teamLeague.getId().equals(cd.teamLeague.getId())).count();
				if(countLnpTeam==1 && countTeam==1) {					
					result.add(cd);
				}
			}
		}
		return result;
	}
	
	private List<Distance> calculateDistances(List<LnpTeam> lnpTeams, List<TeamLeague> teamLeagueList){
		List<Distance> distances = new ArrayList<Distance>();
		for(TeamLeague tl: teamLeagueList) {
			for(LnpTeam lt: lnpTeams) {
				Double distance = new Double(levenshteinDistance.apply(lt.teamName.toLowerCase(), tl.getTeam().getName().toLowerCase()));
				distances.add( new Distance(distance, lt, tl));
				if(distance==0) {
					continue;
				}
			}			
		}
		distances.sort(Comparator.comparing(a -> a.distance));
		return distances;
	}
	
	private void updateTeams(List<Distance> distances){
		for(Distance d: distances) {
			TeamLeague tl = d.teamLeague;
			Team t = tl.getTeam();
			if(tl.getLnp_id()!=null && tl.getLnp_id()>0) {
				if(!tl.getLnp_id().equals(d.lnpTeam.lnpId)){
					log.error("Niejednoznaczne przypisanie: name:"+t.getName()+" lnpName:"+tl.getLnpName()+" "+d.lnpTeam.teamName);
				}
			} else {
				tl.setLnp_id(d.lnpTeam.lnpId);
				tl.setLnpIdName(d.lnpTeam.lnpName);
				tl.setLnpName(d.lnpTeam.teamName);
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
