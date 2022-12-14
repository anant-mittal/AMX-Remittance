package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.GetJaxFieldRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.branchremittance.manager.BranchRemittanceManager;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxDynamicField;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.SwiftMasterView;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.StaffAuthorizationView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.JaxFieldManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldEntity;
import com.amx.jax.model.response.jaxfield.JaxFieldType;
import com.amx.jax.model.response.jaxfield.JaxFieldValueDto;
import com.amx.jax.model.response.remittance.AmlCheckResponseDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.repository.CountryMasterRepository;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.ISwiftMasterDao;
import com.amx.jax.repository.remittance.StaffAuthorizationRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.JaxFieldService;
import com.amx.utils.DateUtil;
import com.google.common.base.Enums;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceAdditionalFieldManager {

	@Autowired
	JaxFieldService jaxFieldService;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	BankService bankService;
	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;

	@Autowired
	BranchRemittanceManager branchRemitManager;
	
	@Autowired
	StaffAuthorizationRepository staffAuthorizationRepository;
	
	@Autowired
	BankMetaService bankMetaService;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	PartnerTransactionManager partnerTransactionManager;
	
	@Autowired
	CountryMasterRepository countryMasterRepository;
	
	@Autowired
	ISwiftMasterDao swiftMasterRepo;
	@Autowired
	JaxFieldManager jaxFieldManager;

	Logger logger = LoggerFactory.getLogger(getClass());

	public void validateAdditionalFields(RemittanceAdditionalBeneFieldModel model, Map<String, Object> remitApplParametersMap) {
	
		
		ApiResponse<JaxConditionalFieldDto> apiResponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.REMITTANCE_ONLINE));
		ApiResponse<JaxConditionalFieldDto> spApiResponse = additionalFlexFieldsServProvider(model, remitApplParametersMap);
		/** for corporate remittance  additional details by Rabil on 08 Aug 2019 **/
		ApiResponse<JaxConditionalFieldDto> interMediateBank1ApiResponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.BENEFICIARY_SWIFT_BANK1));
		ApiResponse<JaxConditionalFieldDto> interMediateBank2ApiResponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.BENEFICIARY_SWIFT_BANK2));
		ApiResponse<JaxConditionalFieldDto> furtherInstruction = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.INSTRUCTION));
		ApiResponse<JaxConditionalFieldDto> beneTeleApiReponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.BNFTELLAB));
		
		// service provider city , street and beneficiary zip code need to populate, but bene zip code check for navigable in service applicability
		List<JaxConditionalFieldDto> allJaxConditionalFields = apiResponse.getResults();
		if(spApiResponse != null && spApiResponse.getResults() != null) {
			//allJaxConditionalFields.addAll(spApiResponse.getResults());
			List<JaxConditionalFieldDto> allSPJaxConditionalFields = spApiResponse.getResults();
			if(allSPJaxConditionalFields != null && allSPJaxConditionalFields.size() != 0) {
				for (JaxConditionalFieldDto jaxConditionalFieldDto : allSPJaxConditionalFields) {
					if(JaxDynamicField.BENE_ZIP_CODE.name().equals(jaxConditionalFieldDto.getField().getName())) {
						JaxConditionalFieldDto beneZipConditionalFieldDto = partnerTransactionManager.checkBeneficiaryZipCodeAvailable(remitApplParametersMap,jaxConditionalFieldDto);
						if(beneZipConditionalFieldDto != null) {
							allJaxConditionalFields.add(beneZipConditionalFieldDto);
						}
					}else {
						allJaxConditionalFields.add(jaxConditionalFieldDto);
					}
				}
			}
		}
		
		if(interMediateBank1ApiResponse!=null && interMediateBank1ApiResponse.getResult()!=null) {
			allJaxConditionalFields.addAll(interMediateBank1ApiResponse.getResults());
		}
		
		if(interMediateBank2ApiResponse!=null && interMediateBank2ApiResponse.getResult()!=null) {
			allJaxConditionalFields.addAll(interMediateBank2ApiResponse.getResults());
		}
		if(furtherInstruction!=null && furtherInstruction.getResult()!=null) {
			allJaxConditionalFields.addAll(furtherInstruction.getResults());
		}
		
		if (beneTeleApiReponse != null && beneTeleApiReponse.getResult() != null) {
			allJaxConditionalFields.addAll(beneTeleApiReponse.getResults());
		}
		
		Map<String, AdditionalDataDisplayView> flexFieldMap = getAdditionalDataDisplayMap(remitApplParametersMap);
		List<JaxConditionalFieldDto> missingJaxConditionalFields = new ArrayList<>();
		Map<String, Object> additionalFields = model.getAdditionalFields();
		boolean isAdditionalFieldMissing = false;
		addDataFromAdditionalDataDisplay(allJaxConditionalFields, flexFieldMap);
		setDefaultDataFromDb(allJaxConditionalFields, model);

		for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
			if (additionalFields == null || additionalFields.get(jaxConditionalField.getField().getName()) == null) {
				if (!isDynamicFieldRequired(jaxConditionalField, model, flexFieldMap)) {
					continue;
				}
				jaxConditionalField.getField().setDtoPath("additionalFields." + jaxConditionalField.getField().getName());
				missingJaxConditionalFields.add(jaxConditionalField);
				isAdditionalFieldMissing = true;
			} else {
				Object fieldValue = additionalFields.get(jaxConditionalField.getField().getName());
				if(fieldValue!=null &&  fieldValue instanceof String) {
					jaxFieldService.validateJaxFieldRegEx(jaxConditionalField.getField(), (String)fieldValue);
				}
			}
		}
		if (isAdditionalFieldMissing) {
			GlobalException ex = new GlobalException(JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED, "Additional fields required");
			ex.setMeta(missingJaxConditionalFields);
			throw ex;
		}
	}

	private void setDefaultDataFromDb(List<JaxConditionalFieldDto> allJaxConditionalFields, RemittanceAdditionalBeneFieldModel model) {
		if (allJaxConditionalFields != null) {
			BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
			BeneficaryMaster beneficaryMaster = beneficiaryService.getBeneficiaryMasterBybeneficaryMasterSeqId(beneficiaryDetail.getBeneficaryMasterSeqId());
			String beneTelePhone = beneficiaryService.getBeneficiaryContactNumber(beneficiaryDetail.getBeneficaryMasterSeqId());
			
			List<JaxFieldValueDto> swiftBeneListDto = getSwiftBankDetails(beneficiaryDetail.getBenificaryCountry());
			
			for (JaxConditionalFieldDto jaxConditionalFieldDto : allJaxConditionalFields) {
				JaxDynamicField jaxDynamicField = JaxDynamicField.valueOf(jaxConditionalFieldDto.getField().getName());

				switch (jaxDynamicField) {
				case BENE_FLAT_NO:
					jaxConditionalFieldDto.getField().setDefaultValue(beneficaryMaster.getFlatNo());
					break;
				case BENE_HOUSE_NO:
					jaxConditionalFieldDto.getField().setDefaultValue(beneficaryMaster.getBuildingNo());
					break;
				case BENE_STREET_NO:
					jaxConditionalFieldDto.getField().setDefaultValue(beneficaryMaster.getStreetNo());
					break;
				case BENE_ZIP_CODE:
					jaxConditionalFieldDto.getField().setDefaultValue(beneficaryMaster.getBeneficiaryZipCode());
					break;
				case BENE_CITY_NAME:
					jaxConditionalFieldDto.getField().setDefaultValue(beneficaryMaster.getCityName());
					break;
				case BENE_TELE_NO:
						jaxConditionalFieldDto.getField().setDefaultValue(beneTelePhone);
					break;
				case BENEFICIARY_SWIFT_BANK1:
					jaxConditionalFieldDto.getField().setPossibleValues(swiftBeneListDto);
					break;
				case BENEFICIARY_SWIFT_BANK2:
					jaxConditionalFieldDto.getField().setPossibleValues(swiftBeneListDto);
					break;	
				
				default:
					break;
				}
			}
		}
	}

	private void addDataFromAdditionalDataDisplay(List<JaxConditionalFieldDto> allJaxConditionalFields, Map<String, AdditionalDataDisplayView> flexFieldMap) {
	for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
			JaxDynamicField jaxDynamicField = Enums.getIfPresent(JaxDynamicField.class, jaxConditionalField.getField().getName()).orNull();
			JaxFieldType jaxFieldType = Enums.getIfPresent(JaxFieldType.class, jaxConditionalField.getField().getType()).orNull();
			if (jaxDynamicField != null && jaxDynamicField.getFlexField() != null) {
				AdditionalDataDisplayView addlDataDisplay = flexFieldMap.get(jaxDynamicField.getFlexField());
				if (addlDataDisplay != null) {
					if (addlDataDisplay.getIsRequired() != null) {
						if (jaxConditionalField.getField().getRequired() != null && !jaxConditionalField.getField().getRequired()) {
							// continue
						} else {
							jaxConditionalField.getField()
									.setRequired(ConstantDocument.Yes.equalsIgnoreCase(addlDataDisplay.getIsRequired()) ? true : false);
						}
					}
					if (jaxConditionalField.getField().getMinLength() == null) {
						jaxConditionalField.getField().setMinLength(addlDataDisplay.getMinLength());
					}
					if (jaxConditionalField.getField().getMaxLength() == null) {
						jaxConditionalField.getField().setMaxLength(addlDataDisplay.getMaxLength());
					}
					if (JaxFieldType.DATE.equals(jaxFieldType)) {
						if (addlDataDisplay.getFieldFormat() == null) {
							jaxConditionalField.getField().getAdditionalValidations().put("format",
									ConstantDocument.MM_DD_YYYY_DATE_FORMAT.toUpperCase());
						} else {
							jaxConditionalField.getField().getAdditionalValidations().put("format", addlDataDisplay.getFieldFormat());
						}
					}
					Map<String, Object> addlValidations = jaxFieldManager.getAdditionalValidations(jaxDynamicField);
					if (addlValidations != null) {
						jaxConditionalField.getField().getAdditionalValidations().putAll(addlValidations);
					}
				}
			}
		}
	}
	
	

	private Map<String, AdditionalDataDisplayView> getAdditionalDataDisplayMap(Map<String, Object> remitApplParametersMap) {
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");

		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicability(applicationCountryId, routingCountryId,
				foreignCurrencyId, remittanceModeId, deliveryModeId, JaxDynamicField.getAllAdditionalFlexFields(), ConstantDocument.No);
		return additionalDataRequired.stream().collect(Collectors.toMap(i -> i.getFlexField(), i -> i, (x1, x2) -> x1));
	}

	private boolean isDynamicFieldRequired(JaxConditionalFieldDto jaxConditionalField, RemittanceAdditionalBeneFieldModel model,
			Map<String, AdditionalDataDisplayView> flexFieldMap) {
		JaxDynamicField jaxDynamicField = JaxDynamicField.valueOf(jaxConditionalField.getField().getName());
		if (jaxDynamicField.getFlexField() != null) {
			AdditionalDataDisplayView addlDataDisplay = flexFieldMap.get(jaxDynamicField.getFlexField());
			if (addlDataDisplay == null) {
				return false;
			}
			if (ConstantDocument.Yes.equalsIgnoreCase(addlDataDisplay.getIsActive())) {
				if (JaxDynamicField.BENE_DOB.equals(jaxDynamicField)) {
					return isBeneDobRequired(model.getBeneId());
				}
				return true;
			} else {
				return false;
			}
		}
		switch (jaxDynamicField) {
		case BENE_BANK_IBAN_NUMBER:
			boolean isBeneIbanFieldRequired = isBeneIbanFieldRequired(model.getBeneId());
			return isBeneIbanFieldRequired;
		default:
			return true;
		}
	}

	private boolean isBeneDobRequired(BigDecimal beneId) {
		BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(beneId);
		if (beneficiaryDetail.getDateOfBirth() == null) {
			return true;
		}
		return false;
	}

	private boolean isBeneIbanFieldRequired(BigDecimal beneId) {
		BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(beneId);
		String ibanFlag = bankService.getBankById(beneficiaryDetail.getBankId()).getIbanFlag();
		if (!ConstantDocument.Yes.equalsIgnoreCase(ibanFlag)) {
			return false;
		}
		if(beneficiaryService.isCashBene(beneficiaryDetail)) {
			return false;
		}
		BeneficaryAccount beneficaryAccount = beneficiaryService.getBeneAccountByAccountSeqId(beneficiaryDetail.getBeneficiaryAccountSeqId());
		if (StringUtils.isBlank(beneficaryAccount.getIbanNumber())) {
			return true;
		}
		return false;
	}

	public void processAdditionalFields(RemittanceAdditionalBeneFieldModel model) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.REMITTANCE_ONLINE));
		List<JaxConditionalFieldDto> allJaxConditionalFields = apiResponse.getResults();
		
		// service Provider details
		ApiResponse<JaxConditionalFieldDto> spApiResponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.SERVICE_PROVIDER));
		
		//Bene telephone number.
		
		ApiResponse<JaxConditionalFieldDto> beneTeleApiReponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.BNFTELLAB));
		
		List<JaxConditionalFieldDto> allSPJaxConditionalFields = spApiResponse.getResults();
		if(allSPJaxConditionalFields != null && allSPJaxConditionalFields.size() != 0) {
			allJaxConditionalFields.addAll(allSPJaxConditionalFields);
		}
		
		if(beneTeleApiReponse!=null && beneTeleApiReponse.getResult()!=null) {
			allJaxConditionalFields.addAll(beneTeleApiReponse.getResults());
		}
				
		Map<String, Object> fieldValues = model.getAdditionalFields();
		if (allJaxConditionalFields != null && fieldValues != null) {
			BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
			BeneficaryMaster beneficaryMaster = beneficiaryService.getBeneficiaryMasterBybeneficaryMasterSeqId(beneficiaryDetail.getBeneficaryMasterSeqId());
			for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
				Object fieldValue = fieldValues.get(jaxConditionalField.getField().getName());
				if (fieldValue == null || StringUtils.isBlank(fieldValue.toString())) {
					continue;
				}
				if (JaxDynamicField.BENE_BANK_IBAN_NUMBER.name().equals(jaxConditionalField.getField().getName())) {
					// set iban number
					String ibanNumber = (String) fieldValue;
					BeneficaryAccount beneficaryAccount = beneficiaryService.getBeneAccountByAccountSeqId(beneficiaryDetail.getBeneficiaryAccountSeqId());
					beneficaryAccount.setIbanNumber(ibanNumber);
					logger.info("setting iban number for bene bank account seq id {} , IBAN : {} ", beneficiaryDetail.getBeneficiaryAccountSeqId(), ibanNumber);
					beneficiaryService.saveBeneAccount(beneficaryAccount);
				}
				if (JaxDynamicField.BENE_FLAT_NO.name().equals(jaxConditionalField.getField().getName()) && fieldValue != null) {
					beneficaryMaster.setFlatNo(fieldValue.toString());
					logger.info("setting flat no number for bene master seq id {} , : {} ", beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
				if (JaxDynamicField.BENE_HOUSE_NO.name().equals(jaxConditionalField.getField().getName()) && fieldValue != null) {
					beneficaryMaster.setBuildingNo(fieldValue.toString());
					logger.info("setting house no number for bene master seq id {} , : {} ", beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
				if (JaxDynamicField.BENE_STREET_NO.name().equals(jaxConditionalField.getField().getName()) && fieldValue != null) {
					beneficaryMaster.setStreetNo(fieldValue.toString());
					logger.info("setting street no number for bene master seq id {} , : {} ", beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
				if (JaxDynamicField.BENE_ZIP_CODE.name().equals(jaxConditionalField.getField().getName()) && fieldValue != null) {
					beneficaryMaster.setBeneficiaryZipCode(fieldValue.toString());
					logger.info("setting zip code for bene master seq id {} , : {} ", beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
				if (JaxDynamicField.BENE_CITY_NAME.name().equals(jaxConditionalField.getField().getName()) && fieldValue != null) {
					beneficaryMaster.setCityName(fieldValue.toString());
					logger.info("setting city name for bene master seq id {} , : {} ", beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
				if (JaxDynamicField.BENE_TELE_NO.name().equals(jaxConditionalField.getField().getName()) && fieldValue != null) {
					BeneficaryContact beneContact = beneficiaryService.getBeneContact(beneficiaryDetail.getBeneficaryMasterSeqId());
					beneContact.setTelephoneNumber(fieldValue.toString());
					if(fieldValue!=null) {
						logger.info("Field Value Mobile Number : " +fieldValue.toString());
						beneContact.setMobileNumber(new BigDecimal(fieldValue.toString()));
					}
					beneficiaryService.saveBeneContact(beneContact);
				}
				if (JaxDynamicField.BENE_DOB.name().equals(jaxConditionalField.getField().getName()) && fieldValue != null) {
					Date dob = DateUtil.parseDate(fieldValue.toString(), ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
					beneficaryMaster.setDateOfBrith(dob);
					logger.info("setting dob for bene master seq id {} , : {} ", beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
			}
		}
	}

	public List<JaxConditionalFieldDto>  validateAmlCheck(RemittanceAdditionalBeneFieldModel model) {
		BigDecimal beneRelId = model.getBeneId();
		BigDecimal localAmount = model.getLocalAmount() == null ? BigDecimal.ZERO : model.getLocalAmount();
		List<JaxConditionalFieldDto> allJaxConditionalFields = null;
		List<AmlCheckResponseDto> amlList = branchRemitManager.amlTranxAmountCheckForRemittance(beneRelId, localAmount);
		
		if(amlList!=null && !amlList.isEmpty()) {
			ApiResponse<JaxConditionalFieldDto> apiResponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.AML_AUTH));
			allJaxConditionalFields = apiResponse.getResults();
			List<JaxConditionalFieldDto> missingJaxConditionalFields = new ArrayList<>();
			CountryBranchMdlv1 countryBranch = bankMetaService.getCountryBranchById(metaData.getCountryBranchId()); 
			List<StaffAuthorizationView> staffList =  staffAuthorizationRepository.findByLocCode(countryBranch.getBranchId());
			for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
					if(JaxDynamicField.AML_MESSAGE.name().equalsIgnoreCase(jaxConditionalField.getField().getName())) {
						jaxConditionalField.getField().setDefaultValue(amlList.get(0)==null?"":amlList.get(0) .getMessageDescription());
					}
					if(JaxDynamicField.AML_USER_NAME.name().equalsIgnoreCase(jaxConditionalField.getField().getName())) {
						jaxConditionalField.getField().setDtoPath("staffUserName");
						List<JaxFieldValueDto> possibleValues =new ArrayList<>();
						JaxFieldDto filedDto = new JaxFieldDto();
						for(StaffAuthorizationView staff : staffList) {	
							JaxFieldValueDto fieldValueDto = new JaxFieldValueDto();
							filedDto.setDefaultValue(staff.getUserName());
							fieldValueDto.setId(staff.getId());
							fieldValueDto.setOptLable(staff.getUserName());
							fieldValueDto.setValue(staff.getUserName());
							possibleValues.add(fieldValueDto);
						}
						jaxConditionalField.getField().setPossibleValues(possibleValues);
					}
					
					if(JaxDynamicField.AML_PASSWORD.name().equalsIgnoreCase(jaxConditionalField.getField().getName())) {
						jaxConditionalField.getField().setDtoPath("staffPassword");
					}
					
					if(JaxDynamicField.AML_REMARKS.name().equalsIgnoreCase(jaxConditionalField.getField().getName())) {
						jaxConditionalField.getField().setDtoPath("amlRemarks");
					}
					
					missingJaxConditionalFields.add(jaxConditionalField);
				}
			}
		return allJaxConditionalFields;
	}
	
	// condition for Service Provider [Home Send] Transaction
	public ApiResponse<JaxConditionalFieldDto> additionalFlexFieldsServProvider(RemittanceAdditionalBeneFieldModel model, Map<String, Object> remitApplParametersMap) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = new ApiResponse<>();
		boolean status = Boolean.FALSE;
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		if(routingBankId != null) {
			BankMasterDTO bankMasterDTO = bankMetaService.getBankMasterDTObyId(routingBankId);
			if(bankMasterDTO.getBankCode().equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.HOME.name())) {
				BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
				BigDecimal beneCountryId = beneficiaryDetail.getBenificaryCountry();
				if(beneCountryId != null) {
					CountryMaster beneCountryMaster = countryMasterRepository.getCountryMasterByCountryId(beneCountryId);
					if(beneCountryMaster != null && bankMasterDTO != null) {
						status = partnerTransactionManager.checkBeneCountryParam(beneCountryMaster.getCountryAlpha3Code());
						if(!status) {
							apiResponse = jaxFieldService.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.SERVICE_PROVIDER));
						}
					}
				}
			}
		}
		
		return apiResponse;
	}

/** @author rabil 
 *  @serialData  19 aug 2019 
 *  @value to fetch intermediate swift bank  
 * **/
private List<JaxFieldValueDto> getSwiftBankDetails(BigDecimal beneBankCountryId){
	List<SwiftMasterView> swiftViewDetails  = swiftMasterRepo.getSwiftMasterDetailsByBeneCountryId(beneBankCountryId);
	
	if(swiftViewDetails!=null && !swiftViewDetails.isEmpty()) {
		return swiftViewDetails.stream().map(x -> {
			FlexFieldDto ffDto = new FlexFieldDto(x.getSerialNumber(), x.getSwiftId(),x.getBankName(),x.getSwiftBIC());
			JaxFieldValueDto dto = new JaxFieldValueDto();
			dto.setId(ffDto.getSrlId());
			dto.setOptLable(ffDto.getAmieceCode()+"-"+ffDto.getAmieceDescription());
			dto.setValue(ffDto);
			return dto;
		}).collect(Collectors.toList());
	}
	return null;
	}
	
	
	
}
