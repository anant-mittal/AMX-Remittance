package com.amx.jax.branchbene;

import java.math.BigDecimal;
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
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryStatus;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.AbstractBeneDetailDto;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.model.request.benebranch.ListBankBranchRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.trnx.BeneficiaryTrnxManager;

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
	@Autowired
	BeneficiaryValidationService beneficiaryValidationService;
	@Autowired
	BeneficiaryTrnxManager beneficiaryTrnxManager;

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

	public void addBeneBankorCash(AbstractBeneDetailDto request) {
		BeneficiaryTrnxModel beneficiaryTrnxModel = request.createBeneficiaryTrnxModelObject();
		beneficiaryValidationService.validateBeneficiaryTrnxModel(beneficiaryTrnxModel);
		beneficiaryTrnxManager.commit(beneficiaryTrnxModel);
		setAdditionalBranchFields(beneficiaryTrnxModel.getBeneficaryRelationSeqId(), request);
	}

	private void setAdditionalBranchFields(BigDecimal beneficaryRelationSeqId, AbstractBeneDetailDto request) {
		BenificiaryListView beneRelationship = beneService.getBeneByIdNo(beneficaryRelationSeqId);
		BeneficaryMaster beneficaryMaster = beneService.getBeneficiaryMasterBybeneficaryMasterSeqId(beneRelationship.getBeneficaryMasterSeqId());
		if (ConstantDocument.INDIVIDUAL_STRING.equals(request.getBeneficaryType())) {
			BeneficaryStatus beneStatus = beneService.getBeneStatusByNameByName(ConstantDocument.INDIVIDUAL_STRING);
			beneficaryMaster.setBeneficaryStatus(beneStatus.getBeneficaryStatusId());
			beneficaryMaster.setBeneficaryStatusName(beneStatus.getBeneficaryStatusName());
		} else {
			BeneficaryStatus beneStatus = beneService.getBeneStatusByNameByName(ConstantDocument.NON_INDIVIDUAL_STRING);
			beneficaryMaster.setBeneficaryStatus(beneStatus.getBeneficaryStatusId());
			beneficaryMaster.setBeneficaryStatusName(beneStatus.getBeneficaryStatusName());
		}
		if (request.getAge() != null) {
			beneficaryMaster.setAge(BigDecimal.valueOf(request.getAge()));
		}
		if (request.getYearOfBirth() != null) {
			beneficaryMaster.setYearOfBrith(BigDecimal.valueOf(request.getYearOfBirth()));
		}
		beneficaryMaster.setDateOfBrith(request.getDateOfBirth());
		beneService.saveBeneMaster(beneficaryMaster);
	}

}
