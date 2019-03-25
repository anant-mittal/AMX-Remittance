package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;

@Transactional
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
	public void updateStatusDeliveryDetails(BigDecimal deleviryDelSeqId,String userName,Date currenctDate,String orderStatus);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.customerId=?1 and sd.orderStatus=?2 and isActive ='Y' ")
	public List<FxOrderTransactionModel> searchOrdeDetailsByCustomerId(BigDecimal customerId, String orderStatus);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.branchDesc= ?1 and sd.orderStatus=?2 and isActive ='Y' ")
	public List<FxOrderTransactionModel> searchOrderDetailsByBranchDesc(String branchDesc,String orderStatus);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.transactionReferenceNo= ?1 and sd.orderStatus=?2 ")
	public List<FxOrderTransactionModel> searchOrderDetailsByTrnxRefNo(String transactionReferenceNo, String orderStatus);
		
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.transactionReferenceNo= ?1 and sd.branchDesc= ?2 and sd.orderStatus=?3 ")
	public List<FxOrderTransactionModel> searchOrderDetailsbyOrderIdNdBranchDesc(String transactionReferenceNo, String branchDesc, String orderStatus);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.transactionReferenceNo= ?1 and sd.customerId=?2 and sd.orderStatus=?3")
	public List<FxOrderTransactionModel> searchEmployeeDetailsByOrderIdNdCustomerId(String transactionReferenceNo,BigDecimal customerId, String orderStatus);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.branchDesc= ?1 and sd.customerId=?2 and sd.orderStatus=?3 and isActive ='Y'")
	public List<FxOrderTransactionModel> searchOrderDetailsByBranchDescNdCustomerID(String branchDesc,BigDecimal customerId, String orderStatus);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.transactionReferenceNo= ?1 and sd.branchDesc= ?2 and sd.customerId=?3 and sd.orderStatus=?4 ")
	public List<FxOrderTransactionModel> searchOrderDetailsByAll(String transactionReferenceNo,String branchDesc,BigDecimal customerId, String orderStatus);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.transactionReferenceNo= ?1 ")
	public List<FxOrderTransactionModel> searchTransactionRefNo(String transactionReferenceNo);
	
	
}
