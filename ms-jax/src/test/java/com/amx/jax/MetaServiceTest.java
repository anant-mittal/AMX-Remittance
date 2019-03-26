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

import com.amx.jax.dao.DocumentSerialityNumberDao;
import com.amx.jax.dbmodel.meta.DocLocModel;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxServiceApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class MetaServiceTest {

	@Autowired
	DocumentSerialityNumberDao documentSerialityNumberDao;

	@Before
	public void contextLoads() {
		TenantContextHolder.setCurrent(Tenant.KWT);
		//TenantContext.setCurrentTenant(Tenant.KWT.toString());
	}

	@Test
	public void testDocLocDataForCustomerReference() {
		List<DocLocModel> output = documentSerialityNumberDao.getDocLocByDocumentCode(new BigDecimal(8));
		assertNotNull(output);
		assertTrue(output.size() > 0);
	}
}
