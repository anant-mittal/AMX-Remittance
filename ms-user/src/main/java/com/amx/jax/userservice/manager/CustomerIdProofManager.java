package com.amx.jax.userservice.manager;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.userservice.dao.CusmosDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.Constants;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerIdProofManager {

	@Autowired
	CustomerDao customerDao;
	@Autowired
	CusmosDao cusmosDao;
	@Autowired
	BizcomponentDao bizcomponentDao;
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerIdProofRepository customerIdProofRepository;
	@Autowired
	UserService userService;
	@Autowired
	JaxDBService jaxDBService;

	/**
	 * deletes previous active id proof and activate new one
	 * 
	 * @param model
	 * @param customer
	 */
	public void createIdProofForExpiredCivilId(ImageSubmissionRequest model, Customer customer) {
		userService.deActivateCustomerIdProof(customer.getCustomerId());
		userService.deactiveteCustomerIdProofPendingCompliance(customer.getCustomerId());
		// customerDao.saveCustomer(customer);
		commitOnlineCustomerIdProof(customer, model);
	}

	public void commitOnlineCustomerIdProof(Customer customer, ImageSubmissionRequest model) {

		CustomerIdProof custProof = null;
		List<CustomerIdProof> customerIdProofs = customerIdProofRepository
				.getCustomerIdProofByCustomerId(customer.getCustomerId());

		if (!customerIdProofs.isEmpty()) {
			custProof = customerIdProofs.get(0);
			custProof.setUpdatedBy(jaxDBService.getCreatedOrUpdatedBy());
		}
		if (custProof == null) {
			custProof = new CustomerIdProof();
			custProof.setCreatedBy(jaxDBService.getCreatedOrUpdatedBy());
		}
		Customer customerData = new Customer();
		customerData.setCustomerId(customer.getCustomerId());
		custProof.setFsCustomer(customerData);
		custProof.setLanguageId(metaData.getLanguageId());
		BizComponentData customerType = new BizComponentData();
		customerType.setComponentDataId(
				bizcomponentDao.getComponentId(Constants.CUSTOMERTYPE_INDU, metaData.getLanguageId())
						.getFsBizComponentData().getComponentDataId());
		custProof.setFsBizComponentDataByCustomerTypeId(customerType);
		custProof.setIdentityInt(customer.getIdentityInt());
		// custProof.setIdentityStatus(Constants.CUST_ACTIVE_INDICATOR);
		custProof.setIdentityStatus(Constants.CUST_COMPLIANCE_CHECK_INDICATOR);
		custProof.setCreationDate(new Date());
		custProof.setIdentityTypeId(customer.getIdentityTypeId());
		if (model != null && model.getIdentityExpiredDate() != null) {
			custProof.setIdentityExpiryDate(model.getIdentityExpiredDate());
		} else if (customer.getIdentityExpiredDate() != null) {
			custProof.setIdentityExpiryDate(customer.getIdentityExpiredDate());
		}
		custProof.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		custProof.setScanSystem(Constants.CUST_DB_SCAN);
		customerIdProofRepository.save(custProof);
	}

	public void commitOnlineCustomerIdProof(Customer customer) {
		commitOnlineCustomerIdProof(customer, null);
	}
}
