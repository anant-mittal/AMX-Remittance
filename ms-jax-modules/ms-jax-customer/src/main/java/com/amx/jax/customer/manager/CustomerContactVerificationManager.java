package com.amx.jax.customer.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.repository.CustomerContactVerificationRepository;

@Component
public class CustomerContactVerificationManager {

	@Autowired
	CustomerContactVerificationRepository customerContactVerificationRepository;

	public BoolRespModel validate(BigDecimal id) {
		CustomerContactVerification x = customerContactVerificationRepository.findById(id);

		return new BoolRespModel();
	}

	public BoolRespModel verifyByCode(BigDecimal id, BigDecimal code) {

		return new BoolRespModel();
	}

}
