package pl.polskieligi.log.minut;

import org.junit.Test;
import pl.polskieligi.log.minut.TimestampParser;

public class TimestampParserTest {

	@Test
	public void test() {
		System.out.println(TimestampParser.parseTimestamp(2012, "21 lipca, 18:00"));
		System.out.println(TimestampParser.parseTimestamp(2012, "16 lutego, 15:30"));
		System.out.println(Integer.parseInt("22 187".replace(" ", "")));
		System.out.println("22 187".split("-")[0]);
		System.out.println(TimestampParser.getRoundStart(2012, "22-23 marca"));
		System.out.println(TimestampParser.getRoundEnd(2012, "22-23 marca"));
		System.out.println(TimestampParser.getRoundStart(2012, "30 listopada-1 grudnia"));
		System.out.println(TimestampParser.getRoundEnd(2012, "30 listopada-1 grudnia"));
	}
}
