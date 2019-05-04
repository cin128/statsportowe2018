package pl.polskieligi.log.lnp;

import java.util.Date;

import pl.polskieligi.log.distance.DistanceLogic;
import pl.polskieligi.model.TeamLeaguePlayer;

public class PlayersDistanceLogic extends DistanceLogic<LnpPlayerDTO, TeamLeaguePlayer> {

	@Override
	protected Integer getWebId(LnpPlayerDTO p) {
		return p.getPlayerId();
	}

	@Override
	protected Long getPersId(TeamLeaguePlayer p) {
		return p.getId();
	}

	@Override
	protected Double getDistance(LnpPlayerDTO p, TeamLeaguePlayer tlp) {
		Double nameDistance = getDistance(p.getName(), removePseudo(tlp.getPlayer().getName()));
		Double surnameDistance = getDistance(p.getSurname(), removePseudo(tlp.getPlayer().getSurname()));
		Double birthDateDistance = 0.0;
		if(p.getBirthDate()!=null && tlp.getPlayer().getBirthDate()!=null) {
			Date tlpBirthDate =new Date(tlp.getPlayer().getBirthDate().getTime());
			if(!p.getBirthDate().equals(tlpBirthDate)) {
				birthDateDistance = 1.0;
			}
		}
		return nameDistance + surnameDistance + birthDateDistance;
	}
	
	private static String removePseudo(String s) {
		if(s.contains("(")) {
			return s.substring(0, s.indexOf("(")-1);
		}
		return s;
	}
}
