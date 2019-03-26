package com.amx.jax;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dao.JaxEmployeeDao;
import com.amx.jax.dict.Tenant;
import com.amx.jax.manager.PromotionManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.scope.TenantContextHolder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxServiceApplication.class)
@TestPropertySource(locations = "classpath:application.properties")

public class PromotionTest {

	@Autowired
	PromotionManager promotionManager;
	@Autowired
	MetaData metaData;
	@Autowired
	JaxEmployeeDao employeeDao;

	@Before
	public void contextLoads() {
		TenantContextHolder.setCurrent(Tenant.KWT2);
		//TenantContext.setCurrentTenant(Tenant.KWT2.toString());
	}

	// @Test
	public void testDocLocDataForCustomerReference() {
		metaData.setCountryBranchId(new BigDecimal(78));
		metaData.setCountryId(new BigDecimal(91));
		promotionManager.promotionWinnerCheck(new BigDecimal(10143), new BigDecimal(2018));
	}

	@Test
	public void testEmployee() {
		String civilId = "284052306594";
		boolean isEmployee = employeeDao.isAmgEmployee(civilId);
		assertTrue("Employee with civil id: " + civilId + " is not employee", isEmployee);
	}
	
	@Test
	public void testNonEmployee() {
		String civilId = "286050903108";
		boolean isEmployee = employeeDao.isAmgEmployee(civilId);
		assertTrue("Employee with civil id: " + civilId + " is employee", !isEmployee);
	}
}
