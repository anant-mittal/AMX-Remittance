package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.response.ApiResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetaClientTest extends AbstractTestClient {

	
	@Autowired
	MetaClient metaclient;

	@Test
	public void testdefaultBeneficiary() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<OnlineConfigurationDto> response = null;
		response = metaclient.getOnlineConfig("O");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}
	
	@Test
	public void testgetBankListForCountry() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<BankMasterDTO> response = null;
		response = metaclient.getBankListForCountry(new BigDecimal(91));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getBankId());
	}
	

}
