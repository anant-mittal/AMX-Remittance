package com.amx.jax.manager;

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
import com.amx.jax.model.response.fx.FcSaleCurrencyAmountModel;
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
	
	public List<FxOrderTransactionHistroyDto> searchOrder(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest){
		Predicate predicate = fxOrderTransactionModelPredicateCreator.searchOrderPredicate(fcDeliveryBranchOrderSearchRequest);
		List<FxOrderTransactionModel> fxOrderList =  fcSaleBranchDao.searchOrder(predicate);
		return convert(fxOrderList);
	}
	
	public List<FxOrderTransactionHistroyDto> convert(List<FxOrderTransactionModel> fxOrderTransactionModelValue) {
		List<FxOrderTransactionHistroyDto> fxOrderTransactionHistroyDto = new ArrayList<>();

		if(fxOrderTransactionModelValue!=null && fxOrderTransactionModelValue.size()!=0) {	
			for (FxOrderTransactionModel fxOrderTransactionModel : fxOrderTransactionModelValue) {	
				FxOrderTransactionHistroyDto dto = new FxOrderTransactionHistroyDto();
				dto.setTransactionReferenceNo(fxOrderTransactionModel.getTransactionReferenceNo());
				dto.setDeliveryDate(fxOrderTransactionModel.getDeliveryDate());
				dto.setOrderStatus(fxOrderTransactionModel.getOrderStatus());
				dto.setOrderStatusCode(fxOrderTransactionModel.getOrderStatusCode());
				dto.setInventoryId(fxOrderTransactionModel.getInventoryId());
				dto.setLocalTrnxAmount(fxOrderTransactionModel.getLocalTrnxAmount());
				dto.setCollectionDocumentNo(fxOrderTransactionModel.getCollectionDocumentNo());
				dto.setDeliveryDate(fxOrderTransactionModel.getDeliveryDate());
				dto.setDeliveryTime(fxOrderTransactionModel.getDeliveryTime());
				dto.setForeignTransactionAmount(fxOrderTransactionModel.getForeignTransactionAmount());
				dto.setBranchDesc(fxOrderTransactionModel.getBranchDesc());
				dto.setDocumentFinanceYear(fxOrderTransactionModel.getDocumentFinanceYear());
				dto.setCustomerName(fxOrderTransactionModel.getCustomerName());
				dto.setCollectionDocumentCode(fxOrderTransactionModel.getCollectionDocumentCode());
				dto.setCurrencyQuoteName(fxOrderTransactionModel.getCurrencyQuoteName());
				dto.setCustomerId(fxOrderTransactionModel.getCustomerId());
				dto.setCustomerReference(fxOrderTransactionModel.getCustomerReference());
				dto.setForeignCurrencyCode(fxOrderTransactionModel.getForeignCurrencyCode());
				dto.setDeliveryDetSeqId(fxOrderTransactionModel.getDeliveryDetSeqId());
				dto.setDocumentNumber(fxOrderTransactionModel.getDocumentNumber());

				List<FcSaleCurrencyAmountModel> lstCurrencyAmt = new ArrayList<>();
				String mutipleInventoryId = null;
				for (FxOrderTransactionModel fxOrderTxnModel : fxOrderTransactionModelValue) {
					if(fxOrderTxnModel.getDocumentFinanceYear().compareTo(fxOrderTransactionModel.getDocumentFinanceYear()) == 0 && fxOrderTxnModel.getCollectionDocumentNo().compareTo(fxOrderTransactionModel.getCollectionDocumentNo()) == 0) {

						FcSaleCurrencyAmountModel fcSaleCurrencyAmountModel = new FcSaleCurrencyAmountModel();
						fcSaleCurrencyAmountModel.setAmount(fxOrderTxnModel.getForeignTransactionAmount());
						fcSaleCurrencyAmountModel.setCurrencyQuote(fxOrderTxnModel.getCurrencyQuoteName());
						lstCurrencyAmt.add(fcSaleCurrencyAmountModel);

						if(mutipleInventoryId != null) {
							mutipleInventoryId = mutipleInventoryId.concat(",").concat(fxOrderTxnModel.getInventoryId());
						}else {
							mutipleInventoryId = fxOrderTxnModel.getInventoryId();
						}
					}
				}

				dto.setMutipleFcAmount(lstCurrencyAmt);
				dto.setMutipleInventoryId(mutipleInventoryId);

				if(fxOrderTransactionModel.getDriverEmployeeId()!=null)
				{		
					Employee driverDetails =  employeeValidationService.getEmployeeName(fxOrderTransactionModel.getDriverEmployeeId());	
					if(driverDetails != null && driverDetails.getEmployeeName()!=null) {
						dto.setDriverName(driverDetails.getEmployeeName());
					}else {
						dto.setDriverName(" ");
					}
				}else {
					dto.setDriverName(" ");
				}	
				if(fxOrderTransactionModel.getEmployeeId()!=null) {
					Employee employeeDetails =  employeeValidationService.getEmployeeName(fxOrderTransactionModel.getEmployeeId());	
					if(employeeDetails != null && employeeDetails.getEmployeeName() != null) {
						dto.setEmployeeName(employeeDetails.getEmployeeName());
					}else {
						dto.setEmployeeName(" ");
					}
				}else {
					dto.setEmployeeName(" ");
				}
				if(fxOrderTransactionModel.getCustomerId() != null) {
					Customer custmoerDetails = customerRepository.getCustomerDetailsByCustomerId(fxOrderTransactionModel.getCustomerId());
					if(custmoerDetails != null && custmoerDetails.getMobile()!=null) {
						dto.setPhoneNumber(custmoerDetails.getMobile());
					}else {
						dto.setPhoneNumber("");	
					}
				}else {
					dto.setPhoneNumber("");	
				}
							
				fxOrderTransactionHistroyDto.add(dto);
			}
		}
		return fxOrderTransactionHistroyDto;
	}

}
