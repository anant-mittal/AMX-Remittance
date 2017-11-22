package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.repository.IContactDetailsDAO;

@Service
public class ContactDetailService {

	
	@Autowired
	IContactDetailsDAO customerDao;
	
	public List<ContactDetail> getContactDetail(BigDecimal customerId){
		return customerDao.getContactDetails(customerId);
	}
	
	public List<ContactDetail> getContactDetailByCotactId(BigDecimal customerId ,BigDecimal contactTypeId ){
		return customerDao.getContactDetailByCotactId(customerId, contactTypeId);
	}
}
