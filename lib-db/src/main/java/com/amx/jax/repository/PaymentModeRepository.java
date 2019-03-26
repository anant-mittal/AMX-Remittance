package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.PaymentModeModel;


public interface PaymentModeRepository  extends CrudRepository<PaymentModeModel, Serializable>{

	@Query("select c from PaymentModeModel c where paymentModeCode=:paymentModeCode and  isActive='Y'")
	public PaymentModeModel getPaymentModeDetails(@Param("paymentModeCode") String paymentModeCode);
	
	
	@Query(value = "Select A.Payment_Mode_Id,B.Payment_Mode_Desc_Id,A.Payment_Code,B.Local_Payment_Name,B.Language_Id,A.Isactive from EX_PAYMENT_MODE A,EX_PAYMENT_MODE_DESC B where A.Payment_Mode_Id = B.Payment_Mode_Id and A.Isactive = 'Y' and B.Language_Id = ?1", nativeQuery = true)
	public List<Object[]> fetchModeOfPayment(BigDecimal languageId);
	
	
	@Query("select c from PaymentModeModel c where paymentModeId=:paymentModeid and  isActive='Y'")
	public PaymentModeModel getPaymentModeDetailsById(@Param("paymentModeid") BigDecimal paymentModeid);
}
