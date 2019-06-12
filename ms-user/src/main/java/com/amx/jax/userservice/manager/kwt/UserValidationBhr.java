package com.amx.jax.userservice.manager.kwt;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.InvalidCivilIdException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dict.Tenant;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.scope.TenantSpecific;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;

@Component
@TenantSpecific(value = { Tenant.BHR, Tenant.BRNDEV })
public class UserValidationBhr implements CustomerValidation {
	Logger logger= Logger.getLogger(UserValidationBhr.class);
	@Autowired
	private CustomerIdProofDao idproofDao;

	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private UserValidationBhr userValidationBhr;

	@Override
	public void validateCustIdProofs(BigDecimal custId) {
		List<CustomerIdProof> idProofs = idproofDao.getCustomerIdProofsExpiry(custId);

		for (CustomerIdProof idProof : idProofs) {
			boolean isCivilId = ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID.equals(idProof.getIdentityTypeId());
			if (idProof.getIdentityExpiryDate() != null&&!idProof.getIdentityExpiryDate().after(new Date())) {
				if (isCivilId) {
					throw new GlobalException(JaxError.ID_PROOF_EXPIRED, "Id Proof is expired");
				} else {
					throw new GlobalException(JaxError.CIVIL_ID_EXPIRED, "Civil id is expired");
				}
			}
		}
		if (idProofs.isEmpty()) {
			throw new GlobalException(JaxError.NO_ID_PROOFS_AVAILABLE, "ID proofs not available, contact branch");
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
		if (civilId.length() != 9) {
			return false;
		}
		return true;
	}

	@Override
	public void validateEmailId(String emailId) {
		List<Customer> list = customerRepo.getCustomerByEmailId(emailId);
		if (list != null && list.size() != 0) {
			throw new GlobalException(JaxError.ALREADY_EXIST_EMAIL, "Email Id already exist");
		}

	}

	@Override
	public void validateDuplicateMobile(String mobileNo) {
		List<Customer> list = customerRepo.getCustomerByMobileCheck(mobileNo);
		if (list != null && list.size() != 0) {
			throw new GlobalException(JaxError.ALREADY_EXIST_MOBILE, "Mobile Number already exist");
		}

	}

}
