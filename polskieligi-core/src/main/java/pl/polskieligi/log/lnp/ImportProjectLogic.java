package pl.polskieligi.log.lnp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.SeasonDAO;
import pl.polskieligi.log.utils.RomanNumber;
import pl.polskieligi.model.LeagueType;
import pl.polskieligi.model.Project;
import pl.polskieligi.model.Region;
import pl.polskieligi.model.Season;

@Component @Transactional
public class ImportProjectLogic {

	private static final String LNP_URL_PATTERN = "https://www.laczynaspilka.pl/league/get_lower?&zpn_id[]={0}&mode=league&season={1}";

	final static Logger log = Logger.getLogger(ImportProjectLogic.class);
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private SeasonDAO seasonDAO;
	
	public void assignLnpIdToProjects() {
		for (Season season : seasonDAO.findAll()) {
			for (Region r : Region.values()) {
				assignLnpIdToProjects(season, r);
			}
		}
	}
	
	private void assignLnpIdToProjects(Season season, Region region) {
		int ok=0;
		int blad=0;
		Map<String, Map<Integer, String>> map = getProjects(season.getName(), region.getLnpId());		
		for(Map<Integer, String> m: map.values()) {
			for(Entry<Integer, String> e: m.entrySet()) {
				Integer lnpId = e.getKey();
				String projectName = e.getValue();
				projectName = projectName.replaceAll("\"", "");
				projectName = projectName.replaceAll("\\.", "");
				
				LeagueType lt = LeagueType.getByLeagueName(projectName);
				
				String regionName = null;
				if(projectName.contains(":")) {
					regionName = projectName.split(":")[0];
				} 
				String groupId = null;
				if(projectName.contains(" Grupa ")) {
					String groupName = projectName.split(" Grupa ")[1];
					if(groupName.contains(" ")) {
						groupName = groupName.split(" ")[0];
					}
					try {
						Integer groupNumber = Integer.parseInt(groupName);
						groupId = RomanNumber.toRoman(groupNumber);
					} catch (NumberFormatException ex) {
						groupId=groupName;
					}					
				}
				StringBuilder sb = new StringBuilder();
				if(regionName!=null) {
					sb.append(regionName);					
				}
				sb.append("%");
				if(groupId!=null) {					
					sb.append(" ");					
					sb.append(groupId);
				}
				
				String groupName = sb.toString();
				Project p = projectDAO.findProject(season, region, lt, groupName); 
				if(p==null) {
					p = projectDAO.findProject(season, region, lt, groupName +" %"); 							
				} 	
				if(p!=null) {
					ok++;
					p.setLnp_id(lnpId);
					p.setLnpIdName("nizsze-ligi");
					projectDAO.update(p);					
				} else {
					log.debug("Project not found: "+lt.getName()+" "+region.getName()+" "+projectName);
					blad++;
				}
			}			
		}
		log.info(season.getName()+" "+region.getName()+" ok: "+ok+" blad: "+blad);
		
	}
	
	private Map<String, Map<Integer, String>> getProjects(String seasonName, Integer zpn_id) {
		Map<String, Map<Integer, String>> result = new HashMap<String, Map<Integer, String>>();
		try {
			URL url = new URL(getUrl(zpn_id, seasonName));
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonobj = (JSONObject) jsonParser.parse(in);
			String html = (String) jsonobj.get("leagues");

			if(!StringUtils.isEmpty(html)) {
				Document doc = Jsoup.parse(html);
				Elements projectTypes = doc.select("div[class=report-info]");
				for(Element projectType: projectTypes) {
					Element pt = projectType.select("div[class=toggle-header toggle-close-all report-info-head report-info-head-gray]").first();					
					String leagueType = pt.text();
					Map<Integer, String> res = new HashMap<Integer, String>();
					result.put(leagueType, res);
					Elements projects = projectType.select("div[class=toggle-content]>ul>li>a");
					for(Element project: projects) {
						String projectName = project.text();						
						Integer projectLnpId = Integer.parseInt(project.attr("data-game-id"));
						res.put(projectLnpId, projectName);
					}
				}
			}

		} catch (IOException|ParseException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	private static String getUrl(Integer zpn_id, String seasonName) {
		return MessageFormat.format(LNP_URL_PATTERN, zpn_id, seasonName);
	}
}
