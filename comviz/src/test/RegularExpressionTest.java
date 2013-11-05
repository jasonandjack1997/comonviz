package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionTest {

	public static void main(String args[]) {
		String test = "ABC:xxxxx;";
		Pattern pattern = Pattern.compile("(.*):");
		Matcher m = pattern.matcher(test);
		//if (m.find()) {

			String result = m.group(1);
		//}
		return;
	}
}
