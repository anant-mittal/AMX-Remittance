package com.amx.jax.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.amx.jax.AppConstants;

@Scope(value = AppConstants.Scopes.TENANT, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Retention(RetentionPolicy.RUNTIME)
public @interface TenantScoped {
}