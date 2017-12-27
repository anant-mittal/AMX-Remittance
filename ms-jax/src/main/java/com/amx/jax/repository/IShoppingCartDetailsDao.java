package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;

public interface IShoppingCartDetailsDao extends JpaRepository<ShoppingCartDetails, Serializable>{

	@Query("select ap from ShoppingCartDetails ap where ap.customerId=:customerid "
			+ " and ap.paymentId=:paymentid and ap.documentFinanceYear=:docFyr")
	public List<ShoppingCartDetails> getApplicationDetailsFromView(
			@Param("customerid") BigDecimal customerid, 
			@Param("paymentid") String paymentid,
			@Param("docFyr") BigDecimal docFyr);
}
