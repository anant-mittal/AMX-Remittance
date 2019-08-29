package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.JaxTransactionResponse;
import com.amx.jax.AppConstants;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeneTrnxClientTest extends AbstractTestClient {

	@Autowired
	BeneClient client;
	

	@Autowired
	MetaClient metaclient;
	
	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	// @Test
	@SuppressWarnings("rawtypes")
	public void testsaveBeneAccountInTrnx() throws IOException, URISyntaxException {
		setDefaults();

		ApiResponse response = null;
		String json = new String(
				Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("bene/bene-account.json").toURI())));
		BeneAccountModel beneAccountModel = JsonUtil.fromJson(json, BeneAccountModel.class);
		response = client.saveBeneAccountInTrnx(beneAccountModel);
		assertNotNull("Response is null", response);
		assertNotNull("Response is null", response.getResult());
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testsaveAndcommitAddBeneTrnx() throws IOException, URISyntaxException {
		setDefaults();
		jaxMetaInfo.setDeviceIp("182.75.54.250");
		jaxMetaInfo.setDeviceType("ONLINE");

		ApiResponse response = null;
		String json = new String(
				Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("bene/bene-account.json").toURI())));
		BeneAccountModel beneAccountModel = JsonUtil.fromJson(json, BeneAccountModel.class);
		ApiResponse<JaxTransactionResponse> saveAccResp = client.saveBeneAccountInTrnx(beneAccountModel);
		String trnxId = saveAccResp.getResult().getTranxId();
		ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, trnxId);

		json = new String(
				Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("bene/bene-details.json").toURI())));
		BenePersonalDetailModel benePersonalDetailModel = JsonUtil.fromJson(json, BenePersonalDetailModel.class);
		response = client.saveBenePersonalDetailInTrnx(benePersonalDetailModel);

		ApiResponse<CivilIdOtpModel> otpResponse = client.sendOtp();
		String mOtp = otpResponse.getResult().getmOtp();
		String eOtp = otpResponse.getResult().geteOtp();
		response = client.commitAddBeneTrnx(mOtp, null);

		assertNotNull("Response is null", response);
		assertNotNull("Response is null", response.getResult());
	}
}
