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
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.model.response.remittance.PaymentLinkRespDTO;
import com.amx.jax.payg.PaymentResponseDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemittancePaymentTest extends AbstractTestClient {

	@Autowired
	RemitClient client;
	
	@Autowired
	RemittanceClient remClient;

	//@Test
	public void testfetchTransactionDetails() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		setDefaults();
		ApiResponse<PaymentResponseDto> response = null;
		PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
		paymentResponseDto.setApplicationCountryId(jaxMetaInfo.getCountryId());
		paymentResponseDto.setCustomerId(jaxMetaInfo.getCustomerId());
		paymentResponseDto.setUdf3("27000022");
		paymentResponseDto.setPaymentId("123");
		response = client.savePaymentId(paymentResponseDto);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}
	
	//@Test
	public void testCreatePaymentLink() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<PaymentLinkRespDTO,Object> response = null;
		
		response = remClient.createAndSendPaymentLink();
		
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	
	@Test
	public void testValidatePaymentLink() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<PaymentLinkRespDTO,Object> response = null;
		BigDecimal linkId = new BigDecimal(63);
		String verificationCode = "48JA";
		
		response = remClient.validatePayLink(linkId, verificationCode);
		
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
}
