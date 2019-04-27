package pl.polskieligi.log.lnp;

public class LnpTeam {
	final String lnpName;
	final Integer lnpId;
	final String teamName;
	LnpTeam(String lnpName, Integer lnpId, String teamName){
		this.lnpName=lnpName;
		this.lnpId=lnpId;
		this.teamName=teamName;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) return false;

	    if (!(obj instanceof LnpTeam))

	        return false;

	    if (obj == this)

	        return true;

	    return this.lnpId.equals(((LnpTeam) obj).lnpId);
	}
	
	@Override
	public int hashCode() {
	    return lnpId;
	}
}
