package com.amx.jax.meta;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.JaxChannel;
import com.amx.jax.constant.Country;
import com.amx.jax.services.AbstractServiceFactory;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaData implements IMetaData {

	private BigDecimal countryId;

	private BigDecimal languageId;

	/**
	 * This is default language id from countryId
	 */
	private BigDecimal defaultLanguageId;

	private BigDecimal companyId;

	/**
	 * This is default currency id from countryId
	 */
	private BigDecimal defaultCurrencyId;

	private JaxChannel channel;

	/**
	 * This returns service factory object depending on site country
	 */
	@Override
	public AbstractServiceFactory getServiceFactory() {

		return null;
	}

	public Country getCountry() {
		Country country = Country.countryIdToCountryMap.get(countryId.intValue());
		return country;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public JaxChannel getChannel() {
		return channel;
	}

	public void setChannel(JaxChannel channel) {
		this.channel = channel;
	}

	public BigDecimal getDefaultLanguageId() {
		return defaultLanguageId;
	}

	public void setDefaultLanguageId(BigDecimal defaultLanguageId) {
		this.defaultLanguageId = defaultLanguageId;
	}

	public BigDecimal getDefaultCurrencyId() {
		return defaultCurrencyId;
	}

	public void setDefaultCurrencyId(BigDecimal defaultCurrencyId) {
		this.defaultCurrencyId = defaultCurrencyId;
	}
}
