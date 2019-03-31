package pl.polskieligi.log.minut;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
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

import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.TeamLeaguePlayerDAO;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.TeamLeaguePlayer;

@Component
@Transactional
public class ImportTeamLeaguePlayerLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportTeamLeaguePlayerLogic.class);

	@Autowired
	private TeamLeaguePlayerDAO teamLeaguePlayerDAO;

	@Autowired
	private PlayerDAO playerDAO;

	@Autowired
	ImportMinutPlayerLogic importMinutPlayerLogic;

	public List<TeamLeaguePlayer> doImport(Long teamLeagueId, Integer seasonMinutId, Integer teamMinutId) {
		List<TeamLeaguePlayer> result = new ArrayList<TeamLeaguePlayer>();
		log.info("Importing teamLeagueId = " + teamLeagueId);
		java.util.Date startDate = new java.util.Date();

		try {
			log.debug("Start parsing... id = " + teamLeagueId);
			Document doc = Jsoup.connect(get90minutLink(seasonMinutId, teamMinutId)).get();
			Elements players = doc
					.select("table[class=main][width=600][border=0]>tbody>tr>td>a[class=main][href][title]");
			doc.select("table[class=main][width=600][border=0]>tbody>tr>td>a").size();
			for (Element p : players) {
				String href = p.attr("href");
				String playerId = href.replace("/kariera.php?id=", "");
				Integer playerMinutId = Integer.parseInt(playerId);
				Player player = playerDAO.retrievePlayerByMinut(playerMinutId);
				if(player==null){
					player=importMinutPlayerLogic.doImport(playerMinutId);
				}
				if(player!=null) {
					TeamLeaguePlayer tlp = new TeamLeaguePlayer();
					tlp.setPlayer_id(player.getId());
					tlp.setTeamLeague_id(teamLeagueId);
					teamLeaguePlayerDAO.update(tlp);
					result.add(tlp);
				} else {
					log.error("Player not found: "+playerMinutId);
				}
			}

			java.util.Date endDate = new java.util.Date();
			long diff = endDate.getTime() - startDate.getTime();
			log.info("End processing id = " + teamLeagueId + " time = " + (diff / 1000) + " sec");
		} catch (SocketTimeoutException | NoRouteToHostException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	private String get90minutLink(Integer seasonId, Integer teamId) {
		return MINUT_URL + "/kadra.php?id_sezon=" + seasonId + "&id_klub=" + teamId;
	}
}
