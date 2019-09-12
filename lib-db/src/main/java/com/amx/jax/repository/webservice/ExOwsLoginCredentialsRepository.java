package com.amx.jax.repository.webservice;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.webservice.ExOwsLoginCredentials;

@Component
public interface ExOwsLoginCredentialsRepository extends CrudRepository<ExOwsLoginCredentials, BigDecimal>
{
	
	public ExOwsLoginCredentials findByApplicationCountryAndBankCode(String applicationCountry, String bankCode);
}



