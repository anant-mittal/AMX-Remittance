package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.partner.RemitTrnxSrvProv;

public interface IRemitTrnxSrvProvRepository extends CrudRepository<RemitTrnxSrvProv, Serializable>{
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update RemitTrnxSrvProv remit set remit.partnerSessionId =:partnerSessionId,remit.partnerReferenceNo =:partnerReferenceNo where remit.remittanceTransactionId=:remittanceTransactionId")
	public void updateServiceProviderDetails(@Param("partnerSessionId") String partnerSessionId,@Param("partnerReferenceNo") String partnerReferenceNo,@Param("remittanceTransactionId") BigDecimal remittanceTransactionId);

}
