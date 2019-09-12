package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.partner.PaymentModeLimitsView;

public interface IPaymentModeLimitsRepository extends CrudRepository<PaymentModeLimitsView, BigDecimal> {
	
	@Query(value = "SELECT * FROM VW_PAYMENT_MODE_LIMIT WHERE BANK_ID =?1 AND CURRENCY_ID=?2 AND CUSTOMER_TYPE_FROM=?3 AND CUSTOMER_TYPE_TO=?4", nativeQuery = true)
	public List<PaymentModeLimitsView> fetchPaymentLimitDetails(BigDecimal bankId,BigDecimal currencyId,String String,String customerTypeTo);

}
