package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.ReceiptPayment;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.fx.ForeignCurrencyOldModel;
import com.amx.jax.dbmodel.fx.ForeignCurrencyStockTransfer;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.dbmodel.fx.UserStockView;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.CurrencyWiseDenominationRepository;
import com.amx.jax.repository.EmployeeRespository;
import com.amx.jax.repository.ForeignCurrencyAdjustOldRepository;
import com.amx.jax.repository.ForeignCurrencyAdjustRepository;
import com.amx.jax.repository.ForeignCurrencyStockRepository;
import com.amx.jax.repository.ICollectionRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.ReceiptPaymentRespository;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.fx.FcSaleOrderManagementRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.repository.fx.FxOrderTransactionRespository;
import com.amx.jax.repository.fx.UserStockRepository;
import com.amx.jax.repository.fx.VwFxDeliveryDetailsRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;

@Component
public class FcSaleBranchDao {
	
	@Autowired
	FcSaleOrderManagementRepository fcSaleOrderManagementRepository;
	
	@Autowired
	VwFxDeliveryDetailsRepository vwFxDeliveryDetailsRepository;
	
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
	ForeignCurrencyAdjustOldRepository foreignCurrencyAdjustOldRepository;
	
	@Autowired
	CurrencyWiseDenominationRepository currencyWiseDenominationRepository;
	
	@Autowired
	ForeignCurrencyStockRepository foreignCurrencyStockRepository;
	
	@Autowired
	ApplicationProcedureDao applicationProcedureDao;
	
	@Autowired
	ReceiptPaymentAppRepository receiptPaymentAppRepository;
	
	@Autowired
	FxOrderTransactionRespository fxOrderTransactionRespository;
	
	public List<OrderManagementView> fetchFcSaleOrderManagement(BigDecimal applicationcountryId,BigDecimal areaCode){
		return fcSaleOrderManagementRepository.findByApplicationCountryIdAndAreaCode(applicationcountryId,areaCode);
	}
	
	public List<OrderManagementView> fetchFcSaleOrderManagementByGovernate(BigDecimal applicationcountryId,BigDecimal governate){
		return fcSaleOrderManagementRepository.findByApplicationCountryIdAndGovernateId(applicationcountryId,governate);
	}
	
	public List<OrderManagementView> fetchFcSaleOrderManagementForHeadOffice(BigDecimal applicationcountryId){
		return fcSaleOrderManagementRepository.findByApplicationCountryId(applicationcountryId);
	}
	
	public List<OrderManagementView> checkPendingOrders(BigDecimal applicationcountryId,BigDecimal orderNumber,BigDecimal orderYear){
		return fcSaleOrderManagementRepository.checkPendingOrders(applicationcountryId,orderNumber,orderYear);
	}
	
	public List<UserStockView> fetchUserStockCurrencyCurrentDate(BigDecimal countryId,String userName,BigDecimal countryBranchId,BigDecimal foreignCurrencyId){
		return userStockRepository.fetchUserStockByCurrencyDate(userName, countryBranchId, foreignCurrencyId);
	}
	
	public List<Object[]> fetchUserStockCurrentDateSum(BigDecimal countryId,String userName,BigDecimal countryBranchId){
		return userStockRepository.fetchUserStockByDateSum(userName, countryBranchId);
	}
	
	public List<UserStockView> fetchUserStockCurrentDate(BigDecimal countryId,String userName,BigDecimal countryBranchId){
		return userStockRepository.fetchUserStockByDate(userName, countryBranchId);
	}
	
	public List<Employee> fetchEmpDriverDetails(String userType,String isActive){
		return employeeRespository.fetchEmpDriverDetails(userType,isActive);
	}
	
	public FxDeliveryDetailsModel fetchDeliveryDetails(BigDecimal deliveryDetailsId,String isActive){
		return fxDeliveryDetailsRepository.findByDeleviryDelSeqIdAndIsActive(deliveryDetailsId, isActive);
	}
	
