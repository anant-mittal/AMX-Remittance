package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.services.AbstractService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BankMetaService extends AbstractService {

	private Logger logger = Logger.getLogger(BankMetaService.class);

	@Autowired
	private BankMasterRepository repo;
	
	@Autowired
	private CountryBranchRepository countryBranchRepository;

	public List<BankMasterModel> getBanksByCountryId(BigDecimal countryId) {
		return repo.findBybankCountryId(countryId);
	}

	public ApiResponse getBanksApiResponseByCountryId(BigDecimal countryId) {
		List<BankMasterModel> banks = this.getBanksByCountryId(countryId);
		ApiResponse response = getBlackApiResponse();
		if (banks.isEmpty()) {
			throw new GlobalException("banks details not avaliable");
		} else {
			response.getData().getValues().addAll(convert(banks));
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("bankmaster");
		return response;
	}

	private List<BankMasterDTO> convert(List<BankMasterModel> banks) {

		List<BankMasterDTO> bankdtos = new ArrayList<>();
		banks.forEach(i -> bankdtos.add(convert(i)));
		return bankdtos;
	}

	public BankMasterDTO convert(BankMasterModel dbmodel) {
		BankMasterDTO dto = new BankMasterDTO();
		try {
			BeanUtils.copyProperties(dto, dbmodel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("error in convert of bankmaster", e);
		}
		return dto;
	}

	public BankMasterDTO getBankMasterDTObyId(BigDecimal bankId) {
		BankMasterModel dbModel = repo.findOne(bankId);
		if (dbModel != null) {
			return convert(dbModel);
		}
		return null;
	}
	
	public BankMasterModel getBankMasterbyId(BigDecimal bankId) {
		BankMasterModel dbModel = repo.findOne(bankId);
		return dbModel;
	}
	
	public CountryBranch getCountryBranchById(BigDecimal id) {
		return countryBranchRepository.findOne(id);
	}

	@Override
	public String getModelType() {
		return "bankmaster";
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
