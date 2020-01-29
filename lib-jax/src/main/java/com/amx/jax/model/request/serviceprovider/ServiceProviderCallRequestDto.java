package com.amx.jax.model.request.serviceprovider;

public class ServiceProviderCallRequestDto {
	Benificiary beneficiaryDto;
	Customer customerDto;
	TransactionData transactionDto;

	public ServiceProviderCallRequestDto() {
		super();
		this.beneficiaryDto = new Benificiary();
		this.customerDto = new Customer();
		this.transactionDto = new TransactionData();
	}

	public Benificiary getBeneficiaryDto() {
		return beneficiaryDto;
	}

	public void setBeneficiaryDto(Benificiary beneficiaryDto) {
		this.beneficiaryDto = beneficiaryDto;
	}

	public Customer getCustomerDto() {
		return customerDto;
	}

	public void setCustomerDto(Customer customerDto) {
		this.customerDto = customerDto;
	}

	public TransactionData getTransactionDto() {
		return transactionDto;
	}

	public void setTransactionDto(TransactionData transactionDto) {
		this.transactionDto = transactionDto;
	}
}
