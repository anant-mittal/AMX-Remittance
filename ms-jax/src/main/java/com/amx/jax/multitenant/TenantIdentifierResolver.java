package com.amx.jax.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver  implements CurrentTenantIdentifierResolver {

	 @Override
	    public String resolveCurrentTenantIdentifier() {
	        String tenantId = TenantContext.getCurrentTenant();
	        if (tenantId != null) {
	            return tenantId;
	        }
	        return "KWT";
	    }
	    @Override
	    public boolean validateExistingCurrentSessions() {
	        return true;
	    }
}
