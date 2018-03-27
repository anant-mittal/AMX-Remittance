package com.amx.jax.postman.service;

import java.text.Bidi;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.ContextUtil;

@Component
public class TemplateUtils {

	private static Logger log = Logger.getLogger(TemplateUtils.class);

	public static void reverseFlag(boolean set) {
		ContextUtil.map().put("reverseflag", true);
	}

	public static boolean reverseFlag() {
		return ArgUtil.parseAsBoolean(ContextUtil.map().get("reverseflag"), false);
	}

	public String reverse(String str) {
		if (reverseFlag()) {
			return fixBiDi(str);
			// return new StringBuilder(str).reverse().toString();
		}
		return str;
	}

	public String reverse() {
		return "-X-X-";
	}

	public static String fixBiDi(String wordTemp) {
		String word = ArgUtil.parseAsString(wordTemp, Constants.defaultString);
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

	public static String fixBiDiCheck(String parseAsString) {
		if (reverseFlag()) {
			return fixBiDi(parseAsString);
		}
		return parseAsString;
	}

}
