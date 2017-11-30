package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICustomerIdProofDAO;
import com.amx.jax.services.AbstractService;

@Service
public class CustomerIdProofService extends AbstractService {

	@Autowired
	ICustomerIdProofDAO customerIdProofRepository;

	public List<CustomerIdProof> validateCustomerIdProofs(BigDecimal customerId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerIdProofByCustomerId(customerId);
		if (idProofList.isEmpty()) {
			throw new GlobalException("NO_ID_PROOFS_AVAILABLE", "ID proofs not available, contact branch");
		}
		return idProofList;
	}

	public List<CustomerIdProof> getCustomerImageValidation(BigDecimal customerId, BigDecimal identityTypeId) {
		List<CustomerIdProof> idProofList = customerIdProofRepository.getCustomerImageValidation(customerId,
				identityTypeId);
		return idProofList;
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
