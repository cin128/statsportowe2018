package pl.polskieligi.log.minut;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.polskieligi.dao.LeagueMatchDAO;
import pl.polskieligi.dao.LeagueMatchPlayerDAO;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.RefereeDAO;
import pl.polskieligi.log.ImportStatus;
import pl.polskieligi.model.*;

@Component
@Transactional
public class ImportLeagueMatchLogic {
	private static final String MINUT_URL = "http://www.90minut.pl";

	final static Logger log = Logger.getLogger(ImportLeagueMatchLogic.class);

	@Autowired
	private PlayerDAO playerDAO;

	@Autowired
	private RefereeDAO refereeDAO;

	@Autowired
	private LeagueMatchDAO matchDAO;

	@Autowired
	ImportMinutPlayerLogic importMinutPlayerLogic;

	@Autowired
	ImportMinutRefereeLogic importMinutRefereeLogic;

	public List<LeagueMatchPlayer> doImport(LeagueMatch lm) {
		List<LeagueMatchPlayer> result = new ArrayList<LeagueMatchPlayer>();
		log.info("Importing LeagueMatch = " + lm);
		java.util.Date startDate = new java.util.Date();

		try {
			log.debug("Start parsing... id = " + lm);

			if (lm.getImportStatus()!=null && lm.getImportStatus()!=null && lm.getImportStatus()==ImportStatus.SUCCESS.getValue()) {
				log.info("LeagueMatch alerady loaded = "+lm);
				result = null;
			} else {
				try {
					Document doc = Jsoup.connect(get90minutLink(lm.getMinut_id())).get();

					Elements playersRow = doc.select(
							"table[class=main][width=480][border=0][cellspacing=0][cellpadding=0][align=center]>tbody>tr[height=20][valign=middle][align=center]");
					Map<String, LeagueMatchPlayer> playersMap = new HashMap<String, LeagueMatchPlayer>();

					for (Element row : playersRow) {
						Elements players = row.select("td[width=45%]");
						for (int i = 0; i < 2; i++) {
							Element p1 = players.get(i);
							Elements p2 = p1.select("a[href][class=main]");
							LeagueMatchPlayer lastPlayer = null;
							for (int j = 0; j < p2.size(); j++) {
								Element p = p2.get(j);
								String href = p.attr("href");
								String playerId = href.replace("/wystepy.php?id=", "").split("&id_sezon=")[0];
								Integer playerMinutId = Integer.parseInt(playerId);
								Player player = playerDAO.retrievePlayerByMinut(playerMinutId);
								if (player == null) {
									player = importMinutPlayerLogic.doImport(playerMinutId);
								}
								if (player != null) {
									LeagueMatchPlayer lmp = new LeagueMatchPlayer();
									playersMap.put(player.getName()+" "+player.getSurname(), lmp);
									lmp.setLeagueMatch_id(lm.getMatch_id());
									lmp.setPlayer_id(player.getId());
									if (i == 0) {
										lmp.setTeam_id(lm.getMatchpart1().getId());
									} else {
										lmp.setTeam_id(lm.getMatchpart2().getId());
									}
									if (j == 0) {
										lmp.setMinutIn(1);
									}
									if (j == p2.size() - 1) {
										lmp.setMinutOut(90);
									}
									String name = p.text();
									if (name.startsWith("(")) {
										String number = name.substring(1, name.indexOf(")"));
										lmp.setNumber(Integer.parseInt(number));
										name = name.replace("("+number+")", "");
									}
									playersMap.put(name.trim(), lmp);
									parseCards(lmp, p);
									if (j == 0) {
										lmp.setFirstSquad(true);
									} else if (j > 0) {
										lmp.setFirstSquad(false);
										Integer time = getSubstitutionTime(p);
										Substitution sb = new Substitution();
										sb.setTeam_id(lmp.getTeam_id());
										sb.setPlayerIn_id(lmp.getPlayer_id());
										if(lastPlayer!=null) {
											sb.setPlayerOut_id(lastPlayer.getPlayer_id());
										}
										if (time != null) {
											sb.setTime(time);
											lmp.setMinutIn(time);
											if(lastPlayer!=null) {
												lastPlayer.setMinutOut(time);
											}
										}
										lm.addSubstitutions(sb);
									}
									lm.addLeagueMatchPlayers(lmp);
									lastPlayer = lmp;
								} else {
									lastPlayer = null;
								}
							}
						}
					}

					Referee ref = parseReferee(doc);
					if(ref!=null) {
						lm.setReferee_id(ref.getId());
					}
					parseGoals(doc, playersMap);

					lm.setImportStatus(ImportStatus.SUCCESS.getValue());
				} catch(SocketTimeoutException e){
					log.warn("Time out for: "+lm);
					lm.setImportStatus(ImportStatus.TIME_OUT.getValue());
				}
				matchDAO.update(lm);
				java.util.Date endDate = new java.util.Date();
				long diff = endDate.getTime() - startDate.getTime();
				log.info("End processing id = " + lm + " time = " + (diff / 1000) + " sec");
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}


		return result;
	}

	private void parseGoals(Document doc, Map<String, LeagueMatchPlayer> playersMap) {
		Elements events = doc.select(
				"table[class=main][width=480][border=0][cellspacing=0][cellpadding=0][align=center]>tbody>tr>td>img[src][width=10][height=10][align=absmiddle]");
		for(Element e: events){
			MatchEventType type = null;
			if(e.attr("src").contains("goal.gif")) {
				type = MatchEventType.SCORE;
			} else if(e.attr("src").contains("missed.gif")) {
				type = MatchEventType.PANELTY_MISSED;
			} else if(e.attr("src").contains("own.gif")) {
				type = MatchEventType.SCORE_OWN;
			}
			if(type!=null){
				String txt = e.parent().text();
				txt = txt.replaceAll(" ", "");
				if(txt.contains("(k)")){
					type = MatchEventType.SCORE_PANELTY;
					txt = txt.replaceAll("\\(k\\)", "");
				}
				if(txt.contains("(nk)")){
					txt = txt.replaceAll("\\(nk\\)", "");
				}
				if(txt.contains("(s)")){
					txt = txt.replaceAll("\\(s\\)", "");
				}
				Integer time = getEventTime(txt);
				if(time!=null){
					txt = txt.replaceAll(" "+time, "");
				}
				txt = txt.trim();
				LeagueMatchPlayer lmp = playersMap.get(txt);
				if(lmp!=null){
					MatchEvent me = new MatchEvent();
					me.setType(type.getId());
					me.setTime(time);
					me.setLeagueMatchplayer_id(lmp.getId());
					lmp.addMatchEvent(me);
				} else {
					log.error("LeagueMatchPlayer not found: "+txt);
				}
			}
		}
	}

	private Integer getEventTime(String txt){
		Integer result = null;
		String[] tmp = txt.split(" ");
		if(tmp.length>1){
			String time = tmp[tmp.length-1].replaceAll(" ", "");
			try {
				result = Integer.parseInt(time);
			}catch(NumberFormatException e){
				log.error(e.getMessage(), e);
			}
		}
		return result;
	}

	private Referee parseReferee(Document doc) {
		Referee result = null;
		Elements refereeRows = doc.select(
				"table[class=main][width=480][border=0][cellspacing=0][cellpadding=0][align=center]>tbody>tr>td[colspan=3][valign=middle][align=center]>a[class=main][href]");
		if(refereeRows.size()>0){
			String href =  refereeRows.get(0).attr("href");
			String refId = href.replace("/sedzia.php?id=", "").split("&id_sezon=")[0];
			try{
				Integer id = Integer.parseInt(refId);
				result = refereeDAO.retrieveRefereeByMinut(id);
				if(result==null){
					result = importMinutRefereeLogic.doImport(id);
				}
			} catch(NumberFormatException e) {
				log.error(e.getMessage(), e);
			}
		}
		return result;
	}

	private void parseCards(LeagueMatchPlayer lmp, Element p){
		Element next = p.nextElementSibling();
		if(next!=null){
			String alt = next.attr("alt");
			switch(alt){
				case "ŻK":
					MatchEvent yellow = new MatchEvent();
					yellow.setLeagueMatchplayer_id(lmp.getId());
					yellow.setType(MatchEventType.YELLOW_CARD.getId());
					lmp.addMatchEvent(yellow);
					parseCards(lmp, next);
					break;
				case "CK":
					MatchEvent red = new MatchEvent();
					red.setLeagueMatchplayer_id(lmp.getId());
					red.setType(MatchEventType.RED_CARD.getId());
					Integer time = getRedCardTime(p);
					if(time!=null){
						lmp.setMinutOut(time);
						red.setTime(time);
					}
					lmp.addMatchEvent(red);
					break;
			}
		}
	}

	private Integer getSubstitutionTime(Element p) {
		String player = p.text();
		String text = p.parent().text();
		int index = text.indexOf(player);
		if(index<0){
			return null;
		}

		String t1 = text.substring(0, index);
		return getTime(t1);
	}

	private Integer getRedCardTime(Element p){
		String txt = p.parent().text();
		return getTime(txt);
	}

	private Integer getTime(String txt){
		String[] tmp = txt.split(" ");
		String last = tmp[tmp.length-1];
		try {
			return Integer.parseInt(last.trim());
		} catch(NumberFormatException e){
			return null;
		}
	}

	private String get90minutLink(Integer matchId) {
		return MINUT_URL + "/mecz.php?id_mecz=" + matchId;
	}

	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	public void setImportMinutPlayerLogic(ImportMinutPlayerLogic importMinutPlayerLogic) {
		this.importMinutPlayerLogic = importMinutPlayerLogic;
	}

	public void setRefereeDAO(RefereeDAO refereeDAO) {
		this.refereeDAO = refereeDAO;
	}

	public void setMatchDAO(LeagueMatchDAO matchDAO) {
		this.matchDAO = matchDAO;
	}
}