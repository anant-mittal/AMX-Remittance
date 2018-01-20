package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.config.JaxConfig;
import com.amx.jax.client.util.ConverterUtility;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxClientApplicationTests {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ConverterUtility util;

	@Autowired
	private UserClient client;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Test
	public void contextLoads() {
	}

	@Autowired
	protected JaxConfig conf;

	private String otp;

	//@Test
	public void testSendotpapi() throws IOException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<CivilIdOtpModel> response = null;
		try {
			response = client.sendOtpForCivilId();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CustomerValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LimitExeededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("response of testSendotpapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getmOtp());
		otp = response.getResult().getmOtp();
	}

	 @Test
	public void testvalidateotpapi() throws IOException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		ApiResponse<CustomerModel> response = null;
		try {
			response = client.validateOtp("289053104436", "112357");
		} catch (IncorrectInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CustomerValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LimitExeededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("response of testSendotpapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
	}

	// @Test
	public void testsavecustapi() throws IOException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<CustomerModel> response = null;
		try {
			response = client.saveLoginIdAndPassword("289072104474", "amx@123", otp);
		} catch (AlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("response of testsavecustapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
	}

	// @Test
	public void updatepasswordapi() throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCustomerId(new BigDecimal(309945));
		ApiResponse<BooleanResponse> response = client.updatePassword("Amx@123456", otp);
		logger.info("response of updatepasswordapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
		assertNotNull("\"Response is null", response.getResult());
		assertTrue("Response is not successful", response.getResult().isSuccess());
	}

	//@Test
	public void testSendotpAndupdatepasswordapi()
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, IOException {
		this.testSendotpapi();
		this.updatepasswordapi();
	}
}
