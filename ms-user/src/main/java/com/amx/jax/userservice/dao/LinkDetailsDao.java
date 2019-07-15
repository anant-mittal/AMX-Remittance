package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.LinkDTO;
import com.amx.jax.dbmodel.ContactLinkDetails;
import com.amx.jax.dbmodel.LinkDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.ContactLinkDetailsRepository;
import com.amx.jax.repository.LinkDetailRepository;

@Component
public class LinkDetailsDao {
     @Autowired
     private MetaData metaData;
     
     @Autowired
     private LinkDetailRepository linkRepo;
     
     @Autowired
     private ContactLinkDetailsRepository contactRepo;
     
     public void saveLink(LinkDetails linkDetails) {
    	 linkDetails.setCreatedBy(metaData.getCustomerId().toString());
//    	 linkDetails.setIsActive("Y");
    	 linkDetails.setCreatedDate(new Date());
    	 linkRepo.save(linkDetails);    	     	         
     }
     
     public void saveLinkContacs(List<ContactLinkDetails> contactLinkDetails) {
    	 for(ContactLinkDetails contactLinkDetail : contactLinkDetails) {
    		 if(contactLinkDetail != null) {
    			 contactRepo.save(contactLinkDetails);
    		 }
    	 }    	 
     }
     
     public void updateLink(LinkDetails linkDetails) {    	 
    	 linkDetails.setModifiedDate(new Date());
    	 linkRepo.save(linkDetails);    	     	         
     }
     
     public LinkDetails getLinkDetails(String linkId) {
    	LinkDetails linkDetails = null;
    	List<LinkDetails> linkDetailList = linkRepo.getLinkDetailsById(linkId);
    	if(linkDetailList.size() > 0){
    		linkDetails = linkDetailList.get(0);
    	}
    	return linkDetails;
     }
}
