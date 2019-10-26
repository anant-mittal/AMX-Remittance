package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AdditionalFlexRequiredException;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BenePackageRequest;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
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

	private static final Logger log = LoggerFactory.getLogger(PreFlexFieldManager.class);

	public void validateBenePackageRequest(BenePackageRequest benePackageRequest) {
		benePackageRequest.populateFlexFieldDtoMap();
		Map<String, FlexFieldDto> requestFlexFields = benePackageRequest.getFlexFieldDtoMap();
		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		BenificiaryListView beneficaryDetails = beneficiaryRepository.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(
				metaData.getCustomerId(), benePackageRequest.getBeneId(), ConstantDocument.Yes);
		BigDecimal applicationCountryId = beneficaryDetails.getApplicationCountryId();
		BigDecimal foreignCurrencyId = beneficaryDetails.getCurrencyId();
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		BigDecimal routingCountryId = bankService.getBankById(routingBankId).getBankCountryId();
		// TODO fetch remit and delivery modes from parameter detail
		BigDecimal remittanceModeId = null;
		BigDecimal deliveryModeId = null;
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicabilityForBank(
				applicationCountryId, routingCountryId, foreignCurrencyId, remittanceModeId, deliveryModeId,
				flexiFieldIn.toArray(new String[flexiFieldIn.size()]), routingBankId, ConstantDocument.No);
		for (AdditionalDataDisplayView flexField : additionalDataRequired) {
			JaxConditionalFieldDto jaxConditionalFieldDto = remittanceTransactionRequestValidator.getConditionalFieldDto(flexField, requestFlexFields,
					foreignCurrencyId, foreignCurrencyId, foreignCurrencyId, foreignCurrencyId, foreignCurrencyId, null);
			if (jaxConditionalFieldDto != null) {
				requiredFlexFields.add(jaxConditionalFieldDto);
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
	}
}
