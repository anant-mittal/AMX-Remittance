package com.amx.jax.complaince;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExCbkReportLogRepo extends JpaRepository<ExCbkStrReportLOG, Serializable> {

	@Query(value = " select * from EX_CBK_STR_REPORT_LOG  where trunc(CREATED_DATE) between to_date(?1,'dd/mm/yyyy') and to_date(?2,'dd/mm/yyyy') ORDER BY CREATED_DATE DESC", nativeQuery = true)
	public List<ExCbkStrReportLOG> getComplainceInqData(String fromDate, String toDate);

	@Transactional
	@Modifying
	@Query(value = "update EX_CBK_STR_REPORT_LOG set CBK_STR_ID = ?1 where REMITTANCE_TRANSACTION_ID=?2", nativeQuery = true)
	public int updatePlaceOrdersForRateAlert(String strId, BigDecimal tranxRef);

}
