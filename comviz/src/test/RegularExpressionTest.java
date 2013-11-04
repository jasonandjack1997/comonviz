package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionTest {

	public static void main(String args[]) {
		String test = "aabfooaabfooabfoob";
		Pattern pattern = Pattern.compile("(ab)");
		Matcher m = pattern.matcher(test);
		//if(m.matches())
		{
		
			test = m.replaceAll("$1" + "xx");
		}

		//Matcher m2 = pattern.matcher(test);
		
		
		// test = test.replaceAll("a", "x");
		
		String test2 = new String("1112111");
		test2 = test2.replaceAll("(111)", "--$1--");
		return;
	}
}
