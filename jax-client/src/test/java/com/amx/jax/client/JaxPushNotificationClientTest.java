package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.CustomerNotifyHubDTO;
import com.amx.jax.api.AmxApiResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxPushNotificationClientTest {

	@Autowired
	MetaClient metaclient;

	@Autowired
	CustomerNotifyHubClient client;

	// @Test
	public void testGetPushNotification() throws IOException, ResourceNotFoundException, InvalidInputException {
		AmxApiResponse<CustomerNotifyHubDTO, Object> response = null;

		BigDecimal customerId = new BigDecimal("5218");

		response = client.get(customerId);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	@Test
	public void saveCustomerNotification() throws ParseException {
		CustomerNotifyHubDTO customerNotificationDTO = new CustomerNotifyHubDTO();

		// customerNotificationDTO.setCustomerId(new BigDecimal(5218));
		customerNotificationDTO.setCountryId(new BigDecimal(91));
		customerNotificationDTO.setNationalityId(new BigDecimal(94));
		customerNotificationDTO.setTitle("OfferMenia");
		customerNotificationDTO.setMessage("For Currency India Offer Today");
		customerNotificationDTO.setNotificationDate(new Date());

		AmxApiResponse<Object, Object> response = client.save(customerNotificationDTO);
		assertNotNull("Response is null", response);

	}

}
