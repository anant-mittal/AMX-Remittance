package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
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
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.Constants;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OffsiteCustomerRegManager {

	@Autowired
	CustomerDao customerDao;
	@Autowired
	OffsiteCustomerRegValidator offsiteCustomerRegValidator;
	@Autowired
	BizcomponentDao bizcomponentDao;
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerIdProofRepository customerIdProofRepository;
	@Autowired
	UserService userService;

	/**
	 * Returns the customer for offiste registration. Only inactive /not registered/
	 * active customers whose id is expired
	 * 
	 * @param identityInt
	 * @param identityType
	 * @return
	 */
	public Customer getCustomerForRegistration(String identityInt, BigDecimal identityType) {
		Customer customer = null;
		String[] status = { ConstantDocument.Yes, ConstantDocument.No };
		List<Customer> customers = customerDao.getActiveCustomerByIndentityIntAndTypeAndIsActive(identityInt,
				identityType, Arrays.asList(status));
		offsiteCustomerRegValidator.validateOffsiteCustomerForRegistration(customers);
		if (CollectionUtils.isNotEmpty(customers)) {
			Date now = new Date();
			Optional<Customer> customerOptional = customers.stream().filter(i -> {
				Date idExpiryDate = i.getIdentityExpiredDate();
				boolean isActive = ConstantDocument.Yes.equals(i.getIsActive());
				boolean isIdExpired = idExpiryDate != null && idExpiryDate.compareTo(now) > 0;
				return isActive && isIdExpired;
			}).findFirst();
			if (!customerOptional.isPresent()) {
				customer = customers.get(0);
			}
		}
		return customer;
	}

	/**
	 * deletes previous active id proof
	 * and activate new one
	 * @param model
	 * @param customer
	 */
	public void createIdProofForExpiredCivilId(ImageSubmissionRequest model, Customer customer) {
		userService.deActivateCustomerIdProof(customer.getCustomerId());
		customer.setIdentityExpiredDate(model.getIdentityExpiredDate());
		customerDao.saveCustomer(customer);
		commitOnlineCustomerIdProof(customer);
	}

	public void commitOnlineCustomerIdProof(Customer customer) {

		CustomerIdProof custProof = null;
		List<CustomerIdProof> customerIdProofs = customerIdProofRepository
				.getCustomerIdProofByCustomerId(customer.getCustomerId());
		if (!customerIdProofs.isEmpty()) {
			custProof = customerIdProofs.get(0);
		}
		if (custProof == null) {
			custProof = new CustomerIdProof();
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
		custProof.setIdentityStatus(Constants.CUST_ACTIVE_INDICATOR);
		custProof.setCreatedBy(customer.getIdentityInt());
		custProof.setCreationDate(new Date());
		custProof.setIdentityTypeId(customer.getIdentityTypeId());

		if (customer.getIdentityExpiredDate() != null) {
			custProof.setIdentityExpiryDate(customer.getIdentityExpiredDate());
		}
		custProof.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		custProof.setScanSystem(Constants.CUST_DB_SCAN);
		customerIdProofRepository.save(custProof);
	}
}
