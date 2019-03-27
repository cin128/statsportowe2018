package pl.polskieligi.log.minut;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.dao.LeagueMatchPlayerDAO;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.dao.TeamDAO;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.Team;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImportMinutLeagueMatchLogicTest {
	
	
	@Mock LeagueMatchPlayerDAO leagueMatchPlayerDAO;

	@Mock PlayerDAO playerDAO;

	@Mock
	ImportMinutPlayerLogic importMinutPlayerLogic;

	ImportLeagueMatchLogic logic;

	@Before
	public void setUp() {
		logic = new ImportLeagueMatchLogic();
		logic.setPlayerDAO(playerDAO);
		logic.setLeagueMatchPlayerDAO(leagueMatchPlayerDAO);
		logic.setImportMinutPlayerLogic(importMinutPlayerLogic);
		when(playerDAO.retrievePlayerByMinut(any())).thenAnswer(i->new Player());
	}

	
	@Test
	public void test1() {
		LeagueMatch lm = new LeagueMatch();
		lm.setMinut_id(9939);
		lm.setMatchpart1(new Team());
		lm.setMatchpart1(new Team());
		System.out.println(logic.doImport(lm));
	}

}
