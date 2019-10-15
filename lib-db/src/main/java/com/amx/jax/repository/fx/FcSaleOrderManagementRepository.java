package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.OrderManagementView;

@Transactional
public interface FcSaleOrderManagementRepository extends CrudRepository<OrderManagementView, Serializable>{
	
	public List<OrderManagementView> findByApplicationCountryIdAndAreaCode(BigDecimal applicationCountryId,BigDecimal areaCode);
	
	public List<OrderManagementView> findByApplicationCountryIdAndGovernateId(BigDecimal applicationCountryId,BigDecimal governateId);
	
	public List<OrderManagementView> findByApplicationCountryId(BigDecimal applicationCountryId);
	
	@Query(value = "SELECT * FROM JAX_VW_ORDER_MANAGEMENT WHERE APPLICATION_COUNTRY_ID=?1 AND COLLECTION_DOCUMENT_NO=?2 AND COLLECTION_DOC_FINANCE_YEAR=?3 ORDER BY DELIVERY_DATE DESC", nativeQuery = true)
	public List<OrderManagementView> checkPendingOrders(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear);
	
	public List<OrderManagementView> findByDeliveryDetailsId(BigDecimal deliveryDetailSeqId);
	
	@Query(value = "SELECT * FROM JAX_VW_ORDER_MANAGEMENT WHERE APPLICATION_COUNTRY_ID=?1 AND TRUNC(DELIVERY_CREATED_DATE) >= TRUNC(?2) AND TRUNC(DELIVERY_CREATED_DATE) <= TRUNC(?3) ORDER BY DELIVERY_DATE DESC", nativeQuery = true)
	public List<OrderManagementView> fetchLastOneWeekRecords(BigDecimal applicationCountryId,Date fromDate,Date toDate);
	
	@Query(value = "SELECT * FROM JAX_VW_ORDER_MANAGEMENT WHERE APPLICATION_COUNTRY_ID=?1 AND GOVERNORATES_ID=?2 AND TRUNC(DELIVERY_CREATED_DATE) >= TRUNC(?3) AND TRUNC(DELIVERY_CREATED_DATE) <= TRUNC(?4) ORDER BY DELIVERY_DATE DESC", nativeQuery = true)
	public List<OrderManagementView> fetchLastOneWeekRecordsByGovernateId(BigDecimal applicationCountryId,BigDecimal governateId,Date fromDate,Date toDate);
	
}
