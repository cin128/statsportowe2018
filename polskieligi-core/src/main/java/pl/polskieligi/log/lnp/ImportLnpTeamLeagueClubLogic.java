package pl.polskieligi.log.lnp;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.*;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Club;
import pl.polskieligi.model.TeamLeague;

@Component @Transactional
public class ImportLnpTeamLeagueClubLogic extends AbstractImportLnpLogic<TeamLeague> {
	final static Logger log = Logger.getLogger(ImportLnpTeamLeagueClubLogic.class);

	@Autowired ProjectDAO projectDAO;

	@Autowired TeamLeagueDAO teamLeagueDAO;

	@Autowired TeamDAO teamDAO;

	@Autowired ClubDAO clubDAO;

	@Autowired ImportClubLogic importClubLogic;

	public ImportLnpTeamLeagueClubLogic() {
		super(TeamLeague.class);
	}

	@Override protected boolean deleteIfInvalid() {
		return false;
	}

	@Override protected ImportStatus process(Document doc, TeamLeague tl) {
		try {
			ImportStatus result;
			Integer clubId = getClubId(doc);
			Club club = clubDAO.retrieveByLnp(clubId);
			if (club == null) {
				club = importClubLogic.doImport(clubId);
			}
			if (club != null) {
				tl.setClub__id(club.getId());
				result = ImportStatus.SUCCESS;
			} else {
				result = ImportStatus.INVALID;
			}
			return result;
		} catch (Exception e) {
			log.error(e);
			return ImportStatus.INVALID;
		}
	}

	private Integer getClubId(Document doc) {
		Elements menu = doc.select(
				"header>nav[class=page-subnavi]>section[class=page-subnavi__rwd-container]>a[class=page-subnavi__item]");
		if (menu.size() > 0) {
			Element klub = menu.get(0);
			if (klub.text().equals("klub")) {
				String href = klub.attr("href");
				return Integer.parseInt(
						href.replace("https://www.laczynaspilka.pl/klub/", "").replace(".html", "").split(",")[1]);
			}
		}

		return null;
	}

	@Override protected TeamLeague retrieveById(Integer id) {
		return teamLeagueDAO.retrieveByLnp(id);
	}

	@Override protected String getLink(TeamLeague tl) {
		return LnpUrlHelper.getTeamLeagueUrl(tl.getLnpIdName(), tl.getLnp_id());
	}

	@Override protected AbstractDAO<TeamLeague> getDAO() {
		return teamLeagueDAO;
	}
}
