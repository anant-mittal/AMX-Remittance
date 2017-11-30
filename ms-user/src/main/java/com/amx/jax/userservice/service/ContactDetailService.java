package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.userservice.repository.ContactDetailsRepository;

@Service
public class ContactDetailService {

	@Autowired
	ContactDetailsRepository contactDetailRepository;

	public List<ContactDetail> getContactDetail(BigDecimal customerId) {
		List<ContactDetail> contactDetailList = contactDetailRepository.getContactDetails(customerId);
		return contactDetailList;

	}

	public List<ContactDetail> getContactDetailByCotactId(BigDecimal customerId, BigDecimal contactTypeId) {
		List<ContactDetail> contactDetailList = contactDetailRepository.getContactDetailByCotactId(customerId,
				contactTypeId);
		return contactDetailList;
	}

}
