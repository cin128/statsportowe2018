package pl.polskieligi.log.minut;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.polskieligi.dao.PlayerDAO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImportMinutPlayerLogicTest {

	@Mock PlayerDAO playerDAO;

	ImportMinutPlayerLogic logic;

	@Before
	public void setUp() {
		logic = new ImportMinutPlayerLogic();
		logic.setPlayerDAO(playerDAO);
		when(playerDAO.saveUpdate(any())).thenAnswer(i->i.getArgument(0));
	}

	@Test
	public void fullTest() {
		System.out.println(logic.doImport(1));
	}

	@Test
	public void emptyTest() {
		System.out.println(logic.doImport(31000));
	}

	@Test
	public void deathTest() {
		System.out.println(logic.doImport(6));
	}
}
