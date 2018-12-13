package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.OrderManagementView;

@Transactional
public interface FcSaleOrderManagementRepository extends CrudRepository<OrderManagementView, Serializable>{
	
	public List<OrderManagementView> findByApplicationCountryIdAndAreaCode(BigDecimal applicationCountryId,BigDecimal areaCode);
	
	public List<OrderManagementView> findByApplicationCountryId(BigDecimal applicationCountryId);
	
	@Query(value = "SELECT * FROM JAX_VW_ORDER_MANAGEMENT WHERE APPLICATION_COUNTRY_ID=?1 AND COLLECTION_DOCUMENT_NO=?2 AND COLLECTION_DOC_FINANCE_YEAR=?3", nativeQuery = true)
	public List<OrderManagementView> checkPendingOrders(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear);
	
	public List<OrderManagementView> findByDeliveryDetailsId(BigDecimal deliveryDetailSeqId);

}
