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
import com.amx.jax.util.JaxUtil;

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
		logger.debug("bene Check " + beneDto.getCustomerId() + "\n beneMa :" + beneDto.getBeneficaryMasterSeqId() + "\n Account Id:" + beneDto.getBeneficiaryAccountSeqId() + "\n Rel Seq Id :"
				+ beneDto.getBeneficiaryRelationShipSeqId() + "\n Bank Id " + beneDto.getBankId() + "\n Acc No :" + beneDto.getBankAccountNumber() + "\n Service Id :" + beneDto.getServiceGroupId()
				+ "\n Name :" + beneDto.getBenificaryName() + "\n Bene Country :" + beneDto.getBenificaryCountry() + "\n State :" + beneDto.getStateId() + "\n District :" + beneDto.getDistrictId());
		beneDto.setUpdateNeeded(false);
		List<BeneficiaryErrorStatusDto> errorListDto = new ArrayList<>();
		BeneficiaryErrorStatusDto errorStatusDto = null;
		String errorDesc = null;

		System.out.println("beneDto :" + beneDto.isUpdateNeeded());
		if (!JaxUtil.isNullZeroBigDecimalCheck(beneDto.getLanguageId())) {
			beneDto.setLanguageId(new BigDecimal(1));
		}

		int benePhoneLength = 0;

		if (!StringUtils.isBlank(beneDto.getBenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByName(beneDto.getBenificaryName());
			if (blist != null && !blist.isEmpty()) {

				errorDesc = "English name Of beneficary matching with black listed customer";
				errorStatusDto = this.setBeneError(JaxError.BLACK_LISTED_CUSTOMER.toString(), errorDesc);

				errorListDto.add(errorStatusDto);
			}
		}
		if (!StringUtils.isBlank(beneDto.getArbenificaryName())) {
			List<BlackListModel> blist = blackListDao.getBlackByName(beneDto.getBenificaryName());
			if (blist != null && !blist.isEmpty()) {

				errorDesc = "Arabic name Of beneficary matching with black listed customer";
				errorStatusDto = this.setBeneError(JaxError.BLACK_LISTED_CUSTOMER.toString(), errorDesc);

				errorListDto.add(errorStatusDto);
			}
		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBeneficaryMasterSeqId())) {
			List<BeneficaryRelationship> beneRelationship = beneficiaryRelationshipDao.getBeneRelationshipByBeneMasterId(beneDto.getBeneficaryMasterSeqId(), beneDto.getCustomerId());
			if (beneRelationship.isEmpty()) {

				errorDesc = "RELATIONSHIP NOT UPDATED FOR THIS CUSTOMER AND BENEFICARY.PLEASE UPDATE THE SAME";
				errorStatusDto = this.setBeneError(JaxError.RECORD_NOT_FOUND.toString(), errorDesc);

				errorListDto.add(errorStatusDto);
			}
		} else {
			errorDesc = "Beneficiary master id shouldnot be blank in relationship";
			errorStatusDto = this.setBeneError(JaxError.NULL_CHECK.toString(), errorDesc);
			errorListDto.add(errorStatusDto);
		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBeneficaryMasterSeqId())) {
			List<BeneficaryMaster> beneMasterList = beneficiaryMasterDao.getBeneficiaryByBeneMasterId(beneDto.getBeneficaryMasterSeqId());
			if (beneMasterList.isEmpty()) {
				errorDesc = "INVALID BENEFICARY MASTER";
				errorStatusDto = this.setBeneError(JaxError.RECORD_NOT_FOUND.toString(), errorDesc);

				errorListDto.add(errorStatusDto);
			} else {
				if (org.apache.commons.lang.StringUtils.isBlank(beneMasterList.get(0).getNationality())) {
					errorDesc = "NATIONALITY IS NOT UPDATED.PLEASE UPDATE THE SAME";
					errorStatusDto = this.setBeneError(JaxError.INVALID_NATIONALITY.toString(), errorDesc);

					errorListDto.add(errorStatusDto);
				}
				if (!JaxUtil.isNullZeroBigDecimalCheck(beneMasterList.get(0).getFsCountryMaster())) {

					errorDesc = "HOME COUNTRY IS NOT UPDATED.PLEASE UPDATE THE SAME";
					errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_COUNTRY.toString(), errorDesc);

					errorListDto.add(errorStatusDto);

				}
				List<BeneficaryContact> beneContactList = beneficaryContactDao.getBeneContact(beneDto.getBeneficaryMasterSeqId());
				if (beneContactList.isEmpty()) {

					errorDesc = "HOME COUNTRY IS NOT UPDATED.PLEASE UPDATE THE SAME";
					errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_COUNTRY.toString(), errorDesc);

					errorListDto.add(errorStatusDto);
				} else {

					if (!StringUtils.isBlank(beneContactList.get(0).getTelephoneNumber())) {
						benePhoneLength = beneContactList.get(0).getTelephoneNumber().length();
					} else if (JaxUtil.isNullZeroBigDecimalCheck(beneContactList.get(0).getMobileNumber())) {
						benePhoneLength = beneContactList.get(0).getMobileNumber().toString().length();
					}
					System.out.println("benePhoneLength :" + benePhoneLength);
				}
			}

		} else {

			errorDesc = "Beneficiary master id shouldnot be blank";
			errorStatusDto = this.setBeneError(JaxError.NULL_CHECK.toString(), errorDesc);

			errorListDto.add(errorStatusDto);
		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBeneficiaryAccountSeqId())) {
			List<BeneficaryAccount> beneAccountList = beneficiaryAccountDao.getBeneficiaryByBeneAccountId(beneDto.getBeneficiaryAccountSeqId(), beneDto.getBeneficaryMasterSeqId(),
					beneDto.getApplicationCountryId());
			if (beneAccountList.isEmpty()) {

				errorDesc = "INVALID ACCOUNT SEQ ID";
				errorStatusDto = this.setBeneError(JaxError.RECORD_NOT_FOUND.toString(), errorDesc);

				errorListDto.add(errorStatusDto);

			} else if (!JaxUtil.isNullZeroBigDecimalCheck(beneAccountList.get(0).getBankAccountTypeId()) && beneAccountList.get(0).getServiceGroupId().compareTo(new BigDecimal(2)) == 0) {

				beneDto.setUpdateNeeded(true);

				errorDesc = "ACCOUNT TYPE IS NOT UPDATED.PLEASE UPDATE THE SAME";
				errorStatusDto = this.setBeneError(JaxError.ACCOUNT_TYPE_UPDATE.toString(), errorDesc);

				errorListDto.add(errorStatusDto);

			}
		} else {
			errorDesc = "Beneficiary account id shouldnot be blank";
			errorStatusDto = this.setBeneError(JaxError.NULL_CHECK.toString(), errorDesc);

			errorListDto.add(errorStatusDto);

		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBankId())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBankId(beneDto.getBankId());
			if (bankList.isEmpty()) {
				errorDesc = "Invalid beneficiary bank";
				errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_BANK.toString(), errorDesc);
				errorListDto.add(errorStatusDto);
			}
		} else {
			errorDesc = "Invalid beneficiary bank";
			errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_BANK.toString(), errorDesc);

			errorListDto.add(errorStatusDto);
		}

		if (StringUtils.isBlank(beneDto.getBenificaryName())) {
			String[] beneNameArray = beneDto.getBenificaryName().split(" ");
			for (String stringName : beneNameArray) {
				List<BankBlWorld> bannedList = beneBankWorldDao.getCheckBankBanned(stringName.trim());
				if (!bannedList.isEmpty()) {
					errorDesc = "Beneficiary name is matching with Charitable Institutions /Religious Organizations.Please verify the details with the remitter first and then proceed for carrying out the transaction";
					errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_BANK.toString(), errorDesc);
					errorListDto.add(errorStatusDto);
				}
			}
		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBankId()) && JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBenificaryCountry())) {
			List<BanksView> bankList = bankMasterDao.getBankListByBeneBankIdAndCountry(beneDto.getBankId(), beneDto.getBenificaryCountry());
			if (bankList.isEmpty()) {

				errorDesc = "Invalid beneficiary bank country";
				errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_BANK_CNTRY.toString(), errorDesc);
				errorListDto.add(errorStatusDto);
			}
		} else {

			errorDesc = "Invalid beneficiary bank country";
			errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_BANK_CNTRY.toString(), errorDesc);

			errorListDto.add(errorStatusDto);

		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBenificaryCountry())) {
			List<CountryMasterView> countryList = countryDao.findByLanguageIdAndCountryId(beneDto.getLanguageId(), beneDto.getBenificaryCountry());
			if (countryList.isEmpty()) {
				isUpdateNeeded = true;
				beneDto.setUpdateNeeded(true);
				errorDesc = "Invalid beneficiary country";
				errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_COUNTRY.toString(), errorDesc);
				errorListDto.add(errorStatusDto);

			}
		} else {
			isUpdateNeeded = true;
			beneDto.setUpdateNeeded(true);
			errorDesc = "Invalid beneficiary country";
			errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_COUNTRY.toString(), errorDesc);

			errorListDto.add(errorStatusDto);

		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getServiceGroupId())) {
			if (beneDto.getServiceGroupId().compareTo(new BigDecimal(2)) == 0) {
				if(StringUtils.isBlank(beneDto.getBankAccountNumber())){
					beneDto.setUpdateNeeded(true);
					errorDesc = "Account number should not blank for Banking channel";
					errorStatusDto = this.setBeneError(JaxError.BENE_ACCOUNT_BLANK.toString(), errorDesc);
					errorListDto.add(errorStatusDto);

				}

					if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getBankId())) {
						List<BankAccountLength> accLengthList = bankAccountLengthDao.getBankAccountLength(beneDto.getBankId());
						if(!accLengthList.isEmpty()){
						boolean accNumCheck = Boolean.FALSE;
						for(BankAccountLength acctLength : accLengthList){
							if( !StringUtils.isBlank(beneDto.getBankAccountNumber()) &&  acctLength !=null && acctLength.getAcLength().compareTo(new BigDecimal(beneDto.getBankAccountNumber().length())) == 0){
								accNumCheck = Boolean.TRUE;
								break;
							}
						}
						if(!accNumCheck){
							beneDto.setUpdateNeeded(true);
							errorDesc = "Invalid Beneficiary Account Number length";
							errorStatusDto = this.setBeneError(JaxError.ACCOUNT_LENGTH.toString(), errorDesc);
							errorListDto.add(errorStatusDto);
						}
						
					}
				}
			}
		} else {
			errorDesc = "Account number should not blank for Banking channel";
			errorStatusDto = this.setBeneError(JaxError.BENE_ACCOUNT_BLANK.toString(), errorDesc);
			errorListDto.add(errorStatusDto);
		}

		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getStateId())) {
			List<ViewState> stateList = viewStateDao.getState(beneDto.getBenificaryCountry(), beneDto.getStateId(), beneDto.getLanguageId());
			if (stateList.isEmpty()) {
				beneDto.setUpdateNeeded(true);
				errorDesc = "Invalid beneficiary state";
				errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_STATE.toString(), errorDesc);

				errorListDto.add(errorStatusDto);

			}
		} else {
			isUpdateNeeded = true;
			errorDesc = "Invalid beneficiary state";
			errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_STATE.toString(), errorDesc);
			errorListDto.add(errorStatusDto);
		}

		
		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getDistrictId())) {
			List<ViewDistrict> districtList = viewDistrictDao.getDistrict(beneDto.getStateId(), beneDto.getDistrictId(), beneDto.getLanguageId());
			if (districtList.isEmpty()) {
				beneDto.setUpdateNeeded(true);
				errorDesc = "Invalid beneficiary district";
				errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_DISTRICT.toString(), errorDesc);
				errorListDto.add(errorStatusDto);

			}
		} else {
			if(beneDto.getCountryName()!=null && (!beneDto.getCountryName().equals("INDIA"))){
				beneDto.setUpdateNeeded(true);
				errorDesc = "Invalid beneficiary district";
				errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_DISTRICT.toString(), errorDesc);
				errorListDto.add(errorStatusDto);
			}
		}
	
		if (JaxUtil.isNullZeroBigDecimalCheck(beneDto.getCityId())) {
			List<ViewCity> cityList = cityDao.getCityDescription(beneDto.getDistrictId(), beneDto.getCityId(), beneDto.getLanguageId());

			if (cityList.isEmpty()) {
				beneDto.setUpdateNeeded(true);
				errorDesc = "Invalid beneficiary city";
				errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_CITY.toString(), errorDesc);
				errorListDto.add(errorStatusDto);

			}
		}/* else {
			beneDto.setUpdateNeeded(true);
			errorDesc = "Invalid beneficiary city";
			errorStatusDto = this.setBeneError(JaxError.INVALID_BENE_CITY.toString(), errorDesc);
			errorListDto.add(errorStatusDto);

		}*/
	
		List<ServiceApplicabilityRule> serviceAppList = serviceApplicabilityRuleDao.getServiceApplicabilityRule(beneDto.getApplicationCountryId(), beneDto.getBenificaryCountry(),
				beneDto.getCurrencyId());

		if (serviceAppList.isEmpty()) {
			errorDesc = "Data not found";
			errorStatusDto = this.setBeneError(JaxError.DATA_NOT_FOUND.toString(), errorDesc);
			errorListDto.add(errorStatusDto);

		} else {
			if (JaxUtil.isNullZeroBigDecimalCheck(serviceAppList.get(0).getMinLenght()) && (JaxUtil.isNullZeroBigDecimalCheck(serviceAppList.get(0).getMaxLenght()))) {
				int minLength = serviceAppList.get(0).getMinLenght().intValue();
				int maxLength = serviceAppList.get(0).getMaxLenght().intValue();

				if (minLength > 0 && benePhoneLength < minLength) {
					beneDto.setUpdateNeeded(true);
					errorDesc = "BENEFICARY TELEPHONE NUMBER - MINIMUM LENGTH MUST BE ||" + minLength + " || AND MAXIMUM LENGTH MUST BE ||" + maxLength;
					errorStatusDto = this.setBeneError(JaxError.INVALID_MOB_TELE.toString(), errorDesc);
					errorListDto.add(errorStatusDto);
				}
				if (maxLength > 0 && benePhoneLength > maxLength) {
					beneDto.setUpdateNeeded(true);
					errorDesc = "BENEFICARY TELEPHONE NUMBER - MINIMUM LENGTH MUST BE ||" + minLength + " || AND MAXIMUM LENGTH MUST BE ||" + maxLength;
					errorStatusDto = this.setBeneError(JaxError.INVALID_MOB_TELE.toString(), errorDesc);
					errorListDto.add(errorStatusDto);
				}
			}
		}
		beneDto.setBeneficiaryErrorStatus(errorListDto);
		return beneDto;
	}

	public BeneficiaryErrorStatusDto setBeneError(String errorCode, String errorDesc) {
		BeneficiaryErrorStatusDto errorStatusDto = new BeneficiaryErrorStatusDto();
		errorStatusDto.setErrorCode(errorCode);
		errorStatusDto.setErrorDesc(errorDesc);
		return errorStatusDto;
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
