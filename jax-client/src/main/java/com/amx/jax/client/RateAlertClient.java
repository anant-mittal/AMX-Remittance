package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.RATE_ALERT_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;

@Component
public class RateAlertClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	public ApiResponse<RateAlertDTO> saveRateAlert(RateAlertDTO rateAlertDTO) {
		ResponseEntity<ApiResponse<RateAlertDTO>> response = null;
		try {

			HttpEntity<RateAlertDTO> requestEntity = new HttpEntity<RateAlertDTO>(rateAlertDTO, getHeader());
			String url = baseUrl.toString() + RATE_ALERT_ENDPOINT + "/save";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception while saving rate alert ", e);
		}
		return response.getBody();
	}

	public ApiResponse<RateAlertDTO> deleteRateAlert(RateAlertDTO rateAlertDTO) {
		ResponseEntity<ApiResponse<RateAlertDTO>> response = null;
		try {
			
			if (rateAlertDTO.getRateAlertId() != null ) {
				HttpEntity<RateAlertDTO> requestEntity = new HttpEntity<RateAlertDTO>(rateAlertDTO, getHeader());
				String url = baseUrl.toString() + RATE_ALERT_ENDPOINT + "/delete";
				response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
						new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
						});
			}else {
				throw new ValidationException("RateAlert ID not provided.");
			}
		} catch (ValidationException ve) {
		    log.error("RateAlert ID is null.", ve);
		}catch (Exception e) {
			log.error("exception while deleting rate alert", e);
		}
		return response.getBody();
	}
	
	public ApiResponse<RateAlertDTO> getRateAlertForCustomer() {
		ResponseEntity<ApiResponse<RateAlertDTO>> response = null;
		try {
		
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = baseUrl.toString() + RATE_ALERT_ENDPOINT + "/get/for/customer";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});

		} catch (Exception e) {
			log.error("exception while fetching rate alert", e);
		}
		return response.getBody();
	}

}
