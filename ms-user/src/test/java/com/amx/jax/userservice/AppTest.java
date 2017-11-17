package com.amx.jax.userservice;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.userservice.service.UserService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	@Autowired
	UserService kwUserService;

	public void civilIdCheck() {
//		assertTrue(kwUserService.validateCivilId("288122507112", "KW"));
//		assertFalse(kwUserService.validateCivilId("1234", "BH"));
	}

	public static void main(String[] args) {

	}
}
