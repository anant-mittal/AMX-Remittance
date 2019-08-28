package com.amx.jax.branchbene;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.RoutingBankMasterParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceImpl;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.benebranch.ListBankBranchRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;

@Service
public class BeneBranchService {

	private Logger logger = LoggerFactory.getLogger(BeneBranchService.class);

	@Autowired
	MetaData metaData;
	@Autowired
	BankService bankService;
	@Autowired
	MetaService metaService;
	@Autowired
	BeneficiaryService beneService;
	@Autowired
	BankMetaService bankMetaService;

	// bank
	public List<BankMasterDTO> getBankByCountryAndCurrency(ListBeneBankOrCashRequest request) {
		List<BankMasterDTO> bankMasterDtolist = null;
		bankMasterDtolist = bankService.getBankByCountryAndCurrency(request.getCountryId(), request.getCurrencyId());
		return bankMasterDtolist;
	}

	// cash
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<RoutingBankMasterDTO> getServiceProviderList(ListBeneBankOrCashRequest request) {
		ServiceGroupMaster cashserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_CASH);
		RoutingBankMasterServiceImpl param = new RoutingBankMasterParam.RoutingBankMasterServiceImpl(metaData.getCountryId(), request.getCountryId(),
				cashserviceMaster.getServiceGroupId());
		ApiResponse serviceProviderResponse = beneService.getServiceProviderList(param);
		List<RoutingBankMasterDTO> serviceProviderList = serviceProviderResponse.getResults();
		return serviceProviderList;
	}

	public List<BankBranchDto> listBankBranch(ListBankBranchRequest request) {
		return bankMetaService.getBankBranches(request);
	}

}
