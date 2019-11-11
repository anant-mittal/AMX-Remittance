package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.ServiceApplicabilityField;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.dbmodel.bene.BankAccountLength;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.dbmodel.bene.predicate.BeneficiaryAccountPredicateCreator;
import com.amx.jax.dbmodel.bene.predicate.BeneficiaryPersonalDetailPredicateCreator;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.repository.IBeneficiaryAccountDao;
import com.amx.jax.repository.IBeneficiaryMasterDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.service.CountryService;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.BankBranchSearchRequestlValidator;
import com.amx.jax.validation.BenePersonalDetailValidator;
import com.google.common.collect.Iterables;
import com.querydsl.core.types.Predicate;

/**
 * validations service for add bene
 * 
 * @author Prashant
 *
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BeneficiaryValidationService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	BeneficiaryService beneficiaryService;

	@Autowired
	BankService bankService;

	@Autowired
	CountryService countryService;

	@Autowired
	JaxUtil jaxUtil;

	@Autowired
	IBeneficiaryAccountDao beneficiaryAccountDao;

	@Autowired
	IBeneficiaryMasterDao iBeneficiaryMasterDao;

	@Autowired
	BeneficiaryAccountPredicateCreator BeneficiaryAccountPredicateCreator;

	@Autowired
	BeneficiaryPersonalDetailPredicateCreator beneficiaryPersonalDetailPredicateCreator;

	@Autowired
	IServiceApplicabilityRuleDao serviceApplicablilityRuleDao;

	@Autowired
	MetaData metaData;
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;
	
	@Autowired
	BlackListDao blackListDao;
	
	@Autowired
	BenePersonalDetailValidator benePersonalDetailValidator;
	@Autowired
	BankBranchSearchRequestlValidator bankBranchSearchRequestlValidator;

	/**
	 * @param beneAccountModel
	 * 
	 */
	public void validateBeneAccount(BeneAccountModel beneAccountModel) {

		// validate only for BANK channel and not for CASH channel
		if (!BigDecimal.ONE.equals(beneAccountModel.getServiceGroupId())) {
			validateBankAccountNumber(beneAccountModel);
			validateIban(beneAccountModel);
			validateDuplicateBankAccount(beneAccountModel);
			validateSwiftCode(beneAccountModel);
		}
	}
	
	public void validateBeneAccountUpdate(BeneAccountModel beneAccountModel) {
		// validate only for BANK channel and not for CASH channel
		if (!BigDecimal.ONE.equals(beneAccountModel.getServiceGroupId())) {
			validateBankAccountNumber(beneAccountModel);
			if (StringUtils.isNotBlank(beneAccountModel.getIbanNumber())) {
				validateIban(beneAccountModel);
			}
			validateDuplicateBankAccount(beneAccountModel);
			if (StringUtils.isNotBlank(beneAccountModel.getSwiftCode())) {
				validateSwiftCode(beneAccountModel);
			}
		}
	}

	private void validateIban(BeneAccountModel beneAccountModel) {
		BankMasterMdlv1 bankMaster = bankService.getBankById(beneAccountModel.getBankId());
		if (bankMaster == null) {
			return;
		}
		String ibanFlag = bankMaster.getIbanFlag();
		if (ConstantDocument.Yes.equalsIgnoreCase(ibanFlag) && StringUtils.isBlank(beneAccountModel.getIbanNumber())) {
			throw new GlobalException(JaxError.BANK_IBAN_EMPTY, "IBAN is required");
		}
	}
	
	private void validateSwiftCode(BeneAccountModel beneAccountModel) {
		validateSwiftCode(beneAccountModel.getBeneficaryCountryId(), beneAccountModel.getCurrencyId(), beneAccountModel.getSwiftCode());
	}

	public void validateSwiftCode(BigDecimal beneBankCountryId, BigDecimal beneBankCurrencyId, String swiftCode) {
		List<ServiceApplicabilityRule> swiftRules = serviceApplicablilityRuleDao.getServiceApplicabilityRulesForBeneficiary(metaData.getCountryId(),
				beneBankCountryId, beneBankCurrencyId, ServiceApplicabilityField.BNFBANK_SWIFT.toString());
		swiftRules.forEach(i -> {
			if (ConstantDocument.Yes.equals(i.getMandatory())) {
				if (StringUtils.isEmpty(swiftCode)) {
					throw new GlobalException(JaxError.BANK_SWIFT_EMPTY, "Swift code is required");
				}
				validateSwiftCode(swiftCode);
			}
		});
		if (!StringUtils.isEmpty(swiftCode)) {
			validateSwiftCode(swiftCode);
		}
	}
	
	public void validateIFscCode(BeneAccountModel beneAccountModel) {
		validateIFscCode(beneAccountModel.getBeneficaryCountryId(), beneAccountModel.getCurrencyId(), beneAccountModel.getIfscCode());
	}

	public void validateIFscCode(BigDecimal beneBankCountryId, BigDecimal beneBankCurrencyId, String ifscCode) {
		List<ServiceApplicabilityRule> swiftRules = serviceApplicablilityRuleDao.getServiceApplicabilityRulesForBeneficiary(metaData.getCountryId(),
				beneBankCountryId, beneBankCurrencyId, ServiceApplicabilityField.BRANCH_IFSC.toString());
		swiftRules.forEach(i -> {
			if (ConstantDocument.Yes.equals(i.getMandatory())) {
				if (StringUtils.isEmpty(ifscCode)) {
					throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "ifsc code is required");
				}
				bankBranchSearchRequestlValidator.validateIfscCode(ifscCode);
			}
		});
		if (!StringUtils.isEmpty(ifscCode)) {
			bankBranchSearchRequestlValidator.validateIfscCode(ifscCode);
		}
	}

	/**
	 * @param beneAccountModel
	 * 
	 */
	private void validateDuplicateBankAccount(BeneAccountModel beneAccountModel) {
		boolean isBangladeshBene = countryService.isBangladeshCountry(beneAccountModel.getBeneficaryCountryId());
		Iterable<BeneficaryAccount> existingAccountItr = beneficiaryAccountDao.findAll(
				BeneficiaryAccountPredicateCreator.createBeneSearchPredicate(beneAccountModel, isBangladeshBene));
		int size = Iterables.size(existingAccountItr);
		if (size > 0) {
			existingAccountItr.forEach(itr -> {
				List<BeneficaryRelationship> beneRelationShip = beneficiaryService
						.getBeneRelationShip(itr.getBeneficaryMasterId(), itr.getBeneficaryAccountSeqId());
				if (beneRelationShip != null && !beneRelationShip.isEmpty()) {
					throw new GlobalException(JaxError.DUPLICATE_BENE_BANK_ACCOUNT, "Duplicate Beneficiary Account");
				}
			});
		}
	}

	/**
	 * @param beneAccountModel
	 * 
	 */
	private void validateBankAccountNumber(BeneAccountModel beneAccountModel) {
		if (StringUtils.isBlank(beneAccountModel.getBankAccountNumber())) {
			return;
		}
		List<BankAccountLength> accontNumLength = bankService.getBankAccountLength(beneAccountModel.getBankId());
		Set<Integer> accNumLength = new HashSet<>();
		accontNumLength.forEach(i -> {
			if (i.getAcLength() != null && i.getAcLength().intValue() > 0) {
				accNumLength.add(i.getAcLength().intValue());
			}
		});
		if (!accNumLength.isEmpty()) {
			boolean isValid = false;
			for (int i : accNumLength) {
				if (beneAccountModel.getBankAccountNumber().length() == i) {
					isValid = true;
				}
			}
			if (!isValid) {
				String validLengths = accNumLength.stream().map(i -> i.toString()).collect(Collectors.joining(":"));
				String errorExpression = jaxUtil
						.buildErrorExpression(JaxError.INVALID_BANK_ACCOUNT_NUM_LENGTH.toString(), validLengths);
				throw new GlobalException(errorExpression, "Invalid Bank Account number length");
			}
		}
	}

	/**
	 * @param beneAccountModel
	 * @return bene account
	 * 
	 */
	public BeneficaryAccount getBeneficaryAccount(BeneAccountModel beneAccountModel) {
		boolean isBangladeshBene = countryService.isBangladeshCountry(beneAccountModel.getBeneficaryCountryId());
		Iterable<BeneficaryAccount> existingAccountItr = beneficiaryAccountDao.findAll(
				BeneficiaryAccountPredicateCreator.createBeneSearchPredicate(beneAccountModel, isBangladeshBene));

		int size = Iterables.size(existingAccountItr);
		if (size > 0) {
			return existingAccountItr.iterator().next();
		} else {
			return null;
		}
	}
	
	/**
	 * @param beneAccountModel
	 * @return bene account
	 * 
	 */
	public BeneficaryAccount getBeneficaryAccountForCash(BeneAccountModel beneAccountModel, BigDecimal beneMasterSeqId) {
		boolean isBangladeshBene = countryService.isBangladeshCountry(beneAccountModel.getBeneficaryCountryId());
		Iterable<BeneficaryAccount> existingAccountItr = beneficiaryAccountDao.findAll(
				BeneficiaryAccountPredicateCreator.createBeneSearchPredicateCash(beneAccountModel, isBangladeshBene, beneMasterSeqId));

		int size = Iterables.size(existingAccountItr);
		if (size > 0) {
			return existingAccountItr.iterator().next();
		} else {
			return null;
		}
	}

	/**
	 * @param benePersonalDetailModel
	 * @return bene master
	 * 
	 */
	public BeneficaryMaster getBeneficaryMaster(BenePersonalDetailModel benePersonalDetailModel) {
		Predicate beneMasterPredicate = beneficiaryPersonalDetailPredicateCreator
				.createBeneSearchPredicate(benePersonalDetailModel);
		Iterable<BeneficaryMaster> existingBeneMaster = iBeneficiaryMasterDao.findAll(beneMasterPredicate);
		int beneMasterCount = Iterables.size(existingBeneMaster);
		if (beneMasterCount > 0) {
			return existingBeneMaster.iterator().next();
		} else {
			return null;
		}
	}

	/**
	 * @param trnxModel
	 * 
	 */
	public void validateDuplicateCashBeneficiary(BeneficiaryTrnxModel trnxModel) {
		BenePersonalDetailModel benePersonalDetailModel = trnxModel.getBenePersonalDetailModel();
		BeneficaryMaster beneMaster = getBeneficaryMaster(benePersonalDetailModel);
		BigDecimal beneMasterSeqId = (beneMaster != null ? beneMaster.getBeneficaryMasterSeqId() : null);
		BeneAccountModel beneAccountModel = trnxModel.getBeneAccountModel();
		BeneficaryAccount beneAccountMaster = getBeneficaryAccountForCash(beneAccountModel, beneMasterSeqId);

		if (beneMaster != null) {
			logger.info("validateDuplicateCashBeneficiary benemaster found: {}", beneMaster.getBeneficaryMasterSeqId());
			trnxModel.setBeneficaryMasterSeqId(beneMaster.getBeneficaryMasterSeqId());
		}

		if (beneAccountMaster != null && beneMaster != null) {
			List<BeneficaryRelationship> beneRelationShip = beneficiaryService.getBeneRelationShip(
					beneMaster.getBeneficaryMasterSeqId(), beneAccountMaster.getBeneficaryAccountSeqId());
			if (beneRelationShip != null && !beneRelationShip.isEmpty()) {
				throw new GlobalException(JaxError.DUPLICATE_BENE_CASH_ACCOUNT, "Duplicate Beneficiary  Cash Account");
			}
		}
	}

	private void validateSwiftCode(String swift) {
		final Pattern pattern = Pattern.compile("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$");
		if (!pattern.matcher(swift).matches()) {
			throw new GlobalException(JaxError.INVALID_BANK_SWIFT, "Invalid swift");
		}

	}
	
	// Black listed Bene check
	public void validateBeneList(BigDecimal beneRelationshipSeqId) {	
		BenificiaryListView beneInfo = null;
		beneInfo = beneficiaryService.getBeneByIdNo(beneRelationshipSeqId);
		
		if (!StringUtils.isBlank(beneInfo.getBenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByName(beneInfo.getBenificaryName());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException(JaxError.BLACK_LISTED_BENEFICIARY.getCode(),
						"The beneficiary you have selected has been black-listed by CBK ");
			}
		}
		
		if (!StringUtils.isBlank(beneInfo.getArbenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByLocalName(beneInfo.getArbenificaryName());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException(JaxError.BLACK_LISTED_ARABIC_BENEFICIARY.getCode(),
						"Beneficiary Arabic name found matching with black list ");
			}
		}
	}

	public BenificiaryListView validateBeneficiary(BigDecimal beneficiaryRelationshipSeqId) {
		BenificiaryListView beneficiaryView = beneficiaryService.getBeneByIdNo(beneficiaryRelationshipSeqId);
		if (beneficiaryView == null) {
			throw new GlobalException(JaxError.DATA_NOT_FOUND,
					"Beneficiary data not found for bene rel id: " + beneficiaryRelationshipSeqId);
		}
		return beneficiaryView;
	}

	public void validateBeneficiaryTrnxModel(BeneficiaryTrnxModel trnxModel) {
		Object benePersonalDetailModel = trnxModel.getBenePersonalDetailModel();
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(benePersonalDetailModel,
				"benePersonalDetailModel");
		BeneAccountModel beneAccountModel = trnxModel.getBeneAccountModel();
		validateBeneAccount(beneAccountModel);
		if (BigDecimal.ONE.equals(beneAccountModel.getServiceGroupId())) {
			validateDuplicateCashBeneficiary(trnxModel);
		}
		benePersonalDetailValidator.validate(trnxModel, errors);
	}
}
