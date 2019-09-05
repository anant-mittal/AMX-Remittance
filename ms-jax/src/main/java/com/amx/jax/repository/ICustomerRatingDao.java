package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerRating;
import com.amx.jax.model.customer.CustomerRatingDTO;

public interface ICustomerRatingDao extends JpaRepository<CustomerRating, Serializable> {
	void save(CustomerRatingDTO dto);

	@Query("select c from CustomerRating c where remittanceTransactionId=?1")
	public List<Customer> getCustomerRatingByRemittanceTransactionId(String remittanceTransactionId);
	
	@Query("select c from CustomerRating c where remittanceTransactionId=?1")
	public CustomerRating getCustomerRatingDataByRemittanceTransactionId(BigDecimal remittanceTransactionId);
		
	@Query("select c from CustomerRating c where delvSeqId=?1")
	public CustomerRating getCustomerRatingDataBycollectionDocNo(BigDecimal delvSeqId);
	
	@Query("select c from CustomerRating c where delvSeqId=?1")
	public CustomerRating getCustomerRatingDataBydelvSeqId(BigDecimal delvSeqId);
	
	

}
