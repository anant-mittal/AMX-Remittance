package com.amx.service_provider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.scope.TenantScoped;
import com.amx.service_provider.manger.ServiceProviderManger;

//@Component
@TenantScoped
@Service
//@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServiceProviderService
{
	@Autowired
	ServiceProviderManger serviceProviderGateWayManger;

	public ServiceProviderResponse getQutation(TransactionData txn_data, Customer customer_data, Benificiary bene_data)
	{
		// TODO: see if you need to get data from Repo to get the customer and bene
		// objects using incoming Ids only
		return serviceProviderGateWayManger.getQutation(txn_data, customer_data, bene_data);
	}

	public ServiceProviderResponse sendRemittance(TransactionData txn_data, Customer customer_data, Benificiary bene_data)
	{
		// TODO: see if you need to get data from Repo to get the customer and bene
		// objects using incoming Ids only
		return serviceProviderGateWayManger.sendRemittance(txn_data, customer_data, bene_data);
	}

}
