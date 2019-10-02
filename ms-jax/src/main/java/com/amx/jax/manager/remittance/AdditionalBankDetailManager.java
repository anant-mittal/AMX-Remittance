package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailData;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.dbmodel.remittance.ViewDeliveryMode;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.dbmodel.remittance.ViewRemittanceMode;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.repository.IAdditionalBankRuleMapRepos;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.remittance.AdditionalBankDetailDataRepository;
import com.amx.jax.repository.remittance.IViewDeliveryMode;
import com.amx.jax.repository.remittance.IViewParameterDetailsRespository;
import com.amx.jax.repository.remittance.IViewRemittanceMode;
import com.amx.jax.serviceprovider.service.ServiceProviderApiManager;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.validation.AdditionalBankDetailValidator;

@Component
public class AdditionalBankDetailManager {
	@Autowired
	AdditionalBankDetailValidator additionalBankDetailValidator;
	@Autowired
	AdditionalBankDetailDataRepository additionalBankDetailDataRepository;
	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	@Autowired
	BankService bankService;
	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;
	@Autowired
	MetaData metaData;
	@Autowired
	IViewParameterDetailsRespository viewParameterDetailsRespository;
	@Autowired
	IAdditionalBankRuleMapRepos additionalBankRuleMapRepos;
	@Autowired
	IViewRemittanceMode viewRemittanceMode;
	@Autowired
	IViewDeliveryMode viewDeliveryMode;
	@Autowired
	ICurrencyDao currencyDao;

	public void setDefaultValues(List<JaxConditionalFieldDto> requiredFlexFields, RemittanceAdditionalBeneFieldModel request,
			Map<String, Object> remitApplParametersMap) {

		BigDecimal beneAccSeqId = beneficiaryService.getBeneByIdNo(request.getBeneId()).getBeneficiaryAccountSeqId();
		List<AdditionalBankDetailData> additionalBeneData = additionalBankDetailDataRepository.findByBeneAccSeqId(beneAccSeqId);
		Map<Object, AdditionalBankDetailData> valueMap = additionalBeneData.stream().collect(Collectors.toMap(x -> x.getKey(), x -> x));

		if (valueMap != null && valueMap.size() != 0) {
			requiredFlexFields.forEach(i -> {
				AdditionalBankDetailData data = valueMap.get(i.getField().getDtoPath().replaceAll("flexFields.", ""));
				if (data != null) {
					i.getField().setDefaultValue(data.getValue());
				}
			});
		}
	}

	public void validateAdditionalBankFields(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap) {
		additionalBankDetailValidator.validateAdditionalBankFields(request, remitApplParametersMap);
	}

	public void saveFlexFields(RemittanceAdditionalBeneFieldModel requestApplModel, Map<String, Object> remitApplParametersMap) {

		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		Map<String, FlexFieldDto> additionalbankFields = requestApplModel.getFlexFieldDtoMap();
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		BenificiaryListView beneficiaryViewModel = beneficiaryService.getBeneByIdNo(requestApplModel.getBeneId());
		BigDecimal beneAccSeqId = beneficiaryViewModel.getBeneficiaryAccountSeqId();

		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicabilityForBank(
				applicationCountryId, routingCountryId, foreignCurrencyId, remittanceModeId, deliveryModeId,
				flexiFieldIn.toArray(new String[flexiFieldIn.size()]), routingBankId);
		if (CollectionUtils.isEmpty(additionalDataRequired)) {
			additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicability(applicationCountryId, routingCountryId,
					foreignCurrencyId, remittanceModeId, deliveryModeId, flexiFieldIn.toArray(new String[flexiFieldIn.size()]));
		}
		Map<String, AdditionalDataDisplayView> additionalDataRequiredMap = additionalDataRequired.stream()
				.collect(Collectors.toMap(i -> i.getFlexField(), i -> i));

		List<AdditionalBankDetailData> additionalBeneData = additionalBankDetailDataRepository.findByBeneAccSeqId(beneAccSeqId);
		Map<Object, AdditionalBankDetailData> valueMap = additionalBeneData.stream().collect(Collectors.toMap(x -> x.getKey(), x -> x));

		additionalbankFields.forEach((k, v) -> {
			AdditionalDataDisplayView additionalDataDisplayView = additionalDataRequiredMap.get(k);
			if (ConstantDocument.Yes.equalsIgnoreCase(additionalDataDisplayView.getIsBeneTag())) {
				AdditionalBankDetailData data = valueMap.get(k);
				if (data == null) {
					data = new AdditionalBankDetailData(beneAccSeqId, k, v.getAmieceDescription());
				} else {
					data.setValue(v.getAmieceDescription());
				}
				additionalBankDetailDataRepository.save(data);
			}
		});

	}

