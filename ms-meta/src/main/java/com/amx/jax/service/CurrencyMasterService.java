package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dao.CurrencyMasterDao;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ViewOnlineCurrency;
import com.amx.jax.dbmodel.bene.ViewBeneServiceCurrency;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ViewBeneficiaryCurrencyRepository;
import com.amx.jax.repository.ViewOnlineCurrencyRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.util.ConverterUtil;

@Service
@SuppressWarnings("rawtypes")
public class CurrencyMasterService extends AbstractService {

	@Autowired
	ICurrencyDao currencyDao;
	
	@Autowired
	CurrencyMasterDao currencyMasterDao;

	@Autowired
	ViewOnlineCurrencyRepository viewOnlineCurrencyRepo;

	@Autowired
	ConverterUtil converterUtil;
	
	@Autowired
	ViewBeneficiaryCurrencyRepository viewBeneficiaryCurrencyRepository;
	
	@Autowired
	ApplicationSetupService applicationSetupService;
	
	@Autowired
	private ExchangeRateProcedureDao exchangeRateProcedureDao;	
	
	@Autowired
	private MetaData metaData;
	
	@Autowired
	MetaService metaSerivce;
	
	@Autowired
	JaxTenantProperties jaxTenantProperties; 
	
	private Logger logger = Logger.getLogger(CurrencyMasterService.class);

	public AmxApiResponse<CurrencyMasterModel, Object> getCurrencyDetails(BigDecimal currencyId) {
		List<CurrencyMasterModel> currencyList = currencyDao.getCurrencyList(currencyId);
		if (currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		} 
		return AmxApiResponse.buildList(currencyList);

	}


	public CurrencyMasterModel getCurrencyMasterById(BigDecimal currencyId) {
		List<CurrencyMasterModel> currencyList = currencyDao.getCurrencyList(currencyId);
		CurrencyMasterModel currencymaster = null;
		if (currencyList != null && !currencyList.isEmpty()) {
			currencymaster = currencyList.get(0);
		}
		return currencymaster;
	}
	
	public CurrencyMasterModel getCurrencyMasterById(String quoteName) {
		CurrencyMasterModel currencymaster = currencyMasterDao.getCurrencyMasterByQuote(quoteName);
		return currencymaster;
	}

