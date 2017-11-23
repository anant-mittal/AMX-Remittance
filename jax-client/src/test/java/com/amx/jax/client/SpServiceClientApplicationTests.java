package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.UserClient;
import com.amx.jax.client.config.Config;
import com.amx.jax.client.util.ConverterUtility;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpServiceClientApplicationTests {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ConverterUtility util;

	@Autowired
	private UserClient client;

	@Test
	public void contextLoads() {
	}
	
	@Autowired
	protected Config conf;

	
	
	@Test
	public void testSendotpapi() throws IOException {
		ApiResponse response = client.sendOtpForCivilId("284052306594");
		logger.info("response of testSendotpapi:" + util.marshall(response));
		assertNotNull("Response is null", response);
	}
}
