package pl.polskieligi.log.lnp;

import java.sql.Date;

public class LnpPlayerDTO {
	private final Date birthDate;
	private final String name;
	private final String surname;
	private final Integer playerId;
	private final String playerName;
	LnpPlayerDTO(Date birthDate,  String name,  String surname, Integer playerId, String playerName){
		this.birthDate = birthDate;
		this.name = name;
		this.surname = surname;
		this.playerId = playerId;
		this.playerName = playerName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public String getPlayerName() {
		return playerName;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;

	    if (!(obj instanceof LnpPlayerDTO))

	        return false;

	    if (obj == this)

	        return true;

	    return this.playerId.equals(((LnpPlayerDTO) obj).playerId);
	}
	
	@Override
	public int hashCode() {
	    return playerId.hashCode();
	}
}