	@Override
	public String getModelType() {
		return "currencyMaster";
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public AmxApiResponse<CurrencyMasterDTO, Object> getAllOnlineCurrencyDetails() {
		List<ViewOnlineCurrency> currencyList = (List<ViewOnlineCurrency>) viewOnlineCurrencyRepo.findAll(new Sort("quoteName"));
		if (currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		}
			return AmxApiResponse.buildList(convert(currencyList));
	}
	
	// added by chetan 30/04/2018 list the country for currency.
	public AmxApiResponse<CurrencyMasterDTO, Object> getAllExchangeRateCurrencyList() {
		List<ViewOnlineCurrency> currencyList = (List<ViewOnlineCurrency>) viewOnlineCurrencyRepo.findAll(new Sort("quoteName"));
		List<BigDecimal> uniqueCurrency = (List<BigDecimal>) exchangeRateProcedureDao.getDistinctCurrencyList();
		Iterator<ViewOnlineCurrency> itr = currencyList.iterator();
		if (!currencyList.isEmpty() && !uniqueCurrency.isEmpty()) {
			while (itr.hasNext()) {
				if (!uniqueCurrency.contains(itr.next().getCurrencyId())) {
					itr.remove();
				}
			}
		}
		if (currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		}
		return AmxApiResponse.buildList(convert(currencyList));
	}

	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByCountryId(BigDecimal countryId) {
		List<CurrencyMasterModel> currencyList = getCurrencyMasterByCountryId(countryId);
		if (currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		}
		return AmxApiResponse.buildList(convertToModelDto(currencyList));
	}
	
	public List<CurrencyMasterModel> getCurrencyMasterByCountryId(BigDecimal countryId){
		return currencyDao.getCurrencyListByCountryId(countryId);
	}

	private List<CurrencyMasterDTO> convert(List<ViewOnlineCurrency> currencyList) {
		List<CurrencyMasterDTO> output = new ArrayList<>();
		currencyList.forEach(currency -> output.add(convert(currency)));
		return output;
	}

	private CurrencyMasterDTO convert(ViewOnlineCurrency currency) {
		CurrencyMasterDTO dto = new CurrencyMasterDTO();
		try {
			BeanUtils.copyProperties(dto, currency);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.debug("unable to convert currency", e);
		}
		return dto;
	}

	private List<CurrencyMasterDTO> convertToModelDto(List<CurrencyMasterModel> currencyList) {
		List<CurrencyMasterDTO> output = new ArrayList<>();
		currencyList.forEach(currency -> output.add(convertModel(currency)));
		return output;
	}

	public CurrencyMasterDTO convertModel(CurrencyMasterModel currency) {
		CurrencyMasterDTO dto = new CurrencyMasterDTO();
		try {
			BeanUtils.copyProperties(dto, currency);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.debug("unable to convert currency", e);
		}
		return dto;
	}
	


	public ApiResponse getBeneficiaryCurrencyList(BigDecimal beneCountryId) {
		List<ViewBeneServiceCurrency> currencyList = viewBeneficiaryCurrencyRepository
				.findByBeneCountryId(beneCountryId, new Sort("currencyName"));
		Map<BigDecimal, CurrencyMasterModel> allCurrencies = currencyMasterDao.getAllCurrencyMap();
		List<CurrencyMasterDTO> currencyListDto = new ArrayList<>();
		currencyList.forEach(currency -> {
			currencyListDto.add(convertModel(allCurrencies.get(currency.getCurrencyId())));
		});
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().addAll(currencyListDto);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType("currencyMaster");
		return response;
	}
	
	public String getApplicationCountryCurrencyQuote() {
		BigDecimal countryId = applicationSetupService.getApplicationSetUp().getApplicationCountryId();
		return getCurrencyMasterByCountryId(countryId).get(0).getQuoteName();
	}
	
	
	/**
	 * @author Chetan Pawar
	 * @param beneCountryId
	 * @param serviceGroupId
	 * @param routingBankId
	 * @return List<CurrencyMasterDTO>
	 */
	public AmxApiResponse<CurrencyMasterDTO, Object> getBeneficiaryCurrencyList(BigDecimal beneCountryId, BigDecimal serviceGroupId,
			BigDecimal routingBankId) {
		List<ViewBeneServiceCurrency> currencyList = viewBeneficiaryCurrencyRepository.findByBeneCountryId(beneCountryId, new Sort("currencyName"));
		List<BigDecimal> currencyIdList = new ArrayList<BigDecimal>();
		if (serviceGroupId != null && routingBankId != null && metaSerivce.isCashSeriveGroup(serviceGroupId)) {
			currencyIdList = currencyMasterDao.getCashCurrencyList(metaData.getCountryId(), beneCountryId,serviceGroupId, routingBankId);
		}
		if (currencyIdList != null && !currencyIdList.isEmpty()) {
			Iterator itr = currencyList.iterator();
			while (itr.hasNext()) {
				ViewBeneServiceCurrency list = (ViewBeneServiceCurrency) itr.next();
				if (!currencyIdList.contains(list.getCurrencyId())) {
					itr.remove();
				}
			}
		}
		Map<BigDecimal, CurrencyMasterModel> allCurrencies = currencyMasterDao.getAllCurrencyMap();
		List<CurrencyMasterDTO> currencyListDto = new ArrayList<>();
		currencyList.forEach(currency -> {
			CurrencyMasterModel currencyMaster = allCurrencies.get(currency.getCurrencyId());
			if (jaxTenantProperties.getBeneThreeCountryCheck() && !beneCountryId.equals(currencyMaster.getCountryId())) {
				return;
			}
			currencyListDto.add(convertModel(currencyMaster));
		});
		return AmxApiResponse.buildList(currencyListDto);
	}

}
