package com.amx.jax.userservice.manager.kwt;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.InvalidCivilIdException;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantSpecific;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;

@Component
@TenantSpecific(value = { Tenant.OMN, Tenant.OMNDEV })
public class UserValidationOmn implements CustomerValidation {

	@Autowired
	private CustomerIdProofDao idproofDao;
	
	@Autowired
	private CustomerRepository customerRepo;

	@Override
	public void validateCustIdProofs(BigDecimal custId) {
		List<CustomerIdProof> idProofs = idproofDao.getCustomerIdProofs(custId);
		for (CustomerIdProof idProof : idProofs) {
			if (!idProof.getIdentityExpiryDate().after(new Date())) {
				throw new GlobalException("Identity proof are expired", JaxError.ID_PROOF_EXPIRED);
			}
		}
		if (idProofs.isEmpty()) {
			throw new GlobalException("ID proofs not available, contact branch", JaxError.NO_ID_PROOFS_AVAILABLE);
		}
	}

	@Override
	public void validateCivilId(String civilId) {
		boolean isValid = isValid(civilId);
		if (!isValid) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " is not valid.");
		}
	}

	public boolean isValid(String civilId) {
		if (civilId.length() >= 4 && civilId.length() <= 12) {
			return true;
		}
		return false;
	}

	@Override
	public void validateEmailId(String emailId) {
		List<Customer> list = customerRepo.getCustomerByEmailId(emailId);	
		if (list != null && list.size()!=0) {
			throw new GlobalException("Email Id already exist", JaxError.ALREADY_EXIST_EMAIL);
		}
		
	}

}
