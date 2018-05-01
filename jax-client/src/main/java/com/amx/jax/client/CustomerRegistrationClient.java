package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_REG_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.SendOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;

/**
 * @author Prashant
 *
 */
@Component
public class CustomerRegistrationClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(CustomerRegistrationClient.class);

	/**
	 * @param personalDetail
	 *            - Person detail object
	 * @return SendOtpModel - contains otp prefix for both e & m otp
	 */
	public ApiResponse<SendOtpModel> sendOtp(CustomerPersonalDetail personalDetail) {
		try {

			ResponseEntity<ApiResponse<SendOtpModel>> response;
			LOGGER.info("calling sendOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(personalDetail, getHeader());
			String url = this.getBaseUrl() + CUSTOMER_REG_ENDPOINT + "/send-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<SendOtpModel>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtp : ", e);
			throw new JaxSystemError();
		}
	}

	/**
	 * @param mOtp
	 *            mobile otp
	 * @param eOtp
	 *            email otp
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> validateOtp(String mOtp, String eOtp) {
		try {

			ResponseEntity<ApiResponse<BooleanResponse>> response;
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + CUSTOMER_REG_ENDPOINT + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		}
	}

	/**
	 * @param customerHomeAddress
	 *            customer home address model
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> saveHomeAddress(CustomerHomeAddress customerHomeAddress) {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response;
			LOGGER.info("calling saveHomeAddress api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(customerHomeAddress, getHeader());
			String url = this.getBaseUrl() + CUSTOMER_REG_ENDPOINT + "/save-home-addr/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveHomeAddress : ", e);
			throw new JaxSystemError();
		}
	}

}
