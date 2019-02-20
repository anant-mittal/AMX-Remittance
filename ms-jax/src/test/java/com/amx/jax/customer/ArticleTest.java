package com.amx.jax.customer;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dict.Tenant;
import com.amx.jax.meta.MetaData;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleTest {

	@Autowired
	ArticleDao articleDao;
	@Autowired
	CustomerDao custDao;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	@Before
	public void contextLoads() {
		TenantContextHolder.setCurrent(Tenant.KWT);
		// TenantContext.setCurrentTenant(Tenant.KWT.toString());
		metaData.setLanguageId(new BigDecimal(1));
	}

	@Test
	public void testArticle() {

		List<Map<String, Object>> map = articleDao.getIncomeRangeForCustomer(custDao.getCustById(new BigDecimal(5218)));
		map.get(0);
	}

	@Test
	public void testFingerprintApi() {
		UserFingerprintResponseModel userFingerprintResponseModel = userService.linkDeviceId(new BigDecimal(5218));
		assertNotNull(userFingerprintResponseModel.getPassword());
	}

	@Test(expected = GlobalException.class)
	public void testFingerprintApiWithExcp() {
		UserFingerprintResponseModel userFingerprintResponseModel = userService.linkDeviceId(new BigDecimal(13));
		assertNotNull(userFingerprintResponseModel.getPassword());

	}
}