	@Transactional
	public void saveDeliveryDetailsDriverId(FxDeliveryDetailsModel deliveryDetail,String docNoBulk,BigDecimal companyId,BigDecimal documentCode,BigDecimal documentYear){
		BigDecimal documentNo = null;
		if(deliveryDetail != null) {
			fxDeliveryDetailsRepository.save(deliveryDetail);
			if(docNoBulk != null) {
				String[] docNum = docNoBulk.split(",");
				for (int i=0; i < docNum.length; i++){
					documentNo = new BigDecimal(docNum[i]);
					if(documentNo != null) {
						List<ForeignCurrencyAdjust> lstForeignCurrencyAdj = foreignCurrencyAdjustRepository.fetchByDocumentDetails(documentNo, documentYear, companyId, documentCode, ConstantDocument.P);
						if(lstForeignCurrencyAdj != null) {
							for (ForeignCurrencyAdjust foreignCurrencyAdjust : lstForeignCurrencyAdj) {
								foreignCurrencyAdjust.setDocumentStatus(null);
								foreignCurrencyAdjust.setModifiedBy(deliveryDetail.getUpdatedBy());
								foreignCurrencyAdjust.setModifiedDate(new Date());
								foreignCurrencyAdjustRepository.save(foreignCurrencyAdjust);
							}
						}
					}
				}
			}
		}else {
			throw new GlobalException(JaxError.SAVE_FAILED,"No records found for delivery detail");
		}
	}
	
