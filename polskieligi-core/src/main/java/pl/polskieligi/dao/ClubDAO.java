package pl.polskieligi.dao;

import pl.polskieligi.model.Club;

public interface ClubDAO extends AbstractDAO<Club> {
	Club retrieveByLnp(Integer lnpId);
}
