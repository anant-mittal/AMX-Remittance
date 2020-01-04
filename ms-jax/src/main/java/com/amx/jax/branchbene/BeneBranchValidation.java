package com.amx.jax.branchbene;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.model.request.AbstractBeneDetailDto;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.model.request.benebranch.UpdateBeneBankRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneCashRequest;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.BenePersonalDetailValidator;

@Component
public class BeneBranchValidation {

	@Autowired
	MetaService metaService;
	@Autowired
	BeneficiaryValidationService beneficiaryValidationService;
	@Autowired
	BenePersonalDetailValidator benePersonalDetailValidator;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	JaxUtil jaxUtil;

	public void validateaddBeneBank(AddBeneBankRequest request) {
		ServiceGroupMaster bankserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_BANK);
		if (!bankserviceMaster.getServiceGroupId().equals(request.getServiceGroupId())) {
			throw new GlobalException("service group id does not belong to bank");
		}
		validateAbtractBeneDetail(request);
	}

	public void validateaddBenecash(AddBeneCashRequest request) {
		ServiceGroupMaster cashserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_CASH);
		if (!cashserviceMaster.getServiceGroupId().equals(request.getServiceGroupId())) {
			throw new GlobalException("service group id does not belong to cash");
		}
		validateAbtractBeneDetail(request);
	}

	public void validateAbtractBeneDetail(AbstractBeneDetailDto request) {
		BeneficiaryTrnxModel beneTrnxModel = request.createBeneficiaryTrnxModelObject();
		BeneAccountModel beneAccountModel = beneTrnxModel.getBeneAccountModel();
		if (StringUtils.isNotBlank(beneAccountModel.getIfscCode())) {
			beneficiaryValidationService.validateIFscCode(beneAccountModel);
		}
		benePersonalDetailValidator.validateBeneNames(beneTrnxModel.getBenePersonalDetailModel());
		// swift validation is already done inside online flow
	}

	public void validateAddNewBankBranchRequest(AddNewBankBranchRequest request) {
		BigDecimal beneBankCountryId = request.getCountryId();
		BigDecimal beneBankCurrencyId = request.getCurrencyId();
		String ifscCode = request.getIfscCode();
		beneficiaryValidationService.validateIFscCode(beneBankCountryId, beneBankCurrencyId, ifscCode);
		String swiftCode = request.getSwift();
		beneficiaryValidationService.validateSwiftCode(beneBankCountryId, beneBankCurrencyId, swiftCode);

	}

	public void validateUpdateBeneBank(UpdateBeneBankRequest request) {
		BeneficiaryTrnxModel beneTrnxModel = request.createBeneficiaryTrnxModelObject();
		BeneAccountModel beneAccountModelRequest = beneTrnxModel.getBeneAccountModel();
		BenificiaryListView benificiaryListView = beneficiaryService.getBeneByIdNo(BigDecimal.valueOf(request.getIdNo()));
		BeneAccountModel beneAccountModel = createBeneAccountModel(benificiaryListView, beneAccountModelRequest);
		beneTrnxModel.setBeneAccountModel(beneAccountModel);

		if (StringUtils.isNotBlank(beneAccountModelRequest.getIfscCode())) {
			beneficiaryValidationService.validateIFscCode(beneAccountModel);
		}
		if (StringUtils.isNotBlank(beneAccountModelRequest.getBankAccountNumber())) {
			beneficiaryValidationService.validateBeneAccountUpdate(beneAccountModel);
		}
		benePersonalDetailValidator.validateUpdateBene(beneTrnxModel, request, benificiaryListView);
	}

	public void validateUpdateBeneCash(UpdateBeneCashRequest request) {
		BeneficiaryTrnxModel beneTrnxModel = request.createBeneficiaryTrnxModelObject();
		BeneAccountModel beneAccountModelRequest = beneTrnxModel.getBeneAccountModel();
		BenificiaryListView benificiaryListView = beneficiaryService.getBeneByIdNo(BigDecimal.valueOf(request.getIdNo()));
		BeneAccountModel beneAccountModel = createBeneAccountModel(benificiaryListView, beneAccountModelRequest);
		beneTrnxModel.setBeneAccountModel(beneAccountModel);
		if (StringUtils.isNotBlank(beneAccountModelRequest.getBankAccountNumber())) {
			beneficiaryValidationService.validateDuplicateCashBeneficiary(beneTrnxModel);
		}
		benePersonalDetailValidator.validateUpdateBene(beneTrnxModel, request, benificiaryListView);
	}

	public BeneAccountModel createBeneAccountModel(BenificiaryListView bv, BeneAccountModel beneAccountModelRequest) {
		BeneficaryAccount beneAccount = beneficiaryService.getBeneAccountByAccountSeqId(bv.getBeneficiaryAccountSeqId());
		BeneAccountModel beneAccountModel = new BeneAccountModel();
		beneAccountModel.setBankAccountNumber(beneAccount.getBankAccountNumber());
		beneAccountModel.setBankAccountTypeId(beneAccount.getBankAccountTypeId());
		beneAccountModel.setBankBranchId(beneAccount.getBankBranchId());
		beneAccountModel.setBankId(beneAccount.getBankId());
		beneAccountModel.setBeneficaryCountryId(beneAccount.getBeneficaryCountryId());
		beneAccountModel.setCurrencyId(beneAccount.getCurrencyId());
		beneAccountModel.setIbanNumber(beneAccount.getIbanNumber());
		beneAccountModel.setServiceGroupId(beneAccount.getServiceGroupId());
		beneAccountModel.setServiceProviderBranchId(beneAccount.getServiceProviderBranchId());
		beneAccountModel.setServiceProviderId(beneAccount.getServiceProviderId());
		beneAccountModel.setSwiftCode(beneAccount.getSwiftCode());
		jaxUtil.convertNotNull(beneAccountModelRequest, beneAccountModel);

		return beneAccountModel;
	}

	public BenePersonalDetailModel createBenePersonalDetailModel(BenificiaryListView bv, BeneAccountModel beneAccountModelRequest) {
		BeneficaryMaster beneMaster = beneficiaryService.getBeneficiaryMasterBybeneficaryMasterSeqId(bv.getBeneficaryMasterSeqId());
		BeneficaryContact beneContact = beneficiaryService.getBeneficiaryContactByMasterId(beneMaster.getBeneficaryMasterSeqId());
		BenePersonalDetailModel benePersonalDetailModel = new BenePersonalDetailModel();

		jaxUtil.convertNotNull(beneAccountModelRequest, beneMaster);
		jaxUtil.convertNotNull(beneAccountModelRequest, beneContact);
		return benePersonalDetailModel;
	}
}
