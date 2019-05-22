package com.amx.jax.remittance;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.AbstractTest;
import com.amx.jax.client.RemitClient;
import com.amx.jax.dict.Tenant;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.scope.TenantContextHolder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemittanceClientTest extends AbstractTest {

	@Autowired
	RemitClient client;

	@Test
	public void testsaveRemittance()
			throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		setDefaults();
		TenantContextHolder.setCurrent(Tenant.KWT2);
		ApiResponse<PaymentResponseDto> response = null;
		PaymentResponseDto request = new PaymentResponseDto();
		request.setAuth_appNo("471599");
		request.setPaymentId("3082792411163298");
		// app docno
		request.setUdf3("27002498");
		request.setResultCode("CAPTURED");
		// cusref
		request.setTrackId("90277");
		request.setReferenceId("801813658796");
		request.setTransactionId("9272568121380181");
		request.setPostDate("0199");
		request.setCustomerId(new BigDecimal(595));
		request.setApplicationCountryId(new BigDecimal(91));
		response = client.saveRemittanceTransaction(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

}
