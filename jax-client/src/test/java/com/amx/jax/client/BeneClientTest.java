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
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeneClientTest extends AbstractTestClient{

	@Autowired
	BeneClient client;
	
	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	//@Test
	public void testdefaultBeneficiary() throws IOException, ResourceNotFoundException, InvalidInputException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		BigDecimal beneId = new BigDecimal(88041);
		ApiResponse<RemittancePageDto> response = null;
		response = client.defaultBeneficiary(beneId,null);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	@Test
	public void testgetBeneficiaryRelations() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<BeneRelationsDescriptionDto> response = null;
		response = client.getBeneficiaryRelations();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

}
