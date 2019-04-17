package com.amx.jax.payment;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.amx.jax.dict.PayGCodes.CodeCategory;
import com.amx.jax.dict.ResponseCodeBHR;

public class PaymentResponseCodeTest {

	@Test
	public void codeCategoryByRespCode() {
		String responseCode = "PY20006";
		CodeCategory output = ResponseCodeBHR.getCodeCategoryByResponseCode(responseCode);
		ResponseCodeBHR outputEnum = ResponseCodeBHR.getResponseCodeEnumByCode(responseCode);
		assertNotNull(output);
		assertNotNull(outputEnum);
	}
}

