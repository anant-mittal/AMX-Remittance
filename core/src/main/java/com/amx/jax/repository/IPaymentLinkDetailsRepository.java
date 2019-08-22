package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.PaymentLinkModel;

public interface IPaymentLinkDetailsRepository extends CrudRepository<PaymentLinkModel, BigDecimal> {

	@Query("select pl from PaymentLinkModel pl where pl.customerId=?1 and trunc(sysdate) > trunc(pl.linkDate) "
			+ " and pl.isActive ='Y' ")
	List<PaymentLinkModel> deactivatePrevLink(BigDecimal customerId);

	@Query("select pl from PaymentLinkModel pl where pl.customerId=?1 and verificationCode=?2 "
			+ " and pl.isActive ='Y' ")
	PaymentLinkModel fetchPayLinkIdForCustomer(BigDecimal customerId, String hashVerifyCode);

	@Query("select pl from PaymentLinkModel pl where pl.linkId=?1 and verificationCode=?2 " + " and pl.isActive ='Y' ")
	PaymentLinkModel fetchPaymentByLinkIdandCode(BigDecimal linkId, String verificationCode);

}
