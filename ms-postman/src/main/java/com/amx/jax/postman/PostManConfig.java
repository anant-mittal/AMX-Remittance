package com.amx.jax.postman;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.scope.TenantProperties;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.utils.ArgUtil;

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

	@TenantValue("${slack.channel.genral.tnt}")
	private String channelGenral;

	@Value("${app.telegram.enabled}")
	private boolean telegramEnabled;

	@Autowired
	TenantProperties tenantProperties;
	private Map<Channel, String> channelMap = null;

	public String getChannelCode(Channel channel) {
		if (channelMap == null) {
			channelMap = new HashMap<Channel, String>();
			for (Channel eachChannel : Channel.values()) {
				String propValue = tenantProperties.getProperties()
						.getProperty("slack.channel." + eachChannel.toString().toLowerCase() + ".tnt");
				if (ArgUtil.isEmpty(propValue)) {
					channelMap.put(eachChannel, eachChannel.getCode());
				} else {
					channelMap.put(eachChannel, propValue);
				}
			}
		}
		return channelMap.getOrDefault(channel, channelGenral);
	}

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
	 * @param file the file
	 * @return the local
	 */
	public Locale getLocal(File file) {
		if (file != null && file.getLang() != null) {
			return new Locale(file.getLang().getCode());
		}
		if (tenantLang != null) {
			new Locale(tenantLang.getCode());
		}
		return new Locale(Language.EN.getCode());
	}

	public Locale getLocal(Message msg) {
		if (msg == null || msg.getLang() == null) {
			return new Locale(tenantLang.getCode());
		}
		return new Locale(msg.getLang().getCode());
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

	@Value("${spring.mail.retry.count}")
	private Integer emailRetryCount;

	@Value("${spring.mail.retry.batch}")
	private Integer emailRetryBatch;

	@Value("${spring.mail.retry.push}")
	private Integer emailRetryPush;

	@Value("${spring.mail.retry.poll}")
	private Integer emailRetryPoll;

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

	public Integer getEmailRetryCount() {
		return emailRetryCount;
	}

	public void setEmailRetryCount(Integer emailRetryCount) {
		this.emailRetryCount = emailRetryCount;
	}

	public Integer getEmailRetryBatch() {
		return emailRetryBatch;
	}

	public void setEmailRetryBatch(Integer emailRetryBatch) {
		this.emailRetryBatch = emailRetryBatch;
	}

	public Integer getEmailRetryPush() {
		return emailRetryPush;
	}

	public void setEmailRetryPush(Integer emailRetryPush) {
		this.emailRetryPush = emailRetryPush;
	}

	public Integer getEmailRetryPoll() {
		return emailRetryPoll;
	}

	public void setEmailRetryPoll(Integer emailRetryPoll) {
		this.emailRetryPoll = emailRetryPoll;
	}

	public boolean isTelegramEnabled() {
		return telegramEnabled;
	}

}
