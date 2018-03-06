package com.amx.jax.scheduler.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.RateAlertClient;
import com.amx.jax.scheduler.ratealert.RateAlertData;
import com.amx.jax.scheduler.ratealert.RateAlertNotificationDTO;
import com.amx.jax.scheduler.service.NotificationService;

import static com.amx.jax.scheduler.ratealert.RateAlertConfig.RATE_ALERT_DATA;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amx.jax.scope.Tenant;
import com.amx.jax.scope.TenantContextHolder;

public class RateAlertTask implements Runnable {

	@Autowired
	ExchangeRateClient exchangeRateClient;

	@Autowired
	MetaClient metaClient;

	@Autowired
	RateAlertClient rateAlertClient;

	@Autowired
	JaxMetaInfo jaxMetaInfo;

	@Autowired
	NotificationService notificationService;

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
		List<RateAlertDTO> modifiedRateAlerts = getModifiedRateAlerts(data);
		Map<CurrencyMasterDTO, List<BankMasterDTO>> modifiedRates = getModifiedRates(data);
		List<RateAlertNotificationDTO> applicableRateAlerts = getApplicableRateAlerts(modifiedRates,
				data.getRateAlerts());

		applicableRateAlerts.addAll(getApplicableRateAlerts(data.getExchangeRates(), modifiedRateAlerts));

