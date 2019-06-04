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

	public JaxAuthMeta getJaxAuthMeta() {
		String contxtId = AppContextUtil.getContextId();
		if (ArgUtil.isEmpty(contxtId)) {
			contxtId = AppContextUtil.getTraceId();
			AppContextUtil.setContextId(contxtId);
		}

		JaxAuthMeta x = this.getOrDefault(contxtId, null);
		if (ArgUtil.isEmpty(x)) {
			x = new JaxAuthMeta();
			x.setId(contxtId);
		}
		return x;
	}

	public void saveJaxAuthMeta(JaxAuthMeta meta) {
		this.put(meta.getId(), meta);
	}

	@Override
	public JaxAuthMeta getDefault() {
		return new JaxAuthMeta(AppContextUtil.getContextId());
	}
	
	
}
