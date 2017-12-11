package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.jax.dbmodel.BanksView;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IBankAccountLengthDao;
import com.amx.jax.repository.IBankMasterFromViewDao;
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
	
	
	
	

	public String beneCheck(BeneficiaryListDTO beneDto) {
		String error = null;
		
		if(Util.isNullZeroBigDecimalCheck(beneDto.getBankId())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBankId(beneDto.getBankId());
			if(bankList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary bank", JaxError.INVALID_BENE_BANK);
			}
		} else {
			throw new GlobalException("Invalid beneficiary bank", JaxError.INVALID_BENE_BANK);
		}
		
		
		if(Util.isNullZeroBigDecimalCheck(beneDto.getBankId()) && Util.isNullZeroBigDecimalCheck(beneDto.getBenificaryCountry())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBeneBankIdAndCountry(beneDto.getBankId(),beneDto.getBenificaryCountry());
			if(bankList.isEmpty()) {
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
			List<ViewState> stateList = viewStateDao.getState(beneDto.getBenificaryCountry(), beneDto.getStateId(), beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary state", JaxError.INVALID_BENE_STATE);
			}
		} else {
			throw new GlobalException("Invalid beneficiary state", JaxError.INVALID_BENE_STATE);
		}
		
		
		if (Util.isNullZeroBigDecimalCheck(beneDto.getDistrictId())) {
			List<ViewDistrict> stateList = viewDistrictDao.getDistrict(beneDto.getStateId(),beneDto.getDistrictId(),beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
			}
		} else {
			throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getDistrictId())) {
			List<ViewDistrict> stateList = viewDistrictDao.getDistrict(beneDto.getStateId(),beneDto.getDistrictId(),beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
			}
		} else {
			throw new GlobalException("Invalid beneficiary district", JaxError.INVALID_BENE_DISTRICT);
		}
		
		List<ServiceApplicabilityRule> serviceAppList = serviceApplicabilityRuleDao.getServiceApplicabilityRule(beneDto.getApplicationCountryId(), beneDto.getBenificaryCountry(), beneDto.getCurrencyId());
		/*if(serviceAppList.isEmpty()) {
			throw new GlobalException("Data not found", JaxError.DATA_NOT_FOUND);
		}else {
			String benePhone = if(beneDto.getT)
			
			
		}*/
		
		

		return error;
	}

	// NVL(ISACTIVE,' ')

}
