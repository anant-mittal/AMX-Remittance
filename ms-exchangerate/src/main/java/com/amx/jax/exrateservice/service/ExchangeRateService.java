package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.ViewCompanyDetailDTO;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;

import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dao.CurrencyMasterDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.exrateservice.dao.ExchangeRateDao;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.services.AbstractService;
import com.amx.jax.util.JaxUtil;


@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class ExchangeRateService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	static Map<BigDecimal, BigDecimal> currencyIdVsExchId = new HashMap<>();

	@Autowired
	PipsMasterDao pipsDao;

	@Autowired
	BankMetaService bankMasterService;

	@Autowired
	ExchangeRateDao exchangeRateDao;

	@Autowired
	 MetaData meta;

	@Autowired
	JaxUtil util;

	@Autowired
	CurrencyMasterDao currencyMasterDao;
	
	@Autowired
	ExchangeRateProcedureDao exchangeRateProcedureDao;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	CurrencyMasterService currencyMasterService;

	@Override
	public String getModelType() {
		return "exchange_rate";
	}

	@Override
	public Class<?> getModelClass() {
		return null;
	}

	public ApiResponse getExchangeRatesForOnline(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal lcAmount,BigDecimal bankId) {
		logger.info("In getExchangeRatesForOnline, parames- " + fromCurrency + " toCurrency " + toCurrency + " amount "+ lcAmount);
		ApiResponse response = getBlackApiResponse();
		if (fromCurrency.equals(meta.getDefaultCurrencyId())) {
			List<PipsMaster> pips = pipsDao.getPipsForOnline(toCurrency);
			if (pips == null || pips.isEmpty()) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}
			validateExchangeRateInputdata(lcAmount);
			BigDecimal countryBranchId = meta.getCountryBranchId();
			List<BigDecimal> validBankIds = exchangeRateProcedureDao.getBankIdsForExchangeRates(toCurrency);
			
			if (validBankIds.isEmpty()) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}
			
			CurrencyMasterModel toCurrencyMaster = currencyMasterDao.getCurrencyMasterById(toCurrency);
			List<ExchangeRateApprovalDetModel> allExchangeRates = exchangeRateDao.getExchangeRates(toCurrency,
					countryBranchId, toCurrencyMaster.getCountryId(), validBankIds);
			filterNonMinServiceIdRates(allExchangeRates);
			Map<ExchangeRateApprovalDetModel, List<PipsMaster>> applicableRatesWithDiscount = getApplicableExchangeRates(allExchangeRates, pips, bankId);
			ExchangeRateBreakup equivalentAmount = getApplicableExchangeAmountWithDiscounts(applicableRatesWithDiscount,lcAmount);
			if (equivalentAmount == null) {
				equivalentAmount = checkAndApplyLowConversionRate(equivalentAmount, allExchangeRates, lcAmount);
			}
			List<BankMasterDTO> bankWiseRates = chooseBankWiseRates(toCurrency, applicableRatesWithDiscount, lcAmount);
			if (equivalentAmount == null && (bankWiseRates == null || bankWiseRates.isEmpty())) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
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

	void filterNonMinServiceIdRates(List<ExchangeRateApprovalDetModel> allExchangeRates) {

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

	ExchangeRateBreakup checkAndApplyLowConversionRate(ExchangeRateBreakup equivalentAmount,List<ExchangeRateApprovalDetModel> allExchangeRates, BigDecimal amount) {
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

	List<BankMasterDTO> chooseBankWiseRates(BigDecimal fromCurrency,
			Map<ExchangeRateApprovalDetModel, List<PipsMaster>> applicableRatesWithDiscount, BigDecimal lcAmount) {
		Set<BankMasterDTO> bankWiseRates = new HashSet<>();
		for (Entry<ExchangeRateApprovalDetModel, List<PipsMaster>> entry : applicableRatesWithDiscount.entrySet()) {
			List<PipsMaster> piplist = entry.getValue();
			ExchangeRateApprovalDetModel rate = entry.getKey();
			BankMasterDTO dto = bankMasterService.convert(rate.getBankMaster());
			dto.setExRateBreakup(getExchangeRateFromPips(piplist, rate, lcAmount));
			logger.info("EXCHANGE_RATE_MASTER_APR_ID= " + rate.getExchangeRateMasterAprDetId() + " ,currencyid= "
					+ rate.getCurrencyId());
			currencyIdVsExchId.put(rate.getCurrencyId(),  rate.getExchangeRateMasterAprDetId());
			bankWiseRates.add(dto);
		}

		List<BankMasterDTO> output = new ArrayList<>(bankWiseRates);
		Iterator<BankMasterDTO> itr = output.iterator();
		while (itr.hasNext()) {
			BankMasterDTO current = itr.next();
			if (current.getExRateBreakup() == null || current.getExRateBreakup().getRate() == null) {
				itr.remove();
			}
		}
		Collections.sort(output, new BankMasterDTO.BankMasterDTOComparator());
		return output;
	}

	ExchangeRateBreakup getExchangeRateFromPips(List<PipsMaster> piplist, ExchangeRateApprovalDetModel rate,
			BigDecimal lcAmount) {
		BigDecimal exrate = null;
		BigDecimal minServiceId = null;
		if (piplist != null) {
			for (PipsMaster pip : piplist) {
				BigDecimal serviceId = rate.getServiceId();
				BigDecimal fromFCLimitAmount = pip.getFromAmount();
				BigDecimal toFCLimitAmount = pip.getToAmount();
				BigDecimal exRateApplicable = rate.getSellRateMax();
				BigDecimal inverseExRateTemp = new BigDecimal(1).divide(exRateApplicable, 10, RoundingMode.HALF_UP);
				BigDecimal convertedFCAmount = inverseExRateTemp.multiply(lcAmount);
				if (convertedFCAmount.compareTo(fromFCLimitAmount) >= 0
						&& convertedFCAmount.compareTo(toFCLimitAmount) <= 0) {
					if (minServiceId == null) {
						minServiceId = rate.getServiceId();
						exrate = rate.getSellRateMax().subtract(pip.getPipsNo());
					}
					if (serviceId.compareTo(minServiceId) < 0) {
						minServiceId = serviceId;
						exrate = rate.getSellRateMax().subtract(pip.getPipsNo());
					}
				}
			}
		} 
		if (exrate == null) {
			exrate = rate.getSellRateMax();
		}
		return createBreakUp(exrate, lcAmount);
	}

	ExchangeRateBreakup createBreakUp(BigDecimal exrate, BigDecimal lcAmount) {
		ExchangeRateBreakup breakup = null;
		if (exrate != null) {
			breakup = new ExchangeRateBreakup();
			breakup.setInverseRate(exrate);
			breakup.setRate(new BigDecimal(1).divide(exrate, 10, RoundingMode.HALF_UP));
			breakup.setConvertedFCAmount(breakup.getRate().multiply(lcAmount));
			breakup.setConvertedLCAmount(lcAmount);
		}
		return breakup;
	}
	
	ExchangeRateBreakup createBreakUpFromForeignCurrency(BigDecimal exrate, BigDecimal fcAmount) {
		ExchangeRateBreakup breakup = null;
		if (exrate != null) {
			breakup = new ExchangeRateBreakup();
			breakup.setConvertedLCAmount(fcAmount.multiply(exrate));
			breakup.setConvertedFCAmount(fcAmount);
			breakup.setInverseRate(exrate);
			breakup.setRate(new BigDecimal(1).divide(exrate, 10, RoundingMode.HALF_UP));
		}
		return breakup;
	}

	ExchangeRateBreakup getApplicableExchangeAmountWithDiscounts(Map<ExchangeRateApprovalDetModel, List<PipsMaster>> applicationRates, BigDecimal localAmount) {

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

	protected void validateExchangeRateInputdata(BigDecimal amount) {
		if (amount.compareTo(new BigDecimal(0)) < 0) {
			throw new GlobalException(JaxError.INVALID_EXCHANGE_AMOUNT, "No exchange data found");
		}
	}

	// logic acc. to VW_EX_TRATE
	Map<ExchangeRateApprovalDetModel, List<PipsMaster>> getApplicableExchangeRates(List<ExchangeRateApprovalDetModel> allExchangeRates, List<PipsMaster> pips, BigDecimal bankId) {
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
					if (value != null && value.getCountryId().equals(pip.getCountryMaster().getCountryId())&& value.getCurrencyId().equals(pip.getCurrencyMaster().getCurrencyId())) {
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

	public ApiResponse setOnlineExchangeRates(String quoteName, BigDecimal value) {
		ApiResponse apiResponse = getBlackApiResponse();
		value  = BigDecimal.ONE.divide(value, 5, RoundingMode.HALF_UP);
		BigDecimal toCurrency = currencyMasterDao.getCurrencyMasterByQuote(quoteName).getCurrencyId();
		this.getExchangeRatesForOnline( BigDecimal.ONE, toCurrency,  BigDecimal.ONE, null);
		BigDecimal exRateId = currencyIdVsExchId.get(toCurrency);
		ExchangeRateApprovalDetModel exRateModel = exchangeRateDao.getExchangeRateApprovalDetModelById(exRateId);
		exRateModel.setSellRateMax(value);
		exchangeRateDao.saveOrUpdate(exRateModel);
		apiResponse.getData().getValues().add(new BooleanResponse(true));
		return apiResponse;
	}

	/**
	 * Get Min Max Exchange Rate API
	 * @return min max exchage rate data
	 */
	public ApiResponse getMinMaxExrate() {
		ApiResponse<MinMaxExRateDTO> apiResponse = getBlackApiResponse();
	
		BigDecimal languageId = meta.getLanguageId();
		AmxApiResponse<ViewCompanyDetailDTO, Object> responseFromCur = companyService.getCompanyDetails(languageId);
		List listFromCur = responseFromCur.getResults();
		ViewCompanyDetailDTO dtoFromCur = (ViewCompanyDetailDTO)listFromCur.get(0);
		BigDecimal fromCurrency = dtoFromCur.getCurrencyId();
		
		CurrencyMasterModel getFromCurrencyData = currencyMasterService.getCurrencyMasterById(fromCurrency);
		
		AmxApiResponse<CurrencyMasterDTO, Object> responseToCur = currencyMasterService.getAllOnlineCurrencyDetails();
		List<CurrencyMasterDTO> listToCur = responseToCur.getResults();
		listToCur.add(currencyMasterService.convertModel(getFromCurrencyData));
		List dtoList = getMinMaxData(listToCur, fromCurrency);
		
		apiResponse.setResponseStatus(ResponseStatus.OK);
		apiResponse.getData().getValues().addAll(dtoList);
		apiResponse.getData().setType("min-max-exrate");
		
		return apiResponse;
	}
	
	/**
	 * call get min max exchange rate method
	 * @return dtoList gives fromCurrency, toCurrency, minRate, maxRate
	 */
	List<MinMaxExRateDTO> getMinMaxData(List<CurrencyMasterDTO> listToCur, BigDecimal fromCurrency){
		List<MinMaxExRateDTO> dtoList = new ArrayList<MinMaxExRateDTO>();
		Map<BigDecimal, CurrencyMasterDTO> mapToCur = listToCur.stream().collect(Collectors.toMap(CurrencyMasterDTO::getCurrencyId, x -> x));
		
		for (CurrencyMasterDTO rec : listToCur) {
			try {
				BigDecimal toCurrency = rec.getCurrencyId();
				ApiResponse exrateresp = this.getExchangeRatesForOnline(fromCurrency, toCurrency, BigDecimal.ONE, null);
				ExchangeRateResponseModel exrate = (ExchangeRateResponseModel) exrateresp.getResult();
				List<BankMasterDTO> bankWiseRates = exrate.getBankWiseRates();
				BigDecimal minRate = bankWiseRates.stream().max(new BankMasterDTO.BankMasterDTOComparator()).get()
						.getExRateBreakup().getRate();
				BigDecimal maxRate = bankWiseRates.stream().min(new BankMasterDTO.BankMasterDTOComparator()).get()
						.getExRateBreakup().getRate();

				MinMaxExRateDTO minMaxDTO = new MinMaxExRateDTO();
				minMaxDTO.setFromCurrency(mapToCur.get(fromCurrency));
				minMaxDTO.setToCurrency(mapToCur.get(toCurrency));
				minMaxDTO.setMinExrate(minRate);
				minMaxDTO.setMaxExrate(maxRate);

				dtoList.add(minMaxDTO);

			} catch (Exception e) {

			}
		}
		return dtoList;
	}
	
	public ApiResponse setOnlineExchangeRatesPlaceorder(String quoteName,BigDecimal bankId, BigDecimal value) {
		ApiResponse apiResponse = getBlackApiResponse();
		value  = BigDecimal.ONE.divide(value, 5, RoundingMode.HALF_UP);
		BigDecimal toCurrency = currencyMasterDao.getCurrencyMasterByQuote(quoteName).getCurrencyId();
		List<ExchangeRateApprovalDetModel> exRateModel =  exchangeRateDao.getExchangeRatesPlaceorder(toCurrency, bankId);
		for(ExchangeRateApprovalDetModel exRate : exRateModel) {
		exRate.setSellRateMax(value);
		exchangeRateDao.saveOrUpdate(exRate);
		}
		apiResponse.getData().getValues().add(new BooleanResponse(true));
		return apiResponse;
	}
}
