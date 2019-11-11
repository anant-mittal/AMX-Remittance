package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BenePackageRequest;
import com.amx.jax.model.response.customer.BenePackageResponse;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldEntity;
import com.amx.jax.model.response.jaxfield.JaxFieldValueDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.remittance.DeliveryModeRepository;
import com.amx.jax.repository.remittance.IViewParameterDetailsRespository;
import com.amx.jax.repository.remittance.RemittanceModeMasterRepository;
import com.amx.jax.services.BankService;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;

@Component
public class PreFlexFieldManager {

	public static final String FC_AMOUNT_FLEX_FIELD_NAME = "PACKAGE_FCAMOUNT";
	public static final String INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL = ">";

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

	private static final Logger log = LoggerFactory.getLogger(PreFlexFieldManager.class);

	public Map<String, Object> validateBenePackageRequest(BenePackageRequest benePackageRequest) {
		BigDecimal packageFcAmount = null;
		BigDecimal monthlyContribution = null;
		BigDecimal volunteerContribution = null;
		int noOfMonth = 0;
		benePackageRequest.populateFlexFieldDtoMap();
		Map<String, FlexFieldDto> requestFlexFields = benePackageRequest.getFlexFieldDtoMap();
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
		}
		BenificiaryListView beneficaryDetails = beneficiaryRepository
				.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(metaData.getCustomerId(),
						benePackageRequest.getBeneId(), ConstantDocument.Yes);
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		BankMasterModel routingBank = bankService.getBankById(routingBankId);
		ViewParameterDetails cashSetUp = viewParameterDetailsRespository
				.findByRecordIdAndCharField1AndCharField2AndNumericField1(ConstantDocument.CASH_STRING,
						routingBank.getBankCode(), beneficaryDetails.getBankCode(), beneficaryDetails.getBranchCode());
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		// when size of package is 1 then we are using parameter setup otherwise we are
		// using additional data setup
		if (cashSetUp != null) {
			requiredFlexFields.addAll(fetchFlexFieldsForCashSetup(cashSetUp, benePackageRequest, beneficaryDetails));
		} else {
			// fetch flex field from parameter setup
			List<ParameterDetailsDto> parameterSetUp = additionalBankDetailManager
					.fetchServiceProviderFcAmount(benePackageRequest.getBeneId());
			if (parameterSetUp.size() == 1) {
				ParameterDetailsDto parameterDto = parameterSetUp.get(0);
				FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(FC_AMOUNT_FLEX_FIELD_NAME);
				JaxConditionalFieldDto jaxConditionalFieldDto = fetchFlexFieldsForParameterSetup(parameterDto);
				requiredFlexFields.add(jaxConditionalFieldDto);
				packageFcAmount = flexFieldValueInRequest == null ? null
						: new BigDecimal(flexFieldValueInRequest.getAmieceDescription());
				if (parameterDto.getAmount() != null && parameterDto.getAmount().doubleValue() > 0) {
					packageFcAmount = parameterDto.getAmount();
				}
				if (packageFcAmount != null) {
					jaxConditionalFieldDto.getField().setDefaultValue(packageFcAmount.toString());
				}
			}
		}
		if (!requiredFlexFields.isEmpty()) {
			log.debug(requiredFlexFields.toString());
			remittanceTransactionRequestValidator.processFlexFields(requiredFlexFields);
		}
		if (requestFlexFields != null) {
			for (Entry<String, FlexFieldDto> element : requestFlexFields.entrySet()) {
				String k = element.getKey();
				FlexFieldDto v = element.getValue();
				if ("INDIC16".equals(k)) {
					monthlyContribution = new BigDecimal(v.getAmieceCode());
				}
				if ("INDIC17".equals(k)) {
					volunteerContribution = new BigDecimal(v.getAmieceCode());
				}
				if ("INDIC18".equals(k)) {
					noOfMonth = Integer.parseInt(v.getAmieceCode());
				}
			}
		}
		Map<String, Object> validationResults = new HashMap<>();
		if (monthlyContribution != null) {
			packageFcAmount = monthlyContribution.multiply(BigDecimal.valueOf(noOfMonth));
		}
		if (volunteerContribution != null) {
			packageFcAmount = monthlyContribution.add(volunteerContribution).multiply(BigDecimal.valueOf(noOfMonth));
		}
		validationResults.put("PACKAGE_FC_AMOUNT", packageFcAmount);
		validationResults.put("requiredFlexFields", requiredFlexFields);
		return validationResults;
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
		field.setType("TEXT");
		dto.setField(field);
		return dto;
	}

	private Collection<? extends JaxConditionalFieldDto> fetchFlexFieldsForCashSetup(ViewParameterDetails cashSetUp,
			BenePackageRequest benePackageRequest, BenificiaryListView beneficaryDetails) {
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		BigDecimal applicationCountryId = beneficaryDetails.getApplicationCountryId();
		BigDecimal foreignCurrencyId = beneficaryDetails.getCurrencyId();
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		BankMasterModel routingBank = bankService.getBankById(routingBankId);
		BigDecimal routingCountryId = routingBank.getBankCountryId();
		String remittanceModeCode = cashSetUp.getCharField3();
		String deliveryModeCode = cashSetUp.getCharField4();
		Map<String, FlexFieldDto> requestFlexFields = benePackageRequest.getFlexFieldDtoMap();
		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		BigDecimal remittanceModeId = remittanceModeMasterRepository.findByRemittance(remittanceModeCode)
				.getRemittanceModeId();
		BigDecimal deliveryModeId = deliveryModeRepository.findByDeliveryMode(deliveryModeCode).getDeliveryModeId();
		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao
				.getAdditionalDataFromServiceApplicabilityForBank(applicationCountryId, routingCountryId,
						foreignCurrencyId, remittanceModeId, deliveryModeId,
						flexiFieldIn.toArray(new String[flexiFieldIn.size()]), routingBankId, ConstantDocument.Yes);
		boolean volunteerContributionIndic = false;
		boolean volunteerContributionSelected = false;
		for (AdditionalDataDisplayView flexField : additionalDataRequired) {
			FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(flexField.getFlexField());
			JaxConditionalFieldDto jaxConditionalFieldDto = remittanceTransactionRequestValidator
					.getConditionalFieldDto(flexField, requestFlexFields, routingCountryId, remittanceModeId,
							deliveryModeId, foreignCurrencyId, routingBankId, null, true);
			sortPossibleValues(jaxConditionalFieldDto);
			if ("INDIC16".equals(flexField.getFlexField()) && flexFieldValueInRequest != null
					&& flexFieldValueInRequest.getAmieceCode().contains(INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL)) {
				volunteerContributionIndic = true;
			}
			if ("INDIC17".equals(flexField.getFlexField()) && flexFieldValueInRequest != null) {
				volunteerContributionSelected = true;
			}
			if (jaxConditionalFieldDto != null) {
				requiredFlexFields.add(jaxConditionalFieldDto);
			}
		}
		if (volunteerContributionIndic && !volunteerContributionSelected) {
			log.debug("volunteer Contribution Selected");
		} else {
			// remove volunteer Indic 17
			Iterator<JaxConditionalFieldDto> itr = requiredFlexFields.iterator();
			while (itr.hasNext()) {
				JaxConditionalFieldDto flexField = itr.next();
				if ("INDIC17".equals(flexField.getField().getName())) {
					itr.remove();
				}
			}
		}
		return requiredFlexFields;
	}

	private void sortPossibleValues(JaxConditionalFieldDto jaxConditionalFieldDto) {
		List<JaxFieldValueDto> possibleValues = jaxConditionalFieldDto.getField().getPossibleValues();
		String fieldName = jaxConditionalFieldDto.getField().getName();
		if ("INDIC16".equals(fieldName) || "INDIC18".equals(fieldName)) {
			if (possibleValues != null) {
				// sorting possible values acc. to amiec code
				Collections.sort(possibleValues, (o1, o2) -> {
					FlexFieldDto dto1 = (FlexFieldDto) o1.getValue();
					FlexFieldDto dto2 = (FlexFieldDto) o2.getValue();
					BigDecimal amount1 = new BigDecimal(
							dto1.getAmieceCode().replace(INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL, ""));
					BigDecimal amount2 = new BigDecimal(dto2.getAmieceCode());
					return amount1.compareTo(amount2);
				});
			}
		}
	}

	public BenePackageResponse createBenePackageResponse(Map<String, Object> validationResults) {
		BenePackageResponse resp = new BenePackageResponse();
		List<JaxConditionalFieldDto> requiredFlexFields = (List<JaxConditionalFieldDto>) validationResults
				.get("requiredFlexFields");
		resp.setRequiredFlexFields(requiredFlexFields);
		if (validationResults.get("PACKAGE_FC_AMOUNT") != null) {
			resp.setAmount(new BigDecimal(validationResults.get("PACKAGE_FC_AMOUNT").toString()));
		}

		return resp;
	}

}
