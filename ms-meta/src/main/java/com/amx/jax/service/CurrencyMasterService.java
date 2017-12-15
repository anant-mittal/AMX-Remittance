package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.ViewOnlineCurrency;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ViewOnlineCurrencyRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.util.ConverterUtil;

@Service
@SuppressWarnings("rawtypes")
public class CurrencyMasterService extends AbstractService {

	@Autowired
	ICurrencyDao currencyDao;

	@Autowired
	ViewOnlineCurrencyRepository viewOnlineCurrencyRepo;

	@Autowired
	ConverterUtil converterUtil;

	private Logger logger = Logger.getLogger(CurrencyMasterService.class);

	public ApiResponse getCurrencyDetails(BigDecimal currencyId) {
		List<CurrencyMasterModel> currencyList = currencyDao.getCurrencyList(currencyId);
		ApiResponse response = getBlackApiResponse();
		if (currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		} else {
			response.getData().getValues().addAll(currencyList);
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("currencyMaster");
		return response;

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

	public ApiResponse getAllOnlineCurrencyDetails() {
		List<ViewOnlineCurrency> currencyList = (List<ViewOnlineCurrency>) viewOnlineCurrencyRepo.findAll();
		ApiResponse response = getBlackApiResponse();
		if (currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		} else {
			List<CurrencyMasterDTO> list = convert(currencyList);
			response.getData().getValues().addAll(list);
			response.getData().setType(list.get(0).getModelType());
			response.setResponseStatus(ResponseStatus.OK);
		}

		return response;
	}

	public ApiResponse getCurrencyByCountryId(BigDecimal countryId) {
		List<CurrencyMasterModel> currencyList = currencyDao.getCurrencyListByCountryId(countryId);
		ApiResponse response = getBlackApiResponse();
		if (currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		} else {
			response.getData().getValues().addAll(convertToModelDto(currencyList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("currencyMaster");
		return response;
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
			logger.error("unable to convert currency", e);
		}
		return dto;
	}

	private List<CurrencyMasterDTO> convertToModelDto(List<CurrencyMasterModel> currencyList) {
		List<CurrencyMasterDTO> output = new ArrayList<>();
		currencyList.forEach(currency -> output.add(convertModel(currency)));
		return output;
	}

	private CurrencyMasterDTO convertModel(CurrencyMasterModel currency) {
		CurrencyMasterDTO dto = new CurrencyMasterDTO();
		try {
			BeanUtils.copyProperties(dto, currency);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("unable to convert currency", e);
		}
		return dto;
	}

}
