package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.PushNotificationRecordDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxPushNotificationClientTest {

	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	JaxPushNotificationClient client;

	@Test
	public void testGetPushNotification() throws IOException, ResourceNotFoundException, InvalidInputException {
		ApiResponse<PushNotificationRecordDTO> response = null;

		String customerId = "5218";

		response = client.getJaxPushNotification(customerId);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

}
