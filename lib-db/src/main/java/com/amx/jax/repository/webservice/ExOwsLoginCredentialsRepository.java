package com.amx.jax.repository.webservice;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.webservice.ExOwsLoginCredentials;

public interface ExOwsLoginCredentialsRepository extends CrudRepository<ExOwsLoginCredentials, BigDecimal>
{
	public ExOwsLoginCredentials findByApplicationCountryAndBankCode(String applicationCountry, String bankCode);
}



