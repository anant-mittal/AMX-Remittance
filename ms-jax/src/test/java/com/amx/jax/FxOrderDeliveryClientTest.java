package com.amx.jax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.fx.FcSaleOrderClient;
import com.amx.jax.client.fx.FxOrderDeliveryClient;
import com.amx.jax.dict.Tenant;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.services.FcSaleDeliveryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FxOrderDeliveryClientTest {

	@Autowired
	FcSaleOrderClient fxOrderClient;

	@Autowired
	FxOrderDeliveryClient fxOrderDeliveryClient;

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;
	@Autowired
	MetaData metaData;
	@Autowired
	FcSaleDeliveryService fcSaleDeliveryService;

	protected void setDefaults() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setEmployeeId(new BigDecimal(421));
		jaxMetaInfo.setTenant(Tenant.KWT);
		TenantContextHolder.setCurrent(Tenant.KWT);
		jaxMetaInfo.setReferrer("DEV-TESTING");
	}

	protected void setJaxMetaData() {
		metaData.setCountryId(new BigDecimal(91));
		metaData.setCompanyId(new BigDecimal(1));
		metaData.setLanguageId(new BigDecimal(1));
		metaData.setCountryBranchId(new BigDecimal(78));
		metaData.setCustomerId(new BigDecimal(5218));
		metaData.setEmployeeId(new BigDecimal(421));
		metaData.setTenant(Tenant.KWT);
		TenantContextHolder.setCurrent(Tenant.KWT);
		metaData.setReferrer("DEV-TESTING");
	}

	// @Test
	public void testMarkAck() {
		setDefaults();
		AmxApiResponse<BoolRespModel, Object> result = fxOrderDeliveryClient.verifyOtp(new BigDecimal(5), "1234");
		assertTrue(result.getResult().isSuccess());
	}

	@Test
	public void testListHistoricalOrders() {
		setJaxMetaData();
		List<FxDeliveryDetailDto> historicalOrders = fcSaleDeliveryService.listHistoricalOrders();
		assertNotNull(historicalOrders);
	}

}
