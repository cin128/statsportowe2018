package pl.polskieligi.log.lnp;

import pl.polskieligi.model.TeamLeague;

public class Distance {
	final Integer distance;
	final LnpTeam lnpTeam;
	final TeamLeague teamLeague;
	Distance(Integer distance, LnpTeam lnpTeam,	TeamLeague teamLeague){
		this.distance = distance;
		this.lnpTeam = lnpTeam;
		this.teamLeague = teamLeague;
	}
}
