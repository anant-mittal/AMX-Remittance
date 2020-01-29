package com.amx.jax.payment;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.amx.jax.dict.PayGCodes.CodeCategory;
import com.amx.jax.dict.ResponseCodeBHR;

public class PaymentResponseCodeTest {
	
	ResponseCodeBHR responseCodeBHR;

	@Test
	public void codeCategoryByRespCode() {
		String responseCode = "PY20006";
		CodeCategory output = responseCodeBHR.getCodeCategoryByResponseCode(responseCode);
		ResponseCodeBHR outputEnum = responseCodeBHR.getResponseCodeEnumByCode(responseCode);
		assertNotNull(output);
		assertNotNull(outputEnum);
	}
}

