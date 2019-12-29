package com.amx.stringutiltest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.amx.utils.StringUtils;
import com.amx.utils.UniqueID;

public class StringUtilTest {

	@Test
	public void testSplit() {
		assertEquals(StringUtils.getByIndex("10.28.42.255", ",", 0), "10.28.42.255");
		assertEquals(StringUtils.getByIndex("10.28.42.109,10.28.42.255", ",", 0), "10.28.42.109");
	}

	@Test
	public void testCapitalize() {
		String s1 = "target";
		String op = StringUtils.capitalize(s1);
		assertEquals("Target", op);

		String s2 = "p";
		String op2 = StringUtils.capitalize(s2);
		assertEquals("P", op2);
		long val = 9999999999L;// 999999999999999L * 248L;
		System.out.println("======" + val);
		System.out.println("======" + Long.toString(val, 36));
		System.out.println("======" + StringUtils.alpha62(val));

		String custId = StringUtils.alpha62(0); // StringUtils.alpha62(9999999999L);
		// KWT-JBQ-7R3Ri-7R3RiGe70zB-aUKYOz-bmt-7R3RiIZQkgH
		String nowTrace = "xxxx-7R3RiIZQkgH";
		System.out.println(custId);
		System.out.println(nowTrace);
		System.out.println(StringUtils.pad(custId, nowTrace, 0, 1));
		System.out.println(UniqueID.generateRequestId("7R3Ri-7R3RiGe70zB", "bmt"));
	}
}
