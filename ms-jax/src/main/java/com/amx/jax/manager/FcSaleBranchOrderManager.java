package com.amx.jax.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.amx.jax.dao.CurrencyMasterDao;
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
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.dbmodel.fx.UserStockView;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchModel;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.JaxConfigRepository;
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
	MetaData metaData;

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
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("fetchFcSaleOrderManagement ", e.getMessage()+"applId :"+applicationCountryId+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("fetchFcSaleOrderManagement ", e.getMessage()+"applId :"+applicationCountryId+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return ordermanage;
	}

	public List<OrderManagementView> fetchFcSaleOrderDetails(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear){
		List<OrderManagementView> ordermanage = new ArrayList<>();
		try{
			ordermanage = fcSaleBranchDao.checkPendingOrders(applicationCountryId,orderNumber,orderYear);
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("fetchFcSaleOrderDetails ", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("fetchFcSaleOrderDetails ", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear);
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
					}
				}else {
					throw new GlobalException("Employee details userName,countryBranchId is empty",JaxError.INVALID_EMPLOYEE);
				}
			}else {
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("fetchUserStockViewByCurrency ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId+" foreignCurrencyId :"+foreignCurrencyId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("fetchUserStockViewByCurrency ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId+" foreignCurrencyId :"+foreignCurrencyId);
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
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
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
					throw new GlobalException("Employee details userName,countryBranchId is empty",JaxError.INVALID_EMPLOYEE);
				}
			}else {
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("fetchUserStockView ", e.getMessage()+" countryId :"+countryId+" userName :"+userName+" countryBranchId :"+countryBranchId);
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
			logger.error("fetchEmpDriverDetails ", e.getMessage());
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("fetchEmpDriverDetails ", e.getMessage());
			throw new GlobalException(e.getMessage());
		}

		return empDrivers;
	}

	// save assign driver details in multiple tables
	public Boolean saveAssignDriver(BigDecimal countryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal driverId,BigDecimal employeeId) {
		BigDecimal deliveryDetailsId = null;
		BigDecimal assignDriverId = null;
		Boolean status = Boolean.FALSE;
		String isActive = ConstantDocument.Yes;
		String userName = null;
		String orderStatus = null;

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
				}
			}

			if(deliveryDetailsId != null) {
				// fetch delivery details by id
				if(orderStatus == null) {
					throw new GlobalException("Order Status should be empty",JaxError.NULL_ORDER_STATUS);
				}else if(assignDriverId != null && driverId != null && assignDriverId.compareTo(driverId) == 0 && orderStatus.equalsIgnoreCase(ConstantDocument.PCK)) {
					throw new GlobalException("Driver is already assign for order",JaxError.DRIVER_ALREADY_ASSIGNED);
				}else {
					FxDeliveryDetailsModel deliveryDetail = fcSaleBranchDao.fetchDeliveryDetails(deliveryDetailsId,isActive);
					if(deliveryDetail != null) {

						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null){
							userName = employeeDt.getUserName();
							if(userName != null) {
								if(deliveryDetail.getDriverEmployeeId() != null) {
									if(deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.PCK) || deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.RTD)) {
										// create new record 
										FxDeliveryDetailsModel deliveryDetailNew = new FxDeliveryDetailsModel();
										deliveryDetailNew.setApplDocNo(deliveryDetail.getApplDocNo());
										deliveryDetailNew.setCreatedBy(userName);
										deliveryDetailNew.setCreatedDate(new Date());
										deliveryDetailNew.setDeliveryCharges(deliveryDetail.getDeliveryCharges());
										deliveryDetailNew.setDeliveryDate(deliveryDetail.getDeliveryDate());
										deliveryDetailNew.setDeliveryTimeSlot(deliveryDetail.getDeliveryTimeSlot());
										deliveryDetailNew.setDriverEmployeeId(driverId);
										deliveryDetailNew.setIsActive(deliveryDetail.getIsActive());
										deliveryDetailNew.setOrderStatus(ConstantDocument.OFD_ACK);
										deliveryDetailNew.setOtpToken(deliveryDetail.getOtpToken());
										deliveryDetailNew.setRemarksId(deliveryDetail.getRemarksId());
										deliveryDetailNew.setShippingAddressId(deliveryDetail.getShippingAddressId());
										deliveryDetailNew.setTransactionReceipt(deliveryDetail.getTransactionReceipt());

										// deactivate current record
										deliveryDetail.setIsActive("D");
										deliveryDetail.setUpdatedBy(userName);
										deliveryDetail.setUopdateDate(new Date());

										fcSaleBranchDao.saveDeliveryDetails(deliveryDetailNew,deliveryDetail,lstOrderManagement);
										status = Boolean.TRUE;
									}else {
										throw new GlobalException("Order status is not packed to assign driver",JaxError.ORDER_STATUS_MISMATCH);
									}
								}else {
									// update the current record by driver id
									if(deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.PCK) || deliveryDetail.getOrderStatus().equalsIgnoreCase(ConstantDocument.RTD)) {
										deliveryDetail.setDriverEmployeeId(driverId);
										deliveryDetail.setUpdatedBy(userName);
										deliveryDetail.setUopdateDate(new Date());
										deliveryDetail.setOrderStatus(ConstantDocument.OFD_ACK);
										fcSaleBranchDao.saveDeliveryDetailsDriverId(deliveryDetail);
										status = Boolean.TRUE;
									}else {
										throw new GlobalException("Order status is not packed to assign driver",JaxError.ORDER_STATUS_MISMATCH);
									}
								}
							}
						}else {
							throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
						}
					}else {
						//multiple active records
						throw new GlobalException("No records available for Delivery details",JaxError.NO_DELIVERY_DETAILS);
					}
				}
			}else {
				// error 
				throw new GlobalException("No records available for Delivery details",JaxError.NO_DELIVERY_DETAILS);
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in Exception saving Assign Driver", e.getMessage()+" countryId :"+countryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" driverId :"+driverId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in Exception saving Assign Driver", e.getMessage()+" countryId :"+countryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" driverId :"+driverId);
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
				throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("fetchEmployee ", e.getMessage()+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("fetchEmployee ", e.getMessage()+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return employeeDto;
	}

	// save the dispatch record 
	public FxOrderReportResponseDto printOrderSave(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest,BigDecimal employeeId,BigDecimal countryId,BigDecimal companyId) {
		String userName = null;
		BigDecimal countryBranchId = null;
		BigDecimal collectionDocYear = null;
		BigDecimal collectionDocNumber = null;
		BigDecimal companyCode = null;
		BigDecimal documentId = null;
		BigDecimal collectionId = null;
		BigDecimal customerId = null;
		BigDecimal deliveryDetailsId = null;
		List<BigDecimal> duplicate = new ArrayList<>();
		List<ForeignCurrencyAdjust> foreignCurrencyAdjusts = new ArrayList<>();
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
				}else {
					throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
				}

				// fetch collection details
				List<CollectionModel> collection = fcSaleBranchDao.fetchCollectionData(collectionDocNumber, collectionDocYear);
				if(collection != null && collection.size() != 0) {
					CollectionModel collectionModel = collection.get(0);
					collectionId = collectionModel.getCollectionId();
					companyCode = collectionModel.getCompanyCode();
					customerId = collectionModel.getFsCustomer().getCustomerId();
				}else {
					throw new GlobalException("Collection details is empty",JaxError.INVALID_COLLECTION_DOCUMENT_NO);
				}

				// fetch
				List<Document> documentDt = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
				if(documentDt != null && documentDt.size() != 0) {
					Document document = documentDt.get(0);
					documentId = document.getDocumentID();
				}else {
					throw new GlobalException("Document details is empty",JaxError.BLANK_DOCUMENT_DETAILS);
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
						}else {
							if(orderManagementView.getDeliveryDetailsId() != null && deliveryDetailsId != null && deliveryDetailsId.compareTo(orderManagementView.getDeliveryDetailsId()) != 0) {
								deliveryDetailsId = null; // fail
								break;
							}
						}
					}
					
					if(deliveryDetailsId == null) {
						throw new GlobalException("No records or mismatch of delivery details",JaxError.NO_DELIVERY_DETAILS);
					}
				}else {
					throw new GlobalException("Collection details is empty",JaxError.INVALID_COLLECTION_DOCUMENT_NO);
				}


				if(orderDetails != null) {

					for (FcSaleBranchDispatchModel fcSaleBranchDispatchModel : orderDetails) {
						if(fcSaleBranchDispatchModel.getDocumentNumber() != null && !duplicate.contains(fcSaleBranchDispatchModel.getDocumentNumber())) {
							duplicate.add(fcSaleBranchDispatchModel.getDocumentNumber());
							mapInventory.put(fcSaleBranchDispatchModel.getDocumentNumber(), fcSaleBranchDispatchModel.getInventoryId());

							List<UserStockDto> userCurrenctStock = fetchUserStockViewByCurrency(countryId, employeeId, fcSaleBranchDispatchModel.getCurrencyId());
							if(userCurrenctStock != null && userCurrenctStock.size() != 0) {
								// continue
							}else {
								throw new GlobalException("Currenct stock for employee not available",JaxError.CURRENCY_STOCK_NOT_AVAILABLE);
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

											foreignCurrencyAdjusts.add(foreignCurrencyAdj);

											// mapping document number with amount
											mapDocAmount.put(fcSaleBranchDispatch.getDocumentNumber(), fcAmount);
										}else {
											throw new GlobalException("Currency Denomination Details is in correct",JaxError.INCORRECT_CURRENCY_DENOMINATION);
										}
									}else {
										throw new GlobalException("Receipt Document Details is in correct",JaxError.BLANK_DOCUMENT_DETAILS);
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
							throw new GlobalException("Amount mismatch with document details",JaxError.MISMATCH_COLLECTION_AMOUNT);
						}
					}

					for (Entry<BigDecimal, String> invertoryData : mapInventory.entrySet()) {
						BigDecimal documentNo = invertoryData.getKey();
						String inventoryId = invertoryData.getValue();
						OrderManagementView orderManagementView = mapOrderManagement.get(documentNo);
						mapInventoryReceiptPayment.put(orderManagementView.getReceiptPaymentId(), inventoryId);
					}

					// saving in currency Adjustment and receipt payment
					fcSaleBranchDao.printOrderSave(foreignCurrencyAdjusts, mapInventoryReceiptPayment,userName,new Date(),deliveryDetailsId,ConstantDocument.PCK);

					// calling report
					fxOrderReportResponseDto = fetchTransactionReport(customerId,collectionDocYear, collectionDocNumber);

					// transfer to emos 
					transferReceiptPaymentEMOS(countryId, collectionDocYear, collectionDocNumber, companyId);
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in Exception saving print receipt denomination", e.getMessage()+" employeeId :"+employeeId+" countryId :"+countryId+" companyId :"+companyId+" fcSaleBranchDispatchRequest :"+fcSaleBranchDispatchRequest);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (ParseException e) {
			e.printStackTrace();
			logger.error("Error in ParseException saving print receipt denomination", e.getMessage()+" employeeId :"+employeeId+" countryId :"+countryId+" companyId :"+companyId+" fcSaleBranchDispatchRequest :"+fcSaleBranchDispatchRequest);
			throw new GlobalException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in Exception saving print receipt denomination", e.getMessage()+" employeeId :"+employeeId+" countryId :"+countryId+" companyId :"+companyId+" fcSaleBranchDispatchRequest :"+fcSaleBranchDispatchRequest);
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
								throw new GlobalException("Employee current stock not matcing",JaxError.MISMATCH_CURRENT_STOCK);
							}
						}else {
							currencyStatus = Boolean.FALSE;
							throw new GlobalException("Currency Adj Amount not matching Denomination Amount and Quantity",JaxError.MISMATCH_ADJ_AMT_AND_DENOMINATION_AMT_QUANTITY);
						}
					}else {
						throw new GlobalException("Currency Denomination Details is not vaild",JaxError.INVALID_CURRENCY_DENOMINATION);
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in checkingDenominationDetails", e.getMessage()+" FcSaleBranchDispatchModel :"+currencyAdj+" List<CurrencyWiseDenomination> :"+currencyDenomination.toString());
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in checkingDenominationDetails", e.getMessage()+" FcSaleBranchDispatchModel :"+currencyAdj+" List<CurrencyWiseDenomination> :"+currencyDenomination.toString());
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
			logger.error("Error in fetchCurrencyDenominationByCurrencyId", e.getMessage()+" currencyId :"+currencyId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in fetchCurrencyDenominationByCurrencyId", e.getMessage()+" currencyId :"+currencyId);
			throw new GlobalException(e.getMessage());
		}

		return lstCurrencyDenomination;
	}

	public Boolean acceptOrderLock(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						Date orderLock = deliveryDetails.getOrderLock();
						BigDecimal employeeDBId = deliveryDetails.getEmployeeId();
						Date currenctSysDate = new Date();
						long diff = currenctSysDate.getTime() - orderLock.getTime();
						long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff); 
						JaxConfig jaxConfig = jaxConfigRepository.findByType("FC_SALE_ONLINE");
						if (jaxConfig != null) {
							if(jaxConfig.getValue() != null && diffMinutes >= Long.parseLong(jaxConfig.getValue())) {
								status = saveAndUpdateOrderLock(employeeId, lstOrderManagement);
							}else {
								// allow
								if(employeeDBId.compareTo(employeeId) == 0) {
									// already
									status = saveAndUpdateOrderLock(employeeId, lstOrderManagement);
								}else {
									throw new GlobalException("record is been locked by other employee",JaxError.ORDER_LOCKED_OTHER_EMPLOYEE);
								}
							}
						}else {
							// default 10 minutes
							if(diffMinutes >= new Long(10)) {
								status = saveAndUpdateOrderLock(employeeId, lstOrderManagement);
							}else {
								// allow
								if(employeeDBId.compareTo(employeeId) == 0) {
									// already
									status = saveAndUpdateOrderLock(employeeId, lstOrderManagement);
								}else {
									throw new GlobalException("record is been locked by other employee",JaxError.ORDER_LOCKED_OTHER_EMPLOYEE);
								}
							}
						}
					}else {
						status = saveAndUpdateOrderLock(employeeId, lstOrderManagement);
					}
				}else {
					throw new GlobalException("No records available for Delivery details",JaxError.NO_DELIVERY_DETAILS);
				}
			}else {
				throw new GlobalException("Collection details is empty",JaxError.INVALID_COLLECTION_DOCUMENT_NO);
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in acceptOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in acceptOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}
	
	// update the status
	public Boolean saveAndUpdateOrderLock(BigDecimal employeeId,List<OrderManagementView> lstOrderManagement) {
		Boolean status = Boolean.FALSE;
		String userName = null;
		FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
		if(employeeDt != null && employeeDt.getEmployeeId() != null){
			userName = employeeDt.getUserName();
			fcSaleBranchDao.saveOrderLockDetails(lstOrderManagement,employeeId,userName,ConstantDocument.ACP);
			status = Boolean.TRUE;
		}else {
			throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
		}
		
		return status;
	}

	public Boolean releaseOrderLock(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;
		String userName = null;

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null){
							userName = employeeDt.getUserName();
							fcSaleBranchDao.saveOrderReleaseDetails(lstOrderManagement,employeeId,userName,ConstantDocument.ORD);
							status = Boolean.TRUE;
						}else {
							throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
						}
					}else {
						throw new GlobalException("Already records are released",JaxError.ORDER_RELEASED_ALREADY);
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in releaseOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in releaseOrderLock", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
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

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null){
							userName = employeeDt.getUserName();
							fcSaleBranchDao.saveDispatchOrder(lstOrderManagement,employeeId,userName,ConstantDocument.OFD);
							fcSaleDeliveryService.sendOtp(deliveryDetails.getDeleviryDelSeqId(), false);
							status = Boolean.TRUE;
						}else {
							throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
						}
					}else {
						throw new GlobalException("Dispatch order is not locked",JaxError.ORDER_IS_NOT_LOCK);
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in dispatchOrder", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in dispatchOrder", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}
	
	// fetch the currency Denomination from currency adjust
	public List<UserStockDto> fetchCurrencyAdjustDetails(BigDecimal documentNo,BigDecimal documentYear,BigDecimal companyId,BigDecimal documentCode){
		List<UserStockDto> currencyAdjust = new ArrayList<>();

		List<ForeignCurrencyAdjust> foreignCurrencyAdjust = fcSaleBranchDao.fetchByCollectionDetails(documentNo, documentYear, companyId, documentCode);
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

		try {
			// receipt payment Details
			List<OrderManagementView> lstOrderManagement = fetchFcSaleOrderDetails(applicationCountryId, orderNumber, orderYear);
			if(lstOrderManagement != null && lstOrderManagement.size() != 0) {
				// checking delivery details order lock and employee id assign
				OrderManagementView orderManagementView = lstOrderManagement.get(0);
				FxDeliveryDetailsModel deliveryDetails = fcSaleBranchDao.fetchDeliveryDetails(orderManagementView.getDeliveryDetailsId(),ConstantDocument.Yes);
				if(deliveryDetails != null) {
					if(deliveryDetails.getOrderLock() != null && deliveryDetails.getEmployeeId() != null) {
						FxEmployeeDetailsDto employeeDt = fetchEmployee(employeeId);
						if(employeeDt != null && employeeDt.getEmployeeId() != null){
							userName = employeeDt.getUserName();
							fcSaleBranchDao.saveAcknowledgeDriver(lstOrderManagement,employeeId,userName,ConstantDocument.OFD_CNF);
							status = Boolean.TRUE;
						}else {
							throw new GlobalException("Employee details is empty",JaxError.INVALID_EMPLOYEE);
						}
					}else {
						throw new GlobalException("Dispatch order is not locked",JaxError.ORDER_IS_NOT_LOCK);
					}
				}
			}
		}catch (GlobalException e) {
			e.printStackTrace();
			logger.error("Error in acknowledgeDriver", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in acknowledgeDriver", e.getMessage()+" applicationCountryId :"+applicationCountryId+" orderNumber :"+orderNumber+" orderYear :"+orderYear+" employeeId :"+employeeId);
			throw new GlobalException(e.getMessage());
		}

		return status;
	}

	// stock move from branch staff to driver and vice versa
	public Boolean currentStockMigration() {
		Boolean status = Boolean.FALSE;

		return status;
	}

}
