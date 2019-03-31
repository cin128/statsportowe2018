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
import pl.polskieligi.dao.RefereeDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Referee;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

@Component @Transactional public class ImportMinutRefereeLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportMinutRefereeLogic.class);

	@Autowired
	private RefereeDAO refereeDAO;

	public Referee doImport(Integer refereeMinutId) {
		Referee result = null;
		log.info("Importing Referee id = " + refereeMinutId);
		java.util.Date startDate = new java.util.Date();

		try {
			Referee oldReferee = refereeDAO.retrieveRefereeByMinut(refereeMinutId);
			if (oldReferee != null && oldReferee.getImportStatus()!=null && oldReferee.getImportStatus()== ImportStatus.SUCCESS.getValue()) {
				log.info("Referee alerady loaded id = "+refereeMinutId);
				result = oldReferee;
			} else {
				log.debug("Start parsing... id = " + refereeMinutId);
				Referee referee = new Referee();
				referee.setMinut_id(refereeMinutId);
				try {
					Document doc = Jsoup.connect(get90minutLink(refereeMinutId)).get();
					Elements bios = doc.select("table[class=main][width=600][border=0]>tbody>tr");

					for (int i = 0; i < bios.size() && i < 10; i++) {
						Element bio = bios.get(i);
						Elements row = bio.select("td");
						if (row.size() > 1) {
							String key = row.get(row.size() > 2 ? 1 : 0).text();
							Element value = row.get(row.size() > 2 ? 2 : 1);
							switch (key) {
								case "Imię":
									referee.setName(value.text());
									break;
								case "Nazwisko":
									referee.setSurname(value.text());
									break;
								case "Kraj":
									referee.setNationality(value.text());
									break;
								case "Data urodzenia":
									referee.setBirthDate(TimestampParser.parseDate(row.get(1).text()));
									break;
								case "Miejsce urodzenia":
									referee.setBirthTown(value.text());
									Elements img = value.select("img");
									referee.setBirthCountry(img.attr("title"));
									break;
								case "Data śmierci":
									referee.setDeathDate(TimestampParser.parseDate(row.get(1).text()));
									break;
								case "Miejsce śmierci":
									referee.setDeatTown(value.text());
									Elements img2 = value.select("img");
									referee.setDeatCountry(img2.attr("title"));
									break;
								case "Okręg":
									referee.setDistrict(value.text());
									break;
							}
						}
					}
					if (!StringUtil.isBlank(referee.getName()) || !StringUtil.isBlank(referee.getSurname())) {
						referee.setImportStatus(ImportStatus.SUCCESS.getValue());
					} else {
						referee.setImportStatus(ImportStatus.INVALID.getValue());
					}
				} catch(SocketTimeoutException | NoRouteToHostException e){
					log.warn("Time out for: "+refereeMinutId+" "+ e.getMessage());
					referee.setImportStatus(ImportStatus.TIME_OUT.getValue());
				}
				if (referee.getImportStatus()==ImportStatus.TIME_OUT.getValue() || referee.getImportStatus()==ImportStatus.SUCCESS.getValue()) {
					referee = refereeDAO.saveUpdate(referee);
					log.info("Player saved: " + referee);
					result = referee;
				} else if(oldReferee!=null){
					refereeDAO.delete(oldReferee);
					log.info("Player deleted: " + oldReferee);
					result = null;
				}
				java.util.Date endDate = new java.util.Date();
				long diff = endDate.getTime() - startDate.getTime();
				log.info("End processing id = " + refereeMinutId + " time = "+ (diff/1000) +" sec");
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	private String get90minutLink(Integer playerMinutId) {
		return MINUT_URL + "/sedzia.php?id=" + playerMinutId;
	}

	public void setRefereeDAO(RefereeDAO playerDAO) {
		this.refereeDAO = refereeDAO;
	}
}
