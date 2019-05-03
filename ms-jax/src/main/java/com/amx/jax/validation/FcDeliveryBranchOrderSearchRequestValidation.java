package com.amx.jax.validation;

import org.springframework.beans.factory.annotation.Autowired;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.model.request.fx.FcDeliveryBranchOrderSearchRequest;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.FcSaleBranchService;
import com.amx.jax.userservice.service.UserValidationService;
import org.springframework.stereotype.Component;

@Component
public class FcDeliveryBranchOrderSearchRequestValidation {

	@Autowired
	UserValidationService userValidationService;

	@Autowired
	MetaService metaService;

	@Autowired
	FcSaleBranchService fcSaleBranchService;

	public void validatingOrderStatus(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		if (fcDeliveryBranchOrderSearchRequest.getOrderStatus() != null) {
			metaService.validateOrderStatus(fcDeliveryBranchOrderSearchRequest);
		}
	}

	public void validatingCivilId(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		if (fcDeliveryBranchOrderSearchRequest.getCivilId() != null) {
			userValidationService.validateIdentityInt(fcDeliveryBranchOrderSearchRequest.getCivilId(),
					ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID);
		}
	}

	public void validatingCountryBranchId(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		if (fcDeliveryBranchOrderSearchRequest.getCountryBranchId() != null) {
			metaService.validateCountryBranchId(fcDeliveryBranchOrderSearchRequest);
		}
	}

	public void validatingOrderId(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		if (fcDeliveryBranchOrderSearchRequest.getOrderId() != null) {
			
			fcSaleBranchService.validateOrderId(fcDeliveryBranchOrderSearchRequest);
		}
	}

	public void validatingAll(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		if (fcDeliveryBranchOrderSearchRequest.getOrderStatus() == null
				&& (fcDeliveryBranchOrderSearchRequest.getCivilId() == null
						&& fcDeliveryBranchOrderSearchRequest.getCountryBranchId() == null
						&& fcDeliveryBranchOrderSearchRequest.getOrderId() == null))
			throw new GlobalException(
					"Orderstatus and CivilId details or CountryBranchId Or OrderId should be entered");
	}

	public void validatingOrderStatusAndAll(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		if (fcDeliveryBranchOrderSearchRequest.getOrderStatus() != null) {
			if (fcDeliveryBranchOrderSearchRequest.getCivilId() == null
					&& fcDeliveryBranchOrderSearchRequest.getCountryBranchId() == null
					&& fcDeliveryBranchOrderSearchRequest.getOrderId() == null) {
				throw new GlobalException(
						"Including Orderstatus we should enter Either CivilId or CountryBranchId Or OrderId should be entered");
			}
		}else{
			if(fcDeliveryBranchOrderSearchRequest.getCivilId() != null
					|| fcDeliveryBranchOrderSearchRequest.getCountryBranchId() != null
					|| fcDeliveryBranchOrderSearchRequest.getOrderId() != null) {
				throw new GlobalException(
						"We should enter Order Status along with CivilId or CountryBranchId or OrderId.");
			}
		}
	
	}

	public void validatingAllValues(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		validatingOrderStatus(fcDeliveryBranchOrderSearchRequest);
		validatingCivilId(fcDeliveryBranchOrderSearchRequest);
		validatingOrderId(fcDeliveryBranchOrderSearchRequest);
		validatingCountryBranchId(fcDeliveryBranchOrderSearchRequest);
	}

}
