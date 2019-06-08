package com.amx.jax.customer.manager;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.request.UpdateCustomerPersonalDetailRequest;
import com.amx.jax.userservice.dao.CustomerDao;

@Component
public class CustomerPersonalDetailManager {

	@Autowired
	CustomerDao customerDao;

	private static final Logger log = LoggerFactory.getLogger(CustomerPersonalDetailManager.class);

	@Transactional
	public void updateCustomerPersonalDetail(Customer customer, UpdateCustomerPersonalDetailRequest req) {

		if (req.getDateOfBirth() != null) {
			customer.setDateOfBirth(req.getDateOfBirth());
		}
		if (req.getInsurance() != null) {
			customer.setMedicalInsuranceInd(req.getInsurance() ? ConstantDocument.Yes : ConstantDocument.No);
		}
		if (req.getCustomerSignature() != null) {
			customer.setSignatureSpecimen(req.getCustomerSignature());
		}
		customerDao.saveCustomer(customer);
	}

}
