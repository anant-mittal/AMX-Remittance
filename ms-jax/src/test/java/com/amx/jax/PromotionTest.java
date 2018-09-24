package com.amx.jax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dict.Tenant;
import com.amx.jax.manager.PromotionManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.multitenant.TenantContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxServiceApplication.class)
@TestPropertySource(locations = "classpath:application.properties")

public class PromotionTest {

	@Autowired
	PromotionManager promotionManager;
	@Autowired
	MetaData metaData;

	@Before
	public void contextLoads() {
		TenantContext.setCurrentTenant(Tenant.KWT2.toString());
	}

	@Test
	public void testDocLocDataForCustomerReference() {
		metaData.setCountryBranchId(new BigDecimal(78));
		metaData.setCountryId(new BigDecimal(91));
		promotionManager.promotionWinnerCheck(new BigDecimal(10143), new BigDecimal(2018));
	}
}
