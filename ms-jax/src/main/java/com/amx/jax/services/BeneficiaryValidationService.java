package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.dbmodel.bene.BankAccountLength;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IBeneficiaryAccountDao;
import com.amx.jax.repository.IBeneficiaryMasterDao;
import com.amx.jax.service.CountryService;
import com.amx.jax.util.JaxUtil;
import com.google.common.collect.Iterables;
import com.querydsl.core.types.Predicate;
import com.amx.jax.dbmodel.bene.predicate.BeneficiaryAccountPredicateCreator;
import com.amx.jax.dbmodel.bene.predicate.BeneficiaryPersonalDetailPredicateCreator;

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

	/**
	 * @param beneAccountModel
	 * 
	 */
	public void validateBeneAccount(BeneAccountModel beneAccountModel) {

		// validate only for BANK channel and not for CASH channel
		if (!BigDecimal.ONE.equals(beneAccountModel.getServiceGroupId())) {
			validateBankAccountNumber(beneAccountModel);
			validateDuplicateBankAccount(beneAccountModel);
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
					throw new GlobalException("Duplicate Beneficiary Account", JaxError.DUPLICATE_BENE_BANK_ACCOUNT);
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
				String validLengths = accNumLength.stream().map(i -> i.toString()).collect(Collectors.joining(","));
				String errorExpression = jaxUtil
						.buildErrorExpression(JaxError.INVALID_BANK_ACCOUNT_NUM_LENGTH.toString(), validLengths);
				throw new GlobalException("Invalid Bank Account number length", errorExpression);
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
		BeneAccountModel beneAccountModel = trnxModel.getBeneAccountModel();
		BeneficaryAccount beneAccountMaster = getBeneficaryAccount(beneAccountModel);
		BenePersonalDetailModel benePersonalDetailModel = trnxModel.getBenePersonalDetailModel();
		BeneficaryMaster beneMaster = getBeneficaryMaster(benePersonalDetailModel);
		if (beneMaster != null) {
			logger.info("validateDuplicateCashBeneficiary benemaster found: {}", beneMaster.getBeneficaryMasterSeqId());
			trnxModel.setBeneficaryMasterSeqId(beneMaster.getBeneficaryMasterSeqId());
		}
		if (beneAccountMaster != null) {
			logger.info("validateDuplicateCashBeneficiary beneaccount found: {}",
					beneAccountMaster.getBeneficaryAccountSeqId());
			trnxModel.setBeneficaryAccountSeqId(beneAccountMaster.getBeneficaryAccountSeqId());
		}
		if (beneAccountMaster != null && beneMaster != null) {
			List<BeneficaryRelationship> beneRelationShip = beneficiaryService.getBeneRelationShip(
					beneMaster.getBeneficaryMasterSeqId(), beneAccountMaster.getBeneficaryAccountSeqId());
			if (beneRelationShip != null && !beneRelationShip.isEmpty()) {
				throw new GlobalException("Duplicate Beneficiary  Cash Account", JaxError.DUPLICATE_BENE_CASH_ACCOUNT);
			}
		}
	}

}
