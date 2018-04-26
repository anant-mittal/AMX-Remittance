package com.amx.jax.postman;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;
import com.amx.jax.postman.model.File;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class PostManConfig {

	@TenantValue("${tenant}")
	private String tenant;

	@TenantValue("${tenant.lang}")
	private Language tenantLang;

	public String getTenant() {
		return tenant;
	}

	public Language getTenantLang() {
		return tenantLang;
	}

	public Locale getLocal(File file) {
		if (file == null || file.getLang() == null) {
			return new Locale(tenantLang.getCode());
		}
		return new Locale(file.getLang().getCode());
	}

}
