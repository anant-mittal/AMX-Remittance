package com.amx.jax.customer.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.CityMaster;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.UpdateCustomerAddressDetailRequest;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.userservice.service.ContactDetailService;

@Component
public class CustomerAddressDetailsManager {

	@Autowired
	ContactDetailService contactDetailService;
	@Autowired
	MetaData metaData;
	@Autowired
	JaxDBService jaxDbservice;

	public void updateCustomerAddressDetail(Customer customer, UpdateCustomerAddressDetailRequest req) {
		if (req.getContactType() == null) {
			throw new GlobalException("null contact type passed");
		}
		ContactDetail contactDetail = saveorUpdateCustomerContact(customer, req);
		contactDetailService.saveContactDetails(contactDetail);
	}

	public ContactDetail saveorUpdateCustomerContact(Customer customer, UpdateCustomerAddressDetailRequest req) {
		ContactDetail contactDetail = null;
		if (ConstantDocument.CONTACT_TYPE_FOR_HOME.equals(req.getContactType())) {
			contactDetail = contactDetailService.getContactsForHome(customer);
		} else if (ConstantDocument.CONTACT_TYPE_FOR_LOCAL.equals(req.getContactType())) {
			contactDetail = contactDetailService.getContactsForLocal(customer);
		}
		if (req != null) {
			if (contactDetail == null) {
				contactDetail = new ContactDetail();
				BizComponentData fsBizComponentDataByContactTypeId = new BizComponentData();
				fsBizComponentDataByContactTypeId.setComponentDataId(req.getContactType());
				contactDetail.setFsBizComponentDataByContactTypeId(fsBizComponentDataByContactTypeId);
				contactDetail.setActiveStatus(ConstantDocument.Yes);
			}
			contactDetail.setFsCountryMaster(new CountryMaster(req.getCountryId()));
			contactDetail.setFsDistrictMaster(new DistrictMaster(req.getDistrictId()));
			contactDetail.setFsStateMaster(new StateMaster(req.getStateId()));
			contactDetail.setFsCityMaster(new CityMaster(req.getCityId()));
			contactDetail.setBuildingNo(req.getHouse());
			contactDetail.setFlat(req.getFlat());
			contactDetail.setBlock(req.getBlock());
			contactDetail.setStreet(req.getStreet());
			contactDetail.setFsCustomer(customer);
			contactDetail.setLanguageId(customer.getLanguageId());
			contactDetail.setCreatedBy(jaxDbservice.getCreatedOrUpdatedBy());
			contactDetail.setCreationDate(customer.getCreationDate());

			contactDetail.setMobile(customer.getMobile());
			contactDetail.setTelephoneCode(customer.getPrefixCodeMobile());
			contactDetail.setIsWatsApp(customer.getIsMobileWhatsApp());

		}
		return contactDetail;
	}
}
