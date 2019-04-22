package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.customer.service.EmployeeValidationService;
import com.amx.jax.dao.FcSaleBranchDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.dbmodel.fx.predicate.FxOrderTransactionModelPredicateCreator;
import com.amx.jax.model.request.fx.FcDeliveryBranchOrderSearchRequest;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.repository.ICustomerRepository;
import com.querydsl.core.types.Predicate;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcDeliveryOrdersearchManager {
	
	@Autowired
	FcSaleBranchDao fcSaleBranchDao;
	@Autowired
	EmployeeValidationService employeeValidationService;
	@Autowired
	ICustomerRepository customerRepository;
	@Autowired
	CustomerService customerService;
	@Autowired
	FxOrderTransactionModelPredicateCreator fxOrderTransactionModelPredicateCreator;
	
	
	public List<FxOrderTransactionModel> searchOrderDetailsByTranxRefNo(String trnxno, String orderStatus) {
		return fcSaleBranchDao.searchOrderDetailsByTrnxRefNo(trnxno,orderStatus);
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrderDetailsByOrderId(String orderId, String orderStatus) {
		List<FxOrderTransactionModel> fxOrderTransactionModel = searchOrderDetailsByTranxRefNo(orderId,orderStatus);
		return convert(fxOrderTransactionModel);
	}
	
	public List<FxOrderTransactionModel> searchOrderDetailsByAll(String orderId,String branchName, BigDecimal customerId, String orderStatus) {
		return fcSaleBranchDao.searchOrderDetailsByAll(orderId,branchName,customerId,orderStatus);
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrderDetails(String orderId,String branchName, BigDecimal customerId, String orderStatus) {
		List<FxOrderTransactionModel> fxOrderTransactionModel = searchOrderDetailsByAll(orderId,branchName,customerId,orderStatus);
		return convert(fxOrderTransactionModel);
		
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrder(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest){
		Predicate predicate = fxOrderTransactionModelPredicateCreator.searchOrderPredicate(fcDeliveryBranchOrderSearchRequest);
		List<FxOrderTransactionModel> fxOrderList =  fcSaleBranchDao.searchOrder(predicate);
		return convert(fxOrderList);
	}
	
	
	
	public List<FxOrderTransactionModel> searchOrderDetailsByBranchDescNdCustomerID(String branchName,BigDecimal customerId,String orderStatus) {
		return fcSaleBranchDao.searchOrderDetailsByBranchDescNdCustomerID(branchName,customerId,orderStatus);
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrderDetailsByBranchDescNdCustID(String branchName,BigDecimal customerId,String orderStatus) {
		List<FxOrderTransactionModel> fxOrderTransactionModel = searchOrderDetailsByBranchDescNdCustomerID(branchName,customerId,orderStatus);
		return convert(fxOrderTransactionModel);
		
	}
	
	
	
	public List<FxOrderTransactionModel> searchEmployeeDetailsbyOrderIdNdCustomerId(String orderId,BigDecimal customerId,String orderStatus) {
		return fcSaleBranchDao.searchEmployeeDetailsbyOrderIdNdCustId(orderId,customerId,orderStatus);
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrderDetailsbyOrderIdNdCustId(String orderId,BigDecimal customerId,String orderStatus) {
		List<FxOrderTransactionModel> fxOrderTransactionModel = searchEmployeeDetailsbyOrderIdNdCustomerId(orderId,customerId,orderStatus);
		return convert(fxOrderTransactionModel);
		
	}
	
	public List<FxOrderTransactionModel> searchOrderDetailsbyOrderIdNdBranchDesc(String orderId,String branchName,String orderStatus) {
		return fcSaleBranchDao.searchOrderDetailsbyOrderIdNdBranchDesc(orderId,branchName,orderStatus);
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrderDetailsbyTxnIdNdBranchDesc(String orderId,String branchName,String orderStatus) {
		List<FxOrderTransactionModel> fxOrderTransactionModel = searchOrderDetailsbyOrderIdNdBranchDesc(orderId,branchName,orderStatus);
		return convert(fxOrderTransactionModel);
		
	}
	
	public List<FxOrderTransactionModel> searchOrderDetailsByCustomerId(BigDecimal customerId,String orderStatus) {
		return fcSaleBranchDao.searchOrdeDetailsByCustomerId(customerId,orderStatus);
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrderDetailsByCustId(BigDecimal customerId,String orderStatus) {
		List<FxOrderTransactionModel> fxOrderTransactionModel = searchOrderDetailsByCustomerId(customerId,orderStatus);
		return convert(fxOrderTransactionModel);
		
	}
	
	public List<FxOrderTransactionModel> searchOrderDetailsByBranchDesc(String branchName,String orderStatus) {
		return fcSaleBranchDao.searchOrderByBranchDesc(branchName,orderStatus);
	}
	
	public List<FxOrderTransactionHistroyDto> searchOrderByBranchDesc(String branchName, String orderStatus) {
		List<FxOrderTransactionModel> fxOrderTransactionModel = searchOrderDetailsByBranchDesc(branchName,orderStatus);
		return convert(fxOrderTransactionModel);
		
	}
		
	
	public List<FxOrderTransactionHistroyDto> convert(List<FxOrderTransactionModel> fxOrderTransactionModelValue) {
		List<FxOrderTransactionHistroyDto> fxOrderTransactionHistroyDto = new ArrayList<>();
		
		if(fxOrderTransactionModelValue!=null && fxOrderTransactionModelValue.size()!=0) {	
		for (FxOrderTransactionModel fxOrderTransactionModel : fxOrderTransactionModelValue) {	
		FxOrderTransactionHistroyDto dto = new FxOrderTransactionHistroyDto();
		dto.setTransactionReferenceNo(fxOrderTransactionModel.getTransactionReferenceNo());
		dto.setDeliveryDate(fxOrderTransactionModel.getDeliveryDate());
		dto.setOrderStatus(fxOrderTransactionModel.getOrderStatus());
		dto.setInventoryId(fxOrderTransactionModel.getInventoryId());
		dto.setLocalTrnxAmount(fxOrderTransactionModel.getLocalTrnxAmount());
	
		if(fxOrderTransactionModel.getDriverEmployeeId()!=null)
		{		
			Employee driverDetails =  employeeValidationService.getEmployeeName(fxOrderTransactionModel.getDriverEmployeeId());	
			if(driverDetails.getEmployeeName()!=null) {
				dto.setDriverName(driverDetails.getEmployeeName());
			}
			}else {
				dto.setDriverName(" ");
			}	
		if(fxOrderTransactionModel.getEmployeeId()!=null) {
			Employee employeeDetails =  employeeValidationService.getEmployeeName(fxOrderTransactionModel.getEmployeeId());	
			if(employeeDetails.getEmployeeName()!=null) {
				dto.setEmployeeName(employeeDetails.getEmployeeName());
			}
			}else {
				dto.setEmployeeName(" ");
			}
			Customer custmoerDetails = customerRepository.getCustomerDetailsByCustomerId(fxOrderTransactionModel.getCustomerId());
			if(custmoerDetails.getMobile()!=null) {
				dto.setPhoneNumber(custmoerDetails.getMobile());
			}
			else {
				dto.setPhoneNumber("");	
			}
			fxOrderTransactionHistroyDto.add(dto);
			}
		}
		return fxOrderTransactionHistroyDto;
	}

}
