package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.RATE_ALERT_ENDPOINT;

import javax.validation.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.rest.RestService;

@Component
public class RateAlertClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	RestService restService;

	public ApiResponse<RateAlertDTO> saveRateAlert(RateAlertDTO rateAlertDTO) {
		try {
			HttpEntity<RateAlertDTO> requestEntity = new HttpEntity<RateAlertDTO>(rateAlertDTO, getHeader());
			String url = this.getBaseUrl() + RATE_ALERT_ENDPOINT + "/save";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<RateAlertDTO> deleteRateAlert(RateAlertDTO rateAlertDTO) {
		ResponseEntity<ApiResponse<RateAlertDTO>> response = null;
		try {

			if (rateAlertDTO.getRateAlertId() != null) {
				HttpEntity<RateAlertDTO> requestEntity = new HttpEntity<RateAlertDTO>(rateAlertDTO, getHeader());
				String url = this.getBaseUrl() + RATE_ALERT_ENDPOINT + "/delete";
				return restService.ajax(url).post(requestEntity)
						.as(new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
						});
			} else {
				throw new ValidationException("RateAlert ID not provided.");
			}
		} catch (ValidationException ve) {
			log.error("RateAlert ID is null.", ve);
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
		return response.getBody();
	}

	public ApiResponse<RateAlertDTO> getRateAlertForCustomer() {
		
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + RATE_ALERT_ENDPOINT + "/get/for/customer";
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});
		
	}

	public ApiResponse<RateAlertDTO> getAllRateAlert() {
		try {
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + RATE_ALERT_ENDPOINT + "/getAll";
			log.info("calling " + url + " for getAllRateAlert");
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

}
