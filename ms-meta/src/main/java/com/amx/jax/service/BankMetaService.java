package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.BranchSearchNotificationModel;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.VwBankBranchRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.services.JaxNotificationService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BankMetaService extends AbstractService {

	private Logger logger = Logger.getLogger(BankMetaService.class);

	@Autowired
	private BankMasterRepository repo;
	
	@Autowired
	private CountryBranchRepository countryBranchRepository;

	@Autowired
	private JaxNotificationService jaxNotificationService;

	@Autowired
	private VwBankBranchRepository vwBankBranchRepository;

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


	@SuppressWarnings("unchecked")
	public ApiResponse<BankBranchDto> getBankBranches(GetBankBranchRequest request) {

		BigDecimal bankId = request.getBankId();
		BigDecimal countryId = request.getCountryId();
		String ifsc = request.getIfscCode();
		String swift = request.getSwift();
		String branchName = request.getBranchName();
		List<BankBranchView> branchesList = new ArrayList<>();
		boolean isparametersSet = false;
		if (ifsc != null) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndIfscCode(countryId, bankId, ifsc));
			isparametersSet = true;
		}
		if (swift != null) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndSwift(countryId, bankId, swift));
			isparametersSet = true;
		}
		if (branchName != null) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndBranchFullNameLike(countryId, bankId,
					branchName));
			isparametersSet = true;
		}
		if (!isparametersSet) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankId(countryId, bankId));
		}
		ApiResponse response = getBlackApiResponse();
		// throw new GlobalException("bank branch list not found",
		// JaxError.BANK_BRANCH_NOT_FOUND);
		response.getData().getValues().addAll(convertBranchView(branchesList));
		response.getData().setType("bank-branch-dto");
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}
	
	

	private List<BankBranchDto> convertBranchView(List<BankBranchView> branchesList) {
		List<BankBranchDto> output = new ArrayList<>();
		branchesList.forEach( i -> output.add(convert(i)));
		return output;
		
	}

	private BankBranchDto convert(BankBranchView i) {
		BankBranchDto dto = new BankBranchDto();
		try {
			BeanUtils.copyProperties(dto, i);
		} catch (Exception e) {
			logger.info("error in copy properties" ,e);
		}
		return dto;
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
