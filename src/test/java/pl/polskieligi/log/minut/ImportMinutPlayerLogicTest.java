package pl.polskieligi.log.minut;

import org.junit.Test;

public class ImportMinutPlayerLogicTest {
	@Test
	public void test() {
		ImportMinutPlayerLogic logic = new ImportMinutPlayerLogic();
		System.out.println(logic.doImport(15661));
	}
}
