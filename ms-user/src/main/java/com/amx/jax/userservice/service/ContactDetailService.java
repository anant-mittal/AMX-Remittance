package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.userservice.repository.ContactDetailsRepository;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ContactDetailService {

	@Autowired
	ContactDetailsRepository contactDetailRepository;
	
	
	public List<ContactDetail> getContactDetail(BigDecimal customerId) {
		List<ContactDetail> contactDetailList = contactDetailRepository.getContactDetails(new Customer(customerId));
		return contactDetailList;

	}

	public List<ContactDetail> getContactDetailByCotactId(BigDecimal customerId, BigDecimal contactTypeId) {
		List<ContactDetail> contactDetailList = contactDetailRepository.getContactDetailByCotactId(new Customer(customerId),new BizComponentData(contactTypeId));
		return contactDetailList;
	}

	public ContactDetail getContactsForLocal(Customer customer) {
		ContactDetail contactDetail = null;
		List<ContactDetail> contactDetails = contactDetailRepository.getContactDetailByCotactId(customer, new BizComponentData(ConstantDocument.CONTACT_TYPE_FOR_LOCAL));
		if(!contactDetails.isEmpty()) {
			contactDetail = contactDetails.get(0);
		}
		return contactDetail;
	}

	public ContactDetail getContactsForHome(Customer customer) {
		ContactDetail contactDetail = null;
		List<ContactDetail> contactDetails = contactDetailRepository.getContactDetailByCotactId(customer, new BizComponentData(ConstantDocument.CONTACT_TYPE_FOR_HOME));
		if(!contactDetails.isEmpty()) {
			contactDetail = contactDetails.get(0);
		}
		return contactDetail;
	}
	

}
