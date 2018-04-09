package name.qd.techAnalyst.utils;

public class StringCombineUtil {
	public static String combine(String ... strings) {
		StringBuilder sb = new StringBuilder();
		for(String s : strings) {
			sb.append(s);
		}
		return sb.toString();
	}
}
