package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dao.FcSaleBranchDao;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.dbmodel.fx.UserStockView;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxEmployeeDetailsDto;
import com.amx.jax.model.response.fx.UserStockDto;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcSaleBranchOrderManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	FcSaleBranchDao fcSaleBranchDao;
	
	public List<OrderManagementView> fetchFcSaleOrderManagement(BigDecimal applicationCountryId,BigDecimal employeeId){
		List<OrderManagementView> ordermanage = new ArrayList<>();
		try{
			FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
			if(employeeDt != null && employeeDt.getEmployeeId() != null){
				BigDecimal areaCode = employeeDt.getAreaCode();
				if(areaCode != null) {
					ordermanage = fcSaleBranchDao.fetchFcSaleOrderManagement(applicationCountryId,areaCode);
				}else {
					throw new GlobalException("Area Code should not be blank",JaxError.NULL_AREA_CODE);
				}
			}else {
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fetchFcSaleOrderManagement ", e.getMessage()+"applId :"+applicationCountryId);
		}
		return ordermanage;
	}
	
	public List<OrderManagementView> fetchFcSaleOrderDetails(BigDecimal applicationCountryId,BigDecimal orderNumber){
		List<OrderManagementView> ordermanage = new ArrayList<>();
		try{
			ordermanage = fcSaleBranchDao.checkPendingOrders(applicationCountryId,orderNumber);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fetchFcSaleOrderDetails ", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber);
		}
		return ordermanage;
	}
	
	public List<UserStockDto> fetchUserStockViewByCurrency(BigDecimal countryId,BigDecimal employeeId,BigDecimal foreignCurrencyId){
		List<UserStockDto> userStock = new ArrayList<>();
		String userName = null;
		BigDecimal countryBranchId = null;
		try{
			FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
			if(employeeDt != null && employeeDt.getEmployeeId() != null){
				userName = employeeDt.getUserName();
				countryBranchId = employeeDt.getCountryBranchId();
				if(userName != null && countryBranchId != null) {
					List<UserStockView> userStockview = fcSaleBranchDao.fetchUserStockCurrencyCurrentDate(countryId,userName,countryBranchId,foreignCurrencyId);
					if(userStockview != null && userStockview.size() != 0) {
						for (UserStockView userStockDt : userStockview) {
							UserStockDto userStockDto = new UserStockDto();
							userStockDto.setCurrencyId(userStockDt.getCurrencyId());
							userStockDto.setCurrentStock(userStockDt.getCurrentStock());
							userStockDto.setDenominationAmount(userStockDt.getDenominationAmount());
							userStockDto.setDenominationDesc(userStockDt.getDenominationDesc());
							userStockDto.setDenominationId(userStockDt.getDenominationId());
							userStockDto.setStockId(userStockDt.getStockId());
							userStock.add(userStockDto);
						}
					}
				}else {
					throw new GlobalException("Employee details userName,countryBranchId is empty",JaxError.INVALID_EMPLOYEE);
				}
			}else {
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fetchUserStockViewByCurrency ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId+" foreignCurrencyId :"+foreignCurrencyId);
		}
		return userStock;
	}
	
	public List<Object[]> fetchUserStockViewSum(BigDecimal countryId,BigDecimal employeeId){
		List<Object[]> userStock = new ArrayList<>();
		String userName = null;
		BigDecimal countryBranchId = null;
		try{
			FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
			if(employeeDt != null && employeeDt.getEmployeeId() != null){
				userName = employeeDt.getUserName();
				countryBranchId = employeeDt.getCountryBranchId();
				if(userName != null && countryBranchId != null) {
					userStock = fcSaleBranchDao.fetchUserStockCurrentDateSum(countryId,userName,countryBranchId);
				}else {
					throw new GlobalException("Employee details userName,countryBranchId is empty",JaxError.INVALID_EMPLOYEE);
				}
			}else {
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
		}
		return userStock;
	}
	
	public List<UserStockDto> fetchUserStockView(BigDecimal countryId,BigDecimal employeeId){
		List<UserStockDto> userStock = new ArrayList<>();
		String userName = null;
		BigDecimal countryBranchId = null;
		try{
			FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
			if(employeeDt != null && employeeDt.getEmployeeId() != null){
				userName = employeeDt.getUserName();
				countryBranchId = employeeDt.getCountryBranchId();
				if(userName != null && countryBranchId != null) {
					List<UserStockView> userStockview = fcSaleBranchDao.fetchUserStockCurrentDate(countryId,userName,countryBranchId);
					if(userStockview != null && userStockview.size() != 0) {
						for (UserStockView userStockDt : userStockview) {
							UserStockDto userStockDto = new UserStockDto();
							userStockDto.setCurrencyId(userStockDt.getCurrencyId());
							userStockDto.setCurrentStock(userStockDt.getCurrentStock());
							userStockDto.setDenominationAmount(userStockDt.getDenominationAmount());
							userStockDto.setDenominationDesc(userStockDt.getDenominationDesc());
							userStockDto.setDenominationId(userStockDt.getDenominationId());
							userStockDto.setStockId(userStockDt.getStockId());
							userStock.add(userStockDto);
						}
					}
				}else {
					throw new GlobalException("Employee details userName,countryBranchId is empty",JaxError.INVALID_EMPLOYEE);
				}
			}else {
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
		}
		return userStock;
	}
	
	public List<FcEmployeeDetailsDto> fetchEmpDriverDetails(){
		List<FcEmployeeDetailsDto> empDrivers = new ArrayList<>();
		try{
			List<Employee> empDriverDt = fcSaleBranchDao.fetchEmpDriverDetails();
			if(empDriverDt != null && empDriverDt.size() != 0) {
				for (Employee employee : empDriverDt) {
					FcEmployeeDetailsDto empDet = new FcEmployeeDetailsDto();
					empDet.setCivilId(employee.getCivilId());
					empDet.setCountryBranchId(employee.getFsCountryBranch());
					empDet.setCountryId(employee.getCountryId());
					empDet.setDesignation(employee.getDesignation());
					empDet.setEmployeeId(employee.getEmployeeId());
					empDet.setEmployeeNumber(employee.getEmployeeNumber());
					empDet.setRoleId(employee.getFsRoleMaster());
					empDet.setTelephoneNumber(employee.getTelephoneNumber());
					empDet.setEmployeeName(employee.getEmployeeName());
					
					empDrivers.add(empDet);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fetchEmpDriverDetails ", e.getMessage());
		}
		return empDrivers;
	}
	
	public Boolean saveAssignDriver(BigDecimal countryId,BigDecimal orderNumber,BigDecimal driverId) {
		BigDecimal deliveryDetailsId = null;
		Boolean status = Boolean.FALSE;
		List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(countryId, orderNumber);
		if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
			for (OrderManagementView orderManagementView : lstOrderManagement) {
				if(deliveryDetailsId == null) {
					deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
				}else {
					if(deliveryDetailsId.compareTo(orderManagementView.getDeliveryDetailsId()) != 0) {
						deliveryDetailsId = null; // fail
						break;
					}
				}
			} 
		}
		
		if(deliveryDetailsId != null) {
			// fetch delivery details by id
			String isActive = "Y";
			List<FxDeliveryDetailsModel> lstDeliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(deliveryDetailsId,isActive);
			if(lstDeliveryDetails != null && lstDeliveryDetails.size() == 1) {
				FxDeliveryDetailsModel deliveryDetail = lstDeliveryDetails.get(0);
				if(deliveryDetail != null && deliveryDetail.getDriverEmployeeId() != null) {
					// create new record 
					FxDeliveryDetailsModel deliveryDetailNew = new FxDeliveryDetailsModel();
					deliveryDetailNew.setApplDocNo(deliveryDetail.getApplDocNo());
					deliveryDetailNew.setCreatedBy(deliveryDetail.getCreatedBy());  // need to change
					deliveryDetailNew.setCreatedDate(new Date());
					deliveryDetailNew.setDeliveryCharges(deliveryDetail.getDeliveryCharges());
					deliveryDetailNew.setDeliveryDate(deliveryDetail.getDeliveryDate());
					deliveryDetailNew.setDeliveryTimeSlot(deliveryDetail.getDeliveryTimeSlot());
					deliveryDetailNew.setDriverEmployeeId(driverId);
					deliveryDetailNew.setIsActive(deliveryDetail.getIsActive());
					deliveryDetailNew.setOrderStatus(deliveryDetail.getOrderStatus());
					deliveryDetailNew.setOtpToken(deliveryDetail.getOtpToken());
					deliveryDetailNew.setRemarksId(deliveryDetail.getRemarksId());
					deliveryDetailNew.setShippingAddressId(deliveryDetail.getShippingAddressId());
					deliveryDetailNew.setTransactionReceipt(deliveryDetail.getTransactionReceipt());
					
					// deactivate current record
					deliveryDetail.setIsActive("D");
					
					fcSaleBranchDao.saveDeliveryDetails(deliveryDetailNew,deliveryDetail,lstOrderManagement);
					status = Boolean.TRUE;
				}else {
					// update the current record by driver id
					deliveryDetail.setDriverEmployeeId(driverId);
					deliveryDetail.setUpdatedBy(deliveryDetail.getCreatedBy());  // need to change
					deliveryDetail.setUopdateDate(new Date());
					fcSaleBranchDao.saveDeliveryDetailsDriverId(deliveryDetail);
					status = Boolean.TRUE;
				}
			}else {
				//multiple active records
			}
		}else {
			// error 
		}
		
		return status;
	}
	
	// fetch the user details based on employee id 
	public FxEmployeeDetailsDto fetchEmployee(BigDecimal employeeId) {
		FxEmployeeDetailsDto employeeDto = new FxEmployeeDetailsDto();
		try{
			EmployeeDetailsView employee = fcSaleBranchDao.fetchEmployeeDetails(employeeId);
			
			if(employee != null && employee.getEmployeeId() != null) {
				BeanUtils.copyProperties(employeeDto, employee);
			}else {
				// fail
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("fetchEmployee ", e.getMessage());
		}
		
		return employeeDto;
	}

}
