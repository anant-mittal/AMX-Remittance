package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constants.FxDeliveryStatus;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryRemark;
import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.FcSaleDeliveryDetailUpdateReceiptRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkDeliveredRequest;
import com.amx.jax.model.request.fx.FcSaleDeliveryMarkNotDeliveredRequest;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.repository.fx.VwFxDeliveryDetailsRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FcSaleDeliveryService {

	Logger logger = LoggerFactory.getLogger(FcSaleService.class);

	@Autowired
	MetaData metaData;
	@Autowired
	FcSaleApplicationDao fcSaleApplicationDao;

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
		ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
		try {
			BeanUtils.copyProperties(dto, model);
			BeanUtils.copyProperties(shippingAddressDto, shippingAddress);
		} catch (Exception e) {
		}
		dto.setTransactionRefId(model.getCollectionDocFinYear() + "/" + model.getCollectionDocNo());
		dto.setDeliveryRemark(delRemark.getDeliveryRemark());
		dto.setAddress(shippingAddressDto);
		return dto;
	}

	public FxDeliveryDetailDto getDeliveryDetail(BigDecimal deliveryDetailSeqId) {
		if (deliveryDetailSeqId == null) {
			throw new GlobalException("deliveryDetailSeqId can't be blank");
		}
		VwFxDeliveryDetailsModel deliveryDetailModel = fcSaleApplicationDao.getDeliveryDetail(deliveryDetailSeqId);
		if(deliveryDetailModel == null) {
			throw new GlobalException("Delivery detail not found", JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND);
		}
		return createFxDeliveryDetailDto(deliveryDetailModel);
	}

	public BoolRespModel markDelivered(FcSaleDeliveryMarkDeliveredRequest fcSaleDeliveryMarkDeliveredRequest) {
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkDeliveredRequest.getDeliveryDetailSeqId());
		deliveryDetail.setDeliveryStatus(FxDeliveryStatus.DELIVERED);
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		// TODO: updated table ex_appl_receipt_payment and column ORDER_STATUS
		return new BoolRespModel(true);
	}

	public BoolRespModel markNotDelivered(FcSaleDeliveryMarkNotDeliveredRequest fcSaleDeliveryMarkNotDeliveredRequest) {
		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryMarkNotDeliveredRequest.getDeliveryDetailSeqId());
		deliveryDetail.setDeliveryStatus(FxDeliveryStatus.NOT_DELIVERED);
		deliveryDetail.setRemarksId(fcSaleDeliveryMarkNotDeliveredRequest.getDeleviryRemarkSeqId());
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		// TODO: updated table ex_appl_receipt_payment and column ORDER_STATUS
		return new BoolRespModel(true);
	}

	private FxDeliveryDetailsModel validateFxDeliveryModel(BigDecimal deliveryDetailSeqId) {
		FxDeliveryDetailsModel deliveryDetail = fcSaleApplicationDao.getDeliveryDetailModel(deliveryDetailSeqId);
		if (deliveryDetail == null) {
			throw new GlobalException("Delivery detail not found", JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND);
		}
		return deliveryDetail;
	}

	public BoolRespModel updateTransactionReceipt(
			FcSaleDeliveryDetailUpdateReceiptRequest fcSaleDeliveryDetailUpdateReceiptRequest) {

		FxDeliveryDetailsModel deliveryDetail = validateFxDeliveryModel(
				fcSaleDeliveryDetailUpdateReceiptRequest.getDeliveryDetailSeqId());
		deliveryDetail.setTransactionReceipt(fcSaleDeliveryDetailUpdateReceiptRequest.getTransactionRecieptImageClob());
		fcSaleApplicationDao.saveDeliveryDetail(deliveryDetail);
		return new BoolRespModel(true);
	}
}
