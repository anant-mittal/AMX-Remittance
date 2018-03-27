package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.dict.Tenant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserClientTest extends AbstractTestClient{
    
    private static final Logger LOGGER = Logger.getLogger(UserClientTest.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;
	
	@Autowired
	UserClient client;
	
	//@Test
	public void getMyProfileInfo() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<CustomerDto> response = null;
		response = client.getMyProfileInfo();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	
	//@Test
	public void deactivate() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<BooleanResponse> response = null;
		response = client.deActivateCustomer();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void saveEmail() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<CustomerModel> response = null;
		String email = "viki.sangani@almullagroup.com";
		//String email = "viki.sangani@gmail.com";
		String mOtp="046961";
		String eOtp="672142";
		response = client.saveEmail(email, mOtp, eOtp);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void saveMobile() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(239));
		ApiResponse<CustomerModel> response = null;
		String mobile = "1234567890";
		String mOtp="570605";
		String eOtp="573782";
		response = client.saveMobile(mobile, mOtp, eOtp);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void sendOtpForMobileUpdate() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
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
		public void testLoginSuccess() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
			jaxMetaInfo.setCountryId(new BigDecimal(91));
			jaxMetaInfo.setCompanyId(new BigDecimal(1));
			jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
			jaxMetaInfo.setCustomerId(new BigDecimal(5128));
			ApiResponse<CustomerModel> response = null;
			try {
			response = client.login("289053104436", "Amx@123456");
			}catch(AbstractException e) {
				e.printStackTrace();
			}
			assertNotNull("Response is null", response);
			assertNotNull(response.getResult());
		}
	
	//@Test
	public void validateSecurityQuestions() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse response = null;

		SecurityQuestionModel model = new SecurityQuestionModel(new BigDecimal(2),"tests");
		List<SecurityQuestionModel> securityquestions = Arrays.asList(model);
		
		response = client.validateSecurityQuestions(securityquestions);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void testFetchRandomDataVerificationQuestions() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<QuestModelDTO> response = null;
		try {
			response = client.getDataVerificationQuestions();
		}catch(AbstractException e) {
			e.printStackTrace();
		}
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	
	//@Test
    public void validateOtp(){
        jaxMetaInfo.setCountryId(new BigDecimal(91));
        jaxMetaInfo.setCompanyId(new BigDecimal(1));
        jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
        jaxMetaInfo.setCustomerId(new BigDecimal(5218));
        ApiResponse<CustomerModel> response = null;
        
        String mOtp="619985";
        String eOtp=null;
        
        try {
            response = client.validateOtp(mOtp, eOtp);
        }catch(JaxSystemError je) {
            je.printStackTrace();
        }catch(AbstractException e) {
            LOGGER.info("Error key is ---> "+e.getErrorKey());
            LOGGER.info("Error message is ---> "+e.getErrorMessage());
        }
        
        assertNotNull("Response is null", response);
        assertNotNull(response.getResult());
    }
    
    @Test
    public void customerLoggedIn(){
        jaxMetaInfo.setCountryId(new BigDecimal(91));
        jaxMetaInfo.setCompanyId(new BigDecimal(1));
        jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
        jaxMetaInfo.setCustomerId(new BigDecimal(5218));
        ApiResponse<CustomerModel> response = null;
        
        try {
            response = client.customerLoggedIn();
        }catch(JaxSystemError je) {
            je.printStackTrace();
        }catch(AbstractException e) {
            LOGGER.info("Error key is ---> "+e.getErrorKey());
            LOGGER.info("Error message is ---> "+e.getErrorMessage());
        }
        
        assertNotNull("Response is null", response);
        assertNotNull(response.getResult());
    }
    
    @Test
  	public void sendResetOtp() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
  		setDefaults();
  		jaxMetaInfo.setCustomerId(null);
  		jaxMetaInfo.setTenant(Tenant.KWT2);
  		ApiResponse<CivilIdOtpModel> response = null;
  		response = client.sendResetOtpForCivilId("265041601498");
  		assertNotNull("Response is null", response);
  		assertNotNull(response.getResult());
  	}
    
   // @Test
  	public void testInitReg() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
  		setDefaults();
  		jaxMetaInfo.setCustomerId(null);
  		jaxMetaInfo.setTenant(Tenant.KWT2);
  		ApiResponse<CivilIdOtpModel> response = null;
  		response = client.initRegistration("279110405405");
  		assertNotNull("Response is null", response);
  		assertNotNull(response.getResult());
  	}
}
