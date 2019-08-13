package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.IdentityTypeMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.repository.customer.DmsApplMappingRepository;
import com.amx.jax.repository.customer.IdentityTypeMasterRepository;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.userservice.dao.CusmosDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.AmxDBConstants;
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
	@Autowired
	CustomerIdProofDao customerIdProofDao;
	@Autowired
	IdentityTypeMasterRepository identityTypeMasterRepository;
	@Autowired
	DmsApplMappingRepository dmsApplMappingRepository;
	
	
	private static final Logger log = LoggerFactory.getLogger(CustomerIdProofManager.class);


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

	public void setIdProofFlags(BigDecimal customerId, CustomerFlags customerFlags) {
		String[] statusIn = { AmxDBConstants.Yes, AmxDBConstants.Compliance };
		List<CustomerIdProof> customerIdProofs = customerIdProofRepository.getCustomerIdProofs(customerId, statusIn);
		if (CollectionUtils.isEmpty(customerIdProofs)) {
			customerFlags.setIdProofRequired(true);
			return;
		}
		String identityStatus = customerIdProofs.get(0).getIdentityStatus();
		if (identityStatus.equals(AmxDBConstants.Compliance)) {
			customerFlags.setIdProofVerificationPending(true);
		}
	}

	public CustomerIdProof getCustomerIdProofByCustomerId(BigDecimal customerId) {
		List<CustomerIdProof> activeIdProofs = customerIdProofDao.getCustomerIdProofs(customerId);
		Optional<CustomerIdProof> lastestRecordByExpiry = activeIdProofs.stream().sorted((o1, o2) -> {
			return o1.getIdentityExpiryDate().compareTo(o2.getIdentityExpiryDate());
		}).findFirst();
		return lastestRecordByExpiry.isPresent() ? lastestRecordByExpiry.get() : null;
	}

	public List<CustomerIdProof> getCustomeridProofForIdType(BigDecimal customerId, BigDecimal identityTypeId) {
		List<CustomerIdProof> idProofList = customerIdProofDao.getCustomeridProofForIdType(customerId, identityTypeId);
		return idProofList;
	}

	public IdentityTypeMaster getIdentityTypeMaster(BigDecimal identityTypeId, String isActive) {
		return identityTypeMasterRepository.findBybusinessComponentIdAndIsActive(identityTypeId, isActive);
	}

	public DmsApplMapping getDmsMapping(CustomerIdProof customerIdProof) {
		DmsApplMapping mapping = null;
		List<DmsApplMapping> dmsApplMapping = dmsApplMappingRepository.getDmsApplMapping(
				customerIdProof.getFsCustomer().getCustomerId(), customerIdProof.getIdentityInt(),
				customerIdProof.getIdentityTypeId(), customerIdProof.getIdentityExpiryDate());
		if (CollectionUtils.isNotEmpty(dmsApplMapping)) {
			mapping = dmsApplMapping.get(0);
		}
		return mapping;
	}

	public DmsApplMapping getDmsMapping(Customer customer) {
		DmsApplMapping mapping = null;
		List<DmsApplMapping> dmsApplMapping = dmsApplMappingRepository.getDmsApplMapping(customer.getCustomerId(),
				customer.getIdentityInt(), customer.getIdentityTypeId(), customer.getIdentityExpiredDate());
		if (CollectionUtils.isNotEmpty(dmsApplMapping)) {
			mapping = dmsApplMapping.get(0);
		}
		return mapping;
	}
	
	public DmsApplMapping getDmsMapping(Customer customer, Date idExpiryDate) {
		DmsApplMapping mapping = null;
		List<DmsApplMapping> dmsApplMapping = dmsApplMappingRepository.getDmsApplMapping(customer.getCustomerId(),
				customer.getIdentityInt(), customer.getIdentityTypeId(), idExpiryDate);
		if (CollectionUtils.isNotEmpty(dmsApplMapping)) {
			mapping = dmsApplMapping.get(0);
		}
		return mapping;
	}

	public DmsApplMapping getDmsMappingByCustomer(Customer customer) {
		DmsApplMapping mapping = null;
		List<DmsApplMapping> dmsApplMapping = dmsApplMappingRepository.getDmsApplMapping(customer.getCustomerId());
		if (CollectionUtils.isNotEmpty(dmsApplMapping)) {
			mapping = dmsApplMapping.get(0);
		}
		return mapping;
	}

	public void activateCustomerPendingCompliance(Customer customer, Date identityExpiryDate) {
		log.debug("in activateCustomerPendingCompliance");
		List<CustomerIdProof> compliancePendingRecords = customerIdProofDao
				.getCompliancePendingCustomerIdProof(customer.getCustomerId(), customer.getIdentityTypeId());
		if (CollectionUtils.isNotEmpty(compliancePendingRecords)) {
			CustomerIdProof customerIdProof = compliancePendingRecords.get(0);
			customerIdProof.setIdentityStatus(ConstantDocument.Yes);
			customerIdProof.setIdentityExpiryDate(identityExpiryDate);
			customerIdProofDao.save(Arrays.asList(customerIdProof));
			customer.setIdentityExpiredDate(identityExpiryDate);
			customer.setIsActive(ConstantDocument.Yes);
			customerDao.saveCustomer(customer);
		}
	}

	public List<IdentityTypeMaster> getActiveIdentityTypes() {
		return identityTypeMasterRepository.findByIsActive(ConstantDocument.Yes);
	}

	public List<CustomerIdProof> fetchCustomerIdProofsForCustomerActivation(BigDecimal customerId) {
		String[] statusIn = { AmxDBConstants.Yes, AmxDBConstants.Compliance };
		List<CustomerIdProof> customerIdProofs = customerIdProofRepository.getCustomerIdProofs(customerId, statusIn);
		return customerIdProofs;
	}

	public void saveIdProof(CustomerIdProof idProof) {
		customerIdProofRepository.save(idProof);
	}

	public void saveIdProof(List<CustomerIdProof> existingIdProof) {
		customerIdProofRepository.save(existingIdProof);

	}

	public List<CustomerIdProof> getCustomerIdProofPendingCompliance(BigDecimal customerId, BigDecimal identityTypeId) {
		return customerIdProofDao.getCompliancePendingCustomerIdProof(customerId, identityTypeId);
	}
}
