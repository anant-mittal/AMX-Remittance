package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.FxDeliveryStatus;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryRemark;
import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.repository.fx.VwFxDeliveryDetailsRepository;
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
		String statusDesc = fcSaleApplicationDao.getStatusMaster(model.getOrderStatus()).getStatusDescription();
		ShippingAddressDto shippingAddressDto = createShippingAddressDto(shippingAddress);
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
		dto.setOrderStatus(statusDesc);
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
		return createFxDeliveryDetailDto(deliveryDetailModel);
	}

	public BoolRespModel markDelivered(FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
		deliveryDetail.setOrderStatus(ConstantDocument.DVD);
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		// TODO: updated table ex_appl_receipt_payment and column ORDER_STATUS
		return new BoolRespModel(true);
	}

	public BoolRespModel markNotDelivered(FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest) {
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkNotDeliveredRequest.getDeliveryDetailSeqId());
		deliveryDetail.setOrderStatus(ConstantDocument.RTD);
		deliveryDetail.setRemarksId(fcSaleDeliveryMarkNotDeliveredRequest.getDeleviryRemarkSeqId());
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		return new BoolRespModel(true);
	}

	private FxDeliveryDetailsModel validateFxDeliveryModel(BigDecimal deliveryDetailSeqId) {
		FxDeliveryDetailsModel deliveryDetail = fcSaleApplicationDao.getDeliveryDetailModel(deliveryDetailSeqId);
		if (deliveryDetail == null) {
			throw new GlobalException("Delivery detail not found", JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND);
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
		VwFxDeliveryDetailsModel vwFxDeliveryDetailsMode = validatetDeliveryDetailView(deliveryDetailSeqId);
		FxDeliveryDetailsModel fxDeliveryDetailsMode = validateFxDeliveryModel(deliveryDetailSeqId);
		PersonInfo pinfo = new PersonInfo();
		pinfo.setFirstName(vwFxDeliveryDetailsMode.getCustomerName());
		pinfo.setMobile(vwFxDeliveryDetailsMode.getMobile());
		CivilIdOtpModel model = new CivilIdOtpModel();
		// generating otp
		String mOtp = Random.randomNumeric(6);
		String hashedmOtp = cryptoUtil.generateHash(deliveryDetailSeqId.toString(), mOtp);
		fxDeliveryDetailsMode.setOtpToken(hashedmOtp);
		fcSaleApplicationDao.saveDeliveryDetail(fxDeliveryDetailsMode);
		model.setmOtp(mOtp);
		model.setmOtpPrefix(Random.randomAlpha(3));
		logger.debug("sending otp for fcsale delivery");
		jaxNotificationService.sendOtpSms(pinfo, model);
		return new BoolRespModel(true);
	}

	public BoolRespModel verifyOtp(BigDecimal deliveryDetailSeqId, BigDecimal mOtp) {
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
}
