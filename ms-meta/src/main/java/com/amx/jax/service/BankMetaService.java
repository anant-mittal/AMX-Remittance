package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.treasury.BankApplicability;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.benebranch.ListBankBranchRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.VwBankBranchRepository;
import com.amx.jax.repository.meta.BankApplicabilityRepository;
import com.amx.jax.services.AbstractService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BankMetaService extends AbstractService {

	private Logger logger = Logger.getLogger(BankMetaService.class);

	@Autowired
	private BankMasterRepository repo;
	@Autowired
	private CountryBranchRepository countryBranchRepository;
	@Autowired
	private VwBankBranchRepository vwBankBranchRepository;
	@Autowired
	BankApplicabilityRepository bankApplicabilityRepository;

	public List<BankMasterModel> getBanksByCountryId(BigDecimal countryId) {
		return repo.findBybankCountryIdAndRecordStatusOrderByBankShortNameAsc(countryId, ConstantDocument.Yes);
	}

	public AmxApiResponse<BankMasterDTO, Object> getBanksApiResponseByCountryId(BigDecimal countryId) {
		List<BankMasterModel> banks = this.getBanksByCountryId(countryId);
		if (banks.isEmpty()) {
			throw new GlobalException("banks details not avaliable");
		}
		return AmxApiResponse.buildList(convert(banks));
	}

	public List<BankMasterDTO> convert(List<BankMasterModel> banks) {

		List<BankMasterDTO> bankdtos = new ArrayList<>();
		banks.forEach(i -> {
			BankMasterDTO bankDto = convert(i);
			if (ConstantDocument.Yes.equalsIgnoreCase(i.getIbanFlag())) {
				bankDto.setIbanRequired(true);
			}
			bankdtos.add(bankDto);
		});
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

	public AmxApiResponse<BankBranchDto, Object> getBankBranches(GetBankBranchRequest request) {

		BigDecimal bankId = request.getBankId();
		validateGetBankBrancheRequest(request);
		BigDecimal countryId = request.getCountryId();
		String ifsc = request.getIfscCode();
		String swift = request.getSwift();
		String branchName = request.getBranchName();
		Set<BankBranchView> branchesList = new LinkedHashSet<>();
		boolean isparametersSet = false;
		Sort sortByBranchName = new Sort("branchFullName");
		if (StringUtils.isNotBlank(ifsc)) {
			ifsc = "%" + ifsc + "%";
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndIfscCodeIgnoreCaseLike(countryId,
					bankId, ifsc, sortByBranchName));
			isparametersSet = true;
		}
		if (StringUtils.isNotBlank(swift)) {
			swift = "%" + swift + "%";
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndSwiftIgnoreCaseLike(countryId, bankId,
					swift, sortByBranchName));
			isparametersSet = true;
		}
		if (StringUtils.isNotBlank(branchName)) {
			branchName = "%" + branchName + "%";
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndBranchFullNameIgnoreCaseLike(
					countryId, bankId, branchName, sortByBranchName));
			isparametersSet = true;
		}
		if (!isparametersSet) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankId(countryId, bankId, sortByBranchName));
		}

		if (branchesList.isEmpty()) {
			throw new GlobalException(JaxError.BANK_BRANCH_SEARCH_EMPTY, "Bank branch list is empty.");
		}
		return AmxApiResponse.buildList(convertBranchView(branchesList));
	}

	private void validateGetBankBrancheRequest(GetBankBranchRequest request) {

		if (request.getBankId() == null) {
			throw new GlobalException(JaxError.BANK_ID_NOT_PRESENT, "No Bank Id provided");
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

	public BankApplicability getBankApplicability(BigDecimal bankId) {
		return bankApplicabilityRepository.findByBankMaster(new BankMasterModel(bankId));
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

	public List<BankBranchDto> getBankBranches(ListBankBranchRequest request) {

		BigDecimal bankId = request.getBankId();
		validateListBankBrancheRequest(request);
		BigDecimal countryId = request.getCountryId();
		String ifsc = request.getIfscCode();
		String swift = request.getSwift();
		String branchName = request.getBranchName();
		Set<BankBranchView> branchesList = new LinkedHashSet<>();
		Sort sortByBranchName = new Sort("branchFullName");
		if (StringUtils.isNotBlank(ifsc)) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndIfscCode(countryId, bankId, ifsc,
					sortByBranchName));
		}

		if (StringUtils.isNotBlank(swift)) {
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndSwift(countryId, bankId, swift,
					sortByBranchName));
		}

		if (branchesList.isEmpty() && StringUtils.isNotBlank(request.getBranchName())) {
			branchName = "%" + branchName + "%";
			branchesList.addAll(vwBankBranchRepository.findByCountryIdAndBankIdAndBranchFullNameIgnoreCaseLike(
					countryId, bankId, branchName, sortByBranchName));
		}

		if (branchesList.isEmpty()) {
			throw new GlobalException(JaxError.BANK_BRANCH_SEARCH_EMPTY, "Bank branch list is empty.");
		}
		return convertBranchView(branchesList);
	}

	private void validateListBankBrancheRequest(ListBankBranchRequest request) {
		if (StringUtils.isBlank(request.getIfscCode()) && StringUtils.isBlank(request.getSwift())) {
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Either swift or ifsc is mandatory");
		}
	}
}