	@Transactional
	public void saveDeliveryDetails(FxDeliveryDetailsModel deliveryDetailNew,FxDeliveryDetailsModel deliveryDetail,List<OrderManagementView> lstOrderManagement,String docNoBulk,BigDecimal companyId,BigDecimal documentCode,BigDecimal documentYear){
		BigDecimal documentNo = null;
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
			
			if(docNoBulk != null) {
				String[] docNum = docNoBulk.split(",");
				for (int i=0; i < docNum.length; i++){
					documentNo = new BigDecimal(docNum[i]);
					if(documentNo != null) {
						List<ForeignCurrencyAdjust> lstForeignCurrencyAdj = foreignCurrencyAdjustRepository.fetchByDocumentDetails(documentNo, documentYear, companyId, documentCode, ConstantDocument.P);
						if(lstForeignCurrencyAdj != null) {
							for (ForeignCurrencyAdjust foreignCurrencyAdjust : lstForeignCurrencyAdj) {
								foreignCurrencyAdjust.setDocumentStatus(null);
								foreignCurrencyAdjust.setModifiedBy(deliveryDetail.getUpdatedBy());
								foreignCurrencyAdjust.setModifiedDate(new Date());
								foreignCurrencyAdjustRepository.save(foreignCurrencyAdjust);
							}
						}
					}
				}
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
	public void printOrderSave(List<ForeignCurrencyAdjust> foreignCurrencyAdjusts,List<ReceiptPayment> updateRecPay,String userName,Date currenctDate,BigDecimal deliveryDetailsId,String orderStatus){
		HashMap<BigDecimal, BigDecimal> mapBranchDocumentNo = new HashMap<>();
		if(foreignCurrencyAdjusts != null && foreignCurrencyAdjusts.size() != 0 && deliveryDetailsId != null) {
			// before updating need to check the status is ordered
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
				if(fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.ACP)) {
					// receipt payment update
					if(updateRecPay != null && !updateRecPay.isEmpty()) {
						for (ReceiptPayment receiptPayment : updateRecPay) {
							BigDecimal branchId = receiptPayment.getLocCode();
							BigDecimal countryId = receiptPayment.getFsCountryMaster().getCountryId();
							BigDecimal companyId = receiptPayment.getFsCompanyMaster().getCompanyId();
							BigDecimal docYear = receiptPayment.getDocumentFinanceYear();
							BigDecimal documentNo = null;
							Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(countryId, companyId,ConstantDocument.DOCUMENT_CODE_FOR_FCSALE,docYear,ConstantDocument.Update,branchId);
							if(output != null && !output.isEmpty()) {
								documentNo = (BigDecimal) output.get("P_DOC_NO");
								mapBranchDocumentNo.put(receiptPayment.getDocumentNo(), documentNo);
							}
							receiptPayment.setDocumentNo(documentNo);
							receiptPaymentRespository.save(receiptPayment);
							
							// application receipt payment Update
							ReceiptPaymentApp appRecPay = receiptPaymentAppRepository.findByCustomerIdAndTransactionFinanceYearAndTransactionRefNo(receiptPayment.getFsCustomer().getCustomerId(), receiptPayment.getDocumentFinanceYear(), receiptPayment.getOnlineDocumentNumber());
							if(appRecPay != null) {
								appRecPay.setTransactionFinanceYear(docYear);
								appRecPay.setTransactionRefNo(documentNo);
								appRecPay.setModifiedBy(userName);
								appRecPay.setModifiedDate(currenctDate);
								receiptPaymentAppRepository.save(appRecPay);
							}
						}
					}
					
					for (ForeignCurrencyAdjust foreignCurrencyAdjust : foreignCurrencyAdjusts) {
						foreignCurrencyAdjust.setDocumentNo(mapBranchDocumentNo.get(foreignCurrencyAdjust.getDocumentNo()));
						foreignCurrencyAdjustRepository.save(foreignCurrencyAdjust);
					}
					
					// update status
					fxDeliveryDetailsModel.setUopdateDate(currenctDate);
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
	
	public List<ForeignCurrencyAdjust> fetchByCollectionDetails(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode,String status){
		return foreignCurrencyAdjustRepository.fetchByCollectionDetails(documentNo,documentYear,companyId,documentCode,status);
	}
	
	public void saveAcknowledgeDriver(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			FxDeliveryDetailsModel fxDeliveryDetailsModel = fetchDeliveryDetails(deliveryDetailsId, ConstantDocument.Yes);
			if(fxDeliveryDetailsModel.getOrderStatus() != null && fxDeliveryDetailsModel != null && fxDeliveryDetailsModel.getOrderStatus() != null) {
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
				if(fxDeliveryDetailsModel.getOrderStatus() != null && fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.RTD_ACK)) {
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
				if(fxDeliveryDetailsModel.getOrderStatus() != null && fxDeliveryDetailsModel.getOrderStatus().equalsIgnoreCase(ConstantDocument.CND_ACK)) {
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
	
	public List<OrderManagementView> fetchOrdersByDeliveryDetailId(BigDecimal deliveryDetailSeqId){
		return fcSaleOrderManagementRepository.findByDeliveryDetailsId(deliveryDetailSeqId);
	}
	
	@Transactional
	public void stockUpdate(List<ForeignCurrencyAdjust> fromFCAdj,List<ForeignCurrencyAdjust> toFCAdj,List<ForeignCurrencyOldModel> oldToFCAdj,List<ForeignCurrencyOldModel> oldFromFCAdj){
		if(fromFCAdj != null) {
			for (ForeignCurrencyAdjust fromCurrencyAdj : fromFCAdj) {
				foreignCurrencyAdjustRepository.save(fromCurrencyAdj);
			}
		}
		if(toFCAdj != null) {
			for (ForeignCurrencyAdjust toCurrencyAdj : toFCAdj) {
				foreignCurrencyAdjustRepository.save(toCurrencyAdj);
			}
		}
		if(oldToFCAdj != null) {
			for (ForeignCurrencyOldModel oldToCurrencyAdj : oldToFCAdj) {
				foreignCurrencyAdjustOldRepository.save(oldToCurrencyAdj);
			}
		}
		if(oldFromFCAdj != null) {
			for (ForeignCurrencyOldModel oldFromCurrencyAdj : oldFromFCAdj) {
				foreignCurrencyAdjustOldRepository.save(oldFromCurrencyAdj);
			}
		}
	}
	
	
	public List<ForeignCurrencyAdjust> fetchByCollectionDetailsByTrnxType(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode,String tranctionType,String stockUpdate,String documentStatus){
		return foreignCurrencyAdjustRepository.fetchByCollectionDetailsByTrnxType(documentNo,documentYear,companyId,documentCode,tranctionType,stockUpdate,documentStatus);
	}
	
	// save stock details
	public void saveFcCurrencyStock(List<ForeignCurrencyStockTransfer> lstFCStkTrnf) {
		foreignCurrencyStockRepository.save(lstFCStkTrnf);
	}
	
	public List<ReceiptPayment> fetchReceiptPaymentByInventory(String inventoryId){
		return receiptPaymentRespository.findByInventoryId(inventoryId);
	}
	
	public List<ReceiptPayment> fetchReceiptPayment(BigDecimal collDocFyr,BigDecimal collDocNo){
		return receiptPaymentRespository.findByColDocFyrAndColDocNo(collDocFyr, collDocNo);
	}
	
	public Boolean fetchRecPayTrnxDetails(BigDecimal companyCode,BigDecimal collDocCode,BigDecimal collDocFyr,BigDecimal collDocNo,int count){
		Boolean status = Boolean.TRUE;
		int recordCount = receiptPaymentRespository.fetchRecPayTrnxDetails(companyCode, collDocCode, collDocFyr, collDocNo);
		if(recordCount != 0 && recordCount == count) {
			status = Boolean.FALSE;
		}
		return status;
	}

	public List<FxOrderTransactionModel> searchOrder(Predicate predicate) {
		Iterable<FxOrderTransactionModel> fxOrdersItr = fxOrderTransactionRespository.findAll(predicate);
		return Lists.newArrayList(fxOrdersItr);
	}

}
