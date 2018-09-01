package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.PLACE_ORDER_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.PlaceOrderNotificationDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.rest.RestService;

@Component
public class PlaceOrderClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	RestService restService;

	public ApiResponse<PlaceOrderDTO> savePlaceOrder(PlaceOrderDTO placeOrderDTO) {
		try {
			HttpEntity<PlaceOrderDTO> requestEntity = new HttpEntity<PlaceOrderDTO>(placeOrderDTO, getHeader());
			String url = this.getBaseUrl() + PLACE_ORDER_ENDPOINT + "/save";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
	}

	public ApiResponse<PlaceOrderDTO> getPlaceOrderForCustomer() {
		try {
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + PLACE_ORDER_ENDPOINT + "/get/placeOrder/forCustomer";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
	}

	public ApiResponse<PlaceOrderDTO> getAllPlaceOrder() {
		try {
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + PLACE_ORDER_ENDPOINT + "/getAll/placeOrder";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
	}

	public ApiResponse<PlaceOrderDTO> deletePlaceOrder(PlaceOrderDTO placeOrderDTO) {
		ResponseEntity<ApiResponse<PlaceOrderDTO>> response = null;
		try {
			if (placeOrderDTO.getPlaceOrderId() != null) {
				HttpEntity<PlaceOrderDTO> requestEntity = new HttpEntity<PlaceOrderDTO>(placeOrderDTO, getHeader());
				String url = this.getBaseUrl() + PLACE_ORDER_ENDPOINT + "/delete";
				return restService.ajax(url).post(requestEntity)
						.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderDTO>>() {
						});
			} else {
				throw new ValidationException("PlaceOrder ID not provided.");
			}
		} catch (ValidationException ve) {
			log.error("RateAlert ID is null.", ve);
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
		return response.getBody();
	}

	public ApiResponse<PlaceOrderDTO> updatePlaceOrder(PlaceOrderDTO placeOrderDTO) {
		ResponseEntity<ApiResponse<PlaceOrderDTO>> response = null;
		try {
			if (placeOrderDTO.getPlaceOrderId() != null) {
				HttpEntity<PlaceOrderDTO> requestEntity = new HttpEntity<PlaceOrderDTO>(placeOrderDTO, getHeader());
				String url = this.getBaseUrl() + PLACE_ORDER_ENDPOINT + "/update";
				return restService.ajax(url).post(requestEntity)
						.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderDTO>>() {
						});
			} else {
				throw new ValidationException("PlaceOrder ID not provided.");
			}
		} catch (ValidationException ve) {
			log.error("RateAlert ID is null.", ve);
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
		return response.getBody();
	}
	
	public ApiResponse<PlaceOrderNotificationDTO> getPlaceOrderOnTrigger(BigDecimal pipsMasterId) throws ResourceNotFoundException, InvalidInputException {
		try {
			String endpoint = PLACE_ORDER_ENDPOINT+"/get/placeorder/ontrigger";
			StringBuilder sb = new StringBuilder();
			sb.append("?").append("pipsMasterId=").append(pipsMasterId);
			String getPlaceOrderRateUrl = this.getBaseUrl() + endpoint + sb.toString();
			HttpEntity<PlaceOrderNotificationDTO> requestEntity = new HttpEntity<PlaceOrderNotificationDTO>(getHeader());
			log.info("calling Place Order api: " + getPlaceOrderRateUrl);
			return restService.ajax(getPlaceOrderRateUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderNotificationDTO>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			log.error("exception in place Order details :", e);
			throw new JaxSystemError();
		} 
	}
}
