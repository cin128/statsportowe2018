package pl.polskieligi.log.minut;

import java.sql.Date;

import org.junit.Test;
import pl.polskieligi.log.minut.TimestampParser;

public class TimestampParserTest {
	
	@SuppressWarnings("deprecation")
	private static Date date2012 = new Date(2012, 6, 1);

	@Test
	public void test() {
		System.out.println(TimestampParser.parseTimestamp(date2012, "21 lipca, 18:00"));
		System.out.println(TimestampParser.parseTimestamp(date2012, "16 lutego, 15:30"));
		System.out.println(Integer.parseInt("22 187".replace(" ", "")));
		System.out.println("22 187".split("-")[0]);
		System.out.println(TimestampParser.getRoundStart(date2012, "22-23 marca"));
		System.out.println(TimestampParser.getRoundEnd(date2012, "22-23 marca"));
		System.out.println(TimestampParser.getRoundStart(date2012, "30 listopada-1 grudnia"));
		System.out.println(TimestampParser.getRoundEnd(date2012, "30 listopada-1 grudnia"));
		System.out.println(TimestampParser.getRoundStart(date2012, "9 sierpnia, 23 sierpnia"));
	}
	
	@Test
	public void testCup() {
		System.out.println(TimestampParser.getRoundStart(date2012, "9 sierpnia, 23 sierpnia"));
		System.out.println(TimestampParser.getRoundEnd(date2012, "9 sierpnia, 23 sierpnia"));
	}
	
	@Test
	public void testMS() {
		System.out.println(TimestampParser.getRoundStart(date2012, "20-21 marca, 27-28 marca"));
		System.out.println(TimestampParser.getRoundEnd(date2012, "20-21 marca, 27-28 marca"));
	}
	
	@Test
	public void testJako() {
		System.out.println(TimestampParser.getRoundStart(date2012, "jako 22) - 12-13 kwietnia"));
		System.out.println(TimestampParser.getRoundEnd(date2012, "jako 22) - 12-13 kwietnia"));
	}
	
	@Test
	public void testPolfinal() {
		System.out.println(TimestampParser.getRoundStart(date2012, "1/2 finału - 2 maja"));
		System.out.println(TimestampParser.getRoundEnd(date2012, "1/2 finału - 2 maja"));
	}
	
}
