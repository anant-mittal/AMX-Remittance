package com.amx.jax.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.repository.EmployeeRepository;
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.customer.service.EmployeeValidationService;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.CurrencyMasterDao;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dao.FcSaleBranchDao;
import com.amx.jax.dao.RemittanceProcedureDao;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.CurrencyWiseDenomination;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.dbmodel.ReceiptPayment;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.fx.ForeignCurrencyOldModel;
import com.amx.jax.dbmodel.fx.ForeignCurrencyStockTransfer;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.dbmodel.fx.UserFcStockView;
import com.amx.jax.dbmodel.fx.UserStockView;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dict.Tenant;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchModel;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.notification.fx.FcSaleEventManager;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.JaxConfigRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.service.CompanyService;
import com.amx.jax.services.FcSaleDeliveryService;
import com.amx.jax.util.ConverterUtil;
import com.amx.jax.util.DateUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcSaleBranchOrderManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleBranchDao fcSaleBranchDao;

	@Autowired
	CompanyService companyService;

	@Autowired
	IDocumentDao documentDao;

	@Autowired
	RemittanceProcedureDao remittanceProcedureDao;

	@Autowired
	JaxConfigRepository jaxConfigRepository;

	@Autowired
	ConverterUtil converterutil;

	@Autowired
	CurrencyMasterDao currencyMasterDao;

	@Autowired
	FxOrderReportManager fxOrderReportManager;
	
	@Autowired
	FcSaleDeliveryService fcSaleDeliveryService;

	@Autowired
	AuditService auditService;
	
	@Autowired
	FcSaleApplicationDao fcSaleApplicationDao;

	@Autowired
	MetaData metaData;
	
	@Autowired
	ICompanyDAO companyDAO;
	
	@Autowired
	FcSaleEventManager fcSaleEventManager;
	
	@Autowired
	ApplicationProcedureDao applicationProcedureDao;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	FxDeliveryDetailsRepository fxDeliveryDetailsRepository;
	
	@Autowired
	ICustomerRepository customerRepository;
	
	@Autowired
	EmployeeValidationService employeeValidationService;
	
	@Autowired
	CustomerService customerService;

	public HashMap<String, Object> fetchFcSaleOrderManagement(BigDecimal applicationCountryId,BigDecimal employeeId,Date fromDate,Date toDate){
		HashMap<String, Object> fetchOrder = new HashMap<>();
		List<OrderManagementView> ordermanage = new ArrayList<>();
		try{
			FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
			if(employeeDt != null && employeeDt.getEmployeeId() != null){
				BigDecimal areaCode = employeeDt.getAreaCode();
				BigDecimal branchId = employeeDt.getBranchId();
				BigDecimal governorate = employeeDt.getGovernorates();
				if(areaCode != null && branchId != null) {
					BigDecimal branchCode = null;
					if (TenantContextHolder.currentSite().equals(Tenant.KWT)) {
						branchCode = ConstantDocument.KUWAIT_FOREIGNCURRENCY;
					} else if (TenantContextHolder.currentSite().equals(Tenant.BHR)) {
						branchCode = ConstantDocument.BAHRAIN_FOREIGNCURRENCY;
					} else if (TenantContextHolder.currentSite().equals(Tenant.OMN)) {
						branchCode = ConstantDocument.OMAN_FOREIGNCURRENCY;
					}
					
					if(fromDate != null) {
						// continue
					}else {
						// one week calculation for calendar
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DAY_OF_YEAR, -7);
						fromDate = cal.getTime();
					}
					
					if(toDate != null) {
						// continue
					}else {
						toDate = new Date();
					}
					
					if(branchCode != null) {
						if(branchId.compareTo(branchCode) == 0) {
							//ordermanage = fcSaleBranchDao.fetchFcSaleOrderManagementForHeadOffice(applicationCountryId);
							ordermanage = fcSaleBranchDao.fetchFcSaleOrderManagementForHeadOfficeLastOneWeek(applicationCountryId,fromDate,toDate);

							fetchOrder.put("ORDERS", ordermanage);
							fetchOrder.put("AREA", Boolean.TRUE);
							fetchOrder.put("BranchId", branchId);
						}else {
							//ordermanage = fcSaleBranchDao.fetchFcSaleOrderManagement(applicationCountryId,areaCode);
							ordermanage = fcSaleBranchDao.fetchFcSaleOrderManagementByGovernateLastOneWeek(applicationCountryId,governorate,fromDate,toDate);

							fetchOrder.put("ORDERS", ordermanage);
							fetchOrder.put("AREA", Boolean.FALSE);
							fetchOrder.put("BRANCH", branchId);
						}
					}else {
						//ordermanage = fcSaleBranchDao.fetchFcSaleOrderManagement(applicationCountryId,areaCode);
						ordermanage = fcSaleBranchDao.fetchFcSaleOrderManagementByGovernateLastOneWeek(applicationCountryId,governorate,fromDate,toDate);

						fetchOrder.put("ORDERS", ordermanage);
						fetchOrder.put("AREA", Boolean.FALSE);
						fetchOrder.put("BRANCH", branchId);
					}
				}else {
					throw new GlobalException(JaxError.NULL_AREA_CODE,"Area Code should not be blank");
				}
			}else {
				throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("fetchFcSaleOrderManagement ", e.getMessage()+"applId :"+applicationCountryId+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("fetchFcSaleOrderManagement ", e.getMessage()+"applId :"+applicationCountryId+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return fetchOrder;
	}

	public List<OrderManagementView> fetchFcSaleOrderDetails(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear){
		List<OrderManagementView> ordermanage = new ArrayList<>();
		try{
			ordermanage = fcSaleBranchDao.checkPendingOrders(applicationCountryId,orderNumber,orderYear);
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("fetchFcSaleOrderDetails ", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("fetchFcSaleOrderDetails ", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear);
			throw new GlobalException(e.getMessage());
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
					}else {
						throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found for user stock");
					}
				}else {
					throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details userName,countryBranchId is empty");
				}
			}else {
				throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("fetchUserStockViewByCurrency ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId+" foreignCurrencyId :"+foreignCurrencyId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("fetchUserStockViewByCurrency ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId+" foreignCurrencyId :"+foreignCurrencyId);
			throw new GlobalException(e.getMessage());
		}

		return userStock;
	}

	public List<Object[]> fetchUserStockViewSum(BigDecimal countryId,BigDecimal employeeId){
		List<Object[]> userStock = new ArrayList<>();
		String userName = null;
		BigDecimal countryBranchId = null;

		try{
			FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
			if(employeeDt != null){
				if(employeeDt.getEmployeeId() != null && employeeDt.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)) {
					userName = employeeDt.getUserName();
					countryBranchId = employeeDt.getCountryBranchId();
					if(userName != null && countryBranchId != null) {
						userStock = fcSaleBranchDao.fetchUserStockCurrentDateSum(countryId,userName,countryBranchId);
					}else {
						throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details userName,countryBranchId is empty");
					}
				}else {
					throw new GlobalException(JaxError.INACTIVE_EMPLOYEE,"Employee details is not active");
				}
			}else {
				throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
			throw new GlobalException(e.getMessage());
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
					throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details userName,countryBranchId is empty");
				}
			}else {
				throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
			throw new GlobalException(e.getMessage());
		}

		return userStock;
	}

	public List<FcEmployeeDetailsDto> fetchEmpDriverDetails(){
		List<FcEmployeeDetailsDto> empDrivers = new ArrayList<>();
		try{
			List<Employee> empDriverDt = fcSaleBranchDao.fetchEmpDriverDetails(ConstantDocument.USER_TYPE_DRIVER,ConstantDocument.Yes);
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
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("fetchEmpDriverDetails ", e.getMessage());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("fetchEmpDriverDetails ", e.getMessage());
			throw new GlobalException(e.getMessage());
		}

		return empDrivers;
	}

	// save assign driver details in multiple tables
	public Boolean saveAssignDriver(BigDecimal countryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal driverId,BigDecimal employeeId,BigDecimal companyId) {
		BigDecimal deliveryDetailsId = null;
		BigDecimal assignDriverId = null;
		Boolean status = Boolean.FALSE;
		String isActive = ConstantDocument.Yes;
		String userName = null;
		String orderStatus = null;
		BigDecimal fromBranchId = null;
		BigDecimal toBranchId = null;
		List<BigDecimal> duplicate = new ArrayList<>();
		String docNoBulk = null;
		Boolean nullDocStatus = Boolean.FALSE;

		try{
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(countryId, orderNumber,orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				for (OrderManagementView orderManagementView : lstOrderManagement) {
					if(deliveryDetailsId == null) {
						deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
						assignDriverId = orderManagementView.getDriverEmployeId();
						orderStatus = orderManagementView.getOrderStatus();
					}else {
						if(orderManagementView.getDeliveryDetailsId() != null && deliveryDetailsId != null && deliveryDetailsId.compareTo(orderManagementView.getDeliveryDetailsId()) != 0) {
							deliveryDetailsId = null;
							break;
						}

						if(orderManagementView.getDriverEmployeId() != null && assignDriverId != null && assignDriverId.compareTo(orderManagementView.getDriverEmployeId()) != 0) {
							assignDriverId = null;
							break;
						}

						if(orderManagementView.getOrderStatus() != null && orderStatus != null && orderStatus.compareTo(orderManagementView.getOrderStatus()) != 0) {
							orderStatus = null;
							break;
						}
					}

					// collecting documents numbers and making currency adj doc status null for old records
					if(orderManagementView.getDocumentNo() != null && !duplicate.contains(orderManagementView.getDocumentNo())) {
						duplicate.add(orderManagementView.getDocumentNo());
						if(docNoBulk == null) {
							docNoBulk = orderManagementView.getDocumentNo().toString();
						}else {
							docNoBulk = docNoBulk.concat(",").concat(orderManagementView.getDocumentNo().toString());
						}
					}
				}

				if(orderStatus.equalsIgnoreCase(ConstantDocument.RTD)) {
					nullDocStatus = Boolean.TRUE;
				}
			}

			if(deliveryDetailsId != null) {
				// fetch delivery details by id
				if(orderStatus == null) {
					throw new GlobalException(JaxError.NULL_ORDER_STATUS,"Order Status should be empty");
				}else if(assignDriverId != null && driverId != null && assignDriverId.compareTo(driverId) == 0 && orderStatus.equalsIgnoreCase(ConstantDocument.PCK)) {
					throw new GlobalException(JaxError.DRIVER_ALREADY_ASSIGNED,"Driver is already assign for order");
				}else {
					FxDeliveryDetailsModel deliveryDetail = fcSaleBranchDao.fetchDeliveryDetails(deliveryDetailsId,isActive);
					if(deliveryDetail != null) {

						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null){
							userName = employeeDt.getUserName();
							fromBranchId = employeeDt.getBranchId();
							// driver details
							FxEmployeeDetailsDto driverEmpDt = fetchEmployee(driverId);
							if(driverEmpDt != null && driverEmpDt.getEmployeeId() != null){
								toBranchId = driverEmpDt.getBranchId();
								
								if(userName != null) {
									if(deliveryDetail.getDriverEmployeeId() != null) {
										if(deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.PCK) || deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.RTD)) {
											String oldOrderStatus = deliveryDetail.getOrderStatus();
											// create new record 
											FxDeliveryDetailsModel deliveryDetailNew = new FxDeliveryDetailsModel();
											deliveryDetailNew.setApplDocNo(deliveryDetail.getApplDocNo());
											deliveryDetailNew.setCreatedBy(userName);
											deliveryDetailNew.setCreatedDate(new Date());
											deliveryDetailNew.setDeliveryCharges(deliveryDetail.getDeliveryCharges());
											deliveryDetailNew.setDeliveryDate(deliveryDetail.getDeliveryDate());
											deliveryDetailNew.setDeliveryTimeSlot(deliveryDetail.getDeliveryTimeSlot());
											deliveryDetailNew.setDriverEmployeeId(driverId);
											deliveryDetailNew.setIsActive(ConstantDocument.Yes);
											deliveryDetailNew.setOrderStatus(ConstantDocument.OFD_ACK);
											deliveryDetailNew.setOtpToken(deliveryDetail.getOtpToken());
											deliveryDetailNew.setRemarksId(deliveryDetail.getRemarksId());
											deliveryDetailNew.setShippingAddressId(deliveryDetail.getShippingAddressId());
											deliveryDetailNew.setTransactionReceipt(deliveryDetail.getTransactionReceipt());
											deliveryDetailNew.setOrderLock(new Date());
											deliveryDetailNew.setEmployeeId(employeeId);
											deliveryDetailNew.setFromBranchId(fromBranchId);
											deliveryDetailNew.setToBranchId(toBranchId);
											deliveryDetailNew.setColDocFyr(deliveryDetail.getColDocFyr());
											deliveryDetailNew.setColDocNo(deliveryDetail.getColDocNo());

											// deactivate current record
											deliveryDetail.setIsActive("D");
											deliveryDetail.setUpdatedBy(userName);
											deliveryDetail.setUopdateDate(new Date());

											if(nullDocStatus) {
												fcSaleBranchDao.saveDeliveryDetails(deliveryDetailNew,deliveryDetail,lstOrderManagement,docNoBulk,companyId,ConstantDocument.DOCUMENT_CODE_FOR_FCSALE,orderYear);
											}else {
												fcSaleBranchDao.saveDeliveryDetails(deliveryDetailNew,deliveryDetail,lstOrderManagement,null,null,null,null);
											}

											// old status
											logStatusChangeAuditEvent(deliveryDetail.getDeleviryDelSeqId(), oldOrderStatus);
											logStatusChangeAuditEvent(deliveryDetailNew.getDeleviryDelSeqId(), oldOrderStatus);
											status = Boolean.TRUE;
										}else {
											throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not packed to assign driver");
										}
									}else {
										// update the current record by driver id
										if(deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.PCK) || deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.RTD)) {
											String oldOrderStatus = deliveryDetail.getOrderStatus();
											deliveryDetail.setDriverEmployeeId(driverId);
											deliveryDetail.setUpdatedBy(userName);
											deliveryDetail.setUopdateDate(new Date());
											deliveryDetail.setOrderStatus(ConstantDocument.OFD_ACK);
											deliveryDetail.setFromBranchId(fromBranchId);
											deliveryDetail.setToBranchId(toBranchId);

											if(nullDocStatus) {
												fcSaleBranchDao.saveDeliveryDetailsDriverId(deliveryDetail,docNoBulk,companyId,ConstantDocument.DOCUMENT_CODE_FOR_FCSALE,orderYear);
											}else {
												fcSaleBranchDao.saveDeliveryDetailsDriverId(deliveryDetail,null,null,null,null);
											}

											// old status
											logStatusChangeAuditEvent(deliveryDetail.getDeleviryDelSeqId(), oldOrderStatus);
											status = Boolean.TRUE;
										}else {
											throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not packed to assign driver");
										}
									}
								}
							}
						}else {
							throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
						}
					}else {
						//multiple active records
						throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records available for Delivery details");
					}
				}
			}else {
				// error 
				throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records available for Delivery details");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in Exception saving Assign Driver", e.getMessage()+" countryId :"+countryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" driverId :"+driverId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in Exception saving Assign Driver", e.getMessage()+" countryId :"+countryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" driverId :"+driverId);
			throw new GlobalException(e.getMessage());
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
				throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("fetchEmployee ", e.getMessage()+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("fetchEmployee ", e.getMessage()+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return employeeDto;
	}

	// save the dispatch record 
	public FxOrderReportResponseDto printOrderSave(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest,BigDecimal employeeId,BigDecimal countryId,BigDecimal companyId) {
		String userName = null;
		BigDecimal countryBranchId = null;
		BigDecimal branchId = null;
		BigDecimal collectionDocYear = null;
		BigDecimal collectionDocNumber = null;
		BigDecimal companyCode = null;
		BigDecimal documentId = null;
		BigDecimal collectionId = null;
		BigDecimal customerId = null;
		BigDecimal deliveryDetailsId = null;
		String oldOrderStatus = null;
		List<BigDecimal> dupdocumentNumbers = new ArrayList<>();
		List<String> dupInventory = new ArrayList<>();
		List<ForeignCurrencyAdjust> foreignCurrencyAdjusts = new ArrayList<>();
		List<ReceiptPayment> updateRecPay = new ArrayList<>();
		HashMap<BigDecimal, String> mapInventory = new HashMap<>();
		HashMap<BigDecimal, String> mapInventoryReceiptPayment = new HashMap<>();
		HashMap<BigDecimal, BigDecimal> mapDocAmount = new HashMap<>();
		HashMap<BigDecimal, List<CurrencyWiseDenomination>> mapCurrencyDenom = new HashMap<>();
		FxOrderReportResponseDto fxOrderReportResponseDto = null;

		try {

			if(fcSaleBranchDispatchRequest != null) {
				List<FcSaleBranchDispatchModel> orderDetails =  fcSaleBranchDispatchRequest.getUserStockDocDetails();
				collectionDocYear = fcSaleBranchDispatchRequest.getCollectionDocumentYear();
				collectionDocNumber = fcSaleBranchDispatchRequest.getCollectionDocumentNo();

				// employee details
				FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
				if(employeeDt != null && employeeDt.getEmployeeId() != null){
					if(employeeDt.getUserName() != null) {
						userName = employeeDt.getUserName();
					}
					if(employeeDt.getCountryBranchId() != null) {
						countryBranchId = employeeDt.getCountryBranchId();
					}
					if(employeeDt.getBranchId() != null) {
						branchId = employeeDt.getBranchId();
					}
				}else {
					throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
				}

				// fetch collection details
				List<CollectionModel> collection = fcSaleBranchDao.fetchCollectionData(collectionDocNumber, collectionDocYear);
				if(collection != null && collection.size() != 0) {
					CollectionModel collectionModel = collection.get(0);
					collectionId = collectionModel.getCollectionId();
					companyCode = collectionModel.getCompanyCode();
					customerId = collectionModel.getFsCustomer().getCustomerId();
				}else {
					throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO,"Collection details is empty");
				}

				// fetch
				List<Document> documentDt = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
				if(documentDt != null && documentDt.size() != 0) {
					Document document = documentDt.get(0);
					documentId = document.getDocumentID();
				}else {
					throw new GlobalException(JaxError.BLANK_DOCUMENT_DETAILS,"Document details is empty");
				}
				
				Map<String, Object> documentSeriality = generateDocumentNumber(branchId,countryId,companyId,ConstantDocument.Yes,collectionDocYear,ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
				if(documentSeriality != null && !documentSeriality.isEmpty()) {
					BigDecimal docNo = (BigDecimal) documentSeriality.get("P_DOC_NO");
					String errMsg = (String) documentSeriality.get("P_ERROR_MESG");
					if(errMsg != null) {
						throw new GlobalException(JaxError.INVALID_APPL_RECEIPT_PAYMNET_DOCUMENT_NO, errMsg);
					}else {
						if(docNo!=null && docNo.compareTo(BigDecimal.ZERO)!=0){
							// document number allocation done
						}else{
							throw new GlobalException(JaxError.INVALID_APPL_RECEIPT_PAYMNET_DOCUMENT_NO, "Receipt document should not be blank.");
						}
					}
				}

				// receipt payment Details
				HashMap<BigDecimal, OrderManagementView> mapOrderManagement = new HashMap<>();
				List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(countryId, collectionDocNumber,collectionDocYear);
				if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
					for (OrderManagementView orderManagementView : lstOrderManagement) {
						mapOrderManagement.put(orderManagementView.getDocumentNo(), orderManagementView);
						List<CurrencyWiseDenomination> lstCurrencyDenomination = fetchCurrencyDenominationByCurrencyId(orderManagementView.getForeignCurrencyId());
						mapCurrencyDenom.put(orderManagementView.getForeignCurrencyId(), lstCurrencyDenomination);
						if(deliveryDetailsId == null) {
							deliveryDetailsId = orderManagementView.getDeliveryDetailsId();
							oldOrderStatus = orderManagementView.getOrderStatus();
						}else {
							if(orderManagementView.getDeliveryDetailsId() != null && deliveryDetailsId != null && deliveryDetailsId.compareTo(orderManagementView.getDeliveryDetailsId()) != 0) {
								deliveryDetailsId = null; // fail
								break;
							}
						}
					}

					if(deliveryDetailsId == null) {
						throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records or mismatch of delivery details");
					}
				}else {
					throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO,"Collection details is empty");
				}


				if(orderDetails != null) {

					for (FcSaleBranchDispatchModel fcSaleBranchDispatchModel : orderDetails) {
						if(fcSaleBranchDispatchModel.getDocumentNumber() != null && !dupdocumentNumbers.contains(fcSaleBranchDispatchModel.getDocumentNumber())) {
							dupdocumentNumbers.add(fcSaleBranchDispatchModel.getDocumentNumber());
							mapInventory.put(fcSaleBranchDispatchModel.getDocumentNumber(), fcSaleBranchDispatchModel.getInventoryId());

							List<UserStockDto> userCurrenctStock = fetchUserStockViewByCurrency(countryId, employeeId, fcSaleBranchDispatchModel.getCurrencyId());
							if(userCurrenctStock != null && userCurrenctStock.size() != 0) {
								// continue
							}else {
								throw new GlobalException(JaxError.CURRENCY_STOCK_NOT_AVAILABLE,"Currenct stock for employee not available");
							}
							
							int i = 0;
							BigDecimal fcAmount = BigDecimal.ZERO;
							for (FcSaleBranchDispatchModel fcSaleBranchDispatch : orderDetails) {
								if(fcSaleBranchDispatch.getDocumentNumber() != null && fcSaleBranchDispatchModel.getDocumentNumber().compareTo(fcSaleBranchDispatch.getDocumentNumber()) == 0) {

									if(mapOrderManagement.get(fcSaleBranchDispatch.getDocumentNumber()) != null && mapCurrencyDenom.get(mapOrderManagement.get(fcSaleBranchDispatch.getDocumentNumber()).getForeignCurrencyId()) != null) {
										// checking denomination details passing correctly
										Boolean denominationStatus = checkingDenominationDetails(fcSaleBranchDispatch, mapCurrencyDenom.get(mapOrderManagement.get(fcSaleBranchDispatch.getDocumentNumber()).getForeignCurrencyId()),userCurrenctStock);
										if(denominationStatus != null) {
											ForeignCurrencyAdjust foreignCurrencyAdj = new ForeignCurrencyAdjust();

											foreignCurrencyAdj.setDocumentLineNumber(new BigDecimal(++i));

											foreignCurrencyAdj.setAccountmmyyyy(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));

											CollectionModel collectionModel = new CollectionModel();
											collectionModel.setCollectionId(collectionId);
											foreignCurrencyAdj.setCollect(collectionModel);

											CountryMaster countryMaster = new CountryMaster();
											countryMaster.setCountryId(countryId);
											foreignCurrencyAdj.setFsCountryMaster(countryMaster);

											CountryBranch countryBranch = new CountryBranch();
											countryBranch.setCountryBranchId(countryBranchId);
											foreignCurrencyAdj.setCountryBranch(countryBranch);

											CompanyMaster companyMaster = new CompanyMaster();
											companyMaster.setCompanyId(companyId);
											foreignCurrencyAdj.setFsCompanyMaster(companyMaster);

											foreignCurrencyAdj.setCompanyCode(companyCode);

											CurrencyMasterModel currencyMasterModel = new CurrencyMasterModel();
											currencyMasterModel.setCurrencyId(mapOrderManagement.get(fcSaleBranchDispatch.getDocumentNumber()).getForeignCurrencyId());
											foreignCurrencyAdj.setFsCurrencyMaster(currencyMasterModel);

											Customer customer = new Customer();
											customer.setCustomerId(customerId);
											foreignCurrencyAdj.setFsCustomer(customer);

											CurrencyWiseDenomination currencyWiseDenomination = new CurrencyWiseDenomination();
											currencyWiseDenomination.setDenominationId(fcSaleBranchDispatch.getDenominationId());
											foreignCurrencyAdj.setFsDenominationId(currencyWiseDenomination);

											foreignCurrencyAdj.setAdjustmentAmount(fcSaleBranchDispatch.getDenominationPrice());
											fcAmount = fcAmount.add(fcSaleBranchDispatch.getDenominationPrice());
											foreignCurrencyAdj.setDenaminationAmount(fcSaleBranchDispatch.getDenominationAmount());
											foreignCurrencyAdj.setDocumentNo(fcSaleBranchDispatch.getDocumentNumber());
											foreignCurrencyAdj.setNotesQuantity(fcSaleBranchDispatch.getDenominationQuatity());
											foreignCurrencyAdj.setExchangeRate(mapOrderManagement.get(fcSaleBranchDispatch.getDocumentNumber()).getTransactionActualRate());

											foreignCurrencyAdj.setDocumentFinanceYear(collectionDocYear);
											foreignCurrencyAdj.setDocumentDate(new Date());

											foreignCurrencyAdj.setOracleUser(userName);
											foreignCurrencyAdj.setCreatedBy(userName);
											foreignCurrencyAdj.setCreatedDate(new Date());

											foreignCurrencyAdj.setTransactionType(ConstantDocument.S);
											foreignCurrencyAdj.setDocumentStatus(ConstantDocument.P);
											foreignCurrencyAdj.setProgNumber(ConstantDocument.FC_SALE);
											foreignCurrencyAdj.setStockUpdated(" ");

											foreignCurrencyAdj.setDocumentId(documentId);
											foreignCurrencyAdj.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
											foreignCurrencyAdj.setStatus(ConstantDocument.Yes);

											foreignCurrencyAdjusts.add(foreignCurrencyAdj);

											// mapping document number with amount
											mapDocAmount.put(fcSaleBranchDispatch.getDocumentNumber(), fcAmount);
										}else {
											throw new GlobalException(JaxError.INCORRECT_CURRENCY_DENOMINATION,"Currency Denomination Details is in correct");
										}
									}else {
										throw new GlobalException(JaxError.BLANK_DOCUMENT_DETAILS,"Receipt Document Details is in correct");
									}
								}
							}
						}
					}

					Boolean amtStatus = Boolean.FALSE;
					for (Entry<BigDecimal, BigDecimal> docAmountComp : mapDocAmount.entrySet()) {
						BigDecimal documentNo = docAmountComp.getKey();
						BigDecimal fcAmount = docAmountComp.getValue();
						OrderManagementView orderManagementView = mapOrderManagement.get(documentNo);
						if(orderManagementView != null && orderManagementView.getDocumentNo() != null) {
							if(orderManagementView.getDocumentNo() != null && orderManagementView.getDocumentNo().compareTo(documentNo) == 0) {
								if(fcAmount != null && fcAmount.compareTo(orderManagementView.getForeignTrnxAmount()) == 0) {
									amtStatus = Boolean.TRUE;
								}
							}
						}
						if(!amtStatus) {
							throw new GlobalException(JaxError.MISMATCH_COLLECTION_AMOUNT,"Amount mismatch with document details");
						}
					}

					for (Entry<BigDecimal, String> invertoryData : mapInventory.entrySet()) {
						BigDecimal documentNo = invertoryData.getKey();
						String inventoryId = invertoryData.getValue();
						OrderManagementView orderManagementView = mapOrderManagement.get(documentNo);
						if(!dupInventory.contains(inventoryId)) {
							dupInventory.add(inventoryId);
							// check inventory Id before save
							List<ReceiptPayment> lstReciptPayment = fcSaleBranchDao.fetchReceiptPaymentByInventory(inventoryId);
							if(lstReciptPayment != null && lstReciptPayment.size() != 0) {
								throw new GlobalException(JaxError.INVENTORY_ID_EXISTS,"Inventory Id already exists");
							}else {
								mapInventoryReceiptPayment.put(orderManagementView.getReceiptPaymentId(), inventoryId);
							}
						}else {
							throw new GlobalException(JaxError.INVENTORY_ID_EXISTS,"Inventory Id passing duplicates");
						}
					}
					
					if(collectionDocYear != null && collectionDocNumber != null) {
						List<ReceiptPayment> lstReceiptPayment = fcSaleBranchDao.fetchReceiptPayment(collectionDocYear, collectionDocNumber);
						for (ReceiptPayment receiptPayment : lstReceiptPayment) {
							receiptPayment.setOnlineCountryBranchId(receiptPayment.getCountryBranch().getCountryBranchId());
							receiptPayment.setOnlineDocumentNumber(receiptPayment.getDocumentNo());
							receiptPayment.setOnlineLocationCode(receiptPayment.getLocCode());
							
							receiptPayment.setDocumentNo(receiptPayment.getDocumentNo());

							CountryBranch countryBranch = new CountryBranch();
							countryBranch.setCountryBranchId(countryBranchId);
							receiptPayment.setCountryBranch(countryBranch);

							receiptPayment.setLocCode(branchId);
							
							receiptPayment.setInventoryId(mapInventoryReceiptPayment.get(receiptPayment.getReceiptId()));
							receiptPayment.setModifiedBy(userName);
							receiptPayment.setModifiedDate(new Date());

							updateRecPay.add(receiptPayment);
						}
					}

					// saving in currency Adjustment and receipt payment
					fcSaleBranchDao.printOrderSave(foreignCurrencyAdjusts,updateRecPay,userName,new Date(),deliveryDetailsId,ConstantDocument.PCK);

					// old status
					logStatusChangeAuditEvent(deliveryDetailsId, oldOrderStatus);

					// calling report
					fxOrderReportResponseDto = fetchTransactionReport(customerId,collectionDocYear, collectionDocNumber);
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in Exception saving print receipt denomination", e.getMessage()+" employeeId :"+employeeId+" countryId :"+countryId+" companyId :"+companyId+" fcSaleBranchDispatchRequest :"+fcSaleBranchDispatchRequest);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (ParseException e) {
			e.printStackTrace();
			logger.debug("Error in ParseException saving print receipt denomination", e.getMessage()+" employeeId :"+employeeId+" countryId :"+countryId+" companyId :"+companyId+" fcSaleBranchDispatchRequest :"+fcSaleBranchDispatchRequest);
			throw new GlobalException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in Exception saving print receipt denomination", e.getMessage()+" employeeId :"+employeeId+" countryId :"+countryId+" companyId :"+companyId+" fcSaleBranchDispatchRequest :"+fcSaleBranchDispatchRequest);
			throw new GlobalException(e.getMessage());
		}

		return fxOrderReportResponseDto;
	}

	// fetch the reprint full data
	public FxOrderReportResponseDto fetchTransactionReport(BigDecimal customerId,BigDecimal collectionDocYear,BigDecimal collectionDocNumber) {
		return fxOrderReportManager.getReportDetails(customerId,collectionDocNumber, collectionDocYear);
	}

	// moving record to old emos
	public void transferReceiptPaymentEMOS(BigDecimal countryId,BigDecimal collectionDocYear,BigDecimal collectionDocNumber,BigDecimal companyId) {
		// moving to old emos
		Map<String, Object> insertEmos = new HashMap<>();
		insertEmos.put("P_APPL_CNTY_ID", countryId);
		insertEmos.put("P_COMPANY_ID", companyId);
		insertEmos.put("P_DOCUMENT_ID", ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		insertEmos.put("P_DOC_FINYR", collectionDocYear);
		insertEmos.put("P_DOCUMENT_NO", collectionDocNumber);

		Map<String, Object> errorStatus = remittanceProcedureDao.insertEMOSLIVETransfer(insertEmos);
		logger.error("Error in insertEMOSLIVETransfer : " + errorStatus.get("P_ERROR_MESSAGE"));
	}

	// check the denomination details send correctly
	public Boolean checkingDenominationDetails(FcSaleBranchDispatchModel currencyAdj,List<CurrencyWiseDenomination> currencyDenomination,List<UserStockDto> currentStock) {
		Boolean currencyStatus = Boolean.FALSE;
		Boolean currentStockStatus = Boolean.FALSE;
		try {
			if(currencyAdj != null) {
				if(currencyAdj.getDenominationPrice() != null && currencyAdj.getDenominationAmount() != null 
						&& currencyAdj.getDenominationQuatity() != null && currencyAdj.getCurrencyId() != null
						&& currencyAdj.getDenominationId() != null) {
					for (CurrencyWiseDenomination currencyWiseDenomination : currencyDenomination) {
						if(currencyWiseDenomination.getDenominationId().compareTo(currencyAdj.getDenominationId()) == 0) {
							if(currencyWiseDenomination.getDenominationAmount().compareTo(currencyAdj.getDenominationAmount()) == 0) {
								currencyStatus = Boolean.TRUE;
								break;
							}
						}
					}

					if(currencyStatus) {
						BigDecimal adjAmount = currencyAdj.getDenominationPrice();
						BigDecimal denominationAmt = currencyAdj.getDenominationAmount();
						BigDecimal denominationQty = currencyAdj.getDenominationQuatity();
						BigDecimal denominationId = currencyAdj.getDenominationId();
						if(adjAmount.compareTo(denominationAmt.multiply(denominationQty)) == 0) {
							for (UserStockDto userStockDto : currentStock) {
								if(userStockDto.getDenominationId().compareTo(denominationId) == 0) {
									if(userStockDto.getDenominationAmount().compareTo(denominationAmt) == 0) {
										if(userStockDto.getCurrentStock().compareTo(denominationQty) >= 0) {
											currentStockStatus = Boolean.TRUE;
											break;
										}
									}
								}
							}
							if(!currentStockStatus) {
								currencyStatus = Boolean.FALSE;
								throw new GlobalException(JaxError.MISMATCH_CURRENT_STOCK,"Employee current stock not matcing");
							}
						}else {
							currencyStatus = Boolean.FALSE;
							throw new GlobalException(JaxError.MISMATCH_ADJ_AMT_AND_DENOMINATION_AMT_QUANTITY,"Currency Adj Amount not matching Denomination Amount and Quantity");
						}
					}else {
						throw new GlobalException(JaxError.INVALID_CURRENCY_DENOMINATION,"Currency Denomination Details is not vaild");
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in checkingDenominationDetails", e.getMessage()+" FcSaleBranchDispatchModel :"+currencyAdj+" List<CurrencyWiseDenomination> :"+currencyDenomination.toString());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in checkingDenominationDetails", e.getMessage()+" FcSaleBranchDispatchModel :"+currencyAdj+" List<CurrencyWiseDenomination> :"+currencyDenomination.toString());
			throw new GlobalException(e.getMessage());
		}

		return currencyStatus;
	}

	public List<CurrencyWiseDenomination> fetchCurrencyDenominationByCurrencyId(BigDecimal currencyId) {
		List<CurrencyWiseDenomination> lstCurrencyDenomination = new ArrayList<>();

		try {
			lstCurrencyDenomination = fcSaleBranchDao.fetchCurrencyDenomination(currencyId, ConstantDocument.Yes);
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in fetchCurrencyDenominationByCurrencyId", e.getMessage()+" currencyId :"+currencyId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in fetchCurrencyDenominationByCurrencyId", e.getMessage()+" currencyId :"+currencyId);
			throw new GlobalException(e.getMessage());
		}

		return lstCurrencyDenomination;
	}

	public Boolean acceptOrderLock(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;
		BigDecimal deliveryDetailsId = null;
		String oldOrderStatus = null;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					deliveryDetailsId = deliveryDetails.getDeleviryDelSeqId();
					oldOrderStatus = deliveryDetails.getOrderStatus();
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						Date orderLock = deliveryDetails.getOrderLock();
						BigDecimal employeeDBId = deliveryDetails.getEmployeeId();
						Date currenctSysDate = new Date();
						long diff = currenctSysDate.getTime() - orderLock.getTime();
						long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
						JaxConfig jaxConfig = jaxConfigRepository.findByType("FC_SALE_ONLINE");
						if (jaxConfig != null) {
							if(jaxConfig.getValue() != null && diffMinutes >= Long.parseLong(jaxConfig.getValue())) {
								status = saveAndUpdateOrderLock(employeeId, lstOrderManagement,deliveryDetailsId,oldOrderStatus);
							}else {
								// allow
								if(employeeDBId.compareTo(employeeId) == 0) {
									// already
									status = saveAndUpdateOrderLock(employeeId, lstOrderManagement,deliveryDetailsId,oldOrderStatus);
								}else {
									throw new GlobalException(JaxError.ORDER_LOCKED_OTHER_EMPLOYEE,"record is been locked by other employee");
								}
							}
						}else {
							// default 10 minutes
							if(diffMinutes >= new Long(10)) {
								status = saveAndUpdateOrderLock(employeeId, lstOrderManagement,deliveryDetailsId,oldOrderStatus);
							}else {
								// allow
								if(employeeDBId.compareTo(employeeId) == 0) {
									// already
									status = saveAndUpdateOrderLock(employeeId, lstOrderManagement,deliveryDetailsId,oldOrderStatus);
								}else {
									throw new GlobalException(JaxError.ORDER_LOCKED_OTHER_EMPLOYEE,"record is been locked by other employee");
								}
							}
						}
					}else {
						status = saveAndUpdateOrderLock(employeeId, lstOrderManagement,deliveryDetailsId,oldOrderStatus);
					}
				}else {
					throw new GlobalException(JaxError.NO_DELIVERY_DETAILS,"No records available for Delivery details");
				}
			}else {
				throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO,"Collection details is empty");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in acceptOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in acceptOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	// update the status
	public Boolean saveAndUpdateOrderLock(BigDecimal employeeId,List<OrderManagementView> lstOrderManagement,BigDecimal deliveryDetailsId,String oldOrderStatus) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
		if(employeeDt != null && employeeDt.getEmployeeId() != null){
			userName = employeeDt.getUserName();
			fcSaleBranchDao.saveOrderLockDetails(lstOrderManagement,employeeId,userName,ConstantDocument.ACP);
			// old status
			logStatusChangeAuditEvent(deliveryDetailsId, oldOrderStatus);
			status = Boolean.TRUE;
		}else {
			throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
		}

		return status;
	}

	public Boolean releaseOrderLock(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		BigDecimal deliveryDetailsId = null;
		String oldOrderStatus = null;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					deliveryDetailsId = deliveryDetails.getDeleviryDelSeqId();
					oldOrderStatus = deliveryDetails.getOrderStatus();
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null){
							userName = employeeDt.getUserName();
							fcSaleBranchDao.saveOrderReleaseDetails(lstOrderManagement,employeeId,userName,ConstantDocument.ORD);
							// old status
							logStatusChangeAuditEvent(deliveryDetailsId, oldOrderStatus);
							status = Boolean.TRUE;
						}else {
							throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
						}
					}else {
						throw new GlobalException(JaxError.ORDER_RELEASED_ALREADY,"Already records are released");
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in releaseOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in releaseOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	public BigDecimal fetchCurrencyMasterDetails(BigDecimal currencyId) {
		CurrencyMasterModel currencyMasterModel = currencyMasterDao.getCurrencyMasterById(currencyId);
		BigDecimal decimalValue = new BigDecimal(3);
		if(currencyMasterModel != null) {
			decimalValue = currencyMasterModel.getDecinalNumber();
		}

		return decimalValue;
	}


	public Boolean dispatchOrder(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		BigDecimal deliveryDetailsId = null;
		String oldOrderStatus = null;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					deliveryDetailsId = deliveryDetails.getDeleviryDelSeqId();
					oldOrderStatus = deliveryDetails.getOrderStatus();
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null){
							userName = employeeDt.getUserName();
							fcSaleBranchDao.saveDispatchOrder(lstOrderManagement,employeeId,userName,ConstantDocument.OFD);

							// old status
							logStatusChangeAuditEvent(deliveryDetailsId, oldOrderStatus);

							fcSaleDeliveryService.sendOtp(deliveryDetails.getDeleviryDelSeqId(), false);
							status = Boolean.TRUE;
						}else {
							throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
						}
					}else {
						throw new GlobalException(JaxError.ORDER_IS_NOT_LOCK,"Dispatch order is not locked");
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in dispatchOrder", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in dispatchOrder", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	// fetch the currency Denomination from currency adjust
	public List<UserStockDto> fetchCurrencyAdjustDetails(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode){
		List<UserStockDto> currencyAdjust = new ArrayList<>();

		List<ForeignCurrencyAdjust> foreignCurrencyAdjust = fcSaleBranchDao.fetchByCollectionDetails(documentNo, documentYear, companyId, documentCode,ConstantDocument.Yes);
		if(foreignCurrencyAdjust != null && foreignCurrencyAdjust.size() != 0) {

			for (ForeignCurrencyAdjust foreignCurrencyAdj : foreignCurrencyAdjust) {

				UserStockDto userStockDto = new UserStockDto();

				userStockDto.setCurrencyId(foreignCurrencyAdj.getFsCurrencyMaster().getCurrencyId());
				userStockDto.setDenominationAmount(foreignCurrencyAdj.getDenaminationAmount());
				userStockDto.setDenominationId(foreignCurrencyAdj.getFsDenominationId().getDenominationId());
				userStockDto.setDenominationPrice(foreignCurrencyAdj.getAdjustmentAmount());
				userStockDto.setDenominationQuatity(foreignCurrencyAdj.getNotesQuantity());

				currencyAdjust.add(userStockDto);
			}
		}

		return currencyAdjust;
	}

	public Boolean acknowledgeDriver(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		BigDecimal deliveryDetailsId = null;
		String oldOrderStatus = null;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					deliveryDetailsId = deliveryDetails.getDeleviryDelSeqId();
					oldOrderStatus = deliveryDetails.getOrderStatus();
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null && employeeDt.getUserName() != null){
							userName = employeeDt.getUserName();
							if(deliveryDetails.getOrderStatus() != null && deliveryDetails.getOrderStatus().equalsIgnoreCase(ConstantDocument.OFD_ACK)) {
								// migrate stock from cashier to driver
								Boolean stockStatus = currentStockMigration(deliveryDetailsId, orderManagementView.getDriverEmployeId(),employeeId);
								if(stockStatus) {
									fcSaleBranchDao.saveAcknowledgeDriver(lstOrderManagement,employeeId,userName,ConstantDocument.OFD_CNF);
									// stock update transfer
									saveFCStockTransferDetails(deliveryDetailsId, orderManagementView.getDriverEmployeId(),employeeId,ConstantDocument.OFD_CNF);
									// old status
									logStatusChangeAuditEvent(deliveryDetailsId, oldOrderStatus);
									status = Boolean.TRUE;
								}
							}else {
								throw new GlobalException(JaxError.ORDER_STATUS_MISMATCH,"Order status is not out for delivery acknowledge ");
							}
						}else {
							throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
						}
					}else {
						throw new GlobalException(JaxError.ORDER_IS_NOT_LOCK,"Dispatch order is not locked");
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in acknowledgeDriver", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in acknowledgeDriver", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	@Transactional
	public Boolean returnAcknowledge(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		BigDecimal deliveryDetailsId = null;
		String oldOrderStatus = null;
		BigDecimal driverEmployeeId = null;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					deliveryDetailsId = deliveryDetails.getDeleviryDelSeqId();
					oldOrderStatus = deliveryDetails.getOrderStatus();
					driverEmployeeId = deliveryDetails.getDriverEmployeeId();
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null && employeeDt.getUserName() != null){
							userName = employeeDt.getUserName();
							if(deliveryDetails.getOrderStatus() != null && deliveryDetails.getOrderStatus().equalsIgnoreCase(ConstantDocument.RTD_ACK)) {
								// migrate stock from driver to cashier
								Boolean stockStatus = migrateStock(lstOrderManagement,employeeId,driverEmployeeId,userName,ConstantDocument.RTD);
								if(stockStatus) {
									fcSaleBranchDao.saveReturnAcknowledge(lstOrderManagement,employeeId,userName,ConstantDocument.RTD);
									// stock update transfer
									saveFCStockTransferDetails(deliveryDetailsId,employeeId,orderManagementView.getDriverEmployeId(),ConstantDocument.RTD);
									// old status
									logStatusChangeAuditEvent(deliveryDetailsId, oldOrderStatus);
									status = Boolean.TRUE;
								}
							}
						}else {
							throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
						}
					}else {
						throw new GlobalException(JaxError.ORDER_IS_NOT_LOCK,"Dispatch order is not locked");
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in returnAcknowledge", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in returnAcknowledge", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	@Transactional
	public Boolean acceptCancellation(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		BigDecimal deliveryDetailsId = null;
		String oldOrderStatus = null;
		BigDecimal driverEmployeeId = null;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					deliveryDetailsId = deliveryDetails.getDeleviryDelSeqId();
					oldOrderStatus = deliveryDetails.getOrderStatus();
					driverEmployeeId = deliveryDetails.getDriverEmployeeId();
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null && employeeDt.getUserName() != null){
							userName = employeeDt.getUserName();
							if(deliveryDetails.getOrderStatus() != null && deliveryDetails.getOrderStatus().equalsIgnoreCase(ConstantDocument.CND_ACK)) {
								// migrate stock from driver to cashier
								Boolean stockStatus = migrateStock(lstOrderManagement,employeeId,driverEmployeeId,userName,ConstantDocument.CND);
								if(stockStatus) {
									fcSaleBranchDao.saveAcceptCancellation(lstOrderManagement,employeeId,userName,ConstantDocument.CND);
									// stock update transfer
									saveFCStockTransferDetails(deliveryDetailsId,employeeId,orderManagementView.getDriverEmployeId(),ConstantDocument.CND);
									// old status
									logStatusChangeAuditEvent(deliveryDetailsId, oldOrderStatus);
									status = Boolean.TRUE;
								}
							}
						}else {
							throw new GlobalException(JaxError.INVALID_EMPLOYEE,"Employee details is empty");
						}
					}else {
						throw new GlobalException(JaxError.ORDER_IS_NOT_LOCK,"Dispatch order is not locked");
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in acceptCancellation", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in acceptCancellation", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	public FxOrderReportResponseDto reprintOrder(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId){
		FxOrderReportResponseDto fxOrderReportResponseDto = null;
		BigDecimal customerId = null;
		// fetch collection details
		List<CollectionModel> collection = fcSaleBranchDao.fetchCollectionData(orderNumber, orderYear);
		if(collection != null && !collection.isEmpty()) {
			CollectionModel collectionModel = collection.get(0);
			customerId = collectionModel.getFsCustomer().getCustomerId();

			fxOrderReportResponseDto = fetchTransactionReport(customerId,orderYear, orderNumber);
		}else {
			throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO,"Collection details is empty");
		}

		return fxOrderReportResponseDto; 
	}
	
	private void logStatusChangeAuditEvent(BigDecimal deliveryDetailSeqId, String oldOrderStatus) {
		fcSaleEventManager.logStatusChangeAuditEvent(deliveryDetailSeqId, oldOrderStatus);
	}
	
	// stock move from branch staff to driver and vice versa
	public Boolean currentStockMigration(BigDecimal deliveryDetailSeqId,BigDecimal driverEmployeeId,BigDecimal reqEmployeeId) {
		Boolean status = Boolean.FALSE;
		String toUserName = null;
		String fromUserName = null;
		BigDecimal toCountryBranchId = null;
		BigDecimal fromCountryBranchId = null;
		BigDecimal toBranchId = null;
		BigDecimal fromBranchId = null;
		BigDecimal countryId = null;
		BigDecimal companyId = null;
		BigDecimal collectionDocumentNo = null;
		BigDecimal collectionDocumentYear = null;
		Boolean oldEmosProdStatus = Boolean.FALSE;
		int count = 0;
		List<ForeignCurrencyAdjust> lstTotalStock = new ArrayList<>();
		List<ForeignCurrencyAdjust> lstToStock = new ArrayList<>();
		List<ForeignCurrencyAdjust> lstFromStock = new ArrayList<>();
		List<ForeignCurrencyOldModel> lstOldToStock = new ArrayList<>();
		List<ForeignCurrencyOldModel> lstOldFromStock = new ArrayList<>();

		if (metaData.getCompanyId() == null) {
			throw new GlobalException("Missing company id");
		}

		try {
			FxEmployeeDetailsDto employeeDt = fetchEmployee(driverEmployeeId);
			if(employeeDt != null && employeeDt.getEmployeeId() != null){
				toUserName = employeeDt.getUserName();
				toCountryBranchId = employeeDt.getCountryBranchId();
				toBranchId = employeeDt.getBranchId();
			}

			FxEmployeeDetailsDto reqEmployeeDt = fetchEmployee(reqEmployeeId);
			if(reqEmployeeDt != null && reqEmployeeDt.getEmployeeId() != null){
				fromUserName = reqEmployeeDt.getUserName();
				fromCountryBranchId = reqEmployeeDt.getCountryBranchId();
				fromBranchId = reqEmployeeDt.getBranchId();
			}

			// fetch records 
			List<OrderManagementView> lstOrderManager = fcSaleBranchDao.fetchOrdersByDeliveryDetailId(deliveryDetailSeqId);
			if(lstOrderManager != null && lstOrderManager.size() != 0){
				count = lstOrderManager.size();
				OrderManagementView ordManager = lstOrderManager.get(0);
				collectionDocumentYear = ordManager.getCollectionDocFinanceYear();
				collectionDocumentNo = ordManager.getCollectionDocumentNo();
				for (OrderManagementView orderManagementView : lstOrderManager) {
					if(orderManagementView.getDocumentNo() != null && orderManagementView.getCollectionDocFinanceYear() != null) {
						List<ForeignCurrencyAdjust> lstFcAdj = fcSaleBranchDao.fetchByCollectionDetails(orderManagementView.getDocumentNo(), orderManagementView.getCollectionDocFinanceYear(), metaData.getCompanyId(), ConstantDocument.DOCUMENT_CODE_FOR_FCSALE,ConstantDocument.Yes);
						if(lstFcAdj != null && lstFcAdj.size() != 0) {
							lstTotalStock.addAll(lstFcAdj);
						}
					}
				}
			}
			
			if(lstTotalStock != null && lstTotalStock.size() != 0) {
				HashMap<String, Object> saveFcAdjPurchase = saveFcAdjJava(lstTotalStock,toUserName,toUserName,ConstantDocument.P,toCountryBranchId);

				if(saveFcAdjPurchase != null && !saveFcAdjPurchase.isEmpty()) {
					lstToStock = (List<ForeignCurrencyAdjust>) saveFcAdjPurchase.get("FC_ADJ");
					countryId = (BigDecimal) saveFcAdjPurchase.get("COUNTRY_ID");
					companyId = (BigDecimal) saveFcAdjPurchase.get("COMPANY_ID");
				}

				lstOldToStock = saveFcAdjOldEmos(lstTotalStock,toUserName,toUserName,toBranchId,ConstantDocument.P);

				ForeignCurrencyAdjust foreignCurAdj = lstTotalStock.get(0);
				if(foreignCurAdj.getDocumentStatus() != null && foreignCurAdj.getDocumentStatus().equalsIgnoreCase(ConstantDocument.P)) {
					oldEmosProdStatus = Boolean.TRUE;
				}
				
				if(!oldEmosProdStatus) {
					HashMap<String, Object> saveFcAdjSale = saveFcAdjJava(lstTotalStock,fromUserName,toUserName,ConstantDocument.S,fromCountryBranchId);

					if(saveFcAdjSale != null && !saveFcAdjSale.isEmpty()) {
						lstFromStock = (List<ForeignCurrencyAdjust>) saveFcAdjSale.get("FC_ADJ");
					}
				}

				if(oldEmosProdStatus) {
					for (ForeignCurrencyAdjust foreignCurrencyAdjust : lstTotalStock) {
						// old record - from branch
						foreignCurrencyAdjust.setModifiedBy(toUserName);
						foreignCurrencyAdjust.setModifiedDate(new Date());
						foreignCurrencyAdjust.setApprovalBy(toUserName);
						foreignCurrencyAdjust.setApprovalDate(new Date());
						lstFromStock.add(foreignCurrencyAdjust);
					}
				}else {
					HashMap<String, Object> saveFcAdjSale = saveFcAdjJava(lstTotalStock,fromUserName,toUserName,ConstantDocument.S,fromCountryBranchId);

					if(saveFcAdjSale != null && !saveFcAdjSale.isEmpty()) {
						lstFromStock = (List<ForeignCurrencyAdjust>) saveFcAdjSale.get("FC_ADJ");
					}
					
					lstOldFromStock = saveFcAdjOldEmos(lstTotalStock,toUserName,fromUserName,fromBranchId, ConstantDocument.S);
				}

			}

			if(lstFromStock != null && lstFromStock.size() != 0) {
				fcSaleBranchDao.stockUpdate(lstFromStock, null,null,null);
				if(countryId != null && collectionDocumentYear != null && collectionDocumentNo != null && companyId != null) {
					// transfer to emos
					if(oldEmosProdStatus) {
						// check whether procedure moved or not
						ForeignCurrencyAdjust fcCurrencyAdjust = lstFromStock.get(0);
						BigDecimal companyCode = fcCurrencyAdjust.getCompanyCode();
						Boolean recPayStatus = fcSaleBranchDao.fetchRecPayTrnxDetails(companyCode, ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION, collectionDocumentYear, collectionDocumentNo,count);
						
						// move java to old emos
						if(recPayStatus) {
							transferReceiptPaymentEMOS(countryId, collectionDocumentYear, collectionDocumentNo, companyId);
						}
					}

					if(lstToStock != null && lstToStock.size() != 0 && lstOldToStock != null && lstOldToStock.size() != 0) {
						fcSaleBranchDao.stockUpdate(null, lstToStock,lstOldToStock,lstOldFromStock);
						status = Boolean.TRUE;
					}else {
						// fail
						logger.error("currentStockMigration : lstToStock " + lstToStock.toString() + "\n lstOldToStock " + lstOldToStock.toString());
					}
				}else {
					// fail
					logger.error("currentStockMigration : countryId " + countryId + " collectionDocumentYear " + collectionDocumentYear + " collectionDocumentNo " + collectionDocumentNo + " companyId " + companyId);
				}
			}else {
				// fail
				logger.error("currentStockMigration : lstFromStock " + lstFromStock.toString());
			}

			if(!status) {
				throw new GlobalException(JaxError.SAVE_FAILED,"Currenct stock migration failed while driver acceptance");
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in currentStockMigration", e.getMessage()+" deliveryDetailSeqId :"+deliveryDetailSeqId+" driverEmployeeId :"+driverEmployeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in currentStockMigration", e.getMessage()+" deliveryDetailSeqId :"+deliveryDetailSeqId+" driverEmployeeId :"+driverEmployeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	public HashMap<String, Object> saveFcAdjJava(List<ForeignCurrencyAdjust> lstTotalStock,String oracleUser,String userName,String trnxType,BigDecimal countryBranchId){
		HashMap<String, Object> saveFcAdj = new HashMap<>();
		List<ForeignCurrencyAdjust> lstToStock = new ArrayList<>();
		BigDecimal countryId = null;
		BigDecimal companyId = null;

		for (ForeignCurrencyAdjust foreignCurrencyAdjust : lstTotalStock) {
			// new record - to branch
			ForeignCurrencyAdjust foreignCurrencyAdj = new ForeignCurrencyAdjust();

			foreignCurrencyAdj.setDocumentLineNumber(foreignCurrencyAdjust.getDocumentLineNumber());
			foreignCurrencyAdj.setAccountmmyyyy(foreignCurrencyAdjust.getAccountmmyyyy());
			foreignCurrencyAdj.setCollect(foreignCurrencyAdjust.getCollect());
			foreignCurrencyAdj.setFsCountryMaster(foreignCurrencyAdjust.getFsCountryMaster());
			if(foreignCurrencyAdjust.getFsCountryMaster() != null) {
				countryId = foreignCurrencyAdjust.getFsCountryMaster().getCountryId();
			}

			if(countryBranchId != null) {
				CountryBranch countryBranch = new CountryBranch();
				countryBranch.setCountryBranchId(countryBranchId);
				foreignCurrencyAdj.setCountryBranch(countryBranch);
			}else if(foreignCurrencyAdjust.getCountryBranch() != null){
				foreignCurrencyAdj.setCountryBranch(foreignCurrencyAdjust.getCountryBranch());
			}

			foreignCurrencyAdj.setFsCompanyMaster(foreignCurrencyAdjust.getFsCompanyMaster());
			if(foreignCurrencyAdjust.getFsCompanyMaster() != null) {
				companyId = foreignCurrencyAdjust.getFsCompanyMaster().getCompanyId();
			}

			foreignCurrencyAdj.setCompanyCode(foreignCurrencyAdjust.getCompanyCode());
			foreignCurrencyAdj.setFsCurrencyMaster(foreignCurrencyAdjust.getFsCurrencyMaster());
			foreignCurrencyAdj.setFsCustomer(foreignCurrencyAdjust.getFsCustomer());
			foreignCurrencyAdj.setFsDenominationId(foreignCurrencyAdjust.getFsDenominationId());
			foreignCurrencyAdj.setAdjustmentAmount(foreignCurrencyAdjust.getAdjustmentAmount());
			foreignCurrencyAdj.setDenaminationAmount(foreignCurrencyAdjust.getDenaminationAmount());
			foreignCurrencyAdj.setDocumentNo(foreignCurrencyAdjust.getDocumentNo());
			foreignCurrencyAdj.setNotesQuantity(foreignCurrencyAdjust.getNotesQuantity());
			foreignCurrencyAdj.setExchangeRate(foreignCurrencyAdjust.getExchangeRate());
			foreignCurrencyAdj.setDocumentFinanceYear(foreignCurrencyAdjust.getDocumentFinanceYear());
			foreignCurrencyAdj.setDocumentDate(foreignCurrencyAdjust.getDocumentDate());
			foreignCurrencyAdj.setOracleUser(oracleUser);
			foreignCurrencyAdj.setCreatedBy(userName);
			foreignCurrencyAdj.setCreatedDate(new Date());
			foreignCurrencyAdj.setTransactionType(trnxType);
			foreignCurrencyAdj.setDocumentStatus(ConstantDocument.P);
			foreignCurrencyAdj.setProgNumber(ConstantDocument.FC_SALE);
			foreignCurrencyAdj.setStockUpdated(" ");
			foreignCurrencyAdj.setApprovalBy(userName);
			foreignCurrencyAdj.setApprovalDate(new Date());
			foreignCurrencyAdj.setDocumentId(foreignCurrencyAdjust.getDocumentId());
			foreignCurrencyAdj.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);

			lstToStock.add(foreignCurrencyAdj);
		}

		saveFcAdj.put("FC_ADJ", lstToStock);
		saveFcAdj.put("COUNTRY_ID", countryId);
		saveFcAdj.put("COMPANY_ID", companyId);

		return saveFcAdj;
	}

	public List<ForeignCurrencyOldModel> saveFcAdjOldEmos(List<ForeignCurrencyAdjust> lstTotalStock,String userName,String oracleUser,BigDecimal branchId,String trnxType){
		List<ForeignCurrencyOldModel> lstOldToStock = new ArrayList<>();

		for (ForeignCurrencyAdjust foreignCurrencyAdjust : lstTotalStock) {
			// old emos table
			ForeignCurrencyOldModel foreignCurrencyOldModel = new ForeignCurrencyOldModel();

			foreignCurrencyOldModel.setAccountmmyyyy(foreignCurrencyAdjust.getAccountmmyyyy());
			if(foreignCurrencyAdjust.getExchangeRate() != null && foreignCurrencyAdjust.getAdjustmentAmount() != null) {
				foreignCurrencyOldModel.setAmountExchange(foreignCurrencyAdjust.getExchangeRate().multiply(foreignCurrencyAdjust.getAdjustmentAmount()));
			}
			foreignCurrencyOldModel.setAmountTransfer(foreignCurrencyAdjust.getAdjustmentAmount());
			foreignCurrencyOldModel.setApprovedBy(userName);
			foreignCurrencyOldModel.setApprovedDate(new Date());
			foreignCurrencyOldModel.setCompanyCode(foreignCurrencyAdjust.getFsCompanyMaster().getCompanyCode());
			foreignCurrencyOldModel.setCreatedDate(new Date());
			foreignCurrencyOldModel.setCreator(userName);
			foreignCurrencyOldModel.setCurrencyExchange(foreignCurrencyAdjust.getFsCurrencyMaster().getCurrencyCode());
			foreignCurrencyOldModel.setCurrencyTransfer(foreignCurrencyAdjust.getFsCurrencyMaster().getCurrencyCode());
			foreignCurrencyOldModel.setDenominationAmount(foreignCurrencyAdjust.getDenaminationAmount());
			foreignCurrencyOldModel.setDenominationId(foreignCurrencyAdjust.getFsDenominationId().getDenominationCode());
			foreignCurrencyOldModel.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
			foreignCurrencyOldModel.setDocumentDate(new Date());
			foreignCurrencyOldModel.setDocumentFYR(foreignCurrencyAdjust.getDocumentFinanceYear());
			foreignCurrencyOldModel.setDocumentLineNo(foreignCurrencyAdjust.getDocumentLineNumber());
			foreignCurrencyOldModel.setDocumentNumber(foreignCurrencyAdjust.getDocumentNo());
			foreignCurrencyOldModel.setDocumentStatus(ConstantDocument.P);
			foreignCurrencyOldModel.setLocationCode(branchId);
			foreignCurrencyOldModel.setNoteQuantity(foreignCurrencyAdjust.getNotesQuantity());
			foreignCurrencyOldModel.setOracleUser(oracleUser);
			foreignCurrencyOldModel.setProgramNo(ConstantDocument.FC_SALE);
			foreignCurrencyOldModel.setRateApplied(foreignCurrencyAdjust.getExchangeRate());
			foreignCurrencyOldModel.setStockUpdate(" ");
			foreignCurrencyOldModel.setTransactionType(trnxType);

			lstOldToStock.add(foreignCurrencyOldModel);
		}

		return lstOldToStock;
	}

	// stock move from branch staff to driver and vice versa
	public Boolean currentStockNullify(BigDecimal deliveryDetailSeqId,BigDecimal driverEmployeeId) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		String oracleUser = null;
		BigDecimal branchId = null;
		List<ForeignCurrencyAdjust> lstTotalStock = new ArrayList<>();
		List<ForeignCurrencyAdjust> lstFromStock = new ArrayList<>();
		List<ForeignCurrencyOldModel> lstOldToStock = new ArrayList<>();

		FxEmployeeDetailsDto employeeDt = fetchEmployee(driverEmployeeId);
		if(employeeDt != null && employeeDt.getEmployeeId() != null){
			userName = employeeDt.getUserName();
			oracleUser = employeeDt.getUserName();
			branchId = employeeDt.getBranchId();
		}

		// fetch records 
		List<OrderManagementView> lstOrderManager = fcSaleBranchDao.fetchOrdersByDeliveryDetailId(deliveryDetailSeqId);
		if(lstOrderManager != null && lstOrderManager.size() != 0){
			for (OrderManagementView orderManagementView : lstOrderManager) {
				if(orderManagementView.getDocumentNo() != null && orderManagementView.getCollectionDocFinanceYear() != null) {
					List<ForeignCurrencyAdjust> lstFcAdj = fcSaleBranchDao.fetchByCollectionDetailsByTrnxType(orderManagementView.getDocumentNo(), orderManagementView.getCollectionDocFinanceYear(), metaData.getCompanyId(), ConstantDocument.DOCUMENT_CODE_FOR_FCSALE,ConstantDocument.P,ConstantDocument.Yes,ConstantDocument.P);
					if(lstFcAdj != null && lstFcAdj.size() != 0) {
						lstTotalStock.addAll(lstFcAdj);
					}
				}
			}
		}

		if(lstTotalStock != null && lstTotalStock.size() != 0) {
			HashMap<String, Object> saveFcAdj = saveFcAdjJava(lstTotalStock, oracleUser,userName, ConstantDocument.S,null);

			if(saveFcAdj != null && !saveFcAdj.isEmpty()) {
				lstFromStock = (List<ForeignCurrencyAdjust>) saveFcAdj.get("FC_ADJ");
				lstOldToStock = saveFcAdjOldEmos(lstTotalStock,oracleUser,userName,branchId,ConstantDocument.S);
			}
		}
		if(lstFromStock != null && lstFromStock.size() != 0) {
			fcSaleBranchDao.stockUpdate(lstFromStock,null,lstOldToStock,null);
			status = Boolean.TRUE;
		}else {
			// fail
			throw new GlobalException(JaxError.SAVE_FAILED,"Currenct stock nullify failed while customer acceptance");
		}

		return status;
	}

	// after cancel or return status stock migration
	public Boolean migrateStock(List<OrderManagementView> lstOrderManagement,BigDecimal employeeId,BigDecimal driverEmployeeId,String userName,String orderStatus) {
		Boolean status = Boolean.FALSE;
		String oracleUser = null;
		BigDecimal toCountryBranchId = null;
		BigDecimal toBranchId = null;
		BigDecimal fromBranchId = null;
		List<ForeignCurrencyAdjust> lstTotalStock = new ArrayList<>();
		List<ForeignCurrencyAdjust> lstToStock = new ArrayList<>();
		List<ForeignCurrencyAdjust> lstFromStock = new ArrayList<>();
		List<ForeignCurrencyOldModel> lstOldToStock = new ArrayList<>();
		List<ForeignCurrencyOldModel> lstOldFromStock = new ArrayList<>();

		FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
		if(employeeDt != null && employeeDt.getEmployeeId() != null){
			userName = employeeDt.getUserName();
			toCountryBranchId = employeeDt.getCountryBranchId();
			toBranchId = employeeDt.getBranchId();
		}
		
		FxEmployeeDetailsDto reqemployeeDt = fetchEmployee(driverEmployeeId);
		if(reqemployeeDt != null && reqemployeeDt.getEmployeeId() != null){
			oracleUser = reqemployeeDt.getUserName();
			fromBranchId = reqemployeeDt.getBranchId();
		}

		if(orderStatus != null) {
			if(lstOrderManagement != null && lstOrderManagement.size() != 0){
				for (OrderManagementView orderManagementView : lstOrderManagement) {
					if(orderManagementView.getDocumentNo() != null && orderManagementView.getCollectionDocFinanceYear() != null) {
						List<ForeignCurrencyAdjust> lstFcAdj = fcSaleBranchDao.fetchByCollectionDetailsByTrnxType(orderManagementView.getDocumentNo(), orderManagementView.getCollectionDocFinanceYear(), metaData.getCompanyId(), ConstantDocument.DOCUMENT_CODE_FOR_FCSALE,ConstantDocument.P,ConstantDocument.Yes,ConstantDocument.P);
						if(lstFcAdj != null && lstFcAdj.size() != 0) {
							lstTotalStock.addAll(lstFcAdj);
						}
					}
				}
			}
		}

		if(lstTotalStock != null && lstTotalStock.size() != 0) {
			HashMap<String, Object> saveFcAdjP = saveFcAdjJava(lstTotalStock,userName, userName, ConstantDocument.P, toCountryBranchId);

			if(saveFcAdjP != null && !saveFcAdjP.isEmpty()) {
				lstToStock = (List<ForeignCurrencyAdjust>) saveFcAdjP.get("FC_ADJ");
			}

			lstOldToStock = saveFcAdjOldEmos(lstTotalStock,userName,userName,toBranchId,ConstantDocument.P);
			lstOldFromStock = saveFcAdjOldEmos(lstTotalStock,userName,oracleUser,fromBranchId, ConstantDocument.S);

			HashMap<String, Object> saveFcAdjS = saveFcAdjJava(lstTotalStock,oracleUser, userName, ConstantDocument.S,null);

			if(saveFcAdjS != null && !saveFcAdjS.isEmpty()) {
				lstFromStock = (List<ForeignCurrencyAdjust>) saveFcAdjS.get("FC_ADJ");
			}
		}

		if(lstFromStock != null && lstFromStock.size() != 0 && lstToStock != null && lstToStock.size() != 0 && lstOldToStock != null && lstOldToStock.size() != 0 && lstOldFromStock != null && lstOldFromStock.size() != 0) {
			fcSaleBranchDao.stockUpdate(lstFromStock, lstToStock,lstOldToStock,lstOldFromStock);
			status = Boolean.TRUE;
		}else {
			// fail
			logger.error("currentStockMigration : lstFromStock " + lstFromStock.toString() + "\n lstToStock " + lstToStock.toString() + "\n lstOldToStock " + lstOldToStock.toString());
		}

		if(!status) {
			throw new GlobalException(JaxError.SAVE_FAILED,"Currenct stock migration failed while cashier acceptance");
		}

		return status;
	}

	// save the fc stock transfer details
	public Boolean saveFCStockTransferDetails(BigDecimal deliveryDetailSeqId,BigDecimal toEmployeeId,BigDecimal fromEmployeeId,String orderStatus) {
		Boolean status = Boolean.FALSE;
		String toUserName = null;
		String fromUserName = null;
		BigDecimal toBranchId = null;
		BigDecimal fromBranchId = null;
		BigDecimal toCountryBranchId = null;
		BigDecimal fromCountryBranchId = null;
		BigDecimal companyId = null;
		BigDecimal companyCode = null;
		List<String> currencyId = new ArrayList<>();
		List<ForeignCurrencyStockTransfer> lstFCStkTrnf = new ArrayList<>();
		List<BigDecimal> duplicate = new ArrayList<>();

		try {
			if (metaData.getCompanyId() == null) {
				throw new GlobalException("Missing company id");
			}else {
				companyId = metaData.getCompanyId();
			}

			if(toEmployeeId != null) {
				FxEmployeeDetailsDto employeeDt = fetchEmployee(toEmployeeId);
				if(employeeDt != null && employeeDt.getEmployeeId() != null){
					toUserName = employeeDt.getUserName();
					toCountryBranchId = employeeDt.getCountryBranchId();
					toBranchId = employeeDt.getBranchId();
				}
			}

			if(fromEmployeeId != null) {
				FxEmployeeDetailsDto reqEmployeeDt = fetchEmployee(fromEmployeeId);
				if(reqEmployeeDt != null && reqEmployeeDt.getEmployeeId() != null){
					fromUserName = reqEmployeeDt.getUserName();
					fromCountryBranchId = reqEmployeeDt.getCountryBranchId();
					fromBranchId = reqEmployeeDt.getBranchId();
				}
			}

			// fetch company code
			List<ViewCompanyDetails> companyMaster = companyDAO.getCompanyDetailsByCompanyId(BigDecimal.ONE, companyId);
			if(companyMaster != null && companyMaster.size() != 0) {
				ViewCompanyDetails viewCompanyDetails = companyMaster.get(0);
				companyCode = viewCompanyDetails.getCompanyCode();
			}

			// fetch records 
			List<OrderManagementView> lstOrderManager = fcSaleBranchDao.fetchOrdersByDeliveryDetailId(deliveryDetailSeqId);
			if(lstOrderManager != null && lstOrderManager.size() != 0){

				for (OrderManagementView orderManagementView : lstOrderManager) {
					if(orderManagementView.getForeignCurrencyId() != null && !duplicate.contains(orderManagementView.getForeignCurrencyId())) {
						duplicate.add(orderManagementView.getForeignCurrencyId());
						currencyId.add(orderManagementView.getForeignCurrencyId().toString());
					}
				}

				// fetch currency code
				Map<BigDecimal, CurrencyMasterModel> currencyMasterModel = currencyMasterDao.getSelectedCurrencyMap(currencyId);

				for (OrderManagementView orderManagementView : lstOrderManager) {
					if(orderManagementView.getDocumentNo() != null && orderManagementView.getCollectionDocFinanceYear() != null) {
						ForeignCurrencyStockTransfer foreignCurrencyStockTransfer = new ForeignCurrencyStockTransfer();

						foreignCurrencyStockTransfer.setAccountMMYYYY(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
						foreignCurrencyStockTransfer.setCompanyCode(companyCode);
						foreignCurrencyStockTransfer.setCompanyId(companyId);
						if(toUserName != null) {
							foreignCurrencyStockTransfer.setCreatedBy(toUserName);
						}else {
							foreignCurrencyStockTransfer.setCreatedBy(fromUserName);
						}
						foreignCurrencyStockTransfer.setCreationDate(new Date());
						if(currencyMasterModel != null && !currencyMasterModel.isEmpty() && currencyMasterModel.get(orderManagementView.getForeignCurrencyId()) != null) {
							foreignCurrencyStockTransfer.setCurrencyCode(currencyMasterModel.get(orderManagementView.getForeignCurrencyId()).getCurrencyCode());
						}
						foreignCurrencyStockTransfer.setCurrencyId(orderManagementView.getForeignCurrencyId());
						foreignCurrencyStockTransfer.setDeliveryDetSeqId(deliveryDetailSeqId);
						foreignCurrencyStockTransfer.setDocumentDate(new Date());
						foreignCurrencyStockTransfer.setDocumentFinanceYear(orderManagementView.getCollectionDocFinanceYear());
						foreignCurrencyStockTransfer.setDocumentNo(orderManagementView.getDocumentNo());
						foreignCurrencyStockTransfer.setFcValue(orderManagementView.getForeignTrnxAmount());
						foreignCurrencyStockTransfer.setFromCountryBranchId(fromCountryBranchId);
						foreignCurrencyStockTransfer.setFromEmployeeId(fromEmployeeId);
						foreignCurrencyStockTransfer.setFromLocationCode(fromBranchId);
						foreignCurrencyStockTransfer.setFromUser(fromUserName);
						foreignCurrencyStockTransfer.setIsActive(ConstantDocument.Yes);
						foreignCurrencyStockTransfer.setOrderStatus(orderStatus);
						foreignCurrencyStockTransfer.setToCountryBranchId(toCountryBranchId);
						foreignCurrencyStockTransfer.setToEmployeeId(toEmployeeId);
						foreignCurrencyStockTransfer.setToLocationCode(toBranchId);
						foreignCurrencyStockTransfer.setToUser(toUserName);
						foreignCurrencyStockTransfer.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
						//foreignCurrencyStockTransfer.setGlDate(glDate);
						//foreignCurrencyStockTransfer.setGlent(glent);
						//foreignCurrencyStockTransfer.setElerr(elerr);

						lstFCStkTrnf.add(foreignCurrencyStockTransfer);
					}
				}

				if(lstFCStkTrnf != null && lstFCStkTrnf.size() != 0) {
					fcSaleBranchDao.saveFcCurrencyStock(lstFCStkTrnf);
					status = Boolean.TRUE;
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.debug("Error in saveFCStockTransferDetails", e.getMessage()+" deliveryDetailSeqId :"+deliveryDetailSeqId+" driverEmployeeId :"+toEmployeeId+" fromEmployeeId :"+fromEmployeeId);
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error in saveFCStockTransferDetails", e.getMessage()+" deliveryDetailSeqId :"+deliveryDetailSeqId+" driverEmployeeId :"+toEmployeeId+" fromEmployeeId :"+fromEmployeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}
	
	public Map<String, Object> generateDocumentNumber(BigDecimal branchId, BigDecimal appCountryId,BigDecimal companyId,String processInd,BigDecimal finYear,BigDecimal documentId) {
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,finYear, processInd, branchId);
		return output;
	}
	
	public boolean validateUserStock(BigDecimal deliveryDetailSeqId,BigDecimal fromEmployeeId) {
		boolean stockStatus = Boolean.FALSE;
		String fromUserName = null;
		BigDecimal fromCountryBranchId = null;
		BigDecimal fromBranchId = null;
		Map<BigDecimal, BigDecimal> currencyStock = new HashMap<>();
		List<BigDecimal> foreignCurrencyId = new ArrayList<>();
		List<ForeignCurrencyAdjust> lstTotalStock = new ArrayList<ForeignCurrencyAdjust>();
		
		FxEmployeeDetailsDto reqEmployeeDt = fetchEmployee(fromEmployeeId);
		if(reqEmployeeDt != null && reqEmployeeDt.getEmployeeId() != null){
			fromUserName = reqEmployeeDt.getUserName();
			fromCountryBranchId = reqEmployeeDt.getCountryBranchId();
			fromBranchId = reqEmployeeDt.getBranchId();
		}
		
		// fetch records customer records
		List<OrderManagementView> lstOrderManager = fcSaleBranchDao.fetchOrdersByDeliveryDetailId(deliveryDetailSeqId);
		if(lstOrderManager != null && lstOrderManager.size() != 0){
			for (OrderManagementView orderManagementView : lstOrderManager) {
				if(!foreignCurrencyId.contains(orderManagementView.getForeignCurrencyId())) {
					foreignCurrencyId.add(orderManagementView.getForeignCurrencyId());
				}
				if(orderManagementView.getDocumentNo() != null && orderManagementView.getCollectionDocFinanceYear() != null) {
					List<ForeignCurrencyAdjust> lstFcAdj = fcSaleBranchDao.fetchByCollectionDetails(orderManagementView.getDocumentNo(), orderManagementView.getCollectionDocFinanceYear(), metaData.getCompanyId(), ConstantDocument.DOCUMENT_CODE_FOR_FCSALE,ConstantDocument.Yes);
					if(lstFcAdj != null && lstFcAdj.size() != 0) {
						lstTotalStock.addAll(lstFcAdj);
					}
				}
			}
		}
		
		if(lstTotalStock != null && lstTotalStock.size() != 0) {
			if(foreignCurrencyId != null && foreignCurrencyId.size() != 0) {
				// fetch staff currency stock
				List<UserFcStockView> userStockData = fcSaleBranchDao.fetchUserFCStockAllCurrencyCurrentDate(fromUserName,fromCountryBranchId,foreignCurrencyId);
				for (UserFcStockView userStockView : userStockData) {
					currencyStock.put(userStockView.getDenominationId(), userStockView.getCurrentStock());
				}
			}
			
			// compare currency stock which is need to moved
			if(currencyStock != null && currencyStock.size() != 0) {
				for (ForeignCurrencyAdjust orderManagementView : lstTotalStock) {
					if(orderManagementView.getNotesQuantity().compareTo(currencyStock.get(orderManagementView.getFsDenominationId().getDenominationId())) <= 0) {
						// continue
						stockStatus = Boolean.TRUE;
					}else {
						// break
						logger.error("Error in Stock Mismatch deliveryDetailSeqId : "+deliveryDetailSeqId+" fromEmployeeId : "+fromEmployeeId+" denomination Id : "+orderManagementView.getFsDenominationId().getDenominationId());
						stockStatus = Boolean.FALSE;
						break;
					}
				}
			}
		}

		return stockStatus;
	}

}
