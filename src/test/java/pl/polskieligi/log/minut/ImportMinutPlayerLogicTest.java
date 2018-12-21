package pl.polskieligi.log.minut;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import pl.polskieligi.dao.PlayerDAO;
import pl.polskieligi.model.Player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doNothing;
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
	public void test() {
		System.out.println(logic.doImport(15661));
	}
}
