package pl.polskieligi.log.minut;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Team;

@Component
@Transactional
public class ImportMinutTeamLogic {

	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportMinutTeamLogic.class);

	@Autowired
	private TeamDAO teamDAO;

	public Team doImport(Integer teamMinutId) {
		Team result = null;
		log.info("Importing team id = " + teamMinutId);
		java.util.Date startDate = new java.util.Date();

		try {
			Team oldTeam = teamDAO.retrieveTeamByMinut(teamMinutId);
			if (oldTeam != null && oldTeam.getImportStatus()!=null && oldTeam.getImportStatus() == ImportStatus.SUCCESS.getValue()) {
				log.info("Team alerady loaded id = " + teamMinutId);
				result = oldTeam;
			} else {
				log.debug("Start parsing... id = " + teamMinutId);

				Team team = new Team();
				team.setMinut_id(teamMinutId);
				try {
					Document doc = Jsoup.connect(get90minutLink(teamMinutId)).get();
					Elements nameElem = doc.select("select[class=main][name=urljump2][onchange=selecturl(this)]>option[selected]");
					Elements info = doc.select("table[class=main][width=600][border=0][align=center]>tbody>tr>td");
					String name = nameElem.text();
					if (StringUtil.isBlank(name)) {
						Elements img = info.select("img[src][alt]");
						name = img.attr("alt");
					}
					if (StringUtil.isBlank(name)) {
						String title = doc.select("title").text();
						name = title.replace("Skarb - ", "");
					}
					name = name.replace(" (f)", "");//futsal
					name = name.replace(" (k)", "");//kobiety
					Elements longNameElem = info.select("font[size=2]>b");

					if (!StringUtil.isBlank(name)) {
						team.setName(name);
					}
					team.setLongName(longNameElem.text());

					if (!StringUtil.isBlank(team.getName())) {
						team.setImportStatus(ImportStatus.SUCCESS.getValue());
					} else {
						team.setImportStatus(ImportStatus.INVALID.getValue());
					}

				} catch (SocketTimeoutException e) {
					log.warn("Time out for: " + teamMinutId);
					team.setImportStatus(ImportStatus.TIME_OUT.getValue());
				}
				if (team.getImportStatus()==ImportStatus.TIME_OUT.getValue() || team.getImportStatus()==ImportStatus.SUCCESS.getValue()) {
					team = teamDAO.saveUpdate(team);
					log.info("Team saved: " + team);
					result = team;
				} else if(oldTeam!=null){
					teamDAO.delete(oldTeam);
					log.info("Team deleted: " + oldTeam);
					result = null;
				}

				java.util.Date endDate = new java.util.Date();
				long diff = endDate.getTime() - startDate.getTime();
				log.info("End processing id = " + teamMinutId + " time = " + (diff / 1000) + " sec");

			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;

	}
	
	private String get90minutLink(Integer teamMinutId) {
		return MINUT_URL + "/skarb.php?id_klub=" + teamMinutId;
	}

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}
}
