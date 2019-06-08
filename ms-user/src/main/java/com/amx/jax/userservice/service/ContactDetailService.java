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
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.LocalAddressDetails;
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
	

	public HomeAddressDetails createHomeAddressDetails(Customer customer) {
		// --- Home Address Data
		HomeAddressDetails homeAddress = new HomeAddressDetails();
		ContactDetail homeData = getContactsForHome(customer);
		if (homeData != null) {
			homeAddress.setContactTypeId(homeData.getFsBizComponentDataByContactTypeId().getComponentDataId());
			homeAddress.setBlock(homeData.getBlock());
			homeAddress.setStreet(homeData.getStreet());
			homeAddress.setHouse(homeData.getBuildingNo());
			homeAddress.setFlat(homeData.getFlat());
			if (null != homeData.getFsCountryMaster()) {
				homeAddress.setCountryId(homeData.getFsCountryMaster().getCountryId());
			}
			if (null != homeData.getFsStateMaster()) {
				homeAddress.setStateId(homeData.getFsStateMaster().getStateId());
			}
			if (null != homeData.getFsDistrictMaster()) {
				homeAddress.setDistrictId(homeData.getFsDistrictMaster().getDistrictId());
			}
			if (null != homeData.getFsCityMaster()) {
				homeAddress.setCityId(homeData.getFsCityMaster().getCityId());
			}
		}
		return homeAddress;
	}

	public LocalAddressDetails createLocalAddressDetails(Customer customer) {
		// --- Local Address Data
		LocalAddressDetails localAddress = new LocalAddressDetails();
		ContactDetail localData = getContactsForLocal(customer);
		if (localData != null) {
			localAddress.setContactTypeId(localData.getFsBizComponentDataByContactTypeId().getComponentDataId());
			localAddress.setBlock(localData.getBlock());
			localAddress.setStreet(localData.getStreet());
			localAddress.setHouse(localData.getBuildingNo());
			localAddress.setFlat(localData.getFlat());
			if (null != localData.getFsCountryMaster()) {
				localAddress.setCountryId(localData.getFsCountryMaster().getCountryId());
			}
			if (null != localData.getFsStateMaster()) {
				localAddress.setStateId(localData.getFsStateMaster().getStateId());
			}
			if (null != localData.getFsDistrictMaster()) {
				localAddress.setDistrictId(localData.getFsDistrictMaster().getDistrictId());
			}
			if (null != localData.getFsCityMaster()) {
				localAddress.setCityId(localData.getFsCityMaster().getCityId());
			}
		}
		return localAddress;
	}

	public void saveContactDetails(ContactDetail contactDetail) {
		contactDetailRepository.save(contactDetail);
	}
}
