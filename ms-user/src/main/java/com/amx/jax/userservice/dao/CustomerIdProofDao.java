package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;

@Component
public class CustomerIdProofDao {

	@Autowired
	CustomerIdProofRepository customerIdProofRepository;

	public List<CustomerIdProof> getCustomerIdProofs(BigDecimal customerId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerIdProofByCustomerId(customerId);
		return idProofList;
	}

	public List<CustomerIdProof> getCustomerImageValidation(BigDecimal customerId, BigDecimal identityTypeId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerImageValidation(customerId,
				identityTypeId);
		return idProofList;
	}
	
	public List<CustomerIdProof> getCustomeridProofForIdType(BigDecimal customerId, BigDecimal identityTypeId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomeridProofForIdType(customerId,
				identityTypeId);
		return idProofList;
	}
	
	public List<CustomerIdProof> getCustomerIdProofsExpiry(BigDecimal customerId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerIdProofExpiryByCustomerId(customerId);
		return idProofList;
	}

	public List<CustomerIdProof> getActiveCustomeridProofForIdType(BigDecimal customerId, BigDecimal identityTypeId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerImageValidation(customerId,
				identityTypeId);
		return idProofList;
	}

	public void save(List<CustomerIdProof> customerIdProof) {
		customerIdProofRepository.save(customerIdProof);
	}
	
	public List<CustomerIdProof> getCompliancePendingCustomerIdProof(BigDecimal customerId, BigDecimal identityTypeId){
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCompliancePendingCustomerIdProof(customerId, identityTypeId);
		return idProofList;
	}

	public List<CustomerIdProof> getActiveCustomerIdProof(BigDecimal customerId, BigDecimal identityTypeId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getActiveCustomerIdProof(customerId,
				identityTypeId);
		return idProofList;
	}
}
