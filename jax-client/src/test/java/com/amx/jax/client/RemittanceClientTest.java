package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.AdditionalFlexRequiredException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Tenant;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxServiceClientApplication.class)
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
		TenantContextHolder.setCurrent(Tenant.KWT2);
		ApiResponse<PurposeOfTransactionModel> response = null;
		RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
		request.setBeneId(new BigDecimal(1424));
		request.setLocalAmount(new BigDecimal(10));
		response = client.getPurposeOfTransactions(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	// @Test
	public void getOldPurposeOfTransactions() throws IOException, ResourceNotFoundException, InvalidInputException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setTenant(Tenant.KWT2);
		ApiResponse<PurposeOfTransactionModel> response = null;
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
		jaxMetaInfo.setCustomerId(new BigDecimal(184466));
		jaxMetaInfo.setTenant(Tenant.KWT);
		ApiResponse<RemittanceApplicationResponseModel> response = null;
		RemittanceTransactionRequestModel request = new RemittanceTransactionRequestModel();
		request.setBeneId(new BigDecimal(4709181));
		request.setLocalAmount(new BigDecimal(2));
		request.setAdditionalBankRuleFiledId(new BigDecimal(101));
		request.setSrlId(new BigDecimal(22));
		ExchangeRateBreakup exRateBreakup = new ExchangeRateBreakup();
		exRateBreakup.setRate(new BigDecimal(472));
		request.setExRateBreakup(exRateBreakup);
		try {
			response = client.saveTransaction(request);
		} catch (AdditionalFlexRequiredException exp) {
			exp.deserializeMeta();
			List<JaxConditionalFieldDto> list = exp.getConditionalFileds();
			list.get(0);
			response = resendRequestWithAddtionalFlexField(request, list);
		}

		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	private ApiResponse<RemittanceApplicationResponseModel> resendRequestWithAddtionalFlexField(
			RemittanceTransactionRequestModel request, List<JaxConditionalFieldDto> list) {

		Map<String, Object> flexFields = new HashMap<String, Object>();
		list.forEach(i -> {
			if (i.getField().getType().equals("select")) {
				flexFields.put(i.getField().getDtoPath().replaceAll("flexFields\\.", ""),
						JsonUtil.toJson(i.getField().getPossibleValues().get(0).getValue()));
			} else if (i.getField().getType().equals("date")) {
				flexFields.put(i.getField().getDtoPath().replaceAll("flexFields\\.", ""), "07/20/2018");
			} else {
				flexFields.put(i.getField().getDtoPath().replaceAll("flexFields\\.", ""), "nnn");
			}
		});
		request.setFlexFields(flexFields);
		return client.saveTransaction(request);

	}

	// @Test
	public void testvalidateTransactionForNEFTRTGS() throws IOException, ResourceNotFoundException,
			InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(184466));
		TenantContextHolder.setCurrent(Tenant.KWT2);
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

	@Test
	public void testsaveRemittance() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(595));
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

	@Test
	public void testfetchTransactionDetails() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(2));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(43904));
		TenantContextHolder.setCurrent(Tenant.OMN);
		ApiResponse<RemittanceTransactionStatusResponseModel> response = null;
		RemittanceTransactionStatusRequestModel request = new RemittanceTransactionStatusRequestModel();
		request.setApplicationDocumentNumber(new BigDecimal(8600417));
		request.setDocumentFinancialYear(new BigDecimal(2018));
		response = client.fetchTransactionDetails(request, false);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	// @Test
	public void testTransactionHistroy() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		ApiResponse<TransactionHistroyDTO> response = null;

		String fromDate = "09/06/2010";
		String toDate = "07/07/2011";
		String docfyr = null, docNumber = null;
		response = client.getTransactionHistroy(docfyr, docNumber, fromDate, toDate);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());

	}
}
