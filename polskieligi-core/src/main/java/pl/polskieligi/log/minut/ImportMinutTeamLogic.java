package pl.polskieligi.log.minut;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.AbstractDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Team;

@Component
@Transactional
public class ImportMinutTeamLogic extends AbstractImportMinutLogic<Team>{

	final static Logger log = Logger.getLogger(ImportMinutTeamLogic.class);

	@Autowired
	private TeamDAO teamDAO;
	
	public ImportMinutTeamLogic() {
		super(Team.class);
	}
	
	@Override
	protected void process(Document doc, Team team) {
		Elements nameElem = doc.select("select[class=main][name=urljump2][onchange=selecturl(this)]>option[selected]");
		Elements info = doc.select("table[class=main][width=600][border=0][align=center]>tbody>tr>td");
		String name = nameElem.text();
		if (StringUtil.isBlank(name)) {
			Elements img = info.select("img[src][alt]");
			name = img.attr("alt");
		}
		if (StringUtil.isBlank(name)) {
			String title = doc.select("title").text();
			if(title.contains("Skarb - ")) {
				name = title.replace("Skarb - ", "");
			}
		}
		if (!StringUtil.isBlank(name)) {
			name = name.replace(" (f)", "");//futsal
			name = name.replace(" (k)", "");//kobiety
		}
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
		
	}

	@Override
	protected String getLink(Team t) {
		return MINUT_URL + "/skarb.php?id_klub=" + t.getMinut_id();
	}

	@Override
	protected Team retrieveById(Integer minutId) {
		return teamDAO.retrieveByMinut(minutId);
	}

	@Override
	protected AbstractDAO<Team> getDAO() {
		return teamDAO;
	}	

	public void setTeamDAO(TeamDAO teamDAO) {
		this.teamDAO = teamDAO;
	}
}
