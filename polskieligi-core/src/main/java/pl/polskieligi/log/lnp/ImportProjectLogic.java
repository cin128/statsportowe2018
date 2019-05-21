package pl.polskieligi.log.lnp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

@Component
@Transactional
public class ImportProjectLogic {

	final static Logger log = Logger.getLogger(ImportProjectLogic.class);

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private SeasonDAO seasonDAO;

	public void assignLnpIdToProjects() {

		//for (Season season : seasonDAO.findAll()) {
			Season season = new Season();
			season.setId(new Long(49));
			season.setName("2018/2019");
			if (season.getId() > 37) {// 37=2006/2007 
		/*		for (Region r : Region.values()) {
					if (r != Region.UNDEFINED) {
						assignLnpIdToProjects(season, r);
					}
				}*/
				assignLnpIdToProjects(season, Region.WIE);
			}
		//}
	}

	private void assignLnpIdToProjects(Season season, Region region) {

		Map<String, Map<Integer, String>> map = getProjects(season.getName(), region.getLnpId());
		processInternal(season, region, map);
	}

	private void processInternal(Season season, Region region, Map<String, Map<Integer, String>> map) {
		int ok = 0;
		int blad = 0;
		for (Entry<String, Map<Integer, String>> e1 : map.entrySet()) {
			String leagueType = e1.getKey();
			LeagueType lt = LeagueType.getByLeagueName(leagueType);
			Map<Integer, String> leagues = e1.getValue();
			if (lt == LeagueType.UNDEFINED) {
				blad += leagues.size();
				log.error("Project type not found: " + leagueType);
			} else {
				for (Entry<Integer, String> e : leagues.entrySet()) {
					Integer lnpId = e.getKey();
					Project old = projectDAO.retrieveByLnp(lnpId);
					if (old != null) {
						log.debug("Projects already assigned: " + old.getName() + " --- " + old.getLnpName());
					} else {
						String projectName = e.getValue().toUpperCase();
						if (projectName.contains("JUNIOR")) {
							log.debug("JUNIOR: " + projectName);
						} else if (projectName.contains("BARAŻ")) {
							log.debug("BARAŻ: " + projectName);
						} else if (projectName.contains("HALOWY")) {
							log.debug("HALOWY: " + projectName);
						} else if (projectName.contains("OLDBOY")) {
							log.debug("OLDBOY: " + projectName);
						} else if (isEmpty(lnpId)) {
							log.debug("Empty: " + projectName);
						} else {
							projectName = projectName.replaceAll("\"", "");
							projectName = projectName.replaceAll("\\.", "");
							String groupId = getGroupId(projectName);

							String regionName = null;
							String regionName2 = null;
							if (projectName.contains(":")) {
								regionName = projectName.split(":")[0];
							} else {
								String uProjectName = lt.removeFromProjectName(projectName);
								uProjectName = uProjectName.replace("SENIORÓW", "");
								if (uProjectName.contains(" GRUPA ")) {
									uProjectName = uProjectName.split(" GRUPA ")[0];
								} else if (uProjectName.contains(" GR ")) {
									uProjectName = uProjectName.split(" GR ")[0];
								}
								uProjectName = uProjectName.trim();
								if (uProjectName.endsWith("SKA")) {
									uProjectName = uProjectName.substring(0, uProjectName.length() - 3);
								} else if (uProjectName.endsWith("KA")) {
									uProjectName = uProjectName.substring(0, uProjectName.length() - 2);
								}
								regionName2 = uProjectName.replaceAll("[^A-ZŻŹĆĄŚĘŁÓŃ ]+", "");
							}
							Project p = findProject(season, region, lt, groupId, regionName);
							if(p==null && lt==LeagueType.LIGA_OKR){
								p = findProject(season, region, LeagueType.LIGA_MOKR, groupId, regionName);
							}
							if (p == null && !StringUtils.isEmpty(regionName2)) {
								for (String rn : regionName2.split(" ")) {
									p = findProject(season, region, lt, groupId, rn);
									if (p != null) {
										break;
									}
								}
							}
							if (p != null) {
								ok++;
/*								p.setLnp_id(lnpId);
								p.setLnpIdName("nizsze-ligi");
								p.setLnpName(e.getValue());
								projectDAO.update(p);*/
								log.info("Projects found: " + p.getName() + " --- " + p.getLnpName());
							} else {
								log.debug("Project not found: " + lt.getName() + " " + region.getName() + " "
										+ projectName + " '" + regionName + "'  '" + regionName2 + "'");
								blad++;
							}
						}
					}

				}
			}
		}
		log.info(season.getName() + " " + region.getName() + " ok: " + ok + " blad: " + blad);
	}

