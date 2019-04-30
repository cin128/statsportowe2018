package pl.polskieligi.log.lnp;

import pl.polskieligi.log.distance.DistanceLogic;
import pl.polskieligi.model.TeamLeaguePlayer;

public class PlayersDistanceLogic extends DistanceLogic<LnpPlayerDTO, TeamLeaguePlayer> {

		@Override protected Integer getWebId(LnpPlayerDTO p) {
			return p.getPlayerId();
		}

		@Override protected Long getPersId(TeamLeaguePlayer p) {
			return p.getId();
		}

		@Override protected Double getDistance(LnpPlayerDTO p, TeamLeaguePlayer tlp) {
			return getDistance(p.getName(), tlp.getPlayer().getName());
		}
	}

