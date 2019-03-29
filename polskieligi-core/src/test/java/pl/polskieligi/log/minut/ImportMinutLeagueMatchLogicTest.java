package pl.polskieligi.log.minut;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.dao.*;
import pl.polskieligi.model.LeagueMatch;
import pl.polskieligi.model.Player;
import pl.polskieligi.model.Referee;
import pl.polskieligi.model.Team;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImportMinutLeagueMatchLogicTest {

	@Mock PlayerDAO playerDAO;

	@Mock RefereeDAO refereeDAO;

	@Mock LeagueMatchDAO matchDAO;

	@Mock
	ImportMinutPlayerLogic importMinutPlayerLogic;

	ImportLeagueMatchLogic logic;

	@Before
	public void setUp() {
		logic = new ImportLeagueMatchLogic();
		logic.setPlayerDAO(playerDAO);
		logic.setImportMinutPlayerLogic(importMinutPlayerLogic);
		logic.setRefereeDAO(refereeDAO);
		logic.setMatchDAO(matchDAO);
		when(playerDAO.retrievePlayerByMinut(any())).thenAnswer(i->new Player());
		when(refereeDAO.retrieveRefereeByMinut(any())).thenAnswer(i->new Referee());
	}

	
	@Test
	public void test1() {
		LeagueMatch lm = new LeagueMatch();
		lm.setMinut_id(1317341);
		lm.setMatchpart1(new Team());
		lm.setMatchpart2(new Team());
		System.out.println(logic.doImport(lm));
	}

}
