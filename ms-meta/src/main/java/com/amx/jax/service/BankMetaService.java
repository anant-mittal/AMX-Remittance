package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.VwBankBranchRepository;
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

	@Autowired
	private VwBankBranchRepository vwBankBranchRepository;

	public List<BankMasterModel> getBanksByCountryId(BigDecimal countryId) {
		return repo.findBybankCountryIdAndRecordStatusOrderByBankShortNameAsc(countryId, ConstantDocument.Yes);
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
		validateGetBankBrancheRequest(request);
		BigDecimal countryId = request.getCountryId();
		String ifsc = request.getIfscCode();
		String swift = request.getSwift();
		String branchName = request.getBranchName();
		Set<BankBranchView> branchesList = new HashSet<>();
		boolean isparametersSet = false;
		if (StringUtils.isNotBlank(ifsc)) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndIfscCodeIgnoreCase(countryId, bankId, ifsc));
			isparametersSet = true;
		}
		if (StringUtils.isNotBlank(swift)) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndSwiftIgnoreCase(countryId, bankId, swift));
			isparametersSet = true;
		}
		if (StringUtils.isNotBlank(branchName)) {
			branchName = "%" + branchName + "%";
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndBranchFullNameIgnoreCaseLike(countryId, bankId,
					branchName));
			isparametersSet = true;
		}
		if (!isparametersSet) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankId(countryId, bankId));
		}
		
		if (branchesList.isEmpty()) {
		    throw new GlobalException("Bank branch list is empty.", JaxError.BANK_BRANCH_SEARCH_EMPTY);
		}
		
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().addAll(convertBranchView(branchesList));
		response.getData().setType("bank-branch-dto");
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	private void validateGetBankBrancheRequest(GetBankBranchRequest request) {

		if (request.getBankId() == null) {
			throw new GlobalException("No Bank Id provided", JaxError.BANK_ID_NOT_PRESENT);
		}
	}

	private List<BankBranchDto> convertBranchView(Set<BankBranchView> branchesList) {
		List<BankBranchDto> output = new ArrayList<>();
		branchesList.forEach(i -> output.add(convert(i)));
		return output;

	}

	private BankBranchDto convert(BankBranchView i) {
		BankBranchDto dto = new BankBranchDto();
		try {
			BeanUtils.copyProperties(dto, i);
		} catch (Exception e) {
			logger.info("error in copy properties", e);
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
