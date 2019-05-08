package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
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
import com.amx.amxlib.meta.model.AnnualIncomeRangeDTO;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Tenant;

import com.amx.jax.model.UserDevice;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.customer.CustomerModelSignupResponse;

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

	 //@Test
	public void testLoginSuccess() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5128));
		ApiResponse<CustomerModel> response = null;
		try {
			response = client.login("284052306594", "Amx@1234");
			LOGGER.info("Flags are "+response.getResults());
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

	// @Test
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
		response = client.initRegistration("123", null);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void positivetestLinkDeviceId() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));

		AmxApiResponse<UserFingerprintResponseModel, Object> response = null;
		response = client.linkDeviceId("289072104474");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResults());

	}

	// @Test
	public void negativetestLinkDeviceId() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<UserFingerprintResponseModel, Object> response = null;
		response = client.linkDeviceId("100000005117");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());

	}

	// @Test
	public void positivetestLinkDeviceIdLoggedInUser() throws IOException, ResourceNotFoundException,
			InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(573));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<UserFingerprintResponseModel, Object> response = null;
		response = client.linkDeviceIdLoggedinUser();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());

	}

	// @Test
	public void negativetestLinkDeviceIdLoggedInUser() throws IOException, ResourceNotFoundException,
			InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(12));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<UserFingerprintResponseModel, Object> response = null;
		response = client.linkDeviceIdLoggedinUser();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());

	}

	// @Test
	public void positivetestLoginCustomerByFingerprint() throws IOException, ResourceNotFoundException,
			InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {

		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(573));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<CustomerModel, Object> response = null;
		response = client.loginUserByFingerprint("281050207628", "<)B_.J");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());

	}

	// @Test
	public void negativetestLoginCustomerByFingerprint() throws IOException, ResourceNotFoundException,
			InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {

		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(573));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<CustomerModel, Object> response = null;
		response = client.loginUserByFingerprint("281050207628", "<)B_.J");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());

	}

	// @Test
	public void delinkFingerprint() throws IOException, ResourceNotFoundException, InvalidInputException,
			RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		BoolRespModel response = null;
		response = client.delinkFingerprint();
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	//@Test
	public void resetFingerprint() throws IOException, ResourceNotFoundException,
	InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		BoolRespModel response = null;

		response = client.resetFingerprint("284052306594");
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	
	//@Test
	public void getCustomerModelResponse() throws IOException, ResourceNotFoundException,
	InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<CustomerModelResponse, Object> response = null;

		response = client.getCustomerModelResponse("281050207628");
		LOGGER.debug("response result is "+response.getResults());
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	
	@SuppressWarnings("null")
	//@Test
	public void saveCustomerSecQuestions() throws IOException, ResourceNotFoundException,
	InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(573));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<BoolRespModel, Object> response = null;
		List<SecurityQuestionModel> securityquestion = new ArrayList<SecurityQuestionModel>();
		int i;
		for(i=0;i<5;i++) {
			SecurityQuestionModel securityQuestionModel = new SecurityQuestionModel();
			securityQuestionModel.setQuestionSrNo(new BigDecimal(i+1));
			securityQuestionModel.setAnswerKey(null);
			securityQuestionModel.setAnswer("Hello Anant");
			securityquestion.add(securityQuestionModel);
		}
		
		
		response = client.saveCustomerSecQuestions(securityquestion);
		LOGGER.debug("response result is "+response.getResults());
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	
	//@Test
	public void getCustomerModelSignupResponse() throws IOException, ResourceNotFoundException,
	InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<CustomerModelSignupResponse, Object> response = null;
		
		response = client.getCustomerModelSignupResponse("285061506787");
		LOGGER.debug("response result is "+response.getResults());
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
	

	@Test
	public void validateOtpTest() {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		ApiResponse<CustomerModel> response = null;
		
		response = client.validateOtp("284052306594", "415752", null, null);
		LOGGER.debug("response result is "+response.getResults());
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
}
