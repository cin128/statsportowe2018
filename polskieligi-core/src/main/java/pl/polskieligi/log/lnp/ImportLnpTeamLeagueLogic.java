package pl.polskieligi.log.lnp;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.dao.AbstractDAO;
import pl.polskieligi.dao.ProjectDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.dao.TeamLeagueDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.log.distance.Distance;
import pl.polskieligi.model.TeamLeague;
import pl.polskieligi.model.TeamLeaguePlayer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImportLnpTeamLeagueLogic extends AbstractImportLnpLogic<TeamLeague>{
	final static Logger log = Logger.getLogger(ImportLnpTeamLeagueLogic.class);

	private static final Double AVG_DISTANCE_TRESHOLD = 1.3;

	@Autowired ProjectDAO projectDAO;

	@Autowired TeamLeagueDAO teamLeagueDAO;

	@Autowired TeamDAO teamDAO;


	public ImportLnpTeamLeagueLogic() {
		super(TeamLeague.class);
	}

	@Override protected boolean deleteIfInvalid() {
		return false;
	}

	private static final PlayersDistanceLogic pdl = new PlayersDistanceLogic();

	@Override
	protected ImportStatus process(Document doc, TeamLeague tl) {
		try {
			Set<LnpPlayerDTO> lnpPlayers = parsePlayers(doc);
			List<TeamLeaguePlayer> playerList = tl.getTeamLeaguePlayers();
			if (lnpPlayers.size() == 0 ) {
				log.error("Invalid number of players: count: " + playerList.size() + " lnp count:" + lnpPlayers.size());
				return ImportStatus.INVALID;
			} else {
				if (lnpPlayers.size() != playerList.size()) {
					log.warn("Different numer of players for project: " + tl.getProject().getId() + " " + tl.getProject().getName()
							+ " count: " + playerList.size() + " lnp count:" + lnpPlayers.size());
				}
				List<Distance<LnpPlayerDTO, TeamLeaguePlayer>> minDistances = pdl.findMatchings(lnpPlayers, playerList);
				Double avgDistance = Double.valueOf(minDistances.stream().mapToDouble(d -> d.getDistance()).sum())
						/ minDistances.size();
				ImportStatus result;
				if (avgDistance < AVG_DISTANCE_TRESHOLD) {
					/*Map<Integer, Long> teamsIds = updateTeams(minDistances);
					updateMatches(lnpMatches, teamsIds, project.getId());*/
					result = ImportStatus.SUCCESS;
				} else {
					result = ImportStatus.INVALID;
				}

				log.info("Avg distance: " + avgDistance);
				return result;
			}
		} catch (Exception e) {
			log.error(e);
			return ImportStatus.INVALID;
		}
	}

	private Set<LnpPlayerDTO> parsePlayers(Document doc) {
		return null;
	}

	@Override
	protected TeamLeague retrieveById(Integer id) {
		return teamLeagueDAO.retrieveByLnp(id);
	}

	@Override
	protected String getLink(TeamLeague tl) {
		return LnpUrlHelper.getTeamLeagueUrl(tl.getLnpIdName(), tl.getLnp_id());
	}

	@Override
	protected AbstractDAO<TeamLeague> getDAO() {
		return teamLeagueDAO;
	}
}
