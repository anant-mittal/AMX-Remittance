package com.amx.jax.services;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.NotificationConstants;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.JaxClientUtil;
import com.amx.jax.client.JaxStompClient;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxDbConfig;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryRemark;
import com.amx.jax.dbmodel.fx.StatusMaster;
import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditService;
import com.amx.jax.manager.FcSaleAddressManager;
import com.amx.jax.manager.FcSaleBranchOrderManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.OtpPrefixDto;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.FxDeliveryDetailNotificationDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.notification.fx.FcSaleEventManager;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CommunicationPreferencesManager;
import com.amx.jax.userservice.manager.UserContactVerificationManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.DateUtil;
import com.amx.utils.Random;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FcSaleDeliveryService {

	Logger logger = LoggerFactory.getLogger(FcSaleService.class);

	@Autowired
	MetaData metaData;
	@Autowired
	FcSaleApplicationDao fcSaleApplicationDao;
	@Autowired
	JaxNotificationService jaxNotificationService;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	UserService userService;
	@Autowired
	FcSaleAddressManager fcSaleAddressManager;
	@Autowired
	AuditService auditService;
	@Autowired
	JaxStompClient jaxStompClient ; 
	@Autowired
	FcSaleEventManager fcSaleEventManager;
	@Autowired
	FcSaleBranchOrderManager fcSaleBranchOrderManager;
	@Autowired
	JaxConfigService jaxConfigService; 
	@Autowired
	CustomerDao custDao;
	@Autowired
	PushNotifyClient pushNotifyClient;
	@Autowired
	UserContactVerificationManager userContactVerificationManager;
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	@Autowired
	PostManService postManService;
	@Autowired
	WhatsAppClient whatsAppClient;
	@Autowired
	CommunicationPreferencesManager communicationPreferencesManager;

	/**
	 * @return today's order to be delivered for logged in driver
	 * 
	 */
	public List<FxDeliveryDetailDto> listOrders() {
		if (metaData.getEmployeeId() == null) {
			throw new GlobalException("Missing driver id");
		}
		List<FxDeliveryDetailDto> results = new ArrayList<>();
		List<VwFxDeliveryDetailsModel> deliveryDetails = fcSaleApplicationDao.listOrders(metaData.getEmployeeId());
		for (VwFxDeliveryDetailsModel model : deliveryDetails) {
			results.add(createFxDeliveryDetailDto(model));
		}
		return results;
	}

	private FxDeliveryDetailDto createFxDeliveryDetailDto(VwFxDeliveryDetailsModel model) {
		FxDeliveryDetailDto dto = new FxDeliveryDetailDto();
		FxDeliveryRemark delRemark = fcSaleApplicationDao.getDeliveryRemarkById(model.getDeliveryRemarkId());
		ShippingAddressDetail shippingAddress = fcSaleApplicationDao
				.getShippingAddressById(model.getShippingAddressId());
		StatusMaster statusMaster = fcSaleApplicationDao.getStatusMaster(model.getOrderStatus());
		// ShippingAddressDto shippingAddressDto =
		// createShippingAddressDto(shippingAddress);
		ShippingAddressDto shippingAddressDto = fcSaleAddressManager.fetchShippingAddress(model.getCustomerId(),
				model.getShippingAddressId());
		try {
			BeanUtils.copyProperties(dto, model);
			shippingAddress.setMobile(shippingAddressDto.getMobile());
			BeanUtils.copyProperties(shippingAddressDto, shippingAddress);
		} catch (Exception e) {
		}
		if (model.getCollectionDocFinYear() != null) {
			dto.setTransactionRefId(model.getCollectionDocFinYear() + "/" + model.getCollectionDocNo());
		}
		if (delRemark != null) {
			dto.setDeliveryRemark(delRemark.getDeliveryRemark());
		}
		dto.setOtpTokenPrefix(model.getOtpTokenPrefix());
		dto.setAddress(shippingAddressDto);
		dto.setOrderStatus(statusMaster.getStatusDescription());
		dto.setOrderStatusCode(statusMaster.getStatusCode());
		dto.setDeliveryDetailSeqId(model.getDeleviryDelSeqId());
		dto.setDeliveryTimeSlot(DateUtil.formatDateTime(new Date()));
		return dto;
	}

	public FxDeliveryDetailDto getDeliveryDetail(BigDecimal deliveryDetailSeqId) {
		if (deliveryDetailSeqId == null) {
			throw new GlobalException("deliveryDetailSeqId can't be blank");
		}
		VwFxDeliveryDetailsModel deliveryDetailModel = fcSaleApplicationDao.getDeliveryDetail(deliveryDetailSeqId);
		if (deliveryDetailModel == null) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, "Delivery detail not found");
		}
		FxDeliveryDetailDto dto = createFxDeliveryDetailDto(deliveryDetailModel);
		dto.setOtpTokenPrefix(deliveryDetailModel.getOtpTokenPrefix());
		return dto;
	}

	/**
	 * Marks fc sale order's status as delivered
	 * 
	 * @param fcSaleDeliveryMarkDeliveredRequest
	 * @return
	 */
	@Transactional
	public BoolRespModel markDelivered(FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		logger.debug("markDelivered request: {}", fcSaleDeliveryMarkDeliveredRequest);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
		VwFxDeliveryDetailsModel vwdeliveryDetail = validatetDeliveryDetailView(
				fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
		String oldStatus = deliveryDetail.getOrderStatus();
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD)) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS, "Order status should be OFD");
		}
		if (StringUtils.isBlank(deliveryDetail.getTransactionReceipt())) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_ORDER_RECIEPT_NOT_FOUND,
					"Order receipt not uploaded ");
		}
		validateOtpStatus(deliveryDetail);
		deliveryDetail.setOrderStatus(ConstantDocument.DVD);
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		fcSaleBranchOrderManager.currentStockNullify(deliveryDetail.getDeleviryDelSeqId(),
				deliveryDetail.getDriverEmployeeId());
		fcSaleBranchOrderManager.saveFCStockTransferDetails(deliveryDetail.getDeleviryDelSeqId(),null,deliveryDetail.getDriverEmployeeId(), ConstantDocument.DVD);
		PersonInfo pinfo = userService.getPersonInfo(vwdeliveryDetail.getCustomerId());
		logger.info("FC_ORDER_SUCCESSStart: {emial sending}");
		FxDeliveryDetailDto ddDto = createFxDeliveryDetailDto(vwdeliveryDetail);
		FxDeliveryDetailNotificationDto notificationModel = new FxDeliveryDetailNotificationDto(ddDto);
		notificationModel.setTranxId(fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
		notificationModel.setVerCode(JaxClientUtil.getTransactionVeryCode(fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId()).output());
		Customer customer = custDao.getActiveCustomerDetailsByCustomerId(vwdeliveryDetail.getCustomerId());
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(CommunicationEvents.FC_ORDER_SUCCESS, customer);
		if(communicationPrefsResult.isEmail()) {
			Email email = new Email();
			email.setSubject("FC Order Successfully Delivered");
			email.addTo(pinfo.getEmail());
			logger.info("FC_ORDER_SUCCESS: {emial sending}");
			email.setITemplate(TemplatesMX.FC_ORDER_SUCCESS);
			logger.info("FC_ORDER_SUCCESS: {emial sent}");
			email.setHtml(true);
			/*email.getModel().put("tranxId", fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
			email.getModel().put("verCode", JaxClientUtil.getTransactionVeryCode(fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId()).output());*/
			
			email.getModel().put(NotificationConstants.RESP_DATA_KEY, notificationModel);
			jaxNotificationService.sendEmail(email);
		}
		if(communicationPrefsResult.isPushNotify()) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.FC_ORDER_SUCCESS);
			pushMessage.addToUser(vwdeliveryDetail.getCustomerId());
			logger.info("customer_id:"+vwdeliveryDetail.getCustomerId());
			pushMessage.getModel().put(RESP_DATA_KEY, notificationModel);
			pushNotifyClient.send(pushMessage);
		}
		if(communicationPrefsResult.isSms()) {
			SMS sms =new SMS();
			sms.setITemplate(TemplatesMX.FC_ORDER_SUCCESS);
			sms.addTo(customer.getPrefixCodeMobile()+customer.getMobile());
			sms.getModel().put(RESP_DATA_KEY, notificationModel);
			postManService.sendSMSAsync(sms);
		}
		if(communicationPrefsResult.isWhatsApp()) {
			
			WAMessage waMessage = new WAMessage();
			waMessage.setITemplate(TemplatesMX.FC_ORDER_SUCCESS);
			waMessage.addTo(customer.getWhatsappPrefix()+customer.getWhatsapp());
			waMessage.getModel().put(RESP_DATA_KEY, notificationModel);
			whatsAppClient.send(waMessage);
		}
		
		logStatusChangeAuditEvent(fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId(), oldStatus);
		return new BoolRespModel(true);
	}

	private void validateOtpStatus(FxDeliveryDetailsModel deliveryDetail) {
		if (!ConstantDocument.Yes.equalsIgnoreCase(deliveryDetail.getOtpValidated())) {
			throw new GlobalException(JaxError.OTP_NOT_VALIDATED, "Order otp not validated");
		}
	}

	public BoolRespModel markCancelled(FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest) {
		logger.info("Cancel request received: {}", fcSaleDeliveryMarkNotDeliveredRequest);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkNotDeliveredRequest.getDeliveryDetailSeqId());
		String oldStatus = deliveryDetail.getOrderStatus();
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD)) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS, "Order status should be OFD");
		}
		deliveryDetail.setOrderStatus(ConstantDocument.CND_ACK);
		deliveryDetail.setRemarksId(fcSaleDeliveryMarkNotDeliveredRequest.getDeleviryRemarkSeqId());
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);

		logStatusChangeAuditEvent(fcSaleDeliveryMarkNotDeliveredRequest.getDeliveryDetailSeqId(), oldStatus);
		return new BoolRespModel(true);
	}

	private FxDeliveryDetailsModel validateFxDeliveryModel(BigDecimal deliveryDetailSeqId) {
		return validateFxDeliveryModel(deliveryDetailSeqId, true);
	}

	private FxDeliveryDetailsModel validateFxDeliveryModel(BigDecimal deliveryDetailSeqId, boolean validateEmployee) {
		FxDeliveryDetailsModel deliveryDetail = fcSaleApplicationDao.getDeliveryDetailModel(deliveryDetailSeqId);
		if (deliveryDetail == null) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, "Delivery detail not found");
		}
		if (validateEmployee) {
			if (!deliveryDetail.getDriverEmployeeId().equals(metaData.getEmployeeId())) {
				throw new GlobalException(JaxError.INVALID_EMPLOYEE, "Invalid driver employee for this order");
			}
		}
		return deliveryDetail;
	}

	private VwFxDeliveryDetailsModel validatetDeliveryDetailView(BigDecimal deliveryDetailSeqId) {
		VwFxDeliveryDetailsModel deliveryDetailModel = fcSaleApplicationDao.getDeliveryDetail(deliveryDetailSeqId);
		if (deliveryDetailModel == null) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND, "Delivery detail not found");
		}
		return deliveryDetailModel;
	}

	public BoolRespModel updateTransactionReceipt(
			FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest) {
		logger.debug("updateTransactionReceipt request: {}", fcSaleDeliveryDetailUpdateReceiptRequest);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryDetailUpdateReceiptRequest.getDeliveryDetailSeqId());
		deliveryDetail.setTransactionReceipt(fcSaleDeliveryDetailUpdateReceiptRequest.getTransactionRecieptImageClob());
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		return new BoolRespModel(true);
	}

	/**
	 * sends the otp for fcdelivery
	 * 
	 * @param deliveryDetailSeqId
	 * @return
	 * 
	 */
	public OtpPrefixDto sendOtp(BigDecimal deliveryDetailSeqId, boolean validateDriverEmployee) {
		logger.debug("sendOtp request: deliveryDetailSeqId {} validateDriverEmployee {}", deliveryDetailSeqId,
				validateDriverEmployee);
		VwFxDeliveryDetailsModel vwFxDeliveryDetailsModel = validatetDeliveryDetailView(deliveryDetailSeqId);
		userService.validateCustomerContactForSendOtp(Arrays.asList(ContactType.MOBILE), vwFxDeliveryDetailsModel.getCustomerId());
		FxDeliveryDetailsModel fxDeliveryDetailsModel = validateFxDeliveryModel(deliveryDetailSeqId,
				validateDriverEmployee);
		PersonInfo pinfo = userService.getPersonInfo(vwFxDeliveryDetailsModel.getCustomerId());
		// generating otp
		String mOtp = Random.randomNumeric(6);
		String mOtpPrefix = Random.randomAlpha(3);
		String hashedmOtp = cryptoUtil.generateHash(deliveryDetailSeqId.toString(), mOtp);
		//String hashedCustOtp = cryptoUtil.encryptAES(deliveryDetailSeqId.toString(), mOtp);
		String hashedCustOtp = cryptoUtil.encryptCOTP(mOtp, deliveryDetailSeqId.toString());
		fxDeliveryDetailsModel.setOtpToken(hashedmOtp);
		fxDeliveryDetailsModel.setOtpTokenPrefix(mOtpPrefix);
		fxDeliveryDetailsModel.setOtpTokenCustomer(hashedCustOtp);
		fxDeliveryDetailsModel.setOtpValidated(ConstantDocument.No);
		fcSaleApplicationDao.saveDeliveryDetail(fxDeliveryDetailsModel);

		FxDeliveryDetailDto ddDto = createFxDeliveryDetailDto(vwFxDeliveryDetailsModel);
		FxDeliveryDetailNotificationDto notificationModel = new FxDeliveryDetailNotificationDto(mOtp, mOtpPrefix,
				ddDto);
		logger.debug("sending otp for fcsale delivery");
		List<ContactType> contactTypes = new ArrayList<ContactType>();
		
		contactTypes.add(ContactType.SMS_EMAIL);
		logger.debug("Comm preferences flow for fx order dispatch ");
		//communicationPreferencesManager.validateCommunicationPreferences(contactTypes,CommunicationEvents.FX_ORDER_OTP,pinfo.getIdentityInt());
		logger.debug("Comm preferences not checked ");
		jaxNotificationService.sendOtpSms(pinfo.getMobile(), notificationModel);
		// send email otp
		Email email = new Email();
		email.setSubject("FC Delivery OTP");
		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.FC_DELIVER_EMAIL_OTP);
		email.setHtml(true);

		email.getModel().put(NotificationConstants.RESP_DATA_KEY, notificationModel);
		jaxNotificationService.sendEmail(email);
		return new OtpPrefixDto(mOtpPrefix);
	}

	public BoolRespModel verifyOtp(BigDecimal deliveryDetailSeqId, String mOtp) {
		logger.debug("verifyOtp request: deliveryDetailSeqId {} mOtp {}", deliveryDetailSeqId, mOtp);
		FxDeliveryDetailsModel fxDeliveryDetailsModel = validateFxDeliveryModel(deliveryDetailSeqId);
		if (mOtp == null) {
			throw new GlobalException(JaxError.MISSING_OTP, "mOtp can not be blank");
		}
		// validating otp
		String hashedmOtp = cryptoUtil.generateHash(deliveryDetailSeqId.toString(), mOtp.toString());
		String dbHashedmOtpToken = fxDeliveryDetailsModel.getOtpToken();
		if (!hashedmOtp.equals(dbHashedmOtpToken)) {
			throw new GlobalException(JaxError.INVALID_OTP, "mOtp is not valid");
		}
		
		// validating customerOtp
		String hashedCustOtp = cryptoUtil.encryptCOTP(mOtp.toString(), deliveryDetailSeqId.toString());
		String dbHashedcustOtpToken = fxDeliveryDetailsModel.getOtpTokenCustomer();
		if (!hashedCustOtp.equals(dbHashedcustOtpToken)) {
			throw new GlobalException(JaxError.INVALID_OTP, "Customer Otp is not valid");
		}
		
		fxDeliveryDetailsModel.setOtpValidated(ConstantDocument.Yes);
		fcSaleApplicationDao.saveDeliveryDetail(fxDeliveryDetailsModel);
		
		// ------ Contact Verified ------
		updateContactVerifyFx(deliveryDetailSeqId, mOtp);
		
		return new BoolRespModel(true);
	}

	private void updateContactVerifyFx(BigDecimal deliveryDetailSeqId, String mOtp) {
		VwFxDeliveryDetailsModel deliveryDetailModel = validatetDeliveryDetailView(deliveryDetailSeqId);
		BigDecimal custId = deliveryDetailModel.getCustomerId();
		Customer customer = custDao.getCustById(custId);
		if(customer != null) {
			userContactVerificationManager.setContactVerified(customer, mOtp, null, null);
		}
	}

	public List<ResourceDTO> listDeliveryRemark() {
		List<FxDeliveryRemark> delRemarks = fcSaleApplicationDao.listDeliveryRemark();
		return delRemarks.stream().map(remark -> {
			return new ResourceDTO(remark.getDeleviryRemarkSeqId(), remark.getDeliveryRemark());
		}).collect(Collectors.toList());
	}

	/**
	 * @param deliveryDetailSeqId
	 * @return
	 */
	public BoolRespModel markReturn(BigDecimal deliveryDetailSeqId) {
		logger.debug("markReturn request: deldetailid {}", deliveryDetailSeqId);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(deliveryDetailSeqId);
		String oldOrderStatus = deliveryDetail.getOrderStatus();
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD)) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS, "Order status should be OFD");
		}
		deliveryDetail.setOrderStatus(ConstantDocument.RTD_ACK);
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		logStatusChangeAuditEvent(deliveryDetailSeqId, oldOrderStatus);
		return new BoolRespModel(true);
	}

	/**
	 * @param deliveryDetailSeqId
	 * @return
	 */
	@Transactional
	public BoolRespModel markAcknowledged(BigDecimal deliveryDetailSeqId) {
		logger.debug("markAcknowledged request: deldetailid {}", deliveryDetailSeqId);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(deliveryDetailSeqId);
		String oldOrderStatus = deliveryDetail.getOrderStatus();
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD_ACK)) {
			throw new GlobalException(JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS, "Order status should be OFD_ACK");
		}
		boolean stockCheck = fcSaleBranchOrderManager.validateUserStock(deliveryDetailSeqId, deliveryDetail.getEmployeeId());
		if(stockCheck) {
			deliveryDetail.setOrderStatus(ConstantDocument.OFD_CNF);
			fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
			fcSaleBranchOrderManager.currentStockMigration(deliveryDetailSeqId, deliveryDetail.getDriverEmployeeId(),
					deliveryDetail.getEmployeeId());
			fcSaleBranchOrderManager.saveFCStockTransferDetails(deliveryDetailSeqId, deliveryDetail.getDriverEmployeeId(), deliveryDetail.getEmployeeId(), ConstantDocument.OFD_CNF);
		}else {
			throw new GlobalException(JaxError.MISMATCH_CURRENT_STOCK,"Employee current stock not matcing to move to driver");
		}
		logStatusChangeAuditEvent(deliveryDetailSeqId, oldOrderStatus);
		return new BoolRespModel(true);
	}

	private void logStatusChangeAuditEvent(BigDecimal deliveryDetailSeqId, String oldOrderStatus) {
		fcSaleEventManager.logStatusChangeAuditEvent(deliveryDetailSeqId, oldOrderStatus);
	}

	public List<FxDeliveryDetailDto> listHistoricalOrders() {
		if (metaData.getEmployeeId() == null) {
			throw new GlobalException("Missing driver id");
		}
		List<FxDeliveryDetailDto> results = new ArrayList<>();
		Integer noOfDays = jaxConfigService.getIntegerConfigValue(JaxDbConfig.FX_DELIVERY_HISTORICAL_LIST_RANGE_DAYS);
		List<VwFxDeliveryDetailsModel> deliveryDetails = fcSaleApplicationDao.listHistoricalOrders(metaData.getEmployeeId(), noOfDays);
		for (VwFxDeliveryDetailsModel model : deliveryDetails) {
			results.add(createFxDeliveryDetailDto(model));
		}
		return results;
	}
}