	public void processMissingFlexFields(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap,
			List<JaxFieldDto> requiredFlexFields) {
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		String bankCode = bankService.getBankById(routingBankId).getBankCode();
		// TODO find if routing bank addl flex field validation is needed or not based
		try {
			ServiceProviderApiManager serviceProviderApiManager = (ServiceProviderApiManager) applicationContext.getBean(bankCode);
			serviceProviderApiManager.setAdditionalFlexFieldParams(request, remitApplParametersMap, requiredFlexFields);
		} catch (NoSuchBeanDefinitionException ex) {

		}

	}

	/**
	 * @param beneId
	 * @return : to fetch service provider amounts available
	 */
	public List<ParameterDetailsDto> fetchServiceProviderFcAmount(BigDecimal beneId) {
		List<ParameterDetailsDto> dtoList = new ArrayList<ParameterDetailsDto>();
		BenificiaryListView beneficaryDetails = beneficiaryRepository
				.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(metaData.getCustomerId(), beneId, ConstantDocument.Yes);
		if (beneficaryDetails.getServiceProvider() != null) {
			String serviceProviderCode = bankService.getBankById(beneficaryDetails.getServiceProvider()).getBankCode();
			if (beneficaryDetails != null && !StringUtils.isBlank(beneficaryDetails.getBankCode()) && beneficaryDetails.getBranchCode() != null) {
				List<ViewParameterDetails> vwParamDetailsList = viewParameterDetailsRespository.findByRecordIdAndCharField2AndNumericField1(
						serviceProviderCode, beneficaryDetails.getBankCode(), beneficaryDetails.getBranchCode());

				if (vwParamDetailsList != null && !vwParamDetailsList.isEmpty()) {
					for (ViewParameterDetails viewParameterDetails : vwParamDetailsList) {
						ParameterDetailsDto pdto = new ParameterDetailsDto();

						AdditionalBankRuleMap addlMap = additionalBankRuleMapRepos.findByFlexFieldAndIsActive(viewParameterDetails.getCharField3(),
								ConstantDocument.Yes);
						// Set default properties
						pdto.importFrom(viewParameterDetails);
						pdto.setResourceValue(new FlexFieldDto(addlMap.getAdditionalBankRuleId(), null, viewParameterDetails.getCharField1(),
								viewParameterDetails.getParamCodeDef(), viewParameterDetails.getCharField3()));

						// Set Additional properties
						pdto.setParameterMasterId(viewParameterDetails.getParameterMasterId());
						pdto.setParameterDetailsId(viewParameterDetails.getParameterDetailsId());
						pdto.setRecordId(viewParameterDetails.getRecordId());
						pdto.setFullDesc(viewParameterDetails.getFullDesc());
						pdto.setParamCodeDef(viewParameterDetails.getParamCodeDef());
						pdto.setCharUdf1(viewParameterDetails.getCharField1());
						pdto.setCharUdf2(viewParameterDetails.getCharField2());
						pdto.setCharUdf3(viewParameterDetails.getCharField3());
						pdto.setCharUdf4(viewParameterDetails.getCharField4());
						pdto.setCharUdf5(viewParameterDetails.getCharField5());
						pdto.setNumericUdf1(viewParameterDetails.getNumericField1());
						pdto.setNumericUdf2(viewParameterDetails.getNumericField2());
						pdto.setNumericUdf3(viewParameterDetails.getNumericField3());
						pdto.setNumericUdf4(viewParameterDetails.getNumericField4());
						pdto.setNumericUdf5(viewParameterDetails.getNumericField5());
						pdto.setAmount(viewParameterDetails.getNumericField2());
						pdto.setResourceName(pdto.getAmount());
						dtoList.add(pdto);
					}
				}

			}

		}
		return dtoList;
	}
}
