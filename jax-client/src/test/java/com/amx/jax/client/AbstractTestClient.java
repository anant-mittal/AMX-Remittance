/**
 * 
 */
package com.amx.jax.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.dict.Tenant;

/**
 * @author Prashant
 *
 */
public abstract class AbstractTestClient {

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	protected void setDefaults() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setTenant(Tenant.KWT);
	}
}
