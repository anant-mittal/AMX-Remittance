package com.amx.spservice.client;

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

import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.userservice.model.AbstractUserModel;
import com.amx.jax.userservice.model.UserModel;
import com.amx.spservice.client.config.Config;
import com.amx.spservice.client.util.ConverterUtility;

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
	public void testUserApi() throws IOException {
		AbstractUserModel[] users = getTestUsers();
		ApiResponse response = client.registerUser(users[0]);
		logger.info("response of register user is:" + util.marshall(response));
		assertNotNull("Response is null", response);
	}

	private AbstractUserModel[] getTestUsers() throws IOException {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("users.json");
		String json = IOUtils.toString(stream);
		AbstractUserModel[] users = (UserModel[]) util.unmarshall(json, UserModel[].class);
		return users;

	}
}
