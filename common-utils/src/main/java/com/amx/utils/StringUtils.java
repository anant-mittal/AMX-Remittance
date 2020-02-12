package com.amx.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class StringUtils {

	public static class StringMatcher {
		String str;
		Matcher m;

		public StringMatcher(String str) {
			this.str = str;
		}

		public boolean match(Pattern pattern) {
			this.m = pattern.matcher(str);
			return this.m != null;
		}

		public boolean find() {
			return this.m != null && this.m.find();
		}

		public boolean isMatch(Pattern pattern) {
			return this.match(pattern) && find();
		}

		public String group(int index) {
			return this.m.group(index);
		}

		public String toString() {
			return this.str;
		}

	}

	public static boolean isNotBlank(String str) {
		return !ArgUtil.isEmptyString(str);
	}

	/**
	 * Remove all characters which are not Alpha or Numeric
	 * 
	 * @param inputString
	 * @return
	 */
	public static String removeSpecialCharacter(String inputString) {
		return inputString.replaceAll("[^a-zA-Z0-9]+", "");
	}

	// public static Map<String, String> getMapFromString(String splitter_char,
	// String key_value_separator_char,
	// String data) {
	// return
	// Splitter.on(splitter_char).withKeyValueSeparator(key_value_separator_char).split(data);
	// }

	public static Map<String, String> getMapFromString(String splitter_char, String key_value_separator_char,
			String data) {
		Map<String, String> map = new HashMap<String, String>();
		String[] stubs = data.split(splitter_char);
		for (String string : stubs) {
			String[] stub = string.split(key_value_separator_char);
			map.put(stub[0], stub[1]);
		}
		return map;
	}

	public static int hash(String str, int max) {
		int hash = max;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * 31 + str.charAt(i);
		}
		return (int) Math.abs(hash % max);
	}

	public static String trim(String str) {
		return (str == null) ? str : str.trim();
	}

	/**
	 * 
	 * @param src
	 * @param pad
	 * @param flip => 0: left, 1:right
	 * @param trim : 1=max size will of pad, 0: min size will be of src;
	 * @return
	 */
	public static String pad(String src, String pad, int flip, int trim) {
		if (trim == 0) {
			return src;
		}

		int lendiff = pad.length() - src.length();
		if (flip == 1) {
			String fullPart = substring(pad, lendiff) + src;
			if (trim == 1) {
				return fullPart.substring(-1 * Math.min(lendiff, 0)); // fullPart.substring(Math.min(src.length(),
			} else {
				return lendiff >= 0 ? fullPart.substring(lendiff) : fullPart;
			}
		} else {
			String fullPart = src + ((lendiff >= 0) ? pad.substring(src.length()) : Constants.BLANK);
			if (trim == 0) {
				return fullPart.substring(0, src.length());
			} else {
				return fullPart.substring(0, pad.length());
			}
		}
	}

	/**
	 * 
	 * @param src
	 * @param pad
	 * @param flip => 0: left, 1:right
	 * @return
	 */
	public static String pad(String src, String pad, int flip) {
		return pad(src, pad, flip, 1);
	}

	public static String substring(String str, int length) {
		if (str == null || str.length() <= 0 || length <= 0) {
			return Constants.BLANK;
		} else if (str.length() <= length) {
			return str;
		} else {
			return str.substring(0, length);
		}
	}

	private static String mask(String strText, int start, int end, char maskChar) {

		if (strText == null || strText.equals(""))
			return "";

		if (start < 0)
			start = 0;

		if (end > strText.length())
			end = strText.length();

		if (start > end) {
			start = end;
		}

		int maskLength = end - start;

		if (maskLength == 0)
			return strText;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append(maskChar);
		}

		return strText.substring(0, start)
				+ sbMaskString.toString()
				+ strText.substring(start + maskLength);
	}

	public static String mask(String strText) {
		if (ArgUtil.isEmpty(strText)) {
			return strText;
		}
		int len = strText.length();
		return mask(strText, len / 10 * 2, len / 10 * 7, '*');
	}

	public static boolean anyMatch(String val, String... matchers) {

		if (val == null)
			return false;

		return Stream.of(matchers).anyMatch(val::equalsIgnoreCase);
	}

	final static char[] digits = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};

	public static String alpha62(long i) {
		int radix = 62;
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = digits[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (65 - charPos));
	}

	public static List<String> capitalize(List<String> input) {
		List<String> output = new ArrayList<>();
		input.forEach(i -> {
			output.add(capitalize(i));
		});
		return output;
	}

	public static String capitalize(String input) {
		if (!StringUtils.isNotBlank(input)) {
			return input;
		}
		if (input.length() == 1) {
			return input.substring(0, 1).toUpperCase();
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

	public static String decapitalize(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		char c[] = string.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}

	public static String[] split(String str, String regex) {
		if (str == null) {
			return new String[0];
		}
		return str.split(regex);
	}

	public static String getByIndex(String str, String regex, int index) {
		String[] x = split(str, regex);
		if (x.length > index) {
			return x[index];
		}
		return null;
	}

	public static boolean isEmpty(String str) {
		return ArgUtil.isEmptyString(str);
	}

	public static String maskIpAddress(String ipAddress) {
		String[] slots = ipAddress.split(":|\\.");
		return String.join("-", Arrays.copyOfRange(slots, 0, slots.length / 4 * 3));
	}

}
