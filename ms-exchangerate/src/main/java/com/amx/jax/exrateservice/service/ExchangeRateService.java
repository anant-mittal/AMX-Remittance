package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
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
import com.amx.jax.service.BankMetaService;
import com.amx.jax.services.AbstractService;
import com.amx.jax.util.Util;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class ExchangeRateService extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PipsMasterDao pipsDao;

	@Autowired
	BankMetaService bankMasterService;

	@Autowired
	private ExchangeRateDao exchangeRateDao;

	@Autowired
	private MetaData meta;

	@Autowired
	Util util;

	@Autowired
	private CurrencyMasterDao currencyMasterDao;

	@Override
	public String getModelType() {
		return "exchange_rate";
	}

	@Override
	public Class<?> getModelClass() {
		return null;
	}

	public ApiResponse getExchangeRatesForOnline(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal bankId) {
		logger.info("In getExchangeRatesForOnline, parames- " + fromCurrency + " toCurrency " + toCurrency + " amount "
				+ lcAmount);
		ApiResponse response = getBlackApiResponse();
		if (fromCurrency.equals(meta.getDefaultCurrencyId())) {
			List<PipsMaster> pips = pipsDao.getPipsForOnline(toCurrency);
			if (pips == null || pips.isEmpty()) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			validateExchangeRateInputdata(lcAmount);
			BigDecimal countryBranchId = meta.getCountryBranchId();
			CurrencyMasterModel toCurrencyMaster = currencyMasterDao.getCurrencyMasterById(toCurrency);
			List<ExchangeRateApprovalDetModel> allExchangeRates = exchangeRateDao.getExchangeRates(toCurrency,
					countryBranchId, toCurrencyMaster.getCountryId());
			filterNonMinServiceIdRates(allExchangeRates);
			Map<ExchangeRateApprovalDetModel, List<PipsMaster>> applicableRatesWithDiscount = getApplicableExchangeRates(
					allExchangeRates, pips, bankId);
			ExchangeRateBreakup equivalentAmount = getApplicableExchangeAmountWithDiscounts(applicableRatesWithDiscount,
					lcAmount);
			if (equivalentAmount == null) {
				equivalentAmount = checkAndApplyLowConversionRate(equivalentAmount, allExchangeRates, lcAmount);
			}
			List<BankMasterDTO> bankWiseRates = chooseBankWiseRates(toCurrency, applicableRatesWithDiscount, lcAmount);
			if (equivalentAmount == null && (bankWiseRates == null || bankWiseRates.isEmpty())) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			ExchangeRateResponseModel outputModel = new ExchangeRateResponseModel();
			outputModel.setExRateBreakup(equivalentAmount);
			outputModel.setBankWiseRates(bankWiseRates);
			response.getData().getValues().add(outputModel);
			response.getData().setType(outputModel.getModelType());
		}
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	private void filterNonMinServiceIdRates(List<ExchangeRateApprovalDetModel> allExchangeRates) {

		final Set<BigDecimal> bankIds = new HashSet<>();
		allExchangeRates.forEach(i -> bankIds.add(i.getBankMaster().getBankId()));

		for (BigDecimal bankId : bankIds) {
			BigDecimal minServiceId = null;
			for (ExchangeRateApprovalDetModel rate : allExchangeRates) {
				if (rate.getBankMaster().getBankId().equals(bankId)) {
					if (minServiceId == null) {
						minServiceId = rate.getServiceId();
					} else if (rate.getServiceId().intValue() < minServiceId.intValue()) {
						minServiceId = rate.getServiceId();
					}
				}
			}
			Iterator<ExchangeRateApprovalDetModel> itr = allExchangeRates.iterator();
			while (itr.hasNext()) {
				ExchangeRateApprovalDetModel rate = itr.next();
				if (rate.getBankMaster().getBankId().equals(bankId)) {
					if (!rate.getServiceId().equals(minServiceId)) {
						itr.remove();
					}
				}
			}
		}
	}

	private ExchangeRateBreakup checkAndApplyLowConversionRate(ExchangeRateBreakup equivalentAmount,
			List<ExchangeRateApprovalDetModel> allExchangeRates, BigDecimal amount) {
		BigDecimal maxInverseRate = null;
		if (equivalentAmount != null) {
			maxInverseRate = equivalentAmount.getInverseRate();
		} else if (allExchangeRates != null && !allExchangeRates.isEmpty()) {
			maxInverseRate = allExchangeRates.get(0).getSellRateMax();
		}
		for (ExchangeRateApprovalDetModel rate : allExchangeRates) {
			if (rate.getSellRateMax().compareTo(maxInverseRate) > 0) {
				equivalentAmount = createBreakUp(rate.getSellRateMax(), amount);
				maxInverseRate = rate.getSellRateMax();
			}
		}
		return equivalentAmount;
	}

	private List<BankMasterDTO> chooseBankWiseRates(BigDecimal fromCurrency,
			Map<ExchangeRateApprovalDetModel, List<PipsMaster>> applicableRatesWithDiscount, BigDecimal amount) {
		Set<BankMasterDTO> bankWiseRates = new HashSet<>();
		for (Entry<ExchangeRateApprovalDetModel, List<PipsMaster>> entry : applicableRatesWithDiscount.entrySet()) {
			List<PipsMaster> piplist = entry.getValue();
			ExchangeRateApprovalDetModel rate = entry.getKey();
			BankMasterDTO dto = bankMasterService.convert(rate.getBankMaster());
			dto.setExRateBreakup(getExchangeRateFromPips(piplist, rate, amount));
			bankWiseRates.add(dto);
		}

		List<BankMasterDTO> output = new ArrayList<>(bankWiseRates);
		Collections.sort(output, new BankMasterDTO.BankMasterDTOComparator());
		return output;
	}

	private ExchangeRateBreakup getExchangeRateFromPips(List<PipsMaster> piplist, ExchangeRateApprovalDetModel rate,
			BigDecimal amount) {
		BigDecimal exrate = null;
		BigDecimal minServiceId = null;
		if (piplist != null) {
			for (PipsMaster pip : piplist) {
				BigDecimal serviceId = rate.getServiceId();
				if (minServiceId == null) {
					minServiceId = rate.getServiceId();
					exrate = rate.getSellRateMax().subtract(pip.getPipsNo());
				}
				if (serviceId.compareTo(minServiceId) < 0) {
					minServiceId = serviceId;
					exrate = rate.getSellRateMax().subtract(pip.getPipsNo());
				}
			}
		} else {
			exrate = rate.getSellRateMax();
		}
		return createBreakUp(exrate, amount);
	}

	private ExchangeRateBreakup createBreakUp(BigDecimal exrate, BigDecimal amount) {
		ExchangeRateBreakup breakup = null;
		if (exrate != null) {
			breakup = new ExchangeRateBreakup();
			breakup.setConvertedFCAmount(amount.divide(exrate, 10, RoundingMode.HALF_UP));
			breakup.setInverseRate(exrate);
			breakup.setRate(new BigDecimal(1).divide(exrate, 10, RoundingMode.HALF_UP));
		}
		return breakup;
	}

	private ExchangeRateBreakup getApplicableExchangeAmountWithDiscounts(
			Map<ExchangeRateApprovalDetModel, List<PipsMaster>> applicationRates, BigDecimal localAmount) {

		BigDecimal minServiceId = null;
		BigDecimal exrate = null;
		ExchangeRateBreakup output = null;
		for (Entry<ExchangeRateApprovalDetModel, List<PipsMaster>> entry : applicationRates.entrySet()) {
			ExchangeRateApprovalDetModel rate = entry.getKey();
			List<PipsMaster> piplist = entry.getValue();

			if (piplist != null) {
				for (PipsMaster pip : piplist) {
					BigDecimal fromFCLimitAmount = pip.getFromAmount();
					BigDecimal toFCLimitAmount = pip.getToAmount();
					BigDecimal exrateTemp = rate.getSellRateMax().subtract(pip.getPipsNo());
					BigDecimal inverseExRateTemp = new BigDecimal(1).divide(exrateTemp, 10, RoundingMode.HALF_UP);
					BigDecimal convertedFCAmount = inverseExRateTemp.multiply(localAmount);
					if (convertedFCAmount.compareTo(fromFCLimitAmount) >= 0
							&& convertedFCAmount.compareTo(toFCLimitAmount) <= 0) {
						BigDecimal serviceId = rate.getServiceId();
						if (minServiceId == null) {
							minServiceId = rate.getServiceId();
							exrate = exrateTemp;
						}
						if (serviceId.compareTo(minServiceId) < 0) {
							minServiceId = serviceId;
							exrate = exrateTemp;
						}
					}
				}
			}
		}
		if (exrate != null) {
			output = new ExchangeRateBreakup();
			output.setConvertedFCAmount(localAmount.divide(exrate, 10, RoundingMode.HALF_UP));
			output.setInverseRate(exrate);
			output.setRate(new BigDecimal(1).divide(exrate, 10, RoundingMode.HALF_UP));
		}
		return output;
	}

	private void validateExchangeRateInputdata(BigDecimal amount) {
		if (amount.compareTo(new BigDecimal(0)) < 0) {
			throw new GlobalException("No exchange data found", JaxError.INVALID_EXCHANGE_AMOUNT);
		}
	}

	// logic acc. to VW_EX_TRATE
	private Map<ExchangeRateApprovalDetModel, List<PipsMaster>> getApplicableExchangeRates(
			List<ExchangeRateApprovalDetModel> allExchangeRates, List<PipsMaster> pips, BigDecimal bankId) {
		Map<ExchangeRateApprovalDetModel, List<PipsMaster>> output = new LinkedHashMap<>();
		Map<BigDecimal, ExchangeRateApprovalDetModel> map = new HashMap<>();
		if (allExchangeRates != null && !allExchangeRates.isEmpty()) {
			for (ExchangeRateApprovalDetModel rate : allExchangeRates) {
				map.put(rate.getBankMaster().getBankId(), rate);
			}
			for (PipsMaster pip : pips) {
				BankMasterModel bankMaster = pip.getBankMaster();
				if (bankMaster != null) {
					// match the bankId passed
					if (bankId != null && !bankMaster.getBankId().equals(bankId)) {
						continue;
					}
					ExchangeRateApprovalDetModel value = map.get(bankMaster.getBankId());
					if (value != null && value.getCountryId().equals(pip.getCountryMaster().getCountryId())
							&& value.getCurrencyId().equals(pip.getCurrencyMaster().getCurrencyId())) {
						List<PipsMaster> piplist = output.get(value);
						if (piplist == null) {
							piplist = new ArrayList<>();
							piplist.add(pip);
							output.put(value, piplist);
						} else {
							piplist.add(pip);
						}

					}
				}
			}
			for (ExchangeRateApprovalDetModel key : allExchangeRates) {
				if (output.get(key) == null) {
					output.put(key, pips);
				}
			}
		}
		return output;
	}

}
