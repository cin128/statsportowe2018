package pl.polskieligi.log.minut;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component @Transactional
public class ImportTeamLeaguePlayerLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportTeamLeaguePlayerLogic.class);

	private String get90minutLink(Integer seasonId, Integer teamId) {
		return MINUT_URL + "/kadra.php?id_sezon=" + seasonId+"&id_klub="+teamId;
	}
}
