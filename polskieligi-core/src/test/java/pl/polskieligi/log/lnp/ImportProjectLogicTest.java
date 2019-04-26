package pl.polskieligi.log.lnp;

import org.junit.Before;
import org.junit.Test;
import pl.polskieligi.log.minut.ImportMinutPlayerLogic;
import pl.polskieligi.model.Season;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ImportProjectLogicTest {

	ImportProjectLogic logic;

	@Before
	public void setUp() {
		logic = new ImportProjectLogic();
	}

	@Test public void getThirdLeagues2018() {
		Season season = new Season();
		season.setName("2018/2019");

		Set<String> links= logic.getThirdLeagues(season);
		links.stream().forEach(s-> System.out.println(s));

	}

	@Test public void getThirdLeagues2017() {
		Season season = new Season();
		season.setName("2014/2015");

		Set<String> links= logic.getThirdLeagues(season);
		links.stream().forEach(s-> System.out.println(s));

	}

}
