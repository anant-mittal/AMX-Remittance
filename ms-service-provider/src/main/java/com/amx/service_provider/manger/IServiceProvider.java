package com.amx.service_provider.manger;

import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;

public interface IServiceProvider
{
	ServiceProviderResponse getQutation(TransactionData txn_data, Customer customer_data, Benificiary bene_data);

	ServiceProviderResponse sendRemittance(TransactionData txn_data, Customer customer_data, Benificiary bene_data);
}
