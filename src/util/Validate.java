package util;

public class Validate {
	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException format) {
			return false;
		}
	}
}
