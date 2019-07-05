package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.LinkDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.AmxResponseSchemes;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dbmodel.ContactLinkDetails;
import com.amx.jax.dbmodel.LinkDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.userservice.dao.LinkDetailsDao;

@Service
@SuppressWarnings("rawtypes")
public class LinkService extends AbstractService{
	@Autowired
	private LinkDetailsDao linkDao;
	
	
	public AmxApiResponse<LinkDTO, Object> makeLink(LinkDTO dto) {		
			LinkDetails linkDetails = new LinkDetails();
			String linkId = "";
			LinkDTO responseDTO = new LinkDTO();
			if(dto.getCustomerId() != null) {
				linkDetails.setCustomerId(dto.getCustomerId());
				UUID uuid = UUID.randomUUID();
				linkDetails.setLinkId(uuid.toString());
				linkDetails.setOpenCounter(0);
				linkId = uuid.toString();
				responseDTO.setLinkId(uuid.toString());
				
			}
			if(dto.getCustomerDetail() != null) {				
		        String delimiter = "\\,";
		        String[] contactDetailsArray = dto.getCustomerDetail().split(delimiter);
		        linkDetails.setNoOfContacts(contactDetailsArray.length);
		        List<ContactLinkDetails> contactLinkDetailList = new ArrayList<ContactLinkDetails>();
		        for (int i = 0; i < contactDetailsArray.length; i++) {
		        	ContactLinkDetails contactLinkDetails = new ContactLinkDetails();
					contactLinkDetails.setLinkId(linkId);
		            contactLinkDetails.setContactDetail(contactDetailsArray[i]);	
		            contactLinkDetailList.add(contactLinkDetails);
		        }
		        linkDao.saveLinkContacs(contactLinkDetailList);
			}
			linkDao.saveLink(linkDetails);
						
		return AmxApiResponse.build(responseDTO);
	}
	
	public void validateLinkDto(LinkDTO dto) {
		// both foreign and domestic amounts should not be null
		if(dto.getCustomerId() == null ) {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID,
					"Customer ID can not be null");
		}
//		if(dto.getCustomerDetail() == null) {
//			throw new GlobalException(JaxError.NULL_CONTACT_DETAILS,
//					"Contact Details can not be null");
//		}
		
		/*if(dto.getPayAmount() != null && dto.getReceiveAmount() != null) {
			throw new GlobalException("Either PayAmount or ReceivedAmount should have value ",
					JaxError.PO_BOTH_PAY_RECEIVED_AMT_VALUE);
		}*/
	}

	public BoolRespModel openLink(LinkDTO linkDto) {
		LinkDetails linkDetails = linkDao.getLinkDetails(linkDto.getLinkId());
		int openCounter = linkDetails.getOpenCounter();
		linkDetails.setOpenCounter(openCounter+1);	
		linkDao.updateLink(linkDetails);
		return new BoolRespModel(true);
	}
	
}
