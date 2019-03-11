/**
 * 
 */
package com.amx.jax;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Tenant;
import com.amx.jax.meta.MetaData;
import com.amx.jax.scope.TenantContextHolder;

/**
 * @author Prashant
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractTest {

	@Autowired
	protected MetaData jaxMetaInfo;

	@Before
	public void setDefaults() {
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
