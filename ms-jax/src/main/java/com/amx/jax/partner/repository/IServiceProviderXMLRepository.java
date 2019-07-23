package com.amx.jax.partner.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.partner.ServiceProviderXmlLog;

public interface IServiceProviderXMLRepository extends CrudRepository<ServiceProviderXmlLog, Serializable>{

}
