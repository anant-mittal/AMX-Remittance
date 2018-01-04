package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.ViewTransfer;

@Transactional
public interface VTransferRepository extends CrudRepository<ViewTransfer, String> {

	@Query(nativeQuery = true, value = "select * from v_TRANSFER WHERE  CUSREF = ?1"
			+ "            AND    ACYYMM = TRUNC(SYSDATE,'MONTH')" + "            AND    NVL(CANSTS,' ') = ' '")
	public List<ViewTransfer> getMonthlyTransactionByCustomerReference(BigDecimal cusRef);

	@Query(value = "SELECT FROM v_TRANSFER WHERE CUSREF=:cusref AND TRUNC(DOCDAT)=TRUNC(SYSDATE) "
			+ " AND NVL(CANSTS,' ') = ' ' AND LOCCOD=:locCode "
			+ " AND BNFBNKCOD=:bnfBankCode AND NVL(BNFACNO,' ') = NVL(:accountNo,' ')"
			+ " AND NVL(E_BNFNAME,'')=NVL(:engBnfName,'')", nativeQuery = true)
	public List<ViewTransfer> todayTransactionCheck(@Param("cusref") BigDecimal cusref,
			@Param("bnfBankCode") String bnfBankCode, @Param("accountNo") String accountNo,
			@Param("engBnfName") String engBnfName, @Param("locCode") BigDecimal locCode);
	
	

	@Query("select ht from ViewTransfer ht where ht.cusRef=:cusref and ht.trnfStatus <>'C'")
	public List<ViewTransfer> getTotalNoOfTrnx(@Param("cusref") BigDecimal cusref);
	

}