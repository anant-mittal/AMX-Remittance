package com.amx.jax.exrateservice;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.amx.utils.JsonUtil;

public class TestJson {

	@Test
	public void testNullJson() {

		String jsonNullStr = JsonUtil.toJson(null);
		assertTrue("null".equals(jsonNullStr));
	}
}
