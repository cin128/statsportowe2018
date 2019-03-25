package pl.polskieligi.log.minut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueMatchPlayerDAO;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.TeamLeaguePlayerDAO;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.LeagueMatchPlayer;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.TeamLeaguePlayer;

@Component
@Transactional
public class ImportLeagueMarchPlayerLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportLeagueMarchPlayerLogic.class);

	@Autowired
	private LeagueMatchPlayerDAO leagueMatchPlayerDAO;

	@Autowired
	private PlayerDAO playerDAO;

	public List<LeagueMatchPlayer> doImport(LeagueMatch lm) {
		List<LeagueMatchPlayer> result = new ArrayList<LeagueMatchPlayer>();
		log.info("Importing LeagueMatch = " + lm);
		java.util.Date startDate = new java.util.Date();

		try {
			log.debug("Start parsing... id = " + lm);
			Document doc = Jsoup.connect(get90minutLink(lm.getMinut_id())).get();
			
			Elements players = doc
					.select("table[class=main][width=600][border=0]>tbody>tr>td>a[class=main][href][title]");
			doc.select("table[class=main][width=600][border=0]>tbody>tr>td>a").size();
			for (Element p : players) {
				String href = p.attr("href");
				String playerId = href.replace("/kariera.php?id=", "");
				Player player = playerDAO.retrievePlayerByMinut(Integer.parseInt(playerId));
				TeamLeaguePlayer tlp = new TeamLeaguePlayer();
				tlp.setPlayer_id(player.getId());
				tlp.setTeamLeague_id(teamLeagueId);
				teamLeaguePlayerDAO.update(tlp);
				result.add(tlp);
			}

			java.util.Date endDate = new java.util.Date();
			long diff = endDate.getTime() - startDate.getTime();
			log.info("End processing id = " + lm + " time = " + (diff / 1000) + " sec");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	private String get90minutLink(Integer matchId) {
		return MINUT_URL + "/mecz.php?id_mecz=" + matchId;
	}
}
