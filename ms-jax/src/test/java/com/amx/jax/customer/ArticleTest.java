package com.amx.jax.customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dict.Tenant;
import com.amx.jax.meta.MetaData;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.userservice.dao.CustomerDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleTest {

	@Autowired
	ArticleDao articleDao;
	@Autowired
	CustomerDao custDao;
	@Autowired
	MetaData metaData;

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
}
