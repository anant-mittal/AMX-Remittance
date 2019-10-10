package com.amx.jax.meta;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Country;
import com.amx.jax.dict.Tenant;
import com.amx.jax.services.AbstractServiceFactory;
import com.amx.utils.ArgUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaData implements IMetaData {
	
	private static final BigDecimal MAX_LANG_ID = new BigDecimal(2);

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
	
	private BigDecimal customerId;
	
	private BigDecimal countryBranchId;
	
	private Tenant tenant = Tenant.DEFAULT;
	
	private String deviceIp;
	private String deviceId;
    private String referrer;
    private String deviceType;
    private String appType;
    private BigDecimal employeeId;
	private BigDecimal terminalId;
    
    private Boolean annualIncomeForceUpdate;
	public Boolean getAnnualIncomeForceUpdate() {
		return annualIncomeForceUpdate;
	}

	public void setAnnualIncomeForceUpdate(Boolean annualIncomeForceUpdate) {
		this.annualIncomeForceUpdate = annualIncomeForceUpdate;
	}

	/**
	 * This returns service factory object depending on site country
	 */
	@Override
	public AbstractServiceFactory getServiceFactory() {

		return null;
	}

	public Tenant getCountry() {
		return Tenant.fromCountryId(countryId.intValue());
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
		if(ArgUtil.isEmpty(languageId) || (MAX_LANG_ID.compareTo(languageId) < 0)) {
			return BigDecimal.ONE;
		}
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

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public BigDecimal getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(BigDecimal terminalId) {
		this.terminalId = terminalId;
	}

}
