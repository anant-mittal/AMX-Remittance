package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.meta.ForexOutlookClient;
import com.amx.jax.model.response.fx.CurrencyPairDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForexOutlookClientTest extends AbstractClientTest {

	@Autowired
	ForexOutlookClient client;

	@Test
	public void testCurrencyPair() {

		AmxApiResponse<CurrencyPairDTO, Object> response = null;
		response = client.getCurrencyPairList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
		
	
}
