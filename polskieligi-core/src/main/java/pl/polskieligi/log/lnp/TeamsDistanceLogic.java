package pl.polskieligi.log.lnp;

import pl.polskieligi.log.distance.DistanceLogic;
import pl.polskieligi.model.TeamLeague;

public class TeamsDistanceLogic extends DistanceLogic<LnpTeamDTO, TeamLeague>{

	@Override protected Integer getWebId(LnpTeamDTO lnpTeam) {
		return lnpTeam.getLnpId();
	}

	@Override protected Long getPersId(TeamLeague teamLeague) {
		return teamLeague.getId();
	}

	@Override protected Double getDistance(LnpTeamDTO lnpTeam, TeamLeague teamLeague) {
		return getDistance(lnpTeam.getTeamName(), teamLeague.getTeam().getName());
	}
}
