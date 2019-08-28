package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.PaygDetailsModel;

public interface PaygDetailsRepository extends CrudRepository<PaygDetailsModel, Serializable>{

	public PaygDetailsModel findByCollDocNumberAndCustomerId(BigDecimal collDocNumber,BigDecimal customerId);
	
	public PaygDetailsModel findByPgPaymentId(BigDecimal pgPaymentId);

	@Query("select pl from PaygDetailsModel pl where pl.customerId=?1 and verifycode=?2 "
			+ " and pl.linkActive ='Y' ")
	public PaygDetailsModel fetchPayLinkIdForCustomer(BigDecimal customerId, String hashVerifyCode);

	@Query("select pl from PaygDetailsModel pl where pl.paygTrnxSeqId=?1 and verifycode=?2 ")
	public PaygDetailsModel fetchPaymentByLinkIdandCode(BigDecimal linkId, String verificationCode);

	@Query("select pl from PaygDetailsModel pl where pl.customerId=?1 and trunc(sysdate) > trunc(pl.linkDate) "
			+ " and pl.linkActive ='Y' ")
	public List<PaygDetailsModel> deactivatePrevLink(BigDecimal customerId);

	@Query("select pl from PaygDetailsModel pl where pl.customerId=?1 and  pl.paymentType=?2 "
			+ " and pl.linkActive ='Y' ")
	public List<PaygDetailsModel> deactivatePreviousLinkResend(BigDecimal customerId, String paymentType);
}
