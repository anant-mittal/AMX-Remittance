package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.AppConstants;
import com.amx.jax.CustomerCredential;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerPersonalDetail;
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
		setBahrainDefaults();	
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		ApiResponse<BooleanResponse> response = null;
		String json = new String(
				Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("cust/person-detail.json").toURI())));
		CustomerPersonalDetail personalDetail = JsonUtil.fromJson(json, CustomerPersonalDetail.class);
		ApiResponse<SendOtpModel> sendOtpresponse = client.sendOtp(personalDetail);
		client.validateOtp(sendOtpresponse.getResult().getmOtp(), sendOtpresponse.getResult().geteOtp());
		json = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("cust/home-addr.json").toURI())));
		CustomerHomeAddress customerHomeAddress = JsonUtil.fromJson(json, CustomerHomeAddress.class);
		client.saveHomeAddress(customerHomeAddress);
		json = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("cust/sec-quest.json").toURI())));
		List<SecurityQuestionModel> securityquestions = JsonUtil.<SecurityQuestionModel>getListFromJsonString(json);
		client.saveSecurityQuestions(securityquestions);
		client.savePhishiingImage("test", "5");
		
		CustomerCredential customerCredential = new CustomerCredential(personalDetail.getIdentityInt(), "Amx@1234");
		response = client.saveLoginDetail(customerCredential, Boolean.TRUE);

		trnxId = (String) ContextUtil.map().get(AppConstants.TRANX_ID_XKEY);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

}
