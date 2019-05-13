package com.amx.jax.ui.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.CustomerRegistrationClient;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.JaxFieldClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.PlaceOrderClient;
import com.amx.jax.client.RateAlertClient;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.UserClient.AppType;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.logger.AuditDetailProvider;
import com.amx.jax.model.UserDevice;
import com.amx.jax.rest.AppRequestContextInFilter;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

/**
 * The Class JaxService.
 */
@Component
public class JaxService implements IMetaRequestOutFilter<JaxMetaInfo>, AppRequestContextInFilter, AuditDetailProvider {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserClient userclient;

	@Autowired
	private RemitClient remitClient;

	@Autowired
	private ExchangeRateClient xRateClient;

	@Autowired
	private BeneClient beneClient;

	@Autowired
	private RateAlertClient rateAlertClient;

	@Autowired
	private JaxFieldClient jaxFieldClient;

	@Autowired
	PlaceOrderClient placeOrderClient;

	@Autowired
	CustomerRegistrationClient customerRegistrationClient;

	/**
	 * Gets the jax field client.
	 *
	 * @return the jax field client
	 */
	public JaxFieldClient getJaxFieldClient() {
		return jaxFieldClient;
	}

	/**
	 * Gets the rate alert client.
	 *
	 * @return the rate alert client
	 */
	public RateAlertClient getRateAlertClient() {
		return rateAlertClient;
	}

	/**
	 * Gets the bene client.
	 *
	 * @return the bene client
	 */
	public BeneClient getBeneClient() {
		return beneClient;
	}

	/**
	 * Gets the x rate client.
	 *
	 * @return the x rate client
	 */
	public ExchangeRateClient getxRateClient() {
		return xRateClient;
	}

	/**
	 * Gets the remit client.
	 *
	 * @return the remit client
	 */
	public RemitClient getRemitClient() {
		return remitClient;
	}

	/**
	 * Gets the userclient.
	 *
	 * @return the userclient
	 */
	public UserClient getUserclient() {
		return userclient;
	}

	/**
	 * Gets the meta client.
	 *
	 * @return the meta client
	 */
	public MetaClient getMetaClient() {
		return metaClient;
	}

	/**
	 * Gets the cust reg client.
	 *
	 * @return the cust reg client
	 */
	public CustomerRegistrationClient getCustRegClient() {
		return customerRegistrationClient;
	}

	/**
	 * Gets the place order client.
	 *
	 * @return the place order client
	 */
	public PlaceOrderClient getPlaceOrderClient() {
		return placeOrderClient;
	}

	/** The meta client. */
	@Autowired
	private MetaClient metaClient;

	@Autowired
	protected JaxMetaInfo jaxMetaInfoBean;

	@Autowired
	protected AmxConfig amxConfig;

	private void populateCommon(JaxMetaInfo jaxMetaInfo) {
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());
		jaxMetaInfo.setCountryId(amxConfig.getDefaultCountryId());
		jaxMetaInfo.setCompanyId(amxConfig.getDefaultCompanyId());
		jaxMetaInfo.setLanguageId(amxConfig.getDefaultLanguageId());
		jaxMetaInfo.setCountryBranchId(amxConfig.getDefaultBranchId());
	}

	private void populateUser(JaxMetaInfo jaxMetaInfo, BigDecimal customerId) {
		jaxMetaInfo.setReferrer(sessionService.getUserSession().getReferrer());
		jaxMetaInfo.setDeviceId(sessionService.getAppDevice().getUserDevice().getFingerprint());
		jaxMetaInfo.setDeviceIp(sessionService.getAppDevice().getUserDevice().getIp());
		jaxMetaInfo.setDeviceType(ArgUtil.parseAsString(sessionService.getAppDevice().getUserDevice().getType()));
		jaxMetaInfo.setAppType(ArgUtil.parseAsString(sessionService.getAppDevice().getUserDevice().getAppType()));

		jaxMetaInfo.setCustomerId(customerId);
	}

	private BigDecimal getCustomerId() {
		if (sessionService.getUserSession().getCustomerModel() != null) {
			return sessionService.getUserSession().getCustomerModel().getCustomerId();
		} else if (sessionService.getGuestSession().getCustomerModel() != null) {
			return sessionService.getGuestSession().getCustomerModel().getCustomerId();
		}
		return null;
	}

	/**
	 * Sets the defaults.
	 *
	 * @param customerId the customer id
	 * @return the jax service
	 */
	public JaxService setDefaults(BigDecimal customerId) {
		populateCommon(jaxMetaInfoBean);
		populateUser(jaxMetaInfoBean, customerId);
		return this;
	}

	/**
	 * Sets the defaults.
	 *
	 * @return the jax service
	 */
	public JaxService setDefaults() {
		return this.setDefaults(getCustomerId());
	}

	@Override
	public void outFilter(JaxMetaInfo requestMeta) {
		populateCommon(requestMeta);
		populateUser(requestMeta, getCustomerId());
	}

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();
		outFilter(jaxMetaInfo);
		return jaxMetaInfo;
	}

	@Override
	public void appRequestContextInFilter() {
		UserDevice userDevice = commonHttpRequest.getUserDevice();
		UserDeviceClient userClient = AppContextUtil.getUserClient();
		userClient.setChannel(Channel.ONLINE);
		if (AppType.ANDROID.equals(userClient.getAppType())) {
			userClient.setClientType(ClientType.ONLINE_AND);
		} else if (AppType.IOS.equals(userClient.getAppType())) {
			userClient.setClientType(ClientType.ONLINE_IOS);
		} else if (AppType.WEB.equals(userClient.getAppType())) {
			userClient.setClientType(ClientType.ONLINE_WEB);
		} else {
			userClient.setClientType(ClientType.UNKNOWN);
		}
	}

	@Override
	public AuditActor getActor() {
		return new AuditActor(sessionService.getUserSession().isValid() ? AuditActor.ActorType.CSTMR
				: AuditActor.ActorType.GUEST, sessionService.getUserSession().getUserid());
	}

}
