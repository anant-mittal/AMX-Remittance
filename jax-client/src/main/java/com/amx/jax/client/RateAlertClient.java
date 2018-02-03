package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.RATE_ALERT_ENDPOINT;

import javax.validation.ValidationException;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.response.ApiResponse;

@Component
public class RateAlertClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());

	public ApiResponse<RateAlertDTO> saveRateAlert(RateAlertDTO rateAlertDTO) {
		try {
			ResponseEntity<ApiResponse<RateAlertDTO>> response = null;
			HttpEntity<RateAlertDTO> requestEntity = new HttpEntity<RateAlertDTO>(rateAlertDTO, getHeader());
			String url = this.getBaseUrl() + RATE_ALERT_ENDPOINT + "/save";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
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
				response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
						new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
						});
			} else {
				throw new ValidationException("RateAlert ID not provided.");
			}
		} catch (ValidationException ve) {
			log.error("RateAlert ID is null.", ve);
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch

		return response.getBody();
	}

	public ApiResponse<RateAlertDTO> getRateAlertForCustomer() {
		try {
			ResponseEntity<ApiResponse<RateAlertDTO>> response = null;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + RATE_ALERT_ENDPOINT + "/get/for/customer";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<RateAlertDTO> getAllRateAlert() {
		try {
			ResponseEntity<ApiResponse<RateAlertDTO>> response = null;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + RATE_ALERT_ENDPOINT + "/getAll";
			log.info("calling " + url + " for getAllRateAlert");
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<RateAlertDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

}
