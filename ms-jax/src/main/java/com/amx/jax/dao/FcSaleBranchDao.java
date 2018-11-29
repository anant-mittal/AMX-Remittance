package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.dbmodel.fx.UserStockView;
import com.amx.jax.repository.EmployeeRespository;
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
	
	public List<OrderManagementView> fetchFcSaleOrderManagement(BigDecimal applicationcountryId,BigDecimal areaCode){
		return fcSaleOrderManagementRepository.findByApplicationCountryId(applicationcountryId);
	}
	
	public List<OrderManagementView> checkPendingOrders(BigDecimal applicationcountryId,BigDecimal orderNumber){
		return fcSaleOrderManagementRepository.checkPendingOrders(applicationcountryId,orderNumber);
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
	
	public List<FxDeliveryDetailsModel> fetchDeliveryDetails(BigDecimal deliveryDetailsId,String isActive){
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
				receiptPaymentRespository.updateDeliveryDetails(orderManagementView.getReceiptPaymentId(),deliveryDetailNew.getDeleviryDelSeqId());
			}
		}
	}
	
	public EmployeeDetailsView fetchEmployeeDetails(BigDecimal employeeId){
		return employeeDetailsRepository.findByEmployeeId(employeeId);
	}

}
