package pl.polskieligi.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.Project;

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
		Session session = getCurrentSession();

		Query query = session.createQuery("from Player where minut_id = :minut_id");
		query.setParameter("minut_id", minutId);
		@SuppressWarnings("unchecked") List<Player> players = query.list();
		for (Player p : players) {
			result = p;
		}

		return result;
	}


	@Override protected Query getRetrieveQuery(Player player) {
		Session session = getCurrentSession();
		Query query = session.createQuery("from Player where minut_id = :minut_id");
		query.setParameter("minut_id", player.getMinut_id());
		return query;
	}
}
