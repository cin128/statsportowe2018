package pl.polskieligi.log.lnp;

import java.util.Date;

public class LnpPlayerDTO {
	private final Date birthDate;
	private final String name;
	private final Integer playerId;
	private final String playerName;
	LnpPlayerDTO(Date birthDate,  String name, Integer playerId, String playerName){
		this.birthDate = birthDate;
		this.name = name;
		this.playerId = playerId;
		this.playerName = playerName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getName() {
		return name;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public String getPlayerName() {
		return playerName;
	}
}
