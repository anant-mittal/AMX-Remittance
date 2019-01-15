package com.amx.jax;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Tenant;
import com.amx.jax.exrateservice.service.JaxDynamicPriceService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.scope.TenantContextHolder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PricerTest {

	@Autowired
	JaxDynamicPriceService jaxDynamicPriceService;

	@Test
	public void testGetPrice() {
		setDefaults();
		BigDecimal fromCurrency = BigDecimal.ONE;
		BigDecimal toCurrency = new BigDecimal(4);
		ExchangeRateResponseModel result = jaxDynamicPriceService.getExchangeRatese(fromCurrency, toCurrency,
				BigDecimal.ONE);
		assertNotNull(result);

	}

	@Autowired
	protected MetaData jaxMetaInfo;

	protected void setDefaults() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setEmployeeId(new BigDecimal(421));
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		TenantContextHolder.setCurrent(Tenant.KWT);
		jaxMetaInfo.setReferrer("DEV-TESTING");
	}
}
