/**
 * 
 */
package com.amx.jax.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;

/**
 * @author Prashant
 *
 */
@ActiveProfiles("test")
public abstract class AbstractTestClient {

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;
	

	protected void setDefaults() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(184466));
		jaxMetaInfo.setTenant(Tenant.KWT);
		TenantContextHolder.setCurrent(Tenant.KWT);
		jaxMetaInfo.setReferrer("DEV-TESTING");
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		
		
	}
	
	protected void setBahrainDefaults() {
		jaxMetaInfo.setCountryId(new BigDecimal(104));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCustomerId(new BigDecimal(96690));
		jaxMetaInfo.setTenant(Tenant.BHR);
		TenantContextHolder.setCurrent(Tenant.BHR);
		jaxMetaInfo.setReferrer("DEV-TESTING");
	}
}
