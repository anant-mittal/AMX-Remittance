package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.api.AmxApiResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountMgmtClientTest extends AbstractTestClient {
	
	private static final Logger LOGGER = Logger.getLogger(DiscountMgmtClientTest.class);
	
	@Autowired
	DiscountMgmtClient disMgmtClient;

	
	@Test
	public void testGetCountryBranch() {
		setDefaults();
		
		AmxApiResponse<CountryBranchDTO,Object> response = null;
		LOGGER.info("In Country Branch Client Test ");
		response = disMgmtClient.getCountryBranchList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
}
