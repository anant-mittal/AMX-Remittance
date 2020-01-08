package com.amx.jax.model.response.serviceprovider;

import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;

public class Get_Rmittance_Details_Call_Response extends ServiceProviderResponse
{
	Benificiary beneficiaryDto;
	Customer customerDto;
	TransactionData transactionDto;

	public Get_Rmittance_Details_Call_Response()
	{
		beneficiaryDto = new Benificiary();
		customerDto = new Customer();
		transactionDto = new TransactionData();
	}

	public Benificiary getBeneficiaryDto()
	{
		return beneficiaryDto;
	}

	public void setBeneficiaryDto(Benificiary beneficiaryDto)
	{
		this.beneficiaryDto = beneficiaryDto;
	}

	public Customer getCustomerDto()
	{
		return customerDto;
	}

	public void setCustomerDto(Customer customerDto)
	{
		this.customerDto = customerDto;
	}

	public TransactionData getTransactionDto()
	{
		return transactionDto;
	}

	public void setTransactionDto(TransactionData transactionDto)
	{
		this.transactionDto = transactionDto;
	}

}
