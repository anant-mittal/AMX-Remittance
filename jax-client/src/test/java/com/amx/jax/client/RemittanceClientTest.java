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
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.dict.Tenant;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RemittanceClientTest {

	@Autowired
	RemitClient client;

	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	// @Test
	public void getPurposeOfTransactions() throws IOException, ResourceNotFoundException, InvalidInputException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setTenant(Tenant.KWT2);
		ApiResponse<PurposeOfTransactionModel> response = null;
		RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
		request.setBeneId(new BigDecimal(1424));
		response = client.getPurposeOfTransactions(new BigDecimal(1424));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	// @Test
		public void testsaveTxn() throws IOException, ResourceNotFoundException, InvalidInputException,
				RemittanceTransactionValidationException, LimitExeededException {
			jaxMetaInfo.setCountryId(new BigDecimal(91));
			jaxMetaInfo.setCompanyId(new BigDecimal(1));
			jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
			jaxMetaInfo.setCustomerId(new BigDecimal(309945));
			jaxMetaInfo.setTenant(Tenant.KWT);
			ApiResponse<RemittanceApplicationResponseModel> response = null;
			RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
			request.setBeneId(new BigDecimal(132053));
			request.setLocalAmount(new BigDecimal(3000));
			request.setAdditionalBankRuleFiledId(new BigDecimal(101));
			request.setSrlId(new BigDecimal(48));
			response = client.saveTransaction(request);
			assertNotNull("Response is null", response);
			assertNotNull(response.getResult());
			assertNotNull(response.getResult().getModelType());
		}

	// @Test
	public void testvalidateTransactionForNEFTRTGS() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(184466));
		jaxMetaInfo.setTenant(Tenant.KWT2);
		ApiResponse<RemittanceTransactionResponsetModel> response = null;
		RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
		request.setBeneId(new BigDecimal(68213));
		request.setLocalAmount(new BigDecimal(100));
		// request.setAdditionalBankRuleFiledId(new BigDecimal(101));
		// request.setSrlId(new BigDecimal(48));
		response = client.validateTransaction(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}
	 
	// @Test
		public void testvalidateTransaction() throws IOException, ResourceNotFoundException, InvalidInputException,
				RemittanceTransactionValidationException, LimitExeededException {
			jaxMetaInfo.setCountryId(new BigDecimal(91));
			jaxMetaInfo.setCompanyId(new BigDecimal(1));
			jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
			jaxMetaInfo.setCustomerId(new BigDecimal(5218));
			jaxMetaInfo.setTenant(Tenant.KWT);
			ApiResponse<RemittanceTransactionResponsetModel> response = null;
			RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
			request.setBeneId(new BigDecimal(88041));
			request.setLocalAmount(new BigDecimal(100));
			// request.setAdditionalBankRuleFiledId(new BigDecimal(101));
			// request.setSrlId(new BigDecimal(48));
			response = client.validateTransaction(request);
			assertNotNull("Response is null", response);
			assertNotNull(response.getResult());
			assertNotNull(response.getResult().getModelType());
		}

	//@Test
	public void testsaveRemittance() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<PaymentResponseDto> response = null;
		PaymentResponseDto request = new PaymentResponseDto();
		request.setAuth_appNo("471504");
		request.setPaymentId("6948171111380180");
		request.setUdf3("27000545");
		request.setResultCode("CAPTURED");
		request.setTrackId("309945");
		request.setReferenceId("801813658796");
		request.setTransactionId("9272568121380180");
		request.setPostDate("0118");
		request.setCustomerId(new BigDecimal(309945));
		request.setApplicationCountryId(new BigDecimal(91));
		response = client.saveRemittanceTransaction(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	@Test
	public void testfetchTransactionDetails() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<RemittanceTransactionStatusResponseModel> response = null;
		RemittanceTransactionStatusRequestModel request = new RemittanceTransactionStatusRequestModel();
		request.setApplicationDocumentNumber(new BigDecimal(27000545));
		request.setDocumentFinancialYear(new BigDecimal(2017));
		response = client.fetchTransactionDetails(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}
	
}