	private boolean isEmpty(Integer lnpId) {
		List<Integer> emptyList = new ArrayList<Integer>();
		emptyList.add(27530);
		emptyList.add(25900);
		emptyList.add(27376);
		emptyList.add(27080);
		return emptyList.contains(lnpId);
	}

	private Project findProject(Season season, Region region, LeagueType lt, String groupId, String regionName) {
		StringBuilder sb = new StringBuilder();
		if (regionName != null) {
			sb.append(regionName);
		}
		sb.append("%");
		if (groupId != null) {
			sb.append(" ");
			sb.append(groupId);
		}

		String groupName = sb.toString().toUpperCase();
		Project p = projectDAO.findProject(season, region, lt, groupName);
		if (p == null && (regionName != null || groupId != null)) {
			p = projectDAO.findProject(season, region, lt, groupName + " %");
		}
		if (p == null && regionName != null) {
			p = projectDAO.findProject(season, region, lt, regionName);
		}
		if (p == null && regionName != null) {
			p = projectDAO.findProject(season, region, lt, "%" + regionName + "%");
		}
		if (p == null && regionName == null && groupId != null) {
			p = projectDAO.findProject(season, region, lt, "%" + groupId + "%");
		}
		if ((p == null && "PODHALAŃSKI".equals(regionName))) {
			p = findProject(season, region, lt, groupId, "PODHALE");
		}
		return p;

	}

	private String getGroupId(String projectName) {
		String uProjectName = projectName.toUpperCase();
		String groupId = getGroupId(uProjectName, " GRUPA ", true);
		if (groupId == null) {
			groupId = getGroupId(uProjectName, " GR ", true);
		}
		if (groupId == null) {
			groupId = getGroupId(uProjectName, " GR", false);
		}
		return groupId;
	}

	private String getGroupId(String projectName, String groupTag, boolean romanAllowed) {
		String groupId = null;
		if (projectName.contains(groupTag)) {
			String[] s = projectName.split(groupTag);
			if (s.length > 1) {
				String groupName = s[1];
				if (groupName.contains(" ")) {
					groupName = groupName.split(" ")[0];
				}
				try {
					Integer groupNumber = Integer.parseInt(groupName);
					groupId = RomanNumber.toRoman(groupNumber);
				} catch (NumberFormatException ex) {
					if (romanAllowed) {
						groupId = groupName;
					}
				}
			}
		}
		return groupId;
	}

	private Map<String, Map<Integer, String>> getProjects(String seasonName, Integer zpn_id) {
		Map<String, Map<Integer, String>> result = new HashMap<String, Map<Integer, String>>();
		try {
			URL url = new URL(LnpUrlHelper.getLowerUrl(zpn_id, seasonName));
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonobj = (JSONObject) jsonParser.parse(in);
			String html = (String) jsonobj.get("leagues");

			if (!StringUtils.isEmpty(html)) {
				Document doc = Jsoup.parse(html);
				Elements projectTypes = doc.select("div[class=report-info]");
				for (Element projectType : projectTypes) {
					Element pt = projectType
							.select("div[class=toggle-header toggle-close-all report-info-head report-info-head-gray]")
							.first();
					String leagueType = pt.text();
					Map<Integer, String> res = new HashMap<Integer, String>();
					result.put(leagueType, res);
					Elements projects = projectType.select("div[class=toggle-content]>ul>li>a");
					for (Element project : projects) {
						String projectName = project.text();
						Integer projectLnpId = Integer.parseInt(project.attr("data-game-id"));
						res.put(projectLnpId, projectName);
					}
				}
			}

		} catch (IOException | ParseException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public Set<String> getThirdLeagues(Season season) {
		Set<String> result = new HashSet<String>();
		for (Region r : Region.values()) {
			try {
				URL url = new URL(LnpUrlHelper.getThirdUrl(r.getLnpId(), season.getName()));
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonobj = (JSONObject) jsonParser.parse(in);
				String html = (String) jsonobj.get("url");
				if (!html.endsWith("trzecia-liga,0.html")) {
					result.add(html);
				}
			} catch (IOException | ParseException e) {
				log.error(e.getMessage(), e);
			}
		}
		return result;
	}
}
