package com.amx.jax;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

@Component
public class JaxAuthCache extends CacheBox<JaxAuthMeta> {
	public JaxAuthMeta getAuthMeta(BigDecimal customerId) {
		return this.get(ArgUtil.parseAsString(customerId));
	}
}
