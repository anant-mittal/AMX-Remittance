package com.amx.jax.repository;


import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.amx.jax.dbmodel.ReceiptPaymentApp;


@Transactional
public interface ReceiptPaymentAppRepository extends CrudRepository<ReceiptPaymentApp, BigDecimal>{

	
	
	@Query("select appl from ReceiptPaymentApp appl where appl.customerId=:customerId and appl.foreignCurrencyId =:currencyId and trunc(sysdate)=trunc(createdDate) "
			+ " and NVL(applicationStatus,' ') NOT IN('T')")
	public List<ReceiptPaymentApp> deActivateNotUsedApplication(@Param("customerId") BigDecimal customerId,@Param("currencyId") BigDecimal currencyId);


	@Query("select appl from ReceiptPaymentApp appl where appl.customerId=:customerId and appl.pgPaymentSeqDtlId =:pgPaymentSeqDtlId and appl.isActive ='Y' and NVL(appl.applicationStatus,' ')='S' and trunc(sysdate)=trunc(appl.documentDate)")
	public List<ReceiptPaymentApp> fetchreceiptPaymentAppl(@Param("customerId") BigDecimal customerId,@Param("pgPaymentSeqDtlId") BigDecimal pgPaymentSeqDtlId);
	
	@Query("select appl from ReceiptPaymentApp appl where appl.customerId=:customerId and appl.documentNo =:appldocNo and appl.documentFinanceYear =:applfyear and appl.isActive ='Y'")
	public ReceiptPaymentApp getApplciationDetailsByDocNoFYear(@Param("customerId") BigDecimal customerId, @Param("appldocNo") BigDecimal appldocNo,@Param("applfyear") BigDecimal applfyear);
	
	
	@Query("select appl from ReceiptPaymentApp appl where appl.customerId=:customerId and appl.pgPaymentSeqDtlId =:pgPaymentSeqDtlId")
	public List<ReceiptPaymentApp> getApplicationByPagdetailSeqIAndcustomerId(@Param("customerId") BigDecimal customerId, @Param("pgPaymentSeqDtlId") BigDecimal pgPaymentSeqDtlId);
	
	public ReceiptPaymentApp findByCustomerIdAndTransactionFinanceYearAndTransactionRefNo(BigDecimal customerId,BigDecimal transactionYear,BigDecimal transactionNo);
	
}
