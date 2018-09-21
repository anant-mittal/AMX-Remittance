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
import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.amxlib.model.response.ApiResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxPushNotificationClientTest {

	@Autowired
	MetaClient metaclient;

	@Autowired
	JaxPushNotificationClient client;

	//@Test
	public void testGetPushNotification() throws IOException, ResourceNotFoundException, InvalidInputException {
		ApiResponse<CustomerNotificationDTO> response = null;

		BigDecimal customerId = new BigDecimal("5218");

		response = client.getJaxPushNotification(customerId);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	@Test
	public void saveCustomerNotification() throws ParseException {
		CustomerNotificationDTO customerNotificationDTO = new CustomerNotificationDTO();
		
		//customerNotificationDTO.setCustomerId(new BigDecimal(5218));
		customerNotificationDTO.setCountryId(new BigDecimal(91));
		customerNotificationDTO.setNationalityId(new BigDecimal(94));
		customerNotificationDTO.setTitle("OfferMenia");
		customerNotificationDTO.setMessage("For Currency India Offer Today");
		customerNotificationDTO.setNotificationDate(new Date());
		
		ApiResponse<CustomerNotificationDTO> response = client.saveCustomerPushNotification(customerNotificationDTO);
		assertNotNull("Response is null", response);
		
	}

}
