package com.amx.jax.repository.remittance;

/**
 * @author rabil
 */
import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ServiceProviderCredentialsModel;

public interface IServiceProviderCredentailsRepository extends CrudRepository<ServiceProviderCredentialsModel, Serializable>{

	public ServiceProviderCredentialsModel findByLoginCredential1(String loginCredentails);
}
