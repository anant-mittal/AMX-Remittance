package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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

import com.amx.amxlib.constant.JaxFieldEntity;
import com.amx.amxlib.exception.AdditionalFlexRequiredException;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BenePackageRequest;
import com.amx.jax.model.response.customer.BenePackageResponse;
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
		BenificiaryListView beneficaryDetails = beneficiaryRepository.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(
				metaData.getCustomerId(), benePackageRequest.getBeneId(), ConstantDocument.Yes);
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		BankMasterModel routingBank = bankService.getBankById(routingBankId);
		ViewParameterDetails cashSetUp = viewParameterDetailsRespository.findByRecordIdAndCharField1AndCharField2AndNumericField1(
				ConstantDocument.CASH_STRING, routingBank.getBankCode(), beneficaryDetails.getBankCode(), beneficaryDetails.getBranchCode());
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		// when size of package is 1 then we are using parameter setup otherwise we are
		// using additional data setup
		if (cashSetUp != null) {
			requiredFlexFields.addAll(fetchFlexFieldsForCashSetup(cashSetUp, benePackageRequest, beneficaryDetails));
		} else {
			List<ParameterDetailsDto> parameterSetUp = additionalBankDetailManager.fetchServiceProviderFcAmount(benePackageRequest.getBeneId());
			if (parameterSetUp.size() == 1) {
				ParameterDetailsDto parameterDto = parameterSetUp.get(0);
				requiredFlexFields.add(fetchFlexFieldsForParameterSetup(parameterDto));
				packageFcAmount = parameterDto.getAmount();
			}
		}
		if (!requiredFlexFields.isEmpty()) {
			log.debug(requiredFlexFields.toString());
			AdditionalFlexRequiredException exp = new AdditionalFlexRequiredException("Addtional flex fields are required",
					JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED);
			remittanceTransactionRequestValidator.processFlexFields(requiredFlexFields);
			exp.setMeta(requiredFlexFields);
			throw exp;
		}
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
		Map<String, Object> validationResults = new HashMap<>();
		validationResults.put("PACKAGE_FC_AMOUNT", packageFcAmount);
		if (monthlyContribution != null) {
			packageFcAmount = monthlyContribution.multiply(BigDecimal.valueOf(noOfMonth));
		}
		if (volunteerContribution != null) {
			packageFcAmount = monthlyContribution.add(volunteerContribution).multiply(BigDecimal.valueOf(noOfMonth));
		}
		return validationResults;
	}

	private JaxConditionalFieldDto fetchFlexFieldsForParameterSetup(ParameterDetailsDto parameterDetailsDto) {
		JaxConditionalFieldDto dto = new JaxConditionalFieldDto();
		dto.setEntityName(JaxFieldEntity.FLEX_FIELD);
		JaxFieldDto field = new JaxFieldDto();
		field.setDtoPath("packageFcAmount");
		field.setLabel("Foreign Amount");
		field.setName("PACKAGE_FCAMOUNT");
		field.setMaxValue(parameterDetailsDto.getMaxAmount());
		field.setMinValue(parameterDetailsDto.getMinAmount());
		if (parameterDetailsDto.getAmount() != null) {
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
		BigDecimal remittanceModeId = remittanceModeMasterRepository.findByRemittance(remittanceModeCode).getRemittanceModeId();
		BigDecimal deliveryModeId = deliveryModeRepository.findByDeliveryMode(deliveryModeCode).getDeliveryModeId();
		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicabilityForBank(
				applicationCountryId, routingCountryId, foreignCurrencyId, remittanceModeId, deliveryModeId,
				flexiFieldIn.toArray(new String[flexiFieldIn.size()]), routingBankId, ConstantDocument.No);
		boolean volunteerContributionIndic = false;
		boolean volunteerContributionSelected = false;
		for (AdditionalDataDisplayView flexField : additionalDataRequired) {
			FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(flexField.getFlexField());
			JaxConditionalFieldDto jaxConditionalFieldDto = remittanceTransactionRequestValidator.getConditionalFieldDto(flexField, requestFlexFields,
					foreignCurrencyId, foreignCurrencyId, foreignCurrencyId, foreignCurrencyId, foreignCurrencyId, null);
			if ("INDIC16".equals(flexField.getFlexField()) && flexFieldValueInRequest.getAmieceCode().contains("+")) {
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
			log.debug("volunteerContributionSelected");
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

	public BenePackageResponse createBenePackageResponse(Map<String, Object> validationResults) {
		BenePackageResponse resp = new BenePackageResponse();
		if (validationResults.get("PACKAGE_FC_AMOUNT") != null) {
			resp.setAmount(new BigDecimal(validationResults.get("PACKAGE_FC_AMOUNT").toString()));
		}
		return resp;
	}

}
