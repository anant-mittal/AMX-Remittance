package com.amx.stringutiltest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.amx.utils.StringUtils;

public class StringUtilTest {

	@Test
	public void testCapitalize() {
		String s1 = "target";
		String op = StringUtils.capitalize(s1);
		assertEquals("Target", op);

		String s2 = "p";
		String op2 = StringUtils.capitalize(s2);
		assertEquals("P", op2);
		long val = 999999999999999L * 248L;
		System.out.println("======" + val);
		System.out.println("======" + Long.toString(val, 36));
		System.out.println("======" + StringUtils.alpha62(val));
	}
}
