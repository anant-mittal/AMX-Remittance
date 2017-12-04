package com.amx.jax.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

	/**
	 * Method simplifyString is used by Security Questions. It returns a string
	 * after the following manipulation convert to upper case : strip special
	 * charaters : stripSpecialCharacters remove s from the end : trim spaces
	 * 
	 * @param s
	 *            The String to simplify
	 * @return the simplified string.
	 */
	public String simplifyString(String s) {
		/* first we split the strings on space */
		String[] result = s.split("\\s");
		for (int x = 0; x < result.length; x++) {
			// remove the trailing s from each word
			result[x] = removeTrailingS(result[x]); /* remove trailing s */
			result[x] = stripSpecialCharacters(result[x].toUpperCase()).trim(); /* strip special characters and trim */
		}
		String finalString = "";
		for (int x = 0; x < result.length; x++) {
			finalString = finalString + result[x];
		}
		return finalString;
	}

	/**
	 * Method removeTrailingS returns a String after removing trailing s
	 * 
	 * @param s
	 *            The string to remove trailing 's'
	 * @return the striped string
	 */
	public String removeTrailingS(String s) {

		return s.replaceAll("[sS]$", "");
	}

	/**
	 * Method stripSpecialCharacters returns a clean representation of a string
	 * without any nonalphanumeric and special characters.
	 * 
	 * @param s
	 *            The string to clean up
	 * @return the striped string
	 */
	public String stripSpecialCharacters(String s) {
		return s.replaceAll("[^\\p{L}\\p{N}]", "");
	}

}