		sendNotifications(applicableRateAlerts);
	}

	private List<RateAlertDTO> getModifiedRateAlerts(RateAlertData data) {

		List<RateAlertDTO> oldAlerts = new ArrayList<>();
		data.getRateAlerts().forEach(i -> {
			try {
				oldAlerts.add((RateAlertDTO) i.clone());
			} catch (CloneNotSupportedException e) {
			}
		});
		loadRateAlerts(data);
		List<RateAlertDTO> newAlerts = data.getRateAlerts();
		List<RateAlertDTO> modifiedAlerts = new ArrayList<>();
		newAlerts.forEach(ra -> {
			if (!oldAlerts.contains(ra)) {
				modifiedAlerts.add(ra);
			}
		});
		return modifiedAlerts;
	}

	private void sendNotifications(List<RateAlertNotificationDTO> applicableRateAlerts) {

		int batchSize = 10;
		for (int i = 0; i < applicableRateAlerts.size(); i += batchSize) {
			int endIndex = (i + batchSize);
			if (endIndex >= applicableRateAlerts.size()) {
				endIndex = applicableRateAlerts.size() - 1;
			}
			notificationService.sendBatchNotification(applicableRateAlerts.subList(i, endIndex + 1));
		}

	}

	/**
	 * @return based on modified rates, this method will trigger notifications
	 */
	private List<RateAlertNotificationDTO> getApplicableRateAlerts(
			Map<CurrencyMasterDTO, List<BankMasterDTO>> modifiedRates, List<RateAlertDTO> allRateAlerts) {
		List<RateAlertNotificationDTO> output = new ArrayList<>();
		for (Entry<CurrencyMasterDTO, List<BankMasterDTO>> entry : modifiedRates.entrySet()) {
			List<BankMasterDTO> rates = entry.getValue();
			CurrencyMasterDTO forCurrencyKey = entry.getKey();
			for (RateAlertDTO rateAlert : allRateAlerts) {
				try {
					BigDecimal forCurrencyId = rateAlert.getForeignCurrencyId();
					if (forCurrencyId.equals(forCurrencyKey.getCurrencyId())) {
						for (BankMasterDTO rate : rates) {
							if (isRateApplicable(rateAlert, rate, forCurrencyKey)) {
								RateAlertNotificationDTO dto = new RateAlertNotificationDTO(rateAlert,
										rate.getExRateBreakup().getRate());
								output.add(dto);
								break;
							}
						}

					}
				} catch (Exception e) {
					logger.error("error in getApplicableRateAlerts", e);
				}
			}
		}
		return output;
	}

	private boolean isRateApplicable(RateAlertDTO rateAlert, BankMasterDTO rate, CurrencyMasterDTO currencyMaster) {

		BigDecimal marketAmount = rate.getExRateBreakup().getRate().multiply(rateAlert.getPayAmount());
		BigDecimal alertAmount = rateAlert.getReceiveAmount();
		marketAmount= marketAmount.setScale(currencyMaster.getDecinalNumber().intValue(), RoundingMode.HALF_UP);
		alertAmount = alertAmount.setScale(currencyMaster.getDecinalNumber().intValue(), RoundingMode.HALF_UP);

		switch (rateAlert.getRule()) {
		case EQUAL:
			if (marketAmount.equals(alertAmount)) {
				return true;
			}
			break;
		case GREATER:
			if (marketAmount.compareTo(alertAmount) > 0) {
				return true;
			}
			break;
		case EQUAL_OR_GREATER:
			if (marketAmount.compareTo(alertAmount) >= 0) {
				return true;
			}
			break;
		case LESS:
			if (marketAmount.compareTo(alertAmount) < 0) {
				return true;
			}
			break;
		case EQUAL_OR_LESS:
			if (marketAmount.compareTo(alertAmount) <= 0) {
				return true;
			}
			break;
		}
		return false;
	}

	private Map<CurrencyMasterDTO, List<BankMasterDTO>> getModifiedRates(RateAlertData data) {
		Map<CurrencyMasterDTO, List<BankMasterDTO>> modifiedRates = new HashMap<>();
		Map<CurrencyMasterDTO, List<BankMasterDTO>> oldExchangeRates = data.getExchangeRates();
		loadExchangeRates(data);
		Map<CurrencyMasterDTO, List<BankMasterDTO>> newExchangeRates = data.getExchangeRates();
		List<CurrencyMasterDTO> currencyList = data.getForeignCurrencyList();
		for (CurrencyMasterDTO currency : currencyList) {
			List<BankMasterDTO> oldRates = oldExchangeRates.get(currency);
			List<BankMasterDTO> newRates = newExchangeRates.get(currency);
			if (oldRates != null) {
				for (BankMasterDTO oldRate : oldRates) {
					for (BankMasterDTO newRate : newRates) {
						if (oldRate.getBankId().equals(newRate.getBankId())) {
							BigDecimal orate = oldRate.getExRateBreakup().getRate();
							BigDecimal nrate = newRate.getExRateBreakup().getRate();
							if (!orate.equals(nrate)) {
								List<BankMasterDTO> rates = modifiedRates.get(currency);
								if (rates != null) {
									rates.add(newRate);
								} else {
									rates = new ArrayList<>();
									rates.add(newRate);
									modifiedRates.put(currency, rates);
								}
							}
						}

					}
				}
			}
		}
		return modifiedRates;
	}

	private void setMetaInfo() {
		// jaxMetaInfo.setCountryId(tenant.getBDCode());
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setTenant(tenant);
		TenantContextHolder.setCurrent(tenant);
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
			loadRateAlerts(data);
			RATE_ALERT_DATA.put(tenant, data);
			logger.info("Initialized rate alert data");
		}
	}

	private void loadRateAlerts(RateAlertData data) {
		data.setRateAlerts(rateAlertClient.getAllRateAlert().getResults());
	}

	public void loadExchangeRates(RateAlertData data) {
		Map<CurrencyMasterDTO, List<BankMasterDTO>> exchangeRates = new HashMap<>();

		List<CurrencyMasterDTO> forCurrencyList = data.getForeignCurrencyList();
		for (CurrencyMasterDTO currency : forCurrencyList) {
			try {
				ApiResponse<ExchangeRateResponseModel> response = exchangeRateClient.getExchangeRate(
						data.getDomesticCurrency().getCurrencyId(), currency.getCurrencyId(), new BigDecimal(1), null);
				if (response != null && response.getResult() != null
						&& response.getResult().getBankWiseRates() != null) {
					exchangeRates.put(currency, response.getResult().getBankWiseRates());
				}
			} catch (Exception e) {
				logger.error("error occured while fetching ex rates for foreign currency:" + currency.getQuoteName());
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
