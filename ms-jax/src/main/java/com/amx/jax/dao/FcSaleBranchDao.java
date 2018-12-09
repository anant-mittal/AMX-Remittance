package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.dbmodel.fx.UserStockView;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.CurrencyWiseDenominationRepository;
import com.amx.jax.repository.EmployeeRespository;
import com.amx.jax.repository.ForeignCurrencyAdjustRepository;
import com.amx.jax.repository.ICollectionRepository;
import com.amx.jax.repository.ReceiptPaymentRespository;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.fx.FcSaleOrderManagementRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.repository.fx.UserStockRepository;

@Component
public class FcSaleBranchDao {
	
	@Autowired
	FcSaleOrderManagementRepository fcSaleOrderManagementRepository;
	
	@Autowired
	UserStockRepository userStockRepository;
	
	@Autowired
	EmployeeRespository employeeRespository;
	
	@Autowired
	FxDeliveryDetailsRepository fxDeliveryDetailsRepository;
	
	@Autowired
	ReceiptPaymentRespository receiptPaymentRespository;
	
	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	ICollectionRepository collectionRepository;
	
	@Autowired
	ForeignCurrencyAdjustRepository foreignCurrencyAdjustRepository;
	
	@Autowired
	CurrencyWiseDenominationRepository currencyWiseDenominationRepository;
	
	public List<OrderManagementView> fetchFcSaleOrderManagement(BigDecimal applicationcountryId,BigDecimal areaCode){
		return fcSaleOrderManagementRepository.findByApplicationCountryId(applicationcountryId);
	}
	
	public List<OrderManagementView> checkPendingOrders(BigDecimal applicationcountryId,BigDecimal orderNumber,BigDecimal orderYear){
		return fcSaleOrderManagementRepository.checkPendingOrders(applicationcountryId,orderNumber,orderYear);
	}
	
	public List<UserStockView> fetchUserStockCurrencyCurrentDate(BigDecimal countryId,String userName,BigDecimal countryBranchId,BigDecimal foreignCurrencyId){
		return userStockRepository.fetchUserStockByCurrencyDate(countryId, userName, countryBranchId, foreignCurrencyId);
	}
	
	public List<Object[]> fetchUserStockCurrentDateSum(BigDecimal countryId,String userName,BigDecimal countryBranchId){
		return userStockRepository.fetchUserStockByDateSum(countryId, userName, countryBranchId);
	}
	
	public List<UserStockView> fetchUserStockCurrentDate(BigDecimal countryId,String userName,BigDecimal countryBranchId){
		return userStockRepository.fetchUserStockByDate(countryId, userName, countryBranchId);
	}
	
	public List<Employee> fetchEmpDriverDetails(String userType,String isActive){
		return employeeRespository.fetchEmpDriverDetails(userType,isActive);
	}
	
	public FxDeliveryDetailsModel fetchDeliveryDetails(BigDecimal deliveryDetailsId,String isActive){
		return fxDeliveryDetailsRepository.findByDeleviryDelSeqIdAndIsActive(deliveryDetailsId, isActive);
	}
	
	@Transactional
	public void saveDeliveryDetailsDriverId(FxDeliveryDetailsModel deliveryDetail){
		if(deliveryDetail != null) {
			fxDeliveryDetailsRepository.save(deliveryDetail);
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for delivery detail");
		}
	}
	
