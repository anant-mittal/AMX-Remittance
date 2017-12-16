package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BeneficiaryErrorStatusDto;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.dbmodel.BanksView;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.dbmodel.bene.BankAccountLength;
import com.amx.jax.dbmodel.bene.BankBlWorld;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IBankAccountLengthDao;
import com.amx.jax.repository.IBankMasterFromViewDao;
import com.amx.jax.repository.IBeneBankBlackCheckDao;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryAccountDao;
import com.amx.jax.repository.IBeneficiaryMasterDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.repository.IBlackMasterRepository;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.util.Util;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BeneficiaryCheckService extends AbstractService {

	private Logger logger = Logger.getLogger(BeneficiaryCheckService.class);

	@Autowired
	IBankMasterFromViewDao bankMasterDao;

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

	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IBankAccountLengthDao bankAccountLengthDao;
	
	@Autowired
	IBeneficaryContactDao beneficaryContactDao;
	
	@Autowired
	JaxMetaInfo jaxMetaInfo;

	public BeneficiaryListDTO beneCheck(BeneficiaryListDTO beneDto) {
		boolean isUpdateNeeded = false;
		logger.info("bene Check "+beneDto.getCustomerId()+"\n beneMa :"+beneDto.getBeneficaryMasterSeqId()
		+"\n Account Id:"+beneDto.getBeneficiaryAccountSeqId()+"\n Rel Seq Id :"+beneDto.getBeneficiaryRelationShipSeqId()
		+"\n Bank Id "+beneDto.getBankId()+"\n Acc No :"+beneDto.getBankAccountNumber()
		+"\n Service Id :"+beneDto.getServiceGroupId()+"\n Name :"+beneDto.getBenificaryName()
		+"\n Bene Country :"+beneDto.getBenificaryCountry()+"\n State :"+beneDto.getStateId()
		+"\n District :"+beneDto.getDistrictId());
		beneDto.setUpdateNeeded(false);
		List<BeneficiaryErrorStatusDto> errorListDto = new ArrayList<>();
		BeneficiaryErrorStatusDto errorStatusDto = new BeneficiaryErrorStatusDto();
		
		System.out.println("beneDto :"+beneDto.isUpdateNeeded());
		if(!Util.isNullZeroBigDecimalCheck(beneDto.getLanguageId())){
			beneDto.setLanguageId(new BigDecimal(1));
		}
		
		int benePhoneLength = 0;

		if (!StringUtils.isBlank(beneDto.getBenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByName(beneDto.getBenificaryName());
			if (blist != null && !blist.isEmpty()) {
				
				errorStatusDto.setErrorCode(JaxError.BLACK_LISTED_CUSTOMER.toString());
				errorStatusDto.setErrorDesc("English name Of beneficary matching with black listed customer");
				errorListDto.add(errorStatusDto);		
			}
		}
		if (!StringUtils.isBlank(beneDto.getArbenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByName(beneDto.getBenificaryName());
			if (blist != null && !blist.isEmpty()) {
				
				errorStatusDto.setErrorCode(JaxError.BLACK_LISTED_CUSTOMER.toString());
				errorStatusDto.setErrorDesc("Arabic name Of beneficary matching with black listed customer");
				errorListDto.add(errorStatusDto);	
			}
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBeneficaryMasterSeqId())) {
			List<BeneficaryRelationship> beneRelationship = beneficiaryRelationshipDao
					.getBeneRelationshipByBeneMasterId(beneDto.getBeneficaryMasterSeqId(), beneDto.getCustomerId());
			if (beneRelationship.isEmpty()) {
				errorStatusDto.setErrorCode(JaxError.RECORD_NOT_FOUND.toString());
				errorStatusDto.setErrorDesc("RELATIONSHIP NOT UPDATED FOR THIS CUSTOMER AND BENEFICARY.PLEASE UPDATE THE SAME");
				errorListDto.add(errorStatusDto);		
			}
		} else {
			errorStatusDto.setErrorCode(JaxError.NULL_CHECK.toString());
			errorStatusDto.setErrorDesc("Beneficiary master id shouldnot be blank in relationship");
			errorListDto.add(errorStatusDto);	
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBeneficaryMasterSeqId())) {
			List<BeneficaryMaster> beneMasterList = beneficiaryMasterDao
					.getBeneficiaryByBeneMasterId(beneDto.getBeneficaryMasterSeqId());
			if (beneMasterList.isEmpty()) {
				errorStatusDto.setErrorCode(JaxError.RECORD_NOT_FOUND.toString());
				errorStatusDto.setErrorDesc("INVALID BENEFICARY MASTER");
				errorListDto.add(errorStatusDto);	
			} else {
				if (org.apache.commons.lang.StringUtils.isBlank(beneMasterList.get(0).getNationality())) {
					errorStatusDto.setErrorCode(JaxError.INVALID_NATIONALITY.toString());
					errorStatusDto.setErrorDesc("NATIONALITY IS NOT UPDATED.PLEASE UPDATE THE SAME");
					errorListDto.add(errorStatusDto);	
				}
				if (!Util.isNullZeroBigDecimalCheck(beneMasterList.get(0).getFsCountryMaster())) {
					
					errorStatusDto.setErrorCode(JaxError.INVALID_BENE_COUNTRY.toString());
					errorStatusDto.setErrorDesc("HOME COUNTRY IS NOT UPDATED.PLEASE UPDATE THE SAME");
					errorListDto.add(errorStatusDto);		

				}
				List<BeneficaryContact> beneContactList = beneficaryContactDao.getBeneContact(beneDto.getBeneficaryMasterSeqId());
				if(beneContactList.isEmpty()) {
					
					errorStatusDto.setErrorCode(JaxError.INVALID_BENE_COUNTRY.toString());
					errorStatusDto.setErrorDesc("HOME COUNTRY IS NOT UPDATED.PLEASE UPDATE THE SAME");
					errorListDto.add(errorStatusDto);	
				}else {
					
							if(!beneContactList.get(0).getTelephoneNumber().isEmpty()) {
								benePhoneLength = beneContactList.get(0).getTelephoneNumber().length();
							}else if(Util.isNullZeroBigDecimalCheck(beneContactList.get(0).getMobileNumber())){
								benePhoneLength = beneContactList.get(0).getMobileNumber().toString().length();
							}
						System.out.println("benePhoneLength :"+benePhoneLength);	
				}
			}

		} else {
			errorStatusDto.setErrorCode(JaxError.NULL_CHECK.toString());
			errorStatusDto.setErrorDesc("Beneficiary master id shouldnot be blank");
			errorListDto.add(errorStatusDto);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBeneficiaryAccountSeqId())) {
			List<BeneficaryAccount> beneAccountList = beneficiaryAccountDao.getBeneficiaryByBeneAccountId(
					beneDto.getBeneficiaryAccountSeqId(), beneDto.getBeneficaryMasterSeqId(),
					beneDto.getApplicationCountryId());
			if (beneAccountList.isEmpty()) {
				errorStatusDto.setErrorCode(JaxError.RECORD_NOT_FOUND.toString());
				errorStatusDto.setErrorDesc("INVALID ACCOUNT SEQ ID");
				errorListDto.add(errorStatusDto);
				
			} else if (!Util.isNullZeroBigDecimalCheck(beneAccountList.get(0).getBankAccountTypeId())
					&& beneAccountList.get(0).getServicegropupId().compareTo(new BigDecimal(2)) == 0) {
			
				beneDto.setUpdateNeeded(true);
				errorStatusDto.setErrorCode(JaxError.ACCOUNT_TYPE_UPDATE.toString());
				errorStatusDto.setErrorDesc("ACCOUNT TYPE IS NOT UPDATED.PLEASE UPDATE THE SAME");
				errorListDto.add(errorStatusDto);
				
			}
		} else {
			errorStatusDto.setErrorCode(JaxError.NULL_CHECK.toString());
			errorStatusDto.setErrorDesc("Beneficiary account id shouldnot be blank");
			errorListDto.add(errorStatusDto);
			
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBankId())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBankId(beneDto.getBankId());
			if (bankList.isEmpty()) {
				errorStatusDto.setErrorCode(JaxError.INVALID_BENE_BANK.toString());
				errorStatusDto.setErrorDesc("Invalid beneficiary bank");
				errorListDto.add(errorStatusDto);
			}
		} else {
			errorStatusDto.setErrorCode(JaxError.INVALID_BENE_BANK.toString());
			errorStatusDto.setErrorDesc("Invalid beneficiary bank");
			errorListDto.add(errorStatusDto);
		}
		
		
		if(StringUtils.isBlank(beneDto.getBenificaryName())) {
			String[] beneNameArray = beneDto.getBenificaryName().split(" ");
			for (String stringName : beneNameArray) {
				List<BankBlWorld> bannedList = beneBankWorldDao.getCheckBankBanned(stringName.trim());
				if(!bannedList.isEmpty()) {
					errorStatusDto.setErrorCode(JaxError.INVALID_BENE_BANK.toString());
					errorStatusDto.setErrorDesc("Beneficiary name is matching with Charitable Institutions /Religious Organizations.Please verify the details with the remitter first and then proceed for carrying out the transaction");
					errorListDto.add(errorStatusDto);
				}
			}
		}
		
		

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBankId())
				&& Util.isNullZeroBigDecimalCheck(beneDto.getBenificaryCountry())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBeneBankIdAndCountry(beneDto.getBankId(),
					beneDto.getBenificaryCountry());
			if (bankList.isEmpty()) {
				
				errorStatusDto.setErrorCode(JaxError.INVALID_BENE_BANK_CNTRY.toString());
				errorStatusDto.setErrorDesc("Invalid beneficiary bank country");
				errorListDto.add(errorStatusDto);
			}
		} else {
			errorStatusDto.setErrorCode(JaxError.INVALID_BENE_BANK_CNTRY.toString());
			errorStatusDto.setErrorDesc("Invalid beneficiary bank country");
			errorListDto.add(errorStatusDto);
			
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getBenificaryCountry())) {
			List<CountryMasterView> countryList = countryDao.findByLanguageIdAndCountryId(beneDto.getLanguageId(),
					beneDto.getBenificaryCountry());
			if (countryList.isEmpty()) {
				isUpdateNeeded =true;
				beneDto.setUpdateNeeded(true);
				
				errorStatusDto.setErrorCode(JaxError.INVALID_BENE_COUNTRY.toString());
				errorStatusDto.setErrorDesc("Invalid beneficiary country");
				errorListDto.add(errorStatusDto);
			
			}
		} else {
			isUpdateNeeded =true;
			beneDto.setUpdateNeeded(true);
			errorStatusDto.setErrorCode(JaxError.INVALID_BENE_COUNTRY.toString());
			errorStatusDto.setErrorDesc("Invalid beneficiary country");
			errorListDto.add(errorStatusDto);
			
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getServiceGroupId())) {
			if (beneDto.getServiceGroupId().compareTo(new BigDecimal(2)) == 0) {
				if (beneDto.getBankAccountNumber().isEmpty() || beneDto.getBankAccountNumber() == null) {
					errorStatusDto.setErrorCode(JaxError.BENE_ACCOUNT_BLANK.toString());
					errorStatusDto.setErrorDesc("Account number should not blank for Banking channel");
					errorListDto.add(errorStatusDto);
						
				}
				
				
				if(Util.isNullZeroBigDecimalCheck(beneDto.getBankId())) {
					List<BankAccountLength> accLengthList  =bankAccountLengthDao.getBankAccountLength(beneDto.getBankId());
					if(accLengthList.isEmpty()) {
						errorStatusDto.setErrorCode(JaxError.ACCOUNT_LENGTH.toString());
						errorStatusDto.setErrorDesc("Invalid Beneficiary Account Number length");
						errorListDto.add(errorStatusDto);
						
					}
					if(accLengthList.get(0).getAcLength().compareTo(new BigDecimal(beneDto.getBankAccountNumber().length()))!=0){
						errorStatusDto.setErrorCode(JaxError.ACCOUNT_LENGTH.toString());
						errorStatusDto.setErrorDesc("Invalid Beneficiary Account Number length");
						errorListDto.add(errorStatusDto);
					
					}
				}
				
				
			}
		} else {
			errorStatusDto.setErrorCode(JaxError.BENE_ACCOUNT_BLANK.toString());
			errorStatusDto.setErrorDesc("Account number should not blank for Banking channel");
			errorListDto.add(errorStatusDto);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getStateId())) {
			List<ViewState> stateList = viewStateDao.getState(beneDto.getBenificaryCountry(), beneDto.getStateId(),
					beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				beneDto.setUpdateNeeded(true);
				errorStatusDto.setErrorCode(JaxError.INVALID_BENE_STATE.toString());
				errorStatusDto.setErrorDesc("Invalid beneficiary state");
				errorListDto.add(errorStatusDto);
				
				
			}
		} else {
			isUpdateNeeded =true;
			errorStatusDto.setErrorCode(JaxError.INVALID_BENE_STATE.toString());
			errorStatusDto.setErrorDesc("Invalid beneficiary state");
			errorListDto.add(errorStatusDto);
		}

		if (Util.isNullZeroBigDecimalCheck(beneDto.getDistrictId())) {
			List<ViewDistrict> districtList = viewDistrictDao.getDistrict(beneDto.getStateId(), beneDto.getDistrictId(),
					beneDto.getLanguageId());
			if (districtList.isEmpty()) {
				beneDto.setUpdateNeeded(true);
				errorStatusDto.setErrorCode(JaxError.INVALID_BENE_DISTRICT.toString());
				errorStatusDto.setErrorDesc("Invalid beneficiary district");
				errorListDto.add(errorStatusDto);
				
			}
		} else {
			beneDto.setUpdateNeeded(true);
			errorStatusDto.setErrorCode(JaxError.INVALID_BENE_DISTRICT.toString());
			errorStatusDto.setErrorDesc("Invalid beneficiary district");
			errorListDto.add(errorStatusDto);
		}

		
		
		if (Util.isNullZeroBigDecimalCheck(beneDto.getCityId())) {
			List<ViewCity> cityList = cityDao.getCityDescription(beneDto.getDistrictId(),beneDto.getCityId(),beneDto.getLanguageId());
				
			if (cityList.isEmpty()) {
				beneDto.setUpdateNeeded(true);
				errorStatusDto.setErrorCode(JaxError.INVALID_BENE_CITY.toString());
				errorStatusDto.setErrorDesc("Invalid beneficiary city");
				errorListDto.add(errorStatusDto);
				
			}
		} else {
			beneDto.setUpdateNeeded(true);
			errorStatusDto.setErrorCode(JaxError.INVALID_BENE_CITY.toString());
			errorStatusDto.setErrorDesc("Invalid beneficiary city");
			errorListDto.add(errorStatusDto);
			
		}
		List<ServiceApplicabilityRule> serviceAppList = serviceApplicabilityRuleDao.getServiceApplicabilityRule(beneDto.getApplicationCountryId(), beneDto.getBenificaryCountry(), beneDto.getCurrencyId());
		
		  if(serviceAppList.isEmpty()) {
			  	errorStatusDto.setErrorCode(JaxError.DATA_NOT_FOUND.toString());
				errorStatusDto.setErrorDesc("Data not found");
				errorListDto.add(errorStatusDto);
			  }
		  else {
			  if(Util.isNullZeroBigDecimalCheck(serviceAppList.get(0).getMinLenght()) && (Util.isNullZeroBigDecimalCheck(serviceAppList.get(0).getMaxLenght()))) {
				  int minLength = serviceAppList.get(0).getMinLenght().intValue();
				  int maxLength = serviceAppList.get(0).getMaxLenght().intValue();
				  
				  if(minLength>0 &&  benePhoneLength<minLength) {
					    beneDto.setUpdateNeeded(true);
						errorStatusDto.setErrorCode(JaxError.INVALID_MOB_TELE.toString());
						errorStatusDto.setErrorDesc("BENEFICARY TELEPHONE NUMBER - MINIMUM LENGTH MUST BE ||"+minLength+" || AND MAXIMUM LENGTH MUST BE ||"+maxLength);
						errorListDto.add(errorStatusDto);
					  }
				  if(maxLength>0 && benePhoneLength >maxLength) {
					 	beneDto.setUpdateNeeded(true);
					 	errorStatusDto.setErrorCode(JaxError.INVALID_MOB_TELE.toString());
						errorStatusDto.setErrorDesc("BENEFICARY TELEPHONE NUMBER - MINIMUM LENGTH MUST BE ||"+minLength+" || AND MAXIMUM LENGTH MUST BE ||"+maxLength);
						errorListDto.add(errorStatusDto);  
				  }
			  }
		     
		 
		  }
		 beneDto.setBeneficiaryErrorStatus(errorListDto);

		return beneDto;
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	// NVL(ISACTIVE,' ')

}
