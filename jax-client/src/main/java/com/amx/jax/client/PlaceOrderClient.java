package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.PLACE_ORDER_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.rest.RestService;

@Component
public class PlaceOrderClient extends AbstractJaxServiceClient {
	
	private Logger log = Logger.getLogger(getClass());
	
	@Autowired
	RestService restService;
	
	public ApiResponse<PlaceOrderDTO> savePlaceOrder(PlaceOrderDTO placeOrderDTO){
		try {
			HttpEntity<PlaceOrderDTO> requestEntity = new HttpEntity<PlaceOrderDTO>(placeOrderDTO, getHeader());
			String url = this.getBaseUrl() + PLACE_ORDER_ENDPOINT + "/save";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		}
	}

	public ApiResponse<PlaceOrderDTO> getPlaceOrderForCustomer() {
		// TODO Auto-generated method stub
		try {
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + PLACE_ORDER_ENDPOINT + "/get/placeOrder/forCustomer";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<PlaceOrderDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractException) {
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
			// TODO: handle exception
		}
		return null;
	}


}
