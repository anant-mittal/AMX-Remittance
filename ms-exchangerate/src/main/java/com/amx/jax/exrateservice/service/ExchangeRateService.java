package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dao.CurrencyMasterDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.exrateservice.dao.ExchangeRateDao;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.AbstractService;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class ExchangeRateService extends AbstractService {

	private Logger logger = Logger.getLogger(ExchangeRateService.class);

	@Autowired
	private PipsMasterDao pipsDao;

	@Autowired
	private ExchangeRateDao exchangeRateDao;

	@Autowired
	private MetaData meta;

	@Autowired
	private CurrencyMasterDao currencyMasterDao;

	@Override
	public String getModelType() {
		return "exchange_rate";
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public ApiResponse getExchangeRatesForOnline(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal amount,
			BigDecimal bankId) {
		ApiResponse response = getBlackApiResponse();
		if (fromCurrency.equals(meta.getDefaultCurrencyId())) {
			List<PipsMaster> pips = pipsDao.getPipsForOnline();
			if (pips == null || pips.isEmpty()) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			validateExchangeRateInputdata(amount);
			BigDecimal countryBranchId = new BigDecimal(78);
			CurrencyMasterModel toCurrencyMaster = currencyMasterDao.getCurrencyMasterById(toCurrency);
			List<ExchangeRateApprovalDetModel> allExchangeRates = exchangeRateDao.getExchangeRates(toCurrency,
					countryBranchId, toCurrencyMaster.getCountryId());

			BigDecimal equivalentAmount = getApplicableExchangeAmount(allExchangeRates, pips, amount, bankId);
			if (equivalentAmount == null) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			ExchangeRateResponseModel outputModel = new ExchangeRateResponseModel();
			outputModel.setRate(equivalentAmount);
			response.getData().getValues().add(outputModel);
			response.getData().setType(outputModel.getModelType());
		}
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	private void validateExchangeRateInputdata(BigDecimal amount) {
		if (amount.compareTo(new BigDecimal(0)) < 0) {
			throw new GlobalException("No exchange data found", JaxError.INVALID_EXCHANGE_AMOUNT);
		}
	}

	// logic acc. to VW_EX_TRATE
	private BigDecimal getApplicableExchangeAmount(List<ExchangeRateApprovalDetModel> allExchangeRates,
			List<PipsMaster> pips, BigDecimal amount, BigDecimal bankId) {
		BigDecimal output = null;
		Map<BigDecimal, ExchangeRateApprovalDetModel> map = new HashMap<>();
		if (allExchangeRates != null && !allExchangeRates.isEmpty()) {
			for (ExchangeRateApprovalDetModel rate : allExchangeRates) {
				map.put(rate.getBankId(), rate);
			}
			BigDecimal minServiceId = null;
			for (PipsMaster pip : pips) {
				BankMasterModel bankMaster = pip.getBankMaster();
				// match the bankId passed
				if (bankId != null && !bankMaster.getBankId().equals(bankId)) {
					continue;
				}
				ExchangeRateApprovalDetModel value = map.get(bankMaster.getBankId());
				if (value != null && value.getCountryId().equals(pip.getCountryMaster().getCountryId())
						&& value.getCurrencyId().equals(pip.getCurrencyMaster().getCurrencyId())) {
					BigDecimal serviceId = value.getServiceId();
					if (minServiceId == null) {
						minServiceId = value.getServiceId();
						output = value.getSellRateMax().subtract(pip.getPipsNo());
					}
					if (serviceId.compareTo(minServiceId) < 0) {
						minServiceId = serviceId;
						output = value.getSellRateMax().subtract(pip.getPipsNo());
					}
				}
			}
		}
		if (output != null) {
			return amount.divide(output, 10, RoundingMode.HALF_UP);
		}
		return output;
	}

}
