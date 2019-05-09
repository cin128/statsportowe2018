package pl.polskieligi.dao;

import java.sql.Date;
import java.util.List;

import pl.polskieligi.model.Player;
import pl.polskieligi.model.Project;

public interface PlayerDAO extends AbstractLnpDAO<Player>{
	Player retrieveByMinut(Integer minutId);
	List<Player> findBySeasonAndTeam(Long seasonId, Long teamId);
	Player find(String name, String surname, Date birthDate);
}
