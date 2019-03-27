package com.amx.jax.repository;

/**
 * @auth :rabil
 * @date :06/03/2019
 * 
 */
		
import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.LanguageType;
import com.amx.jax.dbmodel.PaymentModeDesc;
import com.amx.jax.dbmodel.PaymentModeModel;

public interface IPaymentModeDescRespo extends CrudRepository<PaymentModeDesc, Serializable>{

	public PaymentModeDesc findByPaymentModeAndLanguageType(PaymentModeModel payMode, LanguageType languageType); 
}
