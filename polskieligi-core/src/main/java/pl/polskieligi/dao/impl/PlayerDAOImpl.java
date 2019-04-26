package pl.polskieligi.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.model.Player;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class PlayerDAOImpl extends AbstractDAOImpl<Player> implements PlayerDAO {
	public PlayerDAOImpl() {
		super(Player.class);
	}

	@Override protected TypedQuery<Player> getRetrieveQuery(Player player) {
		return super.getMinutRetrieveQuery(player.getMinut_id());
	}

	@Override
	public List<Player> findBySeasonAndTeam(Long seasonId, Long teamId) {
		TypedQuery<Player>
		query = getEntityManager().createQuery("SELECT distinct p from Player p join p.teamleaguePlayers tlp join tlp.teamLeague tl join tl.project pr join pr.season s where s.id = :seasonId and tl.team_id = :teamId", Player.class);
		query.setParameter("seasonId", seasonId);
		query.setParameter("teamId", teamId);
		return query.getResultList();
	}
}
