package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.GiftPackageDescModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BenePackageRequest;
import com.amx.jax.model.response.customer.BenePackageResponse;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldValueDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.remittance.IGiftPackageDescRepository;
import com.amx.utils.ArgUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PackageDescriptionManager {
	@Autowired
	IGiftPackageDescRepository iGiftPackageDescRepository;
	@Autowired
	MetaData metaData;
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;

	public void fetchGiftPackageDesc(BenePackageRequest benePackageRequest, BenePackageResponse resp, String amiecCode) {
		BenificiaryListView beneficaryDetails = beneficiaryRepository.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(
				metaData.getCustomerId(), benePackageRequest.getBeneId(), ConstantDocument.Yes);
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		List<JaxConditionalFieldDto> requiredFlexFields = null;
		if(resp!=null) {
		 requiredFlexFields = resp.getRequiredFlexFields();
		}
		if(!ArgUtil.isEmpty(requiredFlexFields)) {
		for (JaxConditionalFieldDto jaxConditionalFieldDto : requiredFlexFields) {
			List<JaxFieldValueDto> possibleValues = jaxConditionalFieldDto.getField().getPossibleValues();
			if (!ArgUtil.isEmpty(possibleValues)) {
				for (JaxFieldValueDto jaxFieldValueDto : possibleValues) {
					FlexFieldDto flexFieldDto = (FlexFieldDto) jaxFieldValueDto.getValue();
					GiftPackageDescModel giftpackageDescModel = iGiftPackageDescRepository.findByRoutingBankIdAndAmiecCode(routingBankId,
							flexFieldDto.getAmieceCode());
					if (!ArgUtil.isEmpty(giftpackageDescModel)) {
						jaxFieldValueDto.setPackageDescription(giftpackageDescModel.getPackageDesc());
					}

				}
			}

		}
		}
		if (resp.getFcAmount() != null) {
			GiftPackageDescModel giftpackageDescModel = iGiftPackageDescRepository.findByRoutingBankIdAndBeneficiaryBankIdAndBankBranchIdAndAmiecCode(
					routingBankId, beneficaryDetails.getBankId(), beneficaryDetails.getBranchId(), amiecCode);
			if (giftpackageDescModel != null) {
				resp.setPackageDescription(giftpackageDescModel.getPackageDesc());
			}
		}
	}

}
