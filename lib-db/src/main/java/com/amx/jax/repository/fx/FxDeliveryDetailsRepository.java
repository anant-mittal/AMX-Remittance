package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;

public interface FxDeliveryDetailsRepository extends CrudRepository<FxDeliveryDetailsModel, Serializable>{
	
	public FxDeliveryDetailsModel findByDeleviryDelSeqIdAndIsActive(BigDecimal deliveryDetailsId,String isActive);
	
	@Modifying
	@Query("update FxDeliveryDetailsModel sd set sd.orderLock = ?4 , sd.employeeId = ?2 , sd.updatedBy = ?3 , sd.uopdateDate = ?4 , sd.orderStatus = ?5 where sd.deleviryDelSeqId = ?1 ")
	public void updateOrderLockDetails(BigDecimal deleviryDelSeqId,BigDecimal employeeId,String userName,Date currenctDate,String orderStatus);
	
	@Modifying
	@Query("update FxDeliveryDetailsModel sd set sd.orderLock = ?6 , sd.employeeId = ?2 , sd.updatedBy = ?3 , sd.uopdateDate = ?4 , sd.orderStatus = ?5 where sd.deleviryDelSeqId = ?1 ")
	public void updateOrderReleaseDetails(BigDecimal deleviryDelSeqId,BigDecimal employeeId,String userName,Date currenctDate,String orderStatus,Date orderLock);
	
	@Modifying
	@Query("update FxDeliveryDetailsModel sd set sd.updatedBy = ?2 , sd.uopdateDate = ?3 , sd.orderStatus = ?4 where sd.deleviryDelSeqId = ?1 ")
	public void updateDispatchStatusDetails(BigDecimal deleviryDelSeqId,String userName,Date currenctDate,String orderStatus);
	
}
