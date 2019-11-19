package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxFieldClientTest extends AbstractTestClient {

	@Autowired
	JaxFieldClient jaxFieldClient;

	@Test
	public void testGetDynamicFieldsForBeneficiary()
			throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<JaxConditionalFieldDto> response = null;
		response = jaxFieldClient.getDynamicFieldsForBeneficiary(new BigDecimal(95));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

}
