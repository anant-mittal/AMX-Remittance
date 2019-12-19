package com.amx.jax.manager.remittance;

import static com.amx.jax.serviceprovider.service.AbstractFlexFieldManager.ValidationResultKey.PACKAGE_FC_AMOUNT;
import static com.amx.jax.serviceprovider.service.AbstractFlexFieldManager.ValidationResultKey.PREFLEXCALL_COMPLETE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BenePackageRequest;
import com.amx.jax.model.response.customer.BenePackageResponse;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldEntity;
import com.amx.jax.model.response.jaxfield.JaxFieldType;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.remittance.DeliveryModeRepository;
import com.amx.jax.repository.remittance.IViewParameterDetailsRespository;
import com.amx.jax.repository.remittance.RemittanceModeMasterRepository;
import com.amx.jax.serviceprovider.service.AbstractFlexFieldManager;
import com.amx.jax.services.BankService;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PreFlexFieldManager {

	public static final String FC_AMOUNT_FLEX_FIELD_NAME = "PACKAGE_FCAMOUNT";

	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	@Autowired
	MetaData metaData;
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;
	@Autowired
	BankService bankService;
	@Autowired
	RemittanceTransactionRequestValidator remittanceTransactionRequestValidator;
	@Autowired
	IViewParameterDetailsRespository viewParameterDetailsRespository;
	@Autowired
	RemittanceModeMasterRepository remittanceModeMasterRepository;
	@Autowired
	DeliveryModeRepository deliveryModeRepository;
	@Autowired
	AdditionalBankDetailManager additionalBankDetailManager;
	@Autowired
	ApplicationContext appContext;

	Map<String, Object> localVariableMap = new HashMap<>();

	private static final Logger log = LoggerFactory.getLogger(PreFlexFieldManager.class);

	public Map<String, Object> validateBenePackageRequest(BenePackageRequest benePackageRequest) {
		BigDecimal packageFcAmount = null;
		localVariableMap.put("benePackageRequest", benePackageRequest);
		Map<String, Object> validationResults = new HashMap<>();
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		benePackageRequest.populateFlexFieldDtoMap();
		Map<String, FlexFieldDto> requestFlexFields = benePackageRequest.getFlexFieldDtoMap();
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
		}
		BenificiaryListView beneficaryDetails = beneficiaryRepository.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(
				metaData.getCustomerId(), benePackageRequest.getBeneId(), ConstantDocument.Yes);
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		if (routingBankId == null) {
			return validationResults;
		}
		BankMasterMdlv1 routingBank = bankService.getBankById(routingBankId);
		ViewParameterDetails cashSetUp = viewParameterDetailsRespository.findByRecordIdAndCharField1AndCharField2AndNumericField1(
				ConstantDocument.CASH_STRING, routingBank.getBankCode(), beneficaryDetails.getBankCode(), beneficaryDetails.getBranchCode());

		// fetch flex field from parameter setup
		List<ParameterDetailsDto> parameterSetUp = additionalBankDetailManager.fetchServiceProviderFcAmount(benePackageRequest.getBeneId());
		if (cashSetUp != null) {
			requiredFlexFields.addAll(fetchFlexFieldsForCashSetup(cashSetUp, benePackageRequest, beneficaryDetails, parameterSetUp));
		}
		// when size of package is 1 then we are using parameter setup otherwise we are
		// using additional data setup
		if (parameterSetUp.size() == 1) {
			ParameterDetailsDto parameterDto = parameterSetUp.get(0);
			FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(FC_AMOUNT_FLEX_FIELD_NAME);
			JaxConditionalFieldDto jaxConditionalFieldDto = fetchFlexFieldsForParameterSetup(parameterDto);
			requiredFlexFields.add(jaxConditionalFieldDto);
			try {
				packageFcAmount = flexFieldValueInRequest == null ? null : new BigDecimal(flexFieldValueInRequest.getAmieceDescription());
			} catch (NumberFormatException ex) {
				throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Invalid package amount passed");
			}
			if (parameterDto.getAmount() != null && parameterDto.getAmount().doubleValue() > 0) {
				packageFcAmount = parameterDto.getAmount();
			}
			if (packageFcAmount != null) {
				jaxConditionalFieldDto.getField().setDefaultValue(packageFcAmount.toString());
			}
		}
		if (!requiredFlexFields.isEmpty()) {
			log.debug(requiredFlexFields.toString());
			remittanceTransactionRequestValidator.processFlexFields(requiredFlexFields);
		}

		/*
		 * these are default values.These may get overridden by calling respective
		 * flexfield manager's validatePreFlexField method below
		 */
		validationResults.put("requiredFlexFields", requiredFlexFields);
		validationResults.put(PREFLEXCALL_COMPLETE.getName(), getPreFlexCallComplete(requestFlexFields, requiredFlexFields));
		validationResults.put(PACKAGE_FC_AMOUNT.getName(), packageFcAmount);
		try {
			AbstractFlexFieldManager flexFieldManager = (AbstractFlexFieldManager) appContext
					.getBean(routingBank.getBankCode() + AbstractFlexFieldManager.FLEX_FIELD_MANAGER_BEAN_SUFFIX);
			flexFieldManager.validatePreFlexField(requestFlexFields, localVariableMap, validationResults);
		} catch (NoSuchBeanDefinitionException ex) {

		}
		return validationResults;
	}

	private Boolean getPreFlexCallComplete(Map<String, FlexFieldDto> requestFlexFields, List<JaxConditionalFieldDto> requiredFlexFields) {
		Boolean preFlexCallComplete = true;

		for (JaxConditionalFieldDto dto : requiredFlexFields) {
			String fieldName = dto.getField().getName();
			FlexFieldDto valueInRequest = requestFlexFields.get(fieldName);
			if (valueInRequest == null && !FC_AMOUNT_FLEX_FIELD_NAME.equals(fieldName)) {
				preFlexCallComplete = false;
			}
		}

		return preFlexCallComplete;
	}

	private JaxConditionalFieldDto fetchFlexFieldsForParameterSetup(ParameterDetailsDto parameterDetailsDto) {
		JaxConditionalFieldDto dto = new JaxConditionalFieldDto();
		dto.setEntityName(JaxFieldEntity.FLEX_FIELD);
		JaxFieldDto field = new JaxFieldDto();
		field.setDtoPath("flexFields.PACKAGE_FCAMOUNT");
		field.setLabel("Foreign Amount");
		field.setName(FC_AMOUNT_FLEX_FIELD_NAME);
		field.setMaxValue(parameterDetailsDto.getMaxAmount());
		field.setMinValue(parameterDetailsDto.getMinAmount());
		field.setRequired(true);
		if (parameterDetailsDto.getAmount() != null && parameterDetailsDto.getAmount().doubleValue() > 0) {
			field.setDefaultValue(parameterDetailsDto.getAmount().toString());
		}
		field.setType(JaxFieldType.NUMBER.toString());
		dto.setId(parameterDetailsDto.getParameterDetailsId());
		dto.setField(field);
		return dto;
	}

	private Collection<? extends JaxConditionalFieldDto> fetchFlexFieldsForCashSetup(ViewParameterDetails cashSetUp,
			BenePackageRequest benePackageRequest, BenificiaryListView beneficaryDetails, List<ParameterDetailsDto> parameterSetUp) {
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		BigDecimal applicationCountryId = beneficaryDetails.getApplicationCountryId();
		BigDecimal foreignCurrencyId = beneficaryDetails.getCurrencyId();
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		BankMasterMdlv1 routingBank = bankService.getBankById(routingBankId);
		BigDecimal routingCountryId = routingBank.getBankCountryId();
		String remittanceModeCode = cashSetUp.getCharField3();
		String deliveryModeCode = cashSetUp.getCharField4();
		String bankCode = bankService.getBankById(routingBankId).getBankCode();
		// TODO find if routing bank addl flex field validation is needed or not based

		Map<String, FlexFieldDto> requestFlexFields = benePackageRequest.getFlexFieldDtoMap();
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
		}
		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		BigDecimal remittanceModeId = remittanceModeMasterRepository.findByRemittance(remittanceModeCode).getRemittanceModeId();
		BigDecimal deliveryModeId = deliveryModeRepository.findByDeliveryMode(deliveryModeCode).getDeliveryModeId();
		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicabilityForBank(
				applicationCountryId, routingCountryId, foreignCurrencyId, remittanceModeId, deliveryModeId,
				flexiFieldIn.toArray(new String[flexiFieldIn.size()]), routingBankId, ConstantDocument.Yes);
		if (CollectionUtils.isEmpty(additionalDataRequired)) {
			additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicability(applicationCountryId, routingCountryId,
					foreignCurrencyId, remittanceModeId, deliveryModeId, flexiFieldIn.toArray(new String[flexiFieldIn.size()]), ConstantDocument.Yes);
		}
		boolean flexFieldsAdded = false;
		try {
			AbstractFlexFieldManager flexFieldManager = (AbstractFlexFieldManager) appContext
					.getBean(bankCode + AbstractFlexFieldManager.FLEX_FIELD_MANAGER_BEAN_SUFFIX);
			Map<String, Object> result = flexFieldManager.managePreFlexFields(additionalDataRequired, requestFlexFields, cashSetUp, beneficaryDetails,
					requiredFlexFields);
			localVariableMap.putAll(result);
			flexFieldsAdded = true;
		} catch (NoSuchBeanDefinitionException ex) {

		}
		if (!flexFieldsAdded) {
			for (AdditionalDataDisplayView flexField : additionalDataRequired) {
				JaxConditionalFieldDto jaxConditionalFieldDto = remittanceTransactionRequestValidator.getConditionalFieldDto(flexField,
						requestFlexFields, routingCountryId, remittanceModeId, deliveryModeId, foreignCurrencyId, routingBankId, null, true);
				if (jaxConditionalFieldDto != null) {
					requiredFlexFields.add(jaxConditionalFieldDto);
				}
			}
		}
		return requiredFlexFields;
	}

	public BenePackageResponse createBenePackageResponse(Map<String, Object> validationResults) {
		BenePackageResponse resp = new BenePackageResponse();
		List<JaxConditionalFieldDto> requiredFlexFields = (List<JaxConditionalFieldDto>) validationResults.get("requiredFlexFields");
		resp.setRequiredFlexFields(requiredFlexFields);
		if (validationResults.get(PACKAGE_FC_AMOUNT.getName()) != null) {
			resp.setFcAmount(new BigDecimal(validationResults.get(PACKAGE_FC_AMOUNT.getName()).toString()));
		}
		if (validationResults.get(PREFLEXCALL_COMPLETE.getName()) != null) {
			resp.setPreFlexCallComplete((Boolean) validationResults.get(PREFLEXCALL_COMPLETE.getName()));
		}
		return resp;
	}

}
