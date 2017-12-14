package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.jax.dbmodel.BanksView;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.dbmodel.bene.BankBlWorld;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IBankAccountLengthDao;
import com.amx.jax.repository.IBankMasterFromViewDao;
import com.amx.jax.repository.IBeneBankBlackCheckDao;
import com.amx.jax.repository.IBeneficiaryAccountDao;
import com.amx.jax.repository.IBeneficiaryMasterDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.repository.IBlackMasterRepository;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.util.Util;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BeneficiaryCheckService {

	@Autowired
	IBankMasterFromViewDao bankMasterDao;

	@Autowired
	IBankAccountLengthDao bankAccountLengthDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	IViewStateDao viewStateDao;

	@Autowired
	IViewDistrictDAO viewDistrictDao;

	@Autowired
	IServiceApplicabilityRuleDao serviceApplicabilityRuleDao;

	@Autowired
	IBeneficiaryAccountDao beneficiaryAccountDao;

	@Autowired
	IBeneficiaryMasterDao beneficiaryMasterDao;

	@Autowired
	IBeneficiaryRelationshipDao beneficiaryRelationshipDao;

	@Autowired
	IBlackMasterRepository blackListDao;
	
	@Autowired
	IBeneBankBlackCheckDao beneBankWorldDao;

	public String beneCheck(BeneficiaryListDTO beneDto) {
		String error = null;

		if (!StringUtils.isBlank(beneDto.getBenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByName(beneDto.getBenificaryName());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException("English name Of beneficary matching with black listed customer",
						JaxError.BLACK_LISTED_CUSTOMER);
			}
		}
		if (!StringUtils.isBlank(beneDto.getArbenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByName(beneDto.getBenificaryName());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException(" Arabic name Of beneficary matching with black listed customer ",
						JaxError.BLACK_LISTED_CUSTOMER);
			}
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBeneficaryMasterSeqId())) {
			List<BeneficaryRelationship> beneRelationship = beneficiaryRelationshipDao
					.getBeneRelationshipByBeneMasterId(beneDto.getBeneficaryMasterSeqId(), beneDto.getCustomerId());
			if (beneRelationship.isEmpty()) {
				throw new GlobalException(
						"RELATIONSHIP NOT UPDATED FOR THIS CUSTOMER AND BENEFICARY.PLEASE UPDATE THE SAME",
						JaxError.RECORD_NOT_FOUND);
			}
		} else {
			throw new GlobalException("Beneficiary master id shouldnot be blank in relationship", JaxError.NULL_CHECK);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBeneficaryMasterSeqId())) {
			List<BeneficaryMaster> beneMasterList = beneficiaryMasterDao
					.getBeneficiaryByBeneMasterId(beneDto.getBeneficaryMasterSeqId());
			if (beneMasterList.isEmpty()) {
				throw new GlobalException("INVALID BENEFICARY MASTER", JaxError.RECORD_NOT_FOUND);
			} else {

				if (org.apache.commons.lang.StringUtils.isBlank(beneMasterList.get(0).getNationality())) {
					throw new GlobalException("NATIONALITY IS NOT UPDATED.PLEASE UPDATE THE SAME",
							JaxError.INVALID_NATIONALITY);
				}
				if (!Util.isNullZeroBigDecimalCheck(beneMasterList.get(0).getFsCountryMaster())) {
					throw new GlobalException("HOME COUNTRY IS NOT UPDATED.PLEASE UPDATE THE SAME",
							JaxError.INVALID_BENE_COUNTRY);

				}
			
			}

		} else {
			throw new GlobalException("Beneficiary master id shouldnot be blank", JaxError.NULL_CHECK);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBeneficiaryAccountSeqId())) {
			List<BeneficaryAccount> beneAccountList = beneficiaryAccountDao.getBeneficiaryByBeneAccountId(
					beneDto.getBeneficiaryAccountSeqId(), beneDto.getBeneficaryMasterSeqId(),
					beneDto.getApplicationCountryId());
			if (beneAccountList.isEmpty()) {
				throw new GlobalException("INVALID ACCOUNT SEQ ID", JaxError.RECORD_NOT_FOUND);
			} else if (!Util.isNullZeroBigDecimalCheck(beneAccountList.get(0).getBankAccountTypeId())
					&& beneAccountList.get(0).getServicegropupId().compareTo(new BigDecimal(2)) == 0) {
				throw new GlobalException("ACCOUNT TYPE IS NOT UPDATED.PLEASE UPDATE THE SAME",
						JaxError.ACCOUNT_TYPE_UPDATE);
			}
		} else {
			throw new GlobalException("Beneficiary account id shouldnot be blank", JaxError.NULL_CHECK);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBankId())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBankId(beneDto.getBankId());
			if (bankList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary bank", JaxError.INVALID_BENE_BANK);
			}
		} else {
			throw new GlobalException("Invalid beneficiary bank", JaxError.INVALID_BENE_BANK);
		}
		
		
		if(StringUtils.isBlank(beneDto.getBenificaryName())) {
			String[] beneNameArray = beneDto.getBenificaryName().split(" ");
			for (String stringName : beneNameArray) {
				List<BankBlWorld> bannedList = beneBankWorldDao.getCheckBankBanned(stringName.trim());
				if(!bannedList.isEmpty()) {
					throw new GlobalException("Beneficiary name is matching with Charitable Institutions /Religious Organizations.Please verify the details with the remitter first and then proceed for carrying out the transaction", JaxError.INVALID_BENE_BANK);
				}
			}
		}
		
		

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBankId())
				&& Util.isNullZeroBigDecimalCheck(beneDto.getBenificaryCountry())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBeneBankIdAndCountry(beneDto.getBankId(),
					beneDto.getBenificaryCountry());
			if (bankList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary bank country", JaxError.INVALID_BENE_BANK_CNTRY);
			}
		} else {
			throw new GlobalException("Invalid beneficiary bank country", JaxError.INVALID_BENE_BANK_CNTRY);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBenificaryCountry())) {
			List<CountryMasterView> countryList = countryDao.findByLanguageIdAndCountryId(beneDto.getLanguageId(),
					beneDto.getBenificaryCountry());
			if (countryList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary country", JaxError.INVALID_BENE_COUNTRY);
			}
		} else {
			throw new GlobalException("Invalid beneficiary country", JaxError.INVALID_BENE_COUNTRY);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getServiceGroupId())) {
			if (beneDto.getServiceGroupId().compareTo(new BigDecimal(2)) == 0) {
				if (beneDto.getBankAccountNumber().isEmpty() || beneDto.getBankAccountNumber() == null) {
					throw new GlobalException("Account number should not blank for Banking channel",
							JaxError.BENE_ACCOUNT_BLANK);
				}
			}
		} else {
			throw new GlobalException("Account number should not blank for Banking channel",
					JaxError.BENE_ACCOUNT_BLANK);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getStateId())) {
			List<ViewState> stateList = viewStateDao.getState(beneDto.getBenificaryCountry(), beneDto.getStateId(),
					beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary state", JaxError.INVALID_BENE_STATE);
			}
		} else {
			throw new GlobalException("Invalid beneficiary state", JaxError.INVALID_BENE_STATE);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getDistrictId())) {
			List<ViewDistrict> stateList = viewDistrictDao.getDistrict(beneDto.getStateId(), beneDto.getDistrictId(),
					beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
			}
		} else {
			throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getDistrictId())) {
			List<ViewDistrict> stateList = viewDistrictDao.getDistrict(beneDto.getStateId(), beneDto.getDistrictId(),
					beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
			}
		} else {
			throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
		}

		List<ServiceApplicabilityRule> serviceAppList = serviceApplicabilityRuleDao.getServiceApplicabilityRule(
				beneDto.getApplicationCountryId(), beneDto.getBenificaryCountry(), beneDto.getCurrencyId());
		/*
		 * if(serviceAppList.isEmpty()) { throw new GlobalException("Data not found",
		 * JaxError.DATA_NOT_FOUND); }else { String benePhone = if(beneDto.getT)
		 * 
		 * 
		 * }
		 */

		return error;
	}

	// NVL(ISACTIVE,' ')

}
