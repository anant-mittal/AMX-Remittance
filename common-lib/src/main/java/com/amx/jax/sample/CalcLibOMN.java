package com.amx.jax.sample;

import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantSpecific;

@Component
@TenantSpecific({ Tenant.OMN, Tenant.KWT2 })
public class CalcLibOMN implements CalcLib {

	@Override
	public String getRSName() {
		return "TenantKwt_D is Here";
	}

}
