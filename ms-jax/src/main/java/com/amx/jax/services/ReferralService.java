package com.amx.jax.services;

import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.LinkDTO;
import com.amx.amxlib.model.ReferralDTO;
import com.amx.amxlib.model.ReferralResponseModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.db.utils.EntityDtoUtil;
import com.amx.jax.dbmodel.ReferralDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.userservice.dao.ReferralDetailsDao;

@Service
@SuppressWarnings("rawtypes")
public class ReferralService extends AbstractService {

	@Autowired
	private ReferralDetailsDao refDao;

	public AmxApiResponse saveReferral(ReferralDTO dto) {
		ReferralDetails referralDetails;
		referralDetails = refDao.getReferralByCustomerId();
		if (referralDetails == null) {
			referralDetails = new ReferralDetails();
			if (dto.getCustomerId() != null) {
				referralDetails.setCustomerId(dto.getCustomerId());
			}
			if (dto.getCustomerReferralCode() == null) {
				UUID uuid = UUID.randomUUID();
				referralDetails.setCustomerReferralCode(String.valueOf(uuid));
			}
			if (dto.getRefferedByCustomerId() != null) {
				referralDetails.setRefferedByCustomerId(dto.getRefferedByCustomerId());
				referralDetails.setIsConsumed(ConstantDocument.No);
			}
			dto.setCustomerRefferalCode(referralDetails.getCustomerReferralCode());
			dto.setIsConsumed(referralDetails.getIsConsumed());
			dto.setRefferedByCustomerID(referralDetails.getRefferedByCustomerId());
			refDao.saveReferralCode(referralDetails);
			EntityDtoUtil.entityToDto(referralDetails, dto);
		} else {
			dto.setCustomerRefferalCode(referralDetails.getCustomerReferralCode());
			dto.setIsConsumed(referralDetails.getIsConsumed());
			dto.setRefferedByCustomerID(referralDetails.getRefferedByCustomerId());
		}
		ReferralResponseModel referralResponseModel = new ReferralResponseModel();
		referralResponseModel.setCustomerRefferalCode(dto.getCustomerReferralCode());

		return AmxApiResponse.build(referralResponseModel);
	}

	public void validateReferralDto(ReferralDTO dto) {
		// both foreign and domestic amounts should not be null
		if (dto.getCustomerId() == null) {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "Customer ID can not be null");
		}

		/*
		 * if(dto.getPayAmount() != null && dto.getReceiveAmount() != null) { throw new
		 * GlobalException("Either PayAmount or ReceivedAmount should have value ",
		 * JaxError.PO_BOTH_PAY_RECEIVED_AMT_VALUE); }
		 */
	}
	
	public void validateContactDetails(LinkDTO linkDTO) {
		if(linkDTO.getCustomerDetail() == null) {
			throw new GlobalException(JaxError.NULL_CONTACT_DETAILS,"Contact details can not be null");
		}
	}
	
	public void validateLinkDetails(LinkDTO linkDTO) {
		if(linkDTO.getLinkId() == null) {
			throw new GlobalException(JaxError.NULL_LINK_ID,"Link Id can not be null");
		}
	}
}