	@Transactional
	public void saveDeliveryDetails(FxDeliveryDetailsModel deliveryDetailNew,FxDeliveryDetailsModel deliveryDetail,List<OrderManagementView> lstOrderManagement){
		if(lstOrderManagement != null){
			if(deliveryDetailNew != null) {
				fxDeliveryDetailsRepository.save(deliveryDetailNew);
			}
			
			if(deliveryDetail != null) {
				fxDeliveryDetailsRepository.save(deliveryDetail);
			}
			
			for (OrderManagementView orderManagementView : lstOrderManagement) {
				receiptPaymentRespository.updateDeliveryDetails(orderManagementView.getReceiptPaymentId(),deliveryDetailNew.getDeleviryDelSeqId(),deliveryDetailNew.getCreatedBy(),deliveryDetailNew.getCreatedDate());
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for order management");
		}
	}
	
	public EmployeeDetailsView fetchEmployeeDetails(BigDecimal employeeId){
		return employeeDetailsRepository.findByEmployeeId(employeeId);
	}
	
	public List<CollectionModel> fetchCollectionData(BigDecimal collectDocNo,BigDecimal collectDocYear){
		return collectionRepository.findByDocumentNoAndDocumentFinanceYear(collectDocNo, collectDocYear);
	}
	
	@Transactional
	public void printOrderSave(List<ForeignCurrencyAdjust> foreignCurrencyAdjusts,HashMap<BigDecimal, String> mapInventoryReceiptPayment,String userName,Date currenctDate,BigDecimal deliveryDetailsId,String orderStatus){
		if(foreignCurrencyAdjusts != null && foreignCurrencyAdjusts.size() != 0 && deliveryDetailsId != null) {
			// before updating need to check the status is ordered
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.ACP)) {
					for (ForeignCurrencyAdjust foreignCurrencyAdjust : foreignCurrencyAdjusts) {
						foreignCurrencyAdjustRepository.save(foreignCurrencyAdjust);
					}
					for (Entry<BigDecimal, String> receiptPayment : mapInventoryReceiptPayment.entrySet()) {
						receiptPaymentRespository.updateInventoryId(receiptPayment.getKey(),receiptPayment.getValue(),userName,currenctDate);
					}
					
					// update status
					fxDeliveryDetailsModel.setUopdateDate(new Date());
					fxDeliveryDetailsModel.setUpdatedBy(userName);
					fxDeliveryDetailsModel.setOrderStatus(orderStatus);
					fxDeliveryDetailsRepository.save(fxDeliveryDetailsModel);
				}else {
					throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not acepoted to print");
				}
			}else {
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or order status is empty for delivery details");
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records or delivery details id is empty");
		}
	}
	
	public List<CurrencyWiseDenomination> fetchCurrencyDenomination(BigDecimal currencyId,String isActive){
		return currencyWiseDenominationRepository.fetchCurrencyDenomination(currencyId, isActive);
	}
	
	public void saveOrderLockDetails(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			
			// before updating need to check the status is ordered
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.ORD)) {
					fxDeliveryDetailsModel.setOrderLock(new Date());
					fxDeliveryDetailsModel.setEmployeeId(employeeId);
					fxDeliveryDetailsModel.setUopdateDate(new Date());
					fxDeliveryDetailsModel.setUpdatedBy(userName);
					fxDeliveryDetailsModel.setOrderStatus(orderStatus);
					fxDeliveryDetailsRepository.save(fxDeliveryDetailsModel);
				}else {
					throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not Ordered to lock");
				}
			}else {
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or order status is empty for delivery details");
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for order management");
		}
	}
	
	public void saveOrderReleaseDetails(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.ACP)) {
					fxDeliveryDetailsModel.setOrderLock(null);
					fxDeliveryDetailsModel.setEmployeeId(null);
					fxDeliveryDetailsModel.setUopdateDate(new Date());
					fxDeliveryDetailsModel.setUpdatedBy(userName);
					fxDeliveryDetailsModel.setOrderStatus(orderStatus);
					fxDeliveryDetailsRepository.save(fxDeliveryDetailsModel);
				}else {
					throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not Accepted to release");
				}
			}else {
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or order status is empty for delivery details");
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for order management");
		}
	}
	
	public void saveDispatchOrder(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.OFD_CNF)) {
					fxDeliveryDetailsModel.setUopdateDate(new Date());
					fxDeliveryDetailsModel.setUpdatedBy(userName);
					fxDeliveryDetailsModel.setOrderStatus(orderStatus);
					fxDeliveryDetailsRepository.save(fxDeliveryDetailsModel);
				}else {
					throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not out for delivery pending acknowledgment to dispatch");
				}
			}else {
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or order status is empty for delivery details");
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for order management");
		}
	}
	
	public List<ForeignCurrencyAdjust> fetchByCollectionDetails(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode){
		return foreignCurrencyAdjustRepository.fetchByCollectionDetails(documentNo,documentYear,companyId,documentCode);
	}
	
	public void saveAcknowledgeDriver(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.OFD_ACK)) {
					fxDeliveryDetailsModel.setUopdateDate(new Date());
					fxDeliveryDetailsModel.setUpdatedBy(userName);
					fxDeliveryDetailsModel.setOrderStatus(orderStatus);
					fxDeliveryDetailsRepository.save(fxDeliveryDetailsModel);
				}else {
					throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not out for delivery acknowledge ");
				}
			}else {
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or order status is empty for delivery details");
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for order management");
		}
	}
	
	public void saveReturnAcknowledge(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.RTD_ACK)) {
					fxDeliveryDetailsModel.setUopdateDate(new Date());
					fxDeliveryDetailsModel.setUpdatedBy(userName);
					fxDeliveryDetailsModel.setOrderStatus(orderStatus);
					fxDeliveryDetailsRepository.save(fxDeliveryDetailsModel);
				}else {
					throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not return acknowledge");
				}
			}else {
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or order status is empty for delivery details");
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for order management");
		}
	}
	
	public void saveAcceptCancellation(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.CND_ACK)) {
					fxDeliveryDetailsModel.setUopdateDate(new Date());
					fxDeliveryDetailsModel.setUpdatedBy(userName);
					fxDeliveryDetailsModel.setOrderStatus(orderStatus);
					fxDeliveryDetailsRepository.save(fxDeliveryDetailsModel);
				}else {
					throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not cancelled ");
				}
			}else {
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or order status is empty for delivery details");
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for order management");
		}
	}
}
