package com.amx.jax.scheduler.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.RateAlertClient;
import com.amx.jax.scheduler.ratealert.RateAlertData;

import static com.amx.jax.scheduler.ratealert.RateAlertConfig.RATE_ALERT_DATA;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.scope.Tenant;

public class RateAlertTask implements Runnable {

	@Autowired
	ExchangeRateClient exchangeRateClient;

	@Autowired
	MetaClient metaClient;

	@Autowired
	RateAlertClient rateAlertClient;

	@Autowired
	JaxMetaInfo jaxMetaInfo;

	Tenant tenant;

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void run() {
		setMetaInfo();
		initializeRateAlertData();
		executeTask();
	}

	private void executeTask() {
		RateAlertData data = RATE_ALERT_DATA.get(tenant);
		Map<CurrencyMasterDTO, List<BankMasterDTO>> oldExchangeRates = data.getExchangeRates();
		loadExchangeRates(data);
		Map<CurrencyMasterDTO, List<BankMasterDTO>> newExchangeRates = data.getExchangeRates();
		List<CurrencyMasterDTO> currencyList = data.getForeignCurrencyList();
		for(CurrencyMasterDTO currency: currencyList) {
			
		}
	}

	private void setMetaInfo() {
		jaxMetaInfo.setCountryId(tenant.getBDCode());
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setTenant(tenant);
	}

	/**
	 * Initializes the rate alert task
	 */
	public void initializeRateAlertData() {

		RateAlertData data = RATE_ALERT_DATA.get(tenant);
		if (data == null) {
			logger.info("Initializing rate alert data");
			data = new RateAlertData();
			data.setForeignCurrencyList(metaClient.getAllOnlineCurrency().getResults());
			CurrencyMasterDTO domCurrency = metaClient.getCurrencyByCountryId(tenant.getBDCode()).getResult();
			data.setDomesticCurrency(domCurrency);
			loadExchangeRates(data);
			data.setRateAlerts(rateAlertClient.getAllRateAlert().getResults());
			RATE_ALERT_DATA.put(tenant, data);
			logger.info("Initialized rate alert data");
		}
	}

	public void loadExchangeRates(RateAlertData data) {
		Map<CurrencyMasterDTO, List<BankMasterDTO>> exchangeRates = new HashMap<>();

		List<CurrencyMasterDTO> forCurrencyList = data.getForeignCurrencyList();
		for (CurrencyMasterDTO currency : forCurrencyList) {
			ApiResponse<ExchangeRateResponseModel> response = exchangeRateClient.getExchangeRate(
					data.getDomesticCurrency().getCurrencyId(), currency.getCurrencyId(), new BigDecimal(1), null);
			if (response != null && response.getResult() != null && response.getResult().getBankWiseRates() != null) {
				exchangeRates.put(currency, response.getResult().getBankWiseRates());
			}
		}
		data.setExchangeRates(exchangeRates);
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
}
