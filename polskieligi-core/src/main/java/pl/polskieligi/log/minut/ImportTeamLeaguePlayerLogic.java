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

import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.dao.TeamLeaguePlayerDAO;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.TeamLeague;
import pl.polskieligi.model.TeamLeaguePlayer;

@Component @Transactional
public class ImportTeamLeaguePlayerLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportTeamLeaguePlayerLogic.class);
	
	@Autowired
	private TeamLeaguePlayerDAO teamLeaguePlayerDAO;
	
	@Autowired
	private TeamLeagueDAO teamLeagueDAO;
	
	@Autowired
	private PlayerDAO playerDAO;

	public List<TeamLeaguePlayer> doImport(Long teamLeagueId) {
		List<TeamLeaguePlayer> result = new ArrayList<TeamLeaguePlayer>();
		log.info("Importing teamLeagueId = " + teamLeagueId);
		java.util.Date startDate = new java.util.Date();

		try {
			TeamLeague teamLeague = teamLeagueDAO.find(teamLeagueId);
			if (teamLeague == null) {
				log.error("TeamLeague does not exist = "+teamLeagueId);
			} else
			{
				log.debug("Start parsing... id = " + teamLeagueId);
				Document doc = Jsoup.connect(get90minutLink(teamLeague.getProject().getSeason().getMinut_id(), teamLeague.getTeam().getMinut_id())).get();
				Elements players = doc.select("table[class=main][width=600][border=0]>tbody>tr>td>a[class=main][href][title]");
				for(Element p: players) {
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
				log.info("End processing id = " + teamLeagueId + " time = "+ (diff/1000) +" sec");
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}
		
	

	private String get90minutLink(Integer seasonId, Integer teamId) {
		return MINUT_URL + "/kadra.php?id_sezon=" + seasonId+"&id_klub="+teamId;
	}
}
