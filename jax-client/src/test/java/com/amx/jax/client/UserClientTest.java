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
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.amxlib.model.JaxMetaInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserClientTest {

	@Autowired
	private JaxMetaInfo jaxMetaInfo;
	
	@Autowired
	UserClient client;
	
	@Test
	public void getMyProfileInfo() throws IOException, ResourceNotFoundException, InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<CustomerDto> response = null;
		response = client.getMyProfileInfo();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
}
