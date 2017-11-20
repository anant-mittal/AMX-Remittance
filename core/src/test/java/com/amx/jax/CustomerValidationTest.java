package com.amx.jax;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.util.validation.CustomerValidation;

/**
 * Unit test for customer validation 
 * @author Prashant
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class CustomerValidationTest

{

	public CustomerValidationTest() {
		super();
	}

	@Autowired
	CustomerValidation custVal;

	//@Test
	public void civilIdValidate() {
		assertTrue("civilid validation test failed-284052306594", custVal.validateCivilId("284052306594", "KW"));
		assertFalse("civilid validation test failed-2840523065", custVal.validateCivilId("2840523065", "KW"));
		assertTrue("civilid validation test failed-2840523065", custVal.validateCivilId("2840523065", "BH"));

	}

	//@Test
	public void contactValidate() {
		String output = custVal.validateContact("KW", "13133131", 'R');
		assertFalse(output, "Y".equals(output));
		output = custVal.validateContact("KW", "51780410", 'R');
		assertFalse("output", "Y".equals(output));
		output = custVal.validateContact("KW", "51780410", 'M');
		assertTrue(output, "Y".equals(output));
	}
	
	@Test
	public void test() {}
	
	
}
