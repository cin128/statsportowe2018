package pl.polskieligi.log.minut;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Player;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

@Component @Transactional public class ImportMinutPlayerLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportMinutPlayerLogic.class);

	@Autowired
	private PlayerDAO playerDAO;

	public Player doImport(Integer playerMinutId) {
		Player result = null;
		log.info("Importing player id = " + playerMinutId);
		java.util.Date startDate = new java.util.Date();

		try {
			Player oldPlayer = playerDAO.retrievePlayerByMinut(playerMinutId);
			if (oldPlayer != null && oldPlayer.getImportStatus()!=null && oldPlayer.getImportStatus()== ImportStatus.SUCCESS.getValue()) {
				log.info("Player alerady loaded id = "+playerMinutId);
				result = oldPlayer;
			} else {
				log.debug("Start parsing... id = " + playerMinutId);
				Player player = new Player();
				player.setMinut_id(playerMinutId);
				try {
					Document doc = Jsoup.connect(get90minutLink(playerMinutId)).get();
					Elements bios = doc.select("table[class=main][width=600][border=0]>tbody>tr");

					for (int i = 0; i < bios.size() && i < 10; i++) {
						Element bio = bios.get(i);
						Elements row = bio.select("td");
						if (row.size() > 1) {
							String key = row.get(row.size() > 2 ? 1 : 0).text();
							Element value = row.get(row.size() > 2 ? 2 : 1);
							switch (key) {
								case "Imię":
									player.setName(value.text());
									break;
								case "Nazwisko":
									player.setSurname(value.text());
									break;
								case "Kraj":
									player.setNationality(value.text());
									break;
								case "Data urodzenia":
									player.setBirthDate(TimestampParser.parseDate(row.get(1).text()));
									break;
								case "Miejsce urodzenia":
									player.setBirthTown(value.text());
									Elements img = value.select("img");
									player.setBirthCountry(img.attr("title"));
									break;
								case "Data śmierci":
									player.setDeathDate(TimestampParser.parseDate(row.get(1).text()));
									break;
								case "Miejsce śmierci":
									player.setDeatTown(value.text());
									Elements img2 = value.select("img");
									player.setDeatCountry(img2.attr("title"));
									break;
								case "Wzrost / waga":
									String d = value.text();
									String[] t = d.split("/");
									if (t.length == 2) {
										player.setHeight(Float.parseFloat(t[0].replaceAll(" cm ", "")));
										player.setWeight(Float.parseFloat(t[1].replaceAll(" kg", "")));
									}
									break;
								case "Pozycja":
									player.setPosition(value.text());
									break;
							}
						}
					}
					if (!StringUtil.isBlank(player.getName()) || !StringUtil.isBlank(player.getSurname())) {
						player.setImportStatus(ImportStatus.SUCCESS.getValue());
					} else {
						player.setImportStatus(ImportStatus.INVALID.getValue());
					}
				} catch(SocketTimeoutException | NoRouteToHostException e){
					log.warn("Time out for: "+playerMinutId+" "+ e.getMessage());
					player.setImportStatus(ImportStatus.TIME_OUT.getValue());
				}
				if (player.getImportStatus()==ImportStatus.TIME_OUT.getValue() || player.getImportStatus()==ImportStatus.SUCCESS.getValue()) {
					player = playerDAO.saveUpdate(player);
					log.info("Player saved: " + player);
					result = player;
				} else if(oldPlayer!=null){
					playerDAO.delete(oldPlayer);
					log.info("Player deleted: " + oldPlayer);
					result = null;
				}
				java.util.Date endDate = new java.util.Date();
				long diff = endDate.getTime() - startDate.getTime();
				log.info("End processing id = " + playerMinutId + " time = "+ (diff/1000) +" sec");
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	private String get90minutLink(Integer playerMinutId) {
		return MINUT_URL + "/kariera.php?id=" + playerMinutId;
	}

	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}
}
