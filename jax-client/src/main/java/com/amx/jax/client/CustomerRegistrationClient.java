package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_REG_ENDPOINT;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.CustomerCredential;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.rest.RestService;

/**
 * @author Prashant
 *
 */
@Component
public class CustomerRegistrationClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(CustomerRegistrationClient.class);

	@Autowired
	private RestService restService;

	/**
	 * @param personalDetail - Person detail object
	 * @return SendOtpModel - contains otp prefix for both e & m otp
	 */
	public ApiResponse<SendOtpModel> sendOtp(CustomerPersonalDetail personalDetail) {
		try {
			LOGGER.info("calling sendOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(personalDetail, getHeader());
			String url = this.getBaseUrl() + CUSTOMER_REG_ENDPOINT + "/send-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<SendOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtp : ", e);
			throw new JaxSystemError();
		}
	}

	/**
	 * @param mOtp mobile otp
	 * @param eOtp email otp
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> validateOtp(String mOtp, String eOtp) {
		try {
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + CUSTOMER_REG_ENDPOINT + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError(e);
		}
	}

	/**
	 * @param customerHomeAddress customer home address model
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> saveHomeAddress(CustomerHomeAddress customerHomeAddress) {
		try {
			LOGGER.info("calling saveHomeAddress api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(customerHomeAddress, getHeader());
			String url = this.getBaseUrl() + CUSTOMER_REG_ENDPOINT + "/save-home-addr/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveHomeAddress : ", e);
			throw new JaxSystemError();
		}
	}

	/**
	 * @param securityquestions - list of security question and answers
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> saveSecurityQuestions(List<SecurityQuestionModel> securityquestions) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CUSTOMER_REG_ENDPOINT + "/save-security-questions/").post(securityquestions)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveSecurityQuestions : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	/**
	 * @param caption  - Caption as string
	 * @param imageUrl - url of image as string
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> savePhishiingImage(String caption, String imageUrl) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(CUSTOMER_REG_ENDPOINT + "/save-phishing-image/")
					.queryParam("caption", caption).queryParam("imageUrl", imageUrl).meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in savePhishiingImage : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	/**
	 * @param loginId  - login id
	 * @param password - password
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> saveLoginDetail(CustomerCredential customerCredential) {
		return saveLoginDetail(customerCredential, Boolean.FALSE);
	}

	/**
	 * @param loginId  - login id
	 * @param password - password
	 * @return BooleanResponse - return success or failure
	 */
	public ApiResponse<BooleanResponse> saveLoginDetail(CustomerCredential customerCredential, Boolean isPartialReg) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CUSTOMER_REG_ENDPOINT + "/save-login-detail/").queryParam("isPartialReg", isPartialReg)
					.post(customerCredential).as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveLoginDetail : ", e);
			return JaxSystemError.evaluate(e);
		}
	}
}
