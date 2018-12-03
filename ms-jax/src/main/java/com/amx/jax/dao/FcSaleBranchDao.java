package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.dbmodel.fx.UserStockView;
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
	
	public List<Employee> fetchEmpDriverDetails(){
		return employeeRespository.fetchEmpDriverDetails();
	}
	
	public FxDeliveryDetailsModel fetchDeliveryDetails(BigDecimal deliveryDetailsId,String isActive){
		return fxDeliveryDetailsRepository.findByDeleviryDelSeqIdAndIsActive(deliveryDetailsId, isActive);
	}
	
	@Transactional
	public void saveDeliveryDetailsDriverId(FxDeliveryDetailsModel deliveryDetail){
		if(deliveryDetail != null) {
			fxDeliveryDetailsRepository.save(deliveryDetail);
		}
	}
	
	@Transactional
	public void saveDeliveryDetails(FxDeliveryDetailsModel deliveryDetailNew,FxDeliveryDetailsModel deliveryDetail,List<OrderManagementView> lstOrderManagement){
		if(deliveryDetailNew != null) {
			fxDeliveryDetailsRepository.save(deliveryDetailNew);
		}
		if(deliveryDetail != null) {
			fxDeliveryDetailsRepository.save(deliveryDetail);
		}
		if(lstOrderManagement != null){
			for (OrderManagementView orderManagementView : lstOrderManagement) {
				receiptPaymentRespository.updateDeliveryDetails(orderManagementView.getReceiptPaymentId(),deliveryDetailNew.getDeleviryDelSeqId(),deliveryDetailNew.getCreatedBy(),deliveryDetailNew.getCreatedDate());
			}
		}
	}
	
	public EmployeeDetailsView fetchEmployeeDetails(BigDecimal employeeId){
		return employeeDetailsRepository.findByEmployeeId(employeeId);
	}
	
	public List<CollectionModel> fetchCollectionData(BigDecimal collectDocNo,BigDecimal collectDocYear){
		return collectionRepository.findByDocumentNoAndDocumentFinanceYear(collectDocNo, collectDocYear);
	}
	
	@Transactional
	public void printOrderSave(List<ForeignCurrencyAdjust> foreignCurrencyAdjusts,HashMap<BigDecimal, String> mapInventoryReceiptPayment,String userName,Date currenctDate){
		if(foreignCurrencyAdjusts != null && foreignCurrencyAdjusts.size() != 0) {
			for (ForeignCurrencyAdjust foreignCurrencyAdjust : foreignCurrencyAdjusts) {
				foreignCurrencyAdjustRepository.save(foreignCurrencyAdjust);
			}
			for (Entry<BigDecimal, String> receiptPayment : mapInventoryReceiptPayment.entrySet()) {
				receiptPaymentRespository.updateInventoryId(receiptPayment.getKey(),receiptPayment.getValue(),userName,currenctDate);
			}
		}
	}
	
	public List<CurrencyWiseDenomination> fetchCurrencyDenomination(BigDecimal currencyId,String isActive){
		return currencyWiseDenominationRepository.fetchCurrencyDenomination(currencyId, isActive);
	}
	
	@Transactional
	public void saveOrderLockDetails(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			fxDeliveryDetailsRepository.updateOrderLockDetails(deliveryDetailsId,employeeId,userName,new Date(),orderStatus);
		}
	}
	
	@Transactional
	public void saveOrderReleaseDetails(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,String userName,String orderStatus){
		if(lstOrderManagement != null && lstOrderManagement.size() != 0){
			OrderManagementView orderManagementView = lstOrderManagement.get(0);
			BigDecimal deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
			fxDeliveryDetailsRepository.updateOrderReleaseDetails(deliveryDetailsId,null,userName,null,orderStatus);
		}
	}
	
}
