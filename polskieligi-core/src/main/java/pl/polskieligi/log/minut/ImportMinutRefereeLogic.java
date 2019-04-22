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
import pl.polskieligi.dao.RefereeDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Referee;

@Component @Transactional public class ImportMinutRefereeLogic extends AbstractImportMinutLogic<Referee>{

	final static Logger log = Logger.getLogger(ImportMinutRefereeLogic.class);

	@Autowired
	private RefereeDAO refereeDAO;

	public ImportMinutRefereeLogic() {
		super(Referee.class);
	}
	
	@Override
	protected void process(Document doc, Referee referee) {
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
		
	}
	
	@Override
	protected String getLink(Referee r) {
		return MINUT_URL + "/sedzia.php?id=" + r.getMinut_id();
	}

	@Override
	protected Referee retrieveById(Integer minutId) {
		return refereeDAO.retrieveByMinut(minutId);
	}

	@Override
	protected AbstractDAO<Referee> getDAO() {
		return refereeDAO;
	}	

	public void setRefereeDAO(RefereeDAO refereeDAO) {
		this.refereeDAO = refereeDAO;
	}
}
