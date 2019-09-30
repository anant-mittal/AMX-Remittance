package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.LinkDTO;
import com.amx.amxlib.model.LinkResponseModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.AmxResponseSchemes;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dbmodel.ContactLinkDetails;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.LinkDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.userservice.dao.LinkDetailsDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.MapBuilder;
import com.amx.utils.MapBuilder.BuilderMap;

@Service
@SuppressWarnings("rawtypes")
public class LinkService extends AbstractService{
	@Autowired
	private LinkDetailsDao linkDao;
	
	@Autowired
	private PushNotifyClient pushNotifyClient;
	
	@Autowired
	private UserService userService;
	
	
	public AmxApiResponse<LinkResponseModel, Object> makeLink(LinkDTO dto) {		
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
			if (dto.getContactType() != null) {
				linkDetails.setContactType(dto.getContactType());
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
			}else {
				linkDetails.setNoOfContacts(0);
			}
			linkDao.saveLink(linkDetails);
			LinkResponseModel linkResponseModel = new LinkResponseModel();
			linkResponseModel.setLinkId(responseDTO.getLinkId());
		return AmxApiResponse.build(linkResponseModel);
	}
	
	
	
	public void validateLinkDto(LinkDTO dto) {
		if(dto.getCustomerId() == null ) {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID,
					"Customer ID can not be null");
		}
	}

	public AmxApiResponse<LinkDTO, Object> openLink(LinkDTO linkDto,JaxChannel channel) {		
		LinkDetails linkDetails = linkDao.getLinkDetails(linkDto.getLinkId());				
		int openCounter = linkDetails.getOpenCounter();
		linkDetails.setOpenCounter(openCounter+1);	
		linkDao.updateLink(linkDetails);
		Customer customer = userService.getCustById(linkDetails.getCustomerId());
		if(linkDetails.getCustomerId() != null) {			
			PushMessage pushMessage = new PushMessage();
			pushMessage.setSubject("Refer To Win!");
			pushMessage.setMessage(
					"Congraturlations! Your reference has downloaded the app. Keep sharing the links to as many contacts you can and win exciting prices on referral success!");
			pushMessage.addToUser(linkDetails.getCustomerId());
			pushNotifyClient.send(pushMessage);
		}
		String name = "";
		if (customer.getFirstName() != null) {
			name = name+customer.getFirstName();
		}
		if (customer.getMiddleName() != null) {
			name = name+" "+customer.getMiddleName();
		}
		if(customer.getLastName() != null) {
			name = name+" "+customer.getLastName();
		}
		
		linkDto.setCustomerName(name);
		return AmxApiResponse.build(linkDto);
	}
	
}
