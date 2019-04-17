package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.SubstitutionDAO;
import pl.polskieligi.model.Substitution;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class SubstitutionDAOImpl extends AbstractDAOImpl<Substitution> implements SubstitutionDAO {
	public SubstitutionDAOImpl() {
		super(Substitution.class);
	}

	@Override public List<Substitution> getSubstitutionsForLeagueMatch(Long leagueMatchId) {
		TypedQuery<Substitution> query = getEntityManager()
				.createQuery(
						"select s from Substitution s JOIN FETCH s.playerIn JOIN FETCH s.playerOut where leagueMatch_id = :leagueMatchId", Substitution.class);

		query.setParameter("leagueMatchId", leagueMatchId);
		return query.getResultList();
	}
}
