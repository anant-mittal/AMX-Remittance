package com.amx.jax;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Tenant;
import com.amx.jax.exrateservice.service.JaxDynamicPriceService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.scope.TenantContextHolder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PricerTest {

	@Autowired
	JaxDynamicPriceService jaxDynamicPriceService;

	@Test
	public void testGetPrice() {
		/*setDefaults();
		BigDecimal fromCurrency = BigDecimal.ONE;
		BigDecimal toCurrency = new BigDecimal(4);
		ExchangeRateResponseModel result = jaxDynamicPriceService.getExchangeRatesWithDiscount(fromCurrency, toCurrency,
				BigDecimal.ONE, null, new BigDecimal(94), null);
		assertNotNull(result);*/

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
