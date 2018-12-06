package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.NotificationConstants;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryRemark;
import com.amx.jax.dbmodel.fx.StatusMaster;
import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleAddressManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.FxDeliveryDetailNotificationDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.CryptoUtil;
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
		//ShippingAddressDto shippingAddressDto = createShippingAddressDto(shippingAddress);
		ShippingAddressDto shippingAddressDto = fcSaleAddressManager.fetchShippingAddress(model.getCustomerId(),
				model.getShippingAddressId());
		try {
			BeanUtils.copyProperties(dto, model);
			BeanUtils.copyProperties(shippingAddressDto, shippingAddress);
		} catch (Exception e) {
		}
		if (model.getCollectionDocFinYear() != null) {
			dto.setTransactionRefId(model.getCollectionDocFinYear() + "/" + model.getCollectionDocNo());
		}
		if (delRemark != null) {
			dto.setDeliveryRemark(delRemark.getDeliveryRemark());
		}
		dto.setAddress(shippingAddressDto);
		dto.setOrderStatus(statusMaster.getStatusDescription());
		dto.setOrderStatusCode(statusMaster.getStatusCode());
		dto.setDeliveryDetailSeqId(model.getDeleviryDelSeqId());
		return dto;
	}

	public FxDeliveryDetailDto getDeliveryDetail(BigDecimal deliveryDetailSeqId) {
		if (deliveryDetailSeqId == null) {
			throw new GlobalException("deliveryDetailSeqId can't be blank");
		}
		VwFxDeliveryDetailsModel deliveryDetailModel = fcSaleApplicationDao.getDeliveryDetail(deliveryDetailSeqId);
		if (deliveryDetailModel == null) {
			throw new GlobalException("Delivery detail not found", JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND);
		}
		String otpTokenPrefix = fcSaleApplicationDao.getDeliveryDetailModel(deliveryDetailSeqId).getOtpTokenPrefix();
		FxDeliveryDetailDto dto = createFxDeliveryDetailDto(deliveryDetailModel);
		dto.setOtpTokenPrefix(otpTokenPrefix);
		return dto;
	}

	public BoolRespModel markDelivered(FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		logger.debug("markDelivered request: {}", fcSaleDeliveryMarkDeliveredRequest);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
		VwFxDeliveryDetailsModel vwdeliveryDetail = validatetDeliveryDetailView(
				fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD)) {
			throw new GlobalException("Order status should be OFD", JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS);
		}
		deliveryDetail.setOrderStatus(ConstantDocument.DVD);
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		PersonInfo pinfo = userService.getPersonInfo(vwdeliveryDetail.getCustomerId());
		Email email = new Email();
		email.setSubject("FC Order Successfully Delivered");
		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.FC_ORDER_SUCCESS);
		email.setHtml(true);
		FxDeliveryDetailDto ddDto = createFxDeliveryDetailDto(vwdeliveryDetail);
		FxDeliveryDetailNotificationDto notificationModel = new FxDeliveryDetailNotificationDto(ddDto);
		email.getModel().put(NotificationConstants.RESP_DATA_KEY, notificationModel);
		jaxNotificationService.sendEmail(email);
		return new BoolRespModel(true);
	}

	public BoolRespModel markCancelled(FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest) {
		logger.info("Cancel request received: {}", fcSaleDeliveryMarkNotDeliveredRequest);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkNotDeliveredRequest.getDeliveryDetailSeqId());
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD)) {
			throw new GlobalException("Order status should be OFD", JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS);
		}
		deliveryDetail.setOrderStatus(ConstantDocument.CND);
		deliveryDetail.setRemarksId(fcSaleDeliveryMarkNotDeliveredRequest.getDeleviryRemarkSeqId());
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		return new BoolRespModel(true);
	}

	private FxDeliveryDetailsModel validateFxDeliveryModel(BigDecimal deliveryDetailSeqId) {
		FxDeliveryDetailsModel deliveryDetail = fcSaleApplicationDao.getDeliveryDetailModel(deliveryDetailSeqId);
		if (deliveryDetail == null) {
			throw new GlobalException("Delivery detail not found", JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND);
		}
		if (!deliveryDetail.getDriverEmployeeId().equals(metaData.getEmployeeId())) {
			throw new GlobalException("Invalid driver employee for this order", JaxError.INVALID_EMPLOYEE);
		}
		return deliveryDetail;
	}

	private VwFxDeliveryDetailsModel validatetDeliveryDetailView(BigDecimal deliveryDetailSeqId) {
		VwFxDeliveryDetailsModel deliveryDetailModel = fcSaleApplicationDao.getDeliveryDetail(deliveryDetailSeqId);
		if (deliveryDetailModel == null) {
			throw new GlobalException("Delivery detail not found", JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND);
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
	public BoolRespModel sendOtp(BigDecimal deliveryDetailSeqId) {
		VwFxDeliveryDetailsModel vwFxDeliveryDetailsModel = validatetDeliveryDetailView(deliveryDetailSeqId);
		FxDeliveryDetailsModel fxDeliveryDetailsModel = validateFxDeliveryModel(deliveryDetailSeqId);
		PersonInfo pinfo = userService.getPersonInfo(vwFxDeliveryDetailsModel.getCustomerId());
		// generating otp
		String mOtp = Random.randomNumeric(6);
		String mOtpPrefix = Random.randomAlpha(3);
		String hashedmOtp = cryptoUtil.generateHash(deliveryDetailSeqId.toString(), mOtp);
		fxDeliveryDetailsModel.setOtpToken(hashedmOtp);
		fxDeliveryDetailsModel.setOtpTokenPrefix(mOtpPrefix);
		fcSaleApplicationDao.saveDeliveryDetail(fxDeliveryDetailsModel);

		FxDeliveryDetailDto ddDto = createFxDeliveryDetailDto(vwFxDeliveryDetailsModel);
		FxDeliveryDetailNotificationDto notificationModel = new FxDeliveryDetailNotificationDto(mOtp, mOtpPrefix,
				ddDto);
		logger.debug("sending otp for fcsale delivery");
		jaxNotificationService.sendOtpSms(pinfo.getMobile(), notificationModel);
		// send email otp
		Email email = new Email();
		email.setSubject("FC Order Successfully Delivered");
		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.FC_DELIVER_EMAIL_OTP);
		email.setHtml(true);

		email.getModel().put(NotificationConstants.RESP_DATA_KEY, notificationModel);
		jaxNotificationService.sendEmail(email);
		return new BoolRespModel(true);
	}

	public BoolRespModel verifyOtp(BigDecimal deliveryDetailSeqId, BigDecimal mOtp) {
		logger.debug("verifyOtp request: deliveryDetailSeqId {} mOtp {}", deliveryDetailSeqId, mOtp);
		FxDeliveryDetailsModel fxDeliveryDetailsMode = validateFxDeliveryModel(deliveryDetailSeqId);
		if (mOtp == null) {
			throw new GlobalException("mOtp can not be blank", JaxError.MISSING_OTP);
		}
		// validating otp
		String hashedmOtp = cryptoUtil.generateHash(deliveryDetailSeqId.toString(), mOtp.toString());
		String dbHashedmOtpToken = fxDeliveryDetailsMode.getOtpToken();
		if (!hashedmOtp.equals(dbHashedmOtpToken)) {
			throw new GlobalException("mOtp is not valid", JaxError.INVALID_OTP);
		}
		return new BoolRespModel(true);
	}

	private ShippingAddressDto createShippingAddressDto(ShippingAddressDetail shippingAddressDetail) {
		ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
		if (CollectionUtils.isNotEmpty(shippingAddressDetail.getFsCityMaster().getFsCityMasterDescs())) {
			ResourceDTO cityDto = new ResourceDTO(shippingAddressDetail.getFsCityMaster().getCityId(),
					shippingAddressDetail.getFsCityMaster().getFsCityMasterDescs().get(0).getCityName());
			shippingAddressDto.setCityDto(cityDto);
		}
		if (CollectionUtils.isNotEmpty(shippingAddressDetail.getFsStateMaster().getFsStateMasterDescs())) {
			shippingAddressDto.setLocalContactState(
					shippingAddressDetail.getFsStateMaster().getFsStateMasterDescs().get(0).getStateName());
			ResourceDTO stateDto = new ResourceDTO(shippingAddressDetail.getFsStateMaster().getStateId(),
					shippingAddressDetail.getFsStateMaster().getFsStateMasterDescs().get(0).getStateName());
			shippingAddressDto.setStateDto(stateDto);

		}
		if (CollectionUtils.isNotEmpty(shippingAddressDetail.getFsDistrictMaster().getFsDistrictMasterDescs())) {
			ResourceDTO districtDto = new ResourceDTO(shippingAddressDetail.getFsDistrictMaster().getDistrictId(),
					shippingAddressDetail.getFsDistrictMaster().getFsDistrictMasterDescs().get(0).getDistrict());
			shippingAddressDto.setDistrictDto(districtDto);

		}
		if (CollectionUtils.isNotEmpty(shippingAddressDetail.getFsCountryMaster().getFsCountryMasterDescs())) {
			ResourceDTO districtDto = new ResourceDTO(shippingAddressDetail.getFsCountryMaster().getCountryId(),
					shippingAddressDetail.getFsCountryMaster().getFsCountryMasterDescs().get(0).getCountryName());
			shippingAddressDto.setCountryDto(districtDto);
		}
		try {
			BeanUtils.copyProperties(shippingAddressDto, shippingAddressDetail);
		} catch (Exception e) {
		}
		return shippingAddressDto;
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
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD)) {
			throw new GlobalException("Order status should be OFD", JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS);
		}
		deliveryDetail.setOrderStatus(ConstantDocument.RTD_ACK);
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		return new BoolRespModel(true);
	}

	/**
	 * @param deliveryDetailSeqId
	 * @return
	 */
	public BoolRespModel markAcknowledged(BigDecimal deliveryDetailSeqId) {
		logger.debug("markAcknowledged request: deldetailid {}", deliveryDetailSeqId);
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(deliveryDetailSeqId);
		if (!deliveryDetail.getOrderStatus().equals(ConstantDocument.OFD_ACK)) {
			throw new GlobalException("Order status should be OFD_ACK", JaxError.FC_CURRENCY_DELIVERY_INVALID_STATUS);
		}
		deliveryDetail.setOrderStatus(ConstantDocument.OFD_CNF);
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		return new BoolRespModel(true);
	}
}
