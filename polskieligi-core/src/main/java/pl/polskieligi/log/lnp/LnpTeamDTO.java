package pl.polskieligi.log.lnp;

public class LnpTeamDTO {
	private final String lnpName;
	private final Integer lnpId;
	private final String teamName;
	LnpTeamDTO(String lnpName, Integer lnpId, String teamName){
		this.lnpName=lnpName;
		this.lnpId=lnpId;
		this.teamName=teamName;
	}

	public String getLnpName() {
		return lnpName;
	}

	public Integer getLnpId() {
		return lnpId;
	}

	public String getTeamName() {
		return teamName;
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;

	    if (!(obj instanceof LnpTeamDTO))

	        return false;

	    if (obj == this)

	        return true;

	    return this.lnpId.equals(((LnpTeamDTO) obj).lnpId);
	}
	
	@Override
	public int hashCode() {
	    return lnpId;
	}
}
