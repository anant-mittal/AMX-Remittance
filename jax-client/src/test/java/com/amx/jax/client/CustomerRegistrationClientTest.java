package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.SendOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.AppConstants;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.dict.Tenant;
import com.amx.jax.user.UserDevice;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRegistrationClientTest extends AbstractTestClient {

	@Autowired
	CustomerRegistrationClient client;

	String trnxId;

	@Test
	public void testSendOtp() throws URISyntaxException, IOException {
		setDefaults();
		ApiResponse<SendOtpModel> response = null;
		String json = new String(
				Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("cust/person-detail.json").toURI())));
		CustomerPersonalDetail personalDetail = JsonUtil.fromJson(json, CustomerPersonalDetail.class);
		response = client.sendOtp(personalDetail);
		// client.validateOtp("1234", "1234");
		json = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("cust/home-addr.json").toURI())));
		CustomerHomeAddress customerHomeAddress = JsonUtil.fromJson(json, CustomerHomeAddress.class);
		client.saveHomeAddress(customerHomeAddress);
		json = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("cust/sec-quest.json").toURI())));
		List<SecurityQuestionModel> securityquestions = JsonUtil.<SecurityQuestionModel>getListFromJsonString(json);
		client.saveSecurityQuestions(securityquestions);
		trnxId = (String) ContextUtil.map().get(AppConstants.TRANX_ID_XKEY);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

}
