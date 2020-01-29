package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;

public interface VwFxDeliveryDetailsRepository extends CrudRepository<VwFxDeliveryDetailsModel, Serializable> {

	@Query(value = "select * from JAX_VW_FX_DELIVERY_DETAIL where DRIVER_EMPLOYEE_ID=?1 and TRUNC(DELIVERY_DATE) = TRUNC(?2)", nativeQuery = true)
	List<VwFxDeliveryDetailsModel> findDriverOrders(BigDecimal driverEmployeeId, Date deliveryDate);

	VwFxDeliveryDetailsModel findByDeleviryDelSeqIdAndDriverEmployeeId(BigDecimal deliveryDetailSeqId,
			BigDecimal driverEmployeeId);

	@Query(value = "select * from JAX_VW_FX_DELIVERY_DETAIL where  DRIVER_EMPLOYEE_ID=?1  and trunc(DELIVERY_DATE) <= trunc(sysdate) and  trunc(DELIVERY_DATE) >= trunc(sysdate-?2)", nativeQuery = true)
	List<VwFxDeliveryDetailsModel> findHistoricalDriverOrders(BigDecimal driverEmployeeId, Integer days);
	
}
