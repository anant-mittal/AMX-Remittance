package com.amx.jax.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotNull;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.fx.FcSaleOrderClient;
import com.amx.jax.model.response.CurrencyMasterDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FxOrderDeliveryClientTest  extends AbstractClientTest {
	
	@Autowired
	FcSaleOrderClient fxOrderClient;
	
	/*@Test
	public void currencyList(){
		setDefaults();
		AmxApiResponse<CurrencyMasterDTO, Object> response = null;
		response = fxOrderClient.getFcCurrencyList(); 
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	
	}*/

}
