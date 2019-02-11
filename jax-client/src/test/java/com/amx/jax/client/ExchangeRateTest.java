package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.model.response.CurrencyMasterDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeRateTest {

	@Autowired
	ExchangeRateClient client;
	
	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Test
	public void testGetExchangeRate() throws IOException, ResourceNotFoundException, InvalidInputException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<ExchangeRateResponseModel> response = null;
		response = client.getExchangeRate(new BigDecimal(1), new BigDecimal(4), new BigDecimal(1), null, null);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	//@Test
	public void testgetOnlineCurrency() throws IOException, ResourceNotFoundException, InvalidInputException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		AmxApiResponse<CurrencyMasterDTO,Object> response = null;
		response = metaclient.getAllOnlineCurrency();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	//@Test
	public void testMinMaxExchangeRate() throws IOException, ResourceNotFoundException, InvalidInputException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		ApiResponse<MinMaxExRateDTO> response = null;
		response = client.getMinMaxExchangeRate();
		assertNotNull("Response is null", response);
		List<MinMaxExRateDTO> results = response.getResults();
		for (MinMaxExRateDTO dto : results) {
			assertTrue(dto.getMinExrate().intValue() <= dto.getMaxExrate().intValue());
		}
		assertNotNull(response.getResult().getModelType());

	}
}
