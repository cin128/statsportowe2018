package pl.polskieligi.log.lnp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.*;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.log.distance.Distance;
import pl.polskieligi.model.Club;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.TeamLeague;
import pl.polskieligi.model.TeamLeaguePlayer;

@Component
@Transactional
public class ImportLnpTeamLeagueLogic extends AbstractImportLnpLogic<TeamLeague> {
	final static Logger log = Logger.getLogger(ImportLnpTeamLeagueLogic.class);

	private static final Double AVG_DISTANCE_TRESHOLD = 1.1;

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	@Autowired
	private TeamLeagueDAO teamLeagueDAO;

	@Autowired
	private PlayerDAO playerDAO;

	@Autowired
	private TeamLeaguePlayerDAO teamLeguePlayerDAO;

	@Autowired
	private ClubDAO clubDAO;

	@Autowired
	private ImportClubLogic importClubLogic;

	private static final PlayersDistanceLogic pdl = new PlayersDistanceLogic();

	public ImportLnpTeamLeagueLogic() {
		super(TeamLeague.class);
	}

	@Override
	protected ImportStatus process(Document doc, TeamLeague tl) {
		try {
			assignClub(doc, tl);

			Set<LnpPlayerDTO> lnpPlayers = parsePlayers(doc);

			if (lnpPlayers.size() == 0) {
				log.warn("No players found: " + tl.getId());
				return ImportStatus.INVALID;
			} else {
				List<TeamLeaguePlayer> tlPlayers = tl.getTeamLeaguePlayers();
				if (tlPlayers.size() == 0) {
					lnpPlayers.forEach(p->insertTLP(p, tl));
					return ImportStatus.SUCCESS;
				} else {
					List<Distance<LnpPlayerDTO, TeamLeaguePlayer>> distances = pdl.findMatchings(lnpPlayers, tlPlayers);

					Double avgDistance = Double.valueOf(distances.stream().mapToDouble(d -> d.getDistance()).sum())
							/ distances.size();
/*					distances.stream()
							.forEach(d -> System.out.println(d.getPersObject().getPlayer().getName() + " "
									+ d.getPersObject().getPlayer().getSurname() + " = " + d.getWebObject().getName()
									+ " " + d.getWebObject().getSurname() + " " + d.getDistance()));*/
					ImportStatus result;
					if (avgDistance < AVG_DISTANCE_TRESHOLD) {
						updatePlayers(distances, tl);
						result = ImportStatus.SUCCESS;
					} else {
						result = ImportStatus.INVALID;
					}
					return result;
				}
			}
		} catch (Exception e) {
			log.error(e);
			return ImportStatus.INVALID;
		}
	}

	private void assignClub(Document doc, TeamLeague tl) {
		Integer clubId = getClubId(doc);
		Club club = clubDAO.retrieveByLnp(clubId);
		if (club == null) {
			club = importClubLogic.doImport(clubId);
		}
		if (club != null) {
			tl.setClub_id(club.getId());
			teamLeagueDAO.update(tl);
		}
	}

	private Integer getClubId(Document doc) {
		Elements menu = doc.select(
				"header>nav[class=page-subnavi]>section[class=page-subnavi__rwd-container]>a[class=page-subnavi__item]");
		if (menu.size() > 0) {
			Element klub = menu.get(0);
			if (klub.text().equals("klub")) {
				String href = klub.attr("href");
				return Integer.parseInt(
						href.replace("https://www.laczynaspilka.pl/klub/", "").replace(".html", "").split(",")[1]);
			}
		}

		return null;
	}

	private Set<LnpPlayerDTO> parsePlayers(Document doc) {
		Set<LnpPlayerDTO> result = new HashSet<LnpPlayerDTO>();
		Elements players = doc.select("section[class*=players-list]>a[class*=item]>div[class*=info]");
		for (Element p : players) {
			String name = p.select("span[class=name]").text();
			String surname = p.select("span[class=surname]").text();
			String birthDate = p.select("div[class=small]>strong").get(0).text();
			Date bDate = null;
			try {
				bDate = new java.sql.Date(DATE_FORMAT.parse(birthDate).getTime());
			} catch (ParseException e) {
				log.error(e);
			}
			String href = p.parent().attr("href");
			String[] split = href.replace("https://www.laczynaspilka.pl/zawodnik/", "").replace(".html", "").split(",");
			String lnpName = split[0];
			Integer lnpId = Integer.parseInt(split[1]);
			result.add(new LnpPlayerDTO(bDate, name, surname, lnpId, lnpName));
		}
		return result;
	}

	private void updatePlayers(List<Distance<LnpPlayerDTO, TeamLeaguePlayer>> distances, TeamLeague tl) {
		for (Distance<LnpPlayerDTO, TeamLeaguePlayer> d : distances) {
			if (d.getDistance() < AVG_DISTANCE_TRESHOLD) {
				TeamLeaguePlayer tlp = d.getPersObject();
				Player p = tlp.getPlayer();
				if (p.getLnp_id() != null && p.getLnp_id() > 0) {
					if (!p.getLnp_id().equals(d.getWebObject().getPlayerId())) {
						log.error("Niejednoznaczne przypisanie: name:" + p.getName() + " surname:" + p.getSurname());
					}
				} else {
					p.setLnp_id(d.getWebObject().getPlayerId());
					p.setLnpIdName(d.getWebObject().getPlayerName());
					p.setBirthDate(new java.sql.Date(d.getWebObject().getBirthDate().getTime()));
					playerDAO.update(p);
				}
			} else {
				insertTLP(d.getWebObject(), tl);
			}
		}
	}

	private void insertTLP(LnpPlayerDTO lnpPlayer, TeamLeague tl){
		TeamLeaguePlayer tlp = new TeamLeaguePlayer();
		tlp.setTeamLeague_id(tl.getId());
		tlp.setPlayer_id(getPlayerId(lnpPlayer));
		teamLeguePlayerDAO.saveUpdate(tlp);
	}

	private Long getPlayerId(LnpPlayerDTO lnpPlayer){
		Player player = playerDAO.retrieveByLnp(lnpPlayer.getPlayerId());
		if(player==null){
			player = playerDAO.find(lnpPlayer.getName(), lnpPlayer.getSurname(), lnpPlayer.getBirthDate());
		}
		if(player==null){
			player = new Player();
			player.setName(lnpPlayer.getName());
			player.setSurname(lnpPlayer.getSurname());
			player.setBirthDate(lnpPlayer.getBirthDate());
			player.setLnp_id(lnpPlayer.getPlayerId());
			player.setLnpIdName(lnpPlayer.getPlayerName());
			playerDAO.save(player);
		}
		return player.getId();
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
	protected AbstractLnpDAO<TeamLeague> getDAO() {
		return teamLeagueDAO;
	}
}
