package pl.polskieligi.log.minut;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import pl.polskieligi.dao.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ImportMinutProjectLogicTest {


	@Mock ProjectDAO projectDAO;

	@Mock LeagueDAO leagueDAO;

	@Mock SeasonDAO seasonDAO;

	@Mock TeamDAO teamDAO;

	@Mock
	TeamLeagueDAO teamLeagueDAO;

	@Mock
	LeagueMatchDAO matchDAO;

	@Mock
	RoundDAO roundDAO;

	ImportMinutProjectLogic logic;

	@Before
	public void setUp() {
		System.setProperty("log4j.configuration","log4j.properties");	
		
		logic = new ImportMinutProjectLogic();
		logic.setProjectDAO(projectDAO);
		when(projectDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));

		logic.setLeagueDAO(leagueDAO);
		when(leagueDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));

		logic.setSeasonDAO(seasonDAO);
		when(seasonDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));

		logic.setTeamDAO(teamDAO);
		when(teamDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));

		logic.setTeamLeagueDAO(teamLeagueDAO);
		when(teamLeagueDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));

		logic.setMatchDAO(matchDAO);
		when(matchDAO.saveUpdate(anyList())).thenAnswer(i->((List)i.getArgument(0)).size());

		logic.setRoundDAO(roundDAO);
		when(roundDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));
	}
	
	@Test
	public void fullTest() {
		System.out.println(logic.doImport(10529));
	}
}
