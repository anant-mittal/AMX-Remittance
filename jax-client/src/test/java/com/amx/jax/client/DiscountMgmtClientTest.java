package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountMgmtClientTest extends AbstractTestClient {
	
	private static final Logger LOGGER = Logger.getLogger(DiscountMgmtClientTest.class);
	
	@Autowired
	DiscountMgmtClient disMgmtClient;

	
	//@Test
	public void testGetCountryBranch() {
		setDefaults();
		
		AmxApiResponse<CountryBranchDTO,Object> response = null;
		LOGGER.info("In Country Branch Client Test ");
		response = disMgmtClient.getCountryBranchList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	@Test
	public void testFetchDiscountRates() {
		setDefaults();
		
		AmxApiResponse<PricingResponseDTO,Object> response = null;
		LOGGER.info("In Fetch Discount Client Test ");
		PricingRequestDTO pricingRequestDTO = new PricingRequestDTO();
		
		pricingRequestDTO.setChannel(Channel.BRANCH);
		pricingRequestDTO.setCountryBranchId(new BigDecimal(166));
		//pricingRequestDTO.setCustomerId(new BigDecimal(5281));
		//pricingRequestDTO.setForeignAmount(new BigDecimal(10));
		pricingRequestDTO.setForeignCountryId(new BigDecimal(94));
		pricingRequestDTO.setForeignCurrencyId(new BigDecimal(4));
		pricingRequestDTO.setLocalAmount(new BigDecimal(10));
		pricingRequestDTO.setLocalCountryId(new BigDecimal(91));
		pricingRequestDTO.setLocalCurrencyId(new BigDecimal(1));
		pricingRequestDTO.setPricingLevel(PRICE_BY.ROUTING_BANK);
				
		List<BigDecimal> routingBankIds = new ArrayList<BigDecimal>();
		routingBankIds.add(new BigDecimal(2227));
		pricingRequestDTO.setRoutingBankIds(routingBankIds);;
		
		pricingRequestDTO.setServiceIndicatorId(new BigDecimal(101));
		
		response = disMgmtClient.fetchDiscountedRates(pricingRequestDTO);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
}
