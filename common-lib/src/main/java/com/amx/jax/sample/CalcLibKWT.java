package com.amx.jax.sample;

import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantSpecific;

@Component
@TenantSpecific(Tenant.KWT)
public class CalcLibKWT implements CalcLib {

	@Override
	public String getRSName() {
		return "TenantKwt is Here";
	}

}
