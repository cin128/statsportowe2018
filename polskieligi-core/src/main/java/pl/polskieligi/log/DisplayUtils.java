package pl.polskieligi.log;

public class DisplayUtils {
	public static String getPseudo(String text) {
		if(text !=null && text.contains("(") && text.contains(")")) {
			return text.substring(text.indexOf("(")+1, text.indexOf(")"));
		}
		return text;
	}
}
