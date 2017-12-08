package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.BankMasterModel;
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

	@Autowired
	private PipsMasterDao pipsDao;

	@Autowired
	private ExchangeRateDao exchangeRateDao;

	@Autowired
	private MetaData meta;

	@Override
	public String getModelType() {
		return "exchange_rate";
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public ApiResponse getExchangeRatesForOnline(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal amount) {
		ApiResponse response = getBlackApiResponse();
		if (fromCurrency.equals(meta.getDefaultCurrencyId())) {
			List<PipsMaster> pips = pipsDao.getPipsForOnline();
			if (pips == null || pips.isEmpty()) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			List<ExchangeRateApprovalDetModel> allExchangeRates = exchangeRateDao.getExchangeRates(toCurrency);
			BigDecimal onlineExchangeRates = getApplicableExchangeRate(allExchangeRates, pips);
			if (onlineExchangeRates == null) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			ExchangeRateResponseModel outputModel = new ExchangeRateResponseModel();
			outputModel.setRate(onlineExchangeRates);
			response.getData().getValues().add(outputModel);
			response.getData().setType(outputModel.getModelType());
		}
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	// logic acc. to VW_EX_TRATE
	private BigDecimal getApplicableExchangeRate(List<ExchangeRateApprovalDetModel> allExchangeRates,
			List<PipsMaster> pips) {
		BigDecimal output = null;
		Map<BigDecimal, ExchangeRateApprovalDetModel> map = new HashMap<>();
		if (allExchangeRates != null && !allExchangeRates.isEmpty()) {
			for (ExchangeRateApprovalDetModel rate : allExchangeRates) {
				map.put(rate.getBankId(), rate);
			}
			BigDecimal minServiceId = null;
			for (PipsMaster pip : pips) {
				BankMasterModel bankMaster = pip.getBankMaster();
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
			return new BigDecimal(1).divide(output);
		}
		return output;
	}

}
