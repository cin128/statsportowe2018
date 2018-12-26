package pl.polskieligi.log.minut;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pl.polskieligi.dao.TeamDAO;

@RunWith(MockitoJUnitRunner.class)
public class ImportMinutTeamLogicTest {
	
	
	@Mock TeamDAO teamDAO;

	ImportMinutTeamLogic logic;

	@Before
	public void setUp() {
		logic = new ImportMinutTeamLogic();
		logic.setTeamDAO(teamDAO);
		when(teamDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));
	}

	
	@Test
	public void test1() {
		System.out.println(logic.doImport(171));
	}
	
	@Test
	public void test2() {
		System.out.println(logic.doImport(21966));
	}
	
	@Test
	public void test3() {
		System.out.println(logic.doImport(25));
	}
	
	@Test
	public void titleTest() {
		System.out.println(logic.doImport(2816));
	}
}
