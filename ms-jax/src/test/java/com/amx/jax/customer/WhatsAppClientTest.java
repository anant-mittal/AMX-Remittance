package com.amx.jax.customer;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.WAMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WhatsAppClientTest {

	@Autowired
	WhatsAppClient client;
	
	@Test
	public void sendOtp(){
		WAMessage msg = new WAMessage();
		msg.setTo(Arrays.asList("917506900840"));
		msg.setMessage("hiosifo");
		client.send(msg);
	}
}
