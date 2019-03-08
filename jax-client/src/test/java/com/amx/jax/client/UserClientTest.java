package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Tenant;

import com.amx.jax.model.UserDevice;
import com.amx.jax.model.auth.QuestModelDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserClientTest extends AbstractTestClient {

	private static final Logger LOGGER = Logger.getLogger(UserClientTest.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	UserClient client;
	
	
	
	// @Test
	public void getMyProfileInfo() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(123123123));
		ApiResponse<CustomerDto> response = null;
		response = client.getMyProfileInfo();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void deactivate() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<BooleanResponse> response = null;
		response = client.deActivateCustomer();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void saveEmail() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<CustomerModel> response = null;
		String email = "viki.sangani@almullagroup.com";
		// String email = "viki.sangani@gmail.com";
		String mOtp = "046961";
		String eOtp = "672142";
		response = client.saveEmail(email, mOtp, eOtp);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void saveMobile() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		setBahrainDefaults();
		ApiResponse<CustomerModel> response = null;
		ApiResponse<CivilIdOtpModel> otpResp = client.sendResetOtpForCivilId("450501485");
		
		String mobile = "1234567890";
		String mOtp = otpResp.getResult().getmOtp();
		String eOtp = otpResp.getResult().geteOtp();
		response = client.saveMobile(mobile, mOtp, eOtp);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void sendOtpForMobileUpdate() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(239));
		ApiResponse<CivilIdOtpModel> response = null;
		String mobile = "9920027200";

		response = client.sendOtpForMobileUpdate(mobile);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testLoginSuccess() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5128));
		ApiResponse<CustomerModel> response = null;
		try {
			response = client.login("2810502076280", "Amx@12345");
		} catch (AbstractJaxException e) {
			e.printStackTrace();
		}
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void validateSecurityQuestions() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse response = null;

		SecurityQuestionModel model = new SecurityQuestionModel(new BigDecimal(2), "tests");
		List<SecurityQuestionModel> securityquestions = Arrays.asList(model);

		response = client.validateSecurityQuestions(securityquestions);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testFetchRandomDataVerificationQuestions() throws IOException, ResourceNotFoundException,
			InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<QuestModelDTO> response = null;
		try {
			response = client.getDataVerificationQuestions();
		} catch (AbstractJaxException e) {
			e.printStackTrace();
		}
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void validateOtp() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<CustomerModel> response = null;

		String mOtp = "619985";
		String eOtp = null;

		try {
			response = client.validateOtp(mOtp, eOtp);
		} catch (JaxSystemError je) {
			je.printStackTrace();
		} catch (AbstractJaxException e) {
			LOGGER.info("Error key is ---> " + e.getErrorKey());
			LOGGER.info("Error message is ---> " + e.getErrorMessage());
		}

		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void customerLoggedIn() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<CustomerModel> response = null;

		try {
			response = client.customerLoggedIn(new UserDevice());
		} catch (JaxSystemError je) {
			je.printStackTrace();
		} catch (AbstractJaxException e) {
			LOGGER.info("Error key is ---> " + e.getErrorKey());
			LOGGER.info("Error message is ---> " + e.getErrorMessage());
		}

		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	//@Test
	public void sendResetOtp() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		setDefaults();
		jaxMetaInfo.setCustomerId(null);
		jaxMetaInfo.setTenant(Tenant.KWT2);
		ApiResponse<CivilIdOtpModel> response = null;
		response = client.sendResetOtpForCivilId("265041601498");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testInitReg() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		setDefaults();
		jaxMetaInfo.setCustomerId(null);
		jaxMetaInfo.setTenant(Tenant.KWT2);
		ApiResponse<CivilIdOtpModel> response = null;
		response = client.initRegistration("123");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void saveEmailNew() throws IOException, ResourceNotFoundException, InvalidInputException,
	RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(931428));
		ApiResponse<CustomerModel> response = null;
		String email = "viki.sangani@almullagroup.com";
		
		response = client.saveEmailNew(email);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void saveMobileNew() throws IOException, ResourceNotFoundException, InvalidInputException,
	RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(931428));
		ApiResponse<CustomerModel> response = null;
		String mobile = "5456421";
		
		response = client.saveEmailNew(mobile);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	//@Test
	public void testIncome() {
		setDefaults();
		AmxApiResponse<IncomeDto, Object> response = null;
		response = client.getIncome();
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	
	@Test
	public void saveAnnualIncome(){
		setDefaults();
		AmxApiResponse<IncomeDto, Object> response = null;
		IncomeDto incomeDto = new IncomeDto();

		incomeDto.setApplicationCountryId(new BigDecimal(91));
		incomeDto.setIncomeRangeFrom(new BigDecimal(5001));
		incomeDto.setIncomeRangeTo(new BigDecimal(99999999));
		incomeDto.setFullName("RABIL UDDIN MIR MIR RAJAK");
		incomeDto.setArticleDetailId(new BigDecimal(1));
		incomeDto.setCompanyName("Al Mulla");
		incomeDto.setImage("qwerty");
		
		response = client.saveAnnualIncome(incomeDto);
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	
	//@Test
	public void forceAnnualIncomeUpdate() {
		setDefaults();
		Boolean response = false;		
		response = client.forceAnnualIncomeUpdate();
		assertNotNull("Response is null", response);
		assertNotNull(response);
		
	}
}
