package com.amx.jax.postman;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;
import com.amx.jax.postman.model.File;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

/**
 * The Class PostManConfig.
 */
@TenantScoped
@Component
public class PostManConfig {

	/** The tenant. */
	@TenantValue("${tenant}")
	private String tenant;

	/** The tenant lang. */
	@TenantValue("${tenant.lang}")
	private Language tenantLang;

	/** The exception channel code. */
	@TenantValue("${slack.exception.channel}")
	private String exceptionChannelCode;

	/**
	 * Gets the tenant.
	 *
	 * @return the tenant
	 */
	public String getTenant() {
		return tenant;
	}

	/**
	 * Gets the tenant lang.
	 *
	 * @return the tenant lang
	 */
	public Language getTenantLang() {
		return tenantLang;
	}

	/**
	 * Gets the local.
	 *
	 * @param file
	 *            the file
	 * @return the local
	 */
	public Locale getLocal(File file) {
		if (file == null || file.getLang() == null) {
			return new Locale(tenantLang.getCode());
		}
		return new Locale(file.getLang().getCode());
	}

	/**
	 * Gets the exception channel code.
	 *
	 * @return the exception channel code
	 */
	public String getExceptionChannelCode() {
		return exceptionChannelCode;
	}

}
