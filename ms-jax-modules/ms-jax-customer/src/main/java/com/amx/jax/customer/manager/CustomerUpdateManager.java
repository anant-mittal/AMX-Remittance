package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.text.ParseException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CountryService;
import com.amx.jax.userservice.service.UserService;

@Component
public class CustomerUpdateManager {

	@Autowired
	CustomerEmployementManager customerEmployementManager;
	@Autowired
	CustomerPersonalDetailManager customerPersonalDetailManager;
	@Autowired
	CustomerAddressDetailsManager customerAddressDetailsManager;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	CompanyService companyService;
	@Autowired
	CountryService countryService;
	@Autowired
	CustomerBranchAuditManager customerBranchAuditManager;

	private static final Logger log = LoggerFactory.getLogger(CustomerUpdateManager.class);

	@Transactional
	public void updateCustomer(UpdateCustomerInfoRequest req) throws ParseException {
		BigDecimal customerId = metaData.getCustomerId();
		Customer customer = userService.getCustById(customerId);
		if (req.getHomeAddressDetail() != null) {
			req.getHomeAddressDetail().setContactType(ConstantDocument.CONTACT_TYPE_FOR_HOME);
			customerAddressDetailsManager.updateCustomerAddressDetail(customer, req.getHomeAddressDetail());
		}
		if (req.getLocalAddressDetail() != null) {
			validateLocalContactDetail(req);
			req.getLocalAddressDetail().setContactType(ConstantDocument.CONTACT_TYPE_FOR_LOCAL);
			customerAddressDetailsManager.updateCustomerAddressDetail(customer, req.getLocalAddressDetail());
		}
		if (req.getPersonalDetailInfo() != null) {
			customerPersonalDetailManager.updateCustomerPersonalDetail(customer, req.getPersonalDetailInfo());
		}
		if (req.getEmploymentDetail() != null) {
			customerEmployementManager.updateCustomerEmploymentInfo(customer, req.getEmploymentDetail());
		}
		customerDocumentManager.addCustomerDocument(metaData.getCustomerId());
		customerBranchAuditManager.logAuditUpdateCustomer(req);
	}

	private void validateLocalContactDetail(UpdateCustomerInfoRequest req) {
		ViewCompanyDetails companyDetail = companyService.getCompanyDetail();
		BigDecimal appCountry = companyDetail.getApplicationCountryId();
		String countryName = countryService.getCountryMaster(appCountry).getFsCountryMasterDescs().get(0).getCountryName();

		if (req.getLocalAddressDetail().getCountryId() != null) {
			if (!appCountry.equals(req.getLocalAddressDetail().getCountryId())) {
				throw new GlobalException("Local country should be " + countryName);
			}
		}
	}

}
