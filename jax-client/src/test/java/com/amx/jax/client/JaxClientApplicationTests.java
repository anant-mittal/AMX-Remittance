package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.UserClient;
import com.amx.jax.client.config.Config;
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
	protected Config conf;

	@Test
	public void testSendotpapi() throws IOException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		ApiResponse<CivilIdOtpModel> response = null;
		try {
			response = client.sendOtpForCivilId("284052306594");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("response of testSendotpapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getOtp());
	}

	@Test
	public void testvalidateotpapi() throws IOException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		ApiResponse<CustomerModel> response = null;
		try {
			response = client.validateOtp("284052306594", "1234");
		} catch (IncorrectInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("response of testSendotpapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
	}

	@Test
	public void testsavecustapi() throws IOException, AlreadyExistsException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<CustomerModel> response = client.saveLoginIdAndPassword("284052306594", "12345");
		logger.info("response of testsavecustapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
	}
}
