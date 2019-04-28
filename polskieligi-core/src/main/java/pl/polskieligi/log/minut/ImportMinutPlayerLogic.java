package pl.polskieligi.log.minut;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.AbstractDAO;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Player;

@Component @Transactional public class ImportMinutPlayerLogic extends AbstractImportMinutLogic<Player>{

	final static Logger log = Logger.getLogger(ImportMinutPlayerLogic.class);

	@Autowired
	private PlayerDAO playerDAO;

	public ImportMinutPlayerLogic() {
		super(Player.class);
	}
	
	@Override
	protected ImportStatus process(Document doc, Player player) {
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
					case "Przydomek":
						player.setPseudo(value.text());
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
			return ImportStatus.SUCCESS;
		} else {
			return ImportStatus.INVALID;
		}
	}
	
	@Override
	protected String getLink(Player p) {
		return MinutUrlHelper.getPlayerUrl(p.getMinut_id());
	}

	@Override
	protected Player retrieveById(Integer minutId) {
		return playerDAO.retrieveByMinut(minutId.intValue());
	}

	@Override
	protected AbstractDAO<Player> getDAO() {
		return playerDAO;
	}	

	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}
}
