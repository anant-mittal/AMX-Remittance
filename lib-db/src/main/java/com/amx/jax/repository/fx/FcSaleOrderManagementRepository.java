package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.OrderManagementView;

public interface FcSaleOrderManagementRepository extends CrudRepository<OrderManagementView, Serializable>{
	
	public List<OrderManagementView> findByApplicationCountryIdAndAreaCode(BigDecimal applicationCountryId,BigDecimal areaCode);
	
	public List<OrderManagementView> findByApplicationCountryId(BigDecimal applicationCountryId);
	
	@Query("select em from OrderManagementView em where em.applicationCountryId =:applicationCountryId and em.collectionDocumentNo =:orderNumber")
	public List<OrderManagementView> checkPendingOrders(BigDecimal applicationCountryId,BigDecimal orderNumber);

}
