package com.bootloaderjs;

import java.text.Bidi;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amx.jax.postman.service.TemplateUtils;

public class App { // Noncompliant
	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	TemplateUtils templateUtils = new TemplateUtils();
	public static final Pattern pattern = Pattern.compile("^(.*)<(.*)>$");
	

	public static void main(String[] args) {
		String from = "Al Mulla International Exchange<amxjax@gmail.com>";
		String[] path = "html/sms/omsoe".split("^html\\/");

		System.out.println(path[0] + " " + path[1]);
	}
	
	public static void main4(String[] args) {
		String from = "Al Mulla International Exchange<amxjax@gmail.com>";
		String[] path = "html/sms/omsoe".split("^html\\/");

		System.out.println(path[0] + " " + path[1]);
	}

	public static void main3(String[] args) {
		String from = "Al Mulla International Exchange<amxjax@gmail.com>";
		// String from = "amxjax@gmail.com";
		Matcher matcher = pattern.matcher(from);
		if (matcher.find()) {
			System.out.println(matcher.group(1) + "   =   " + matcher.group(2));
		} else {
			System.out.println(from);
		}
	}

	public static void main2(String[] args) {
		String input = "\u0628\u064A\u0627\u0646\u0627\u062A 177 \u0627\u0644\u063189\u0627\u0633\u0644";
		System.out.println(input);

		System.out.println(fixBiDi(input));

		char[] temparray = input.toCharArray();
		int left, right = 0;
		right = temparray.length - 1;
		for (left = 0; left < right; left++, right--) {
			// Swap values of left and right
			char temp = temparray[left];
			temparray[left] = temparray[right];
			temparray[right] = temp;
		}
		for (char c : temparray)
			System.out.print(c);
		System.out.println();
	}

	public static String fixBiDi(String word) {
		Bidi bidi = new Bidi(word, -2);
		if (!bidi.isMixed() && bidi.getBaseLevel() == 0) {
			return word;
		} else {
			int runCount = bidi.getRunCount();
			byte[] levels = new byte[runCount];
			Integer[] runs = new Integer[runCount];

			for (int result = 0; result < runCount; ++result) {
				levels[result] = (byte) bidi.getRunLevel(result);
				runs[result] = Integer.valueOf(result);
			}

			Bidi.reorderVisually(levels, 0, runs, 0, runCount);
			StringBuilder bidiText = new StringBuilder();

			for (int i = 0; i < runCount; ++i) {
				int index = runs[i].intValue();
				int start = bidi.getRunStart(index);
				int end = bidi.getRunLimit(index);
				byte level = levels[index];
				if ((level & 1) != 0) {
					while (true) {
						--end;
						if (end < start) {
							break;
						}

						char character = word.charAt(end);
						if (Character.isMirrored(word.codePointAt(end))) {
							bidiText.append(character);
						} else {
							bidiText.append(character);
						}
					}
				} else {
					bidiText.append(word, start, end);
				}
			}

			return bidiText.toString();
		}
	}

}
