package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.model.Player;

import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class PlayerDAOImpl extends AbstractDAOImpl<Player> implements PlayerDAO {
	public PlayerDAOImpl() {
		super(Player.class);
	}

	@Override
	public Player retrievePlayerByMinut(Integer minutId) {
		Player result = null;

		Query query = getEntityManager().createQuery("SELECT p from Player p where minut_id = :minut_id");
		query.setParameter("minut_id", minutId);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked") List<Player> players = query.getResultList();
		for (Player p : players) {
			result = p;
		}

		return result;
	}


	@Override protected Query getRetrieveQuery(Player player) {
		Query query = getEntityManager().createQuery("SELECT p from Player p where minut_id = :minut_id");
		query.setParameter("minut_id", player.getMinut_id());
		return query;
	}
}
