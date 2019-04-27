package pl.polskieligi.log.lnp;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.AbstractDAO;
import pl.polskieligi.dao.ClubDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.Club;

import java.text.MessageFormat;

@Component @Transactional
public class ImportClubLogic extends AbstractImportLnpLogic<Club>{
	private static final String LNP_CLUB_URL_PATTERN = LNP_URL+"/klub/x,{0}.html";

	final static Logger log = Logger.getLogger(ImportClubLogic.class);

	@Autowired
	private ClubDAO clubDAO;

	public ImportClubLogic() {
		super(Club.class);
	}

	@Override protected ImportStatus process(Document doc, Club club) {
		Elements clubNameEl = doc.select("section[class=box-standard]>div>div[class=informations grid]>section[class]>div[class]>a[href]>h1");
		if(clubNameEl.size()==1){
			String clubName = clubNameEl.text();
			if(!StringUtil.isBlank(clubName)) {
				club.setName(clubName);
				String href = clubNameEl.get(0).parent().attr("href");
				String lnpIdName = href.replace("https://www.laczynaspilka.pl/klub/", "").split(",")[0];
				club.setLnpIdName(lnpIdName);
			}
		}
		
		if (!StringUtil.isBlank(club.getName())) {
			return ImportStatus.SUCCESS;
		} else {
			return ImportStatus.INVALID;
		}
	}

	@Override protected Club retrieveById(Integer id) {
		return clubDAO.retrieveByLnp(id);
	}

	@Override protected String getLink(Club obj) {
		return MessageFormat.format(LNP_CLUB_URL_PATTERN, obj.getLnp_id().toString());
	}

	@Override protected AbstractDAO<Club> getDAO() {
		return clubDAO;
	}
}
