package com.amx.jax.postman;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;
import com.amx.jax.dict.Project;
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

	/** The mail from. */
	@TenantValue("${spring.mail.from}")
	private String mailFrom;

	@TenantValue("${spring.mail.from.title}")
	private String mailFromTitle;

	/** The mail host. */
	@TenantValue("${spring.mail.host}")
	private String mailHost;

	/** The mail port. */
	@TenantValue("${spring.mail.port}")
	private int mailPort;

	/** The mail username. */
	@TenantValue("${spring.mail.username}")
	private String mailUsername;

	/** The mail password. */
	@TenantValue("${spring.mail.password}")
	private String mailPassword;

	/** The mail smtp auth. */
	@TenantValue("${spring.mail.properties.mail.smtp.auth}")
	private boolean mailSmtpAuth;

	/** The mail smtp tls. */
	@TenantValue("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean mailSmtpTls;

	/** The mail protocol. */
	@TenantValue("${spring.mail.protocol}")
	private String mailProtocol;

	/** The mail default encoding. */
	@TenantValue("${spring.mail.defaultEncoding}")
	private String mailDefaultEncoding;

	/** The default sender. */
	@Value("${spring.mail.from}")
	private String mailDefaultSender;

	public String getMailFrom() {
		return mailFrom;
	}

	public String getMailFromTitle() {
		return mailFromTitle;
	}

	public String getMailHost() {
		return mailHost;
	}

	public int getMailPort() {
		return mailPort;
	}

	public String getMailUsername() {
		return mailUsername;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public boolean isMailSmtpAuth() {
		return mailSmtpAuth;
	}

	public boolean isMailSmtpTls() {
		return mailSmtpTls;
	}

	public String getMailProtocol() {
		return mailProtocol;
	}

	public String getMailDefaultEncoding() {
		return mailDefaultEncoding;
	}

	public String getMailDefaultSender() {
		return mailDefaultSender;
	}

}
