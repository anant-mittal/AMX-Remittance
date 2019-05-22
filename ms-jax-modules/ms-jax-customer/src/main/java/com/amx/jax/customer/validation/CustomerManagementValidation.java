package com.amx.jax.customer.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.IDNumberLengthCheckView;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.remittance.IIdNumberLengthCheckRepository;

@Component
public class CustomerManagementValidation {

	@Autowired
	IIdNumberLengthCheckRepository idnumberLengthCheckRepos;

	public void validateIdentityIntLength(String identityInt, BigDecimal identityTypeId) {

		IDNumberLengthCheckView idnumberLengthCheck = idnumberLengthCheckRepos.findByIDTypeId(identityTypeId);
		if (idnumberLengthCheck != null) {
			BigDecimal maxLength = idnumberLengthCheck.getiDLength();
			BigDecimal minimumLength = idnumberLengthCheck.getMinIDLength();
			BigDecimal identityIntLength = identityInt == null ? BigDecimal.ZERO : new BigDecimal(identityInt.length());
			if (identityIntLength.compareTo(minimumLength) < 0 || identityIntLength.compareTo(maxLength) > 0) {
				throw new GlobalException(JaxError.ID_TYPE_LENGTH_NOT_DEFINED,
						"The minimum length should be:" + minimumLength + " and maximum should be :" + maxLength);
			}
		} else {
			throw new GlobalException(JaxError.ID_TYPE_LENGTH_NOT_DEFINED,
					"Id length setup is missing  in paramter :" + identityInt + " identityTypeId :" + identityTypeId);
		}
	}

	public void validateCustomerRecord(Customer customer) {

		if (customer.getSignatureSpecimenClob() == null) {
			throw new GlobalException(JaxError.CUSTOMER_SIGNATURE_UNAVAILABLE, "customer signature unavailable");
		}

		boolean insuranceCheck = ("Y".equals(customer.getMedicalInsuranceInd())
				|| "N".equals(customer.getMedicalInsuranceInd()));
		if (!insuranceCheck) {
			throw new GlobalException(JaxError.INVALID_INSURANCE_INDICATOR, "invalid insurance indicator");
		}
	}
}
