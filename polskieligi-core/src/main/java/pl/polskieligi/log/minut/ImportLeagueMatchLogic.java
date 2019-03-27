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
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.LeagueMatchPlayer;
import pl.polskieligi.model.Player;

@Component
@Transactional
public class ImportLeagueMatchLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportLeagueMatchLogic.class);

	@Autowired
	private LeagueMatchPlayerDAO leagueMatchPlayerDAO;

	@Autowired
	private PlayerDAO playerDAO;

	@Autowired
	ImportMinutPlayerLogic importMinutPlayerLogic;

	public List<LeagueMatchPlayer> doImport(LeagueMatch lm) {
		List<LeagueMatchPlayer> result = new ArrayList<LeagueMatchPlayer>();
		log.info("Importing LeagueMatch = " + lm);
		java.util.Date startDate = new java.util.Date();

		try {
			log.debug("Start parsing... id = " + lm);
			Document doc = Jsoup.connect(get90minutLink(lm.getMinut_id())).get();
			
			Elements playersRow = doc
					.select("table[class=main][width=480][border=0][cellspacing=0][cellpadding=0][align=center]>tbody>tr[height=20][valign=middle][align=center]");
			for (Element row : playersRow) {
				Elements players = row.select("td[width=45%]");
				for(int i=0; i<2; i++){
					Element p1 = players.get(i);
					Elements p2 = p1.select("a[href][class=main]");
					List<LeagueMatchPlayer>  lmpl = new ArrayList<LeagueMatchPlayer>();
					for(int j=0; j<p2.size(); j++){
						Element p = p2.get(j);
						String href = p.attr("href");
						String playerId = href.replace("/wystepy.php?id=", "").split("&id_sezon=")[0];
						Integer playerMinutId = Integer.parseInt(playerId);
						Player player = playerDAO.retrievePlayerByMinut(playerMinutId);
						if(player==null){
							player=importMinutPlayerLogic.doImport(playerMinutId);
						}
						if(player!=null) {
							LeagueMatchPlayer lmp = new LeagueMatchPlayer();
							lmp.setLeagueMatch_id(lm.getMatch_id());
							lmp.setPlayer_id(player.getId());
							if(i==0){
								lmp.setTeam_id(lm.getMatchpart1().getId());
							} else {
								lmp.setTeam_id(lm.getMatchpart2().getId());
							}
							if(j==0){
								lmp.setMinutIn(1);
							}
							if(j==p2.size()-1){
								lmp.setMinutOut(90);
							}
							String name = p.text();
							if(name.startsWith("(")){
								String number = name.substring(1, name.indexOf(")"));
								lmp.setNumber(Integer.parseInt(number));
							}
							p.nextElementSibling();
							lmpl.add(lmp);
						}
					}
					for(LeagueMatchPlayer lmp: lmpl){
						leagueMatchPlayerDAO.update(lmp);
						log.info(lmp);
					}
				}
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

	public void setLeagueMatchPlayerDAO(LeagueMatchPlayerDAO leagueMatchPlayerDAO) {
		this.leagueMatchPlayerDAO = leagueMatchPlayerDAO;
	}

	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	public void setImportMinutPlayerLogic(ImportMinutPlayerLogic importMinutPlayerLogic) {
		this.importMinutPlayerLogic = importMinutPlayerLogic;
	}
}
