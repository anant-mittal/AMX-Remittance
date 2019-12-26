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
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.dict.Tenant;
import com.amx.jax.model.response.remittance.CardTypeDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.scope.TenantContextHolder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemittanceClientTest extends AbstractTest {

	@Autowired
	RemitClient client;
	
	@Autowired
	RemittanceClient brRemitClient;

	//@Test
	public void testsaveRemittance()
			throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		setDefaults();
		TenantContextHolder.setCurrent(Tenant.KWT);
		ApiResponse<PaymentResponseDto> response = null;
		PaymentResponseDto request = new PaymentResponseDto();
		request.setAuth_appNo("471599");
		request.setPaymentId("27000586");
		// app docno
		request.setUdf3("27000586");
		request.setResultCode("CAPTURED");
		// cusref
		request.setTrackId("90277");
		request.setReferenceId("80181365879169");
		request.setTransactionId("927256812138018119");
		request.setPostDate("0199");
		request.setCustomerId(new BigDecimal(5218));
		request.setApplicationCountryId(new BigDecimal(91));
		response = client.saveRemittanceTransaction(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	//@Test
	public void testCustomerCardType() {
		setDefaults();
		AmxApiResponse<CardTypeDto, Object> response = null;
		response = brRemitClient.getCustomerCardTypeList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResults());
	}
	
	@Test
	public void testUpdateCustomerCardType() {
		setDefaults();
		AmxApiResponse<BoolRespModel, Object> response = null;
		BigDecimal chequeBankId = new BigDecimal(62);
		BigDecimal cardTypeId = new BigDecimal(2);
		String nameOnCard = "test";
		response = brRemitClient.updateCustomerCardType(chequeBankId, cardTypeId, nameOnCard);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResults());
	}
}
