package com.amx.jax.auth;

import java.text.Bidi;

public class App { // Noncompliant
	/**
	 * This is just a test method
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
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

	@Async
	public void myAsyncMethod(){
		
	}

}
