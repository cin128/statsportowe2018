package pl.polskieligi.log.lnp;

import pl.polskieligi.model.TeamLeague;

public class Distance {
	final Double distance;
	final LnpTeam lnpTeam;
	final TeamLeague teamLeague;
	Distance(Double distance, LnpTeam lnpTeam,	TeamLeague teamLeague){
		this.distance = distance;
		this.lnpTeam = lnpTeam;
		this.teamLeague = teamLeague;
	}
}
