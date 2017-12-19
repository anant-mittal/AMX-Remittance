package com.amx.jax.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Scope(value = "tenant", proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface TenantScoped {

}