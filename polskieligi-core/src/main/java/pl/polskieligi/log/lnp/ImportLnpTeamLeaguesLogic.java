package pl.polskieligi.log.lnp;

import java.text.MessageFormat;
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

@Component @Transactional 
public class ImportLnpTeamLeaguesLogic extends AbstractImportLnpLogic<Project>{
	private static final Double AVG_DISTANCE_TRESHOLD = 6.0;

	private static final String LNP_PAGE = "/druzyny/{0},{1}.html";

	final static Logger log = Logger.getLogger(ImportLnpTeamLeaguesLogic.class);

	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	TeamLeagueDAO teamLeagueDAO;
	
	@Autowired
	TeamDAO teamDAO;
	
	private static final LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
	
	public ImportLnpTeamLeaguesLogic() {
		super(Project.class);
	}

	@Override protected boolean deleteIfInvalid() {
		return false;
	}

	@Override
	protected void process(Document doc, Project project) {
		ImportStatus result = ImportStatus.SUCCESS;
		Elements teams = doc.select("section>div[class=league-teams-list grid]>div[class=row]>a");
		List<Team> teamList = teamLeagueDAO.getTeams(project.getId());
		if(teams.size()==0 || teamList.size()==0) {
			log.error("Invalid number of teams: count: "+teamList.size()+" lnp count:"+teams.size());
			result = ImportStatus.INVALID;
		} else {
			if (teams.size() != teamList.size()) {
				log.error("Different numer of teams for project: " + project.getId() + " " + project.getName());
			}
			List<LnpTeam> lnpTeams = parseTeams(teams);
			List<Distance> distances = findMatchingTeam(lnpTeams, teamList);


			Double avgDistance = Double.valueOf(distances.stream().mapToInt(d -> d.distance).sum())/distances.size();
			if(avgDistance<AVG_DISTANCE_TRESHOLD) {
				updateTeams(distances);
				result = ImportStatus.SUCCESS;
			} else {
				result = ImportStatus.INVALID;
			}

			log.info("Avg distance: "+avgDistance);
		}
		project.setImportLnpStatus(result.getValue());
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
	
	private List<Distance> findMatchingTeam(List<LnpTeam> lnpTeams, List<Team> teamList) {
		List<Distance> distances = calculateDistances(lnpTeams, teamList);
		Set<Long> processedTeams = new HashSet<Long>();
		Set<Integer> processedLnpTeams = new HashSet<Integer>();
		
		
		List<Distance> result = new ArrayList<Distance>();
		Integer currentDistance = -1;
		List<Distance> currentDistances = new ArrayList<Distance>();
		for(Distance d: distances) {
			if(currentDistance<d.distance) {
				for(Distance cd: currentDistances) {
					processedTeams.add(cd.team.getId());
					processedLnpTeams.add(cd.lnpTeam.lnpId);
					long countLnpTeam = currentDistances.stream().filter(a->a.lnpTeam.lnpId.equals(cd.lnpTeam.lnpId)).count();
					long countTeam = currentDistances.stream().filter(a->a.team.getId().equals(cd.team.getId())).count();
					if(countLnpTeam==1 && countTeam==1) {
						result.add(cd);
					}
				}
											
				currentDistance = d.distance;
				currentDistances.clear();				
			}
			if(!processedTeams.contains(d.team.getId()) && !processedLnpTeams.contains(d.lnpTeam.lnpId)) {
				currentDistances.add(d);			
			}
		}
		if(currentDistances.size()>0) {
			for(Distance cd: currentDistances) {
				long countLnpTeam = currentDistances.stream().filter(a->a.lnpTeam.lnpId.equals(cd.lnpTeam.lnpId)).count();
				long countTeam = currentDistances.stream().filter(a->a.team.getId().equals(cd.team.getId())).count();
				if(countLnpTeam==1 && countTeam==1) {					
					result.add(cd);
				}
			}
		}
		return result;
	}
	
	private List<Distance> calculateDistances(List<LnpTeam> lnpTeams, List<Team> teamList){
		List<Distance> distances = new ArrayList<Distance>();
		for(Team t: teamList) {
			for(LnpTeam lt: lnpTeams) {
				Integer distance = levenshteinDistance.apply(lt.teamName.toLowerCase(), t.getName().toLowerCase());
				distances.add( new Distance(distance, lt, t));
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
			Team t = d.team;
			if(t.getLnp_id()!=null && t.getLnp_id()>0) {
				if(!t.getLnp_id().equals(d.lnpTeam.lnpId)){
					log.error("Niejednoznaczne przypisanie: name:"+t.getName()+" lnpName:"+t.getLnpName()+" "+d.lnpTeam.teamName);
				}
			} else {
				t.setLnp_id(d.lnpTeam.lnpId);
				t.setLnpIdName(d.lnpTeam.lnpName);
				t.setLnpName(d.lnpTeam.teamName);
				teamDAO.update(t);
			}
		}
	}

	@Override
	protected Project retrieveById(Integer id) {
		return projectDAO.retrieveByLnp(id);
	}

	@Override
	protected String getLink(Project p) {
		return MessageFormat.format(LNP_URL+LNP_PAGE, p.getLnpIdName(), p.getLnp_id().toString());
	}

	@Override
	protected AbstractDAO<Project> getDAO() {
		return projectDAO;
	}

	private class LnpTeam{
		final String lnpName;
		final Integer lnpId;
		final String teamName;
		LnpTeam(String lnpName, Integer lnpId, String teamName){
			this.lnpName=lnpName;
			this.lnpId=lnpId;
			this.teamName=teamName;
		}
	}
	
	private class Distance{
		final Integer distance;
		final LnpTeam lnpTeam;
		final Team team;
		Distance(Integer distance, LnpTeam lnpTeam,	Team team){
			this.distance = distance;
			this.lnpTeam = lnpTeam;
			this.team = team;
		}
	}

}
