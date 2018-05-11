package com.amx.jax.def;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.cache.annotation.Cacheable;

import com.amx.jax.AppConstants;

@Cacheable(value = AppConstants.CACHE_NAME_HTTP, keyGenerator = TenantMethodKey.KEY)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheBoxEnabled {

}