package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.UPDATE_CUSTOMER_PASSWORD_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.USER_API_ENDPOINT;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;

@Component
public class UserClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	public ApiResponse<CustomerModel> validateOtp(String identityId, String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		if (StringUtils.isBlank(identityId) || "null".equalsIgnoreCase(identityId)) {
			return validateOtp(mOtp, eOtp);
		}
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		log.info("calling validateOtp api: ");
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		String validateOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + identityId + "/validate-otp/?otp=" + mOtp;
		response = restTemplate.exchange(validateOtpUrl, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
				});
		return response.getBody();
	}

	public ApiResponse<CustomerModel> validateOtp(String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		log.info("calling validateOtp api: ");
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		String validateOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/validate-otp/?otp=" + mOtp;
		response = restTemplate.exchange(validateOtpUrl, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
				});
		return response.getBody();
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		ResponseEntity<ApiResponse<CivilIdOtpModel>> response = null;
		HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
		String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-otp/";
		log.info("calling sendOtpForCivilId api: " + sendOtpUrl);
		response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
				});
		log.info("responce from  sendOtpForCivilId api: " + util.marshall(response.getBody()));

		return response.getBody();
	}

	public ApiResponse<CivilIdOtpModel> sendResetOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		ResponseEntity<ApiResponse<CivilIdOtpModel>> response = null;
		HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
		String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-reset-otp/";
		log.info("calling sendResetOtpForCivilId api: " + sendOtpUrl);
		response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
				});
		log.info("responce from  sendResetOtpForCivilId api: " + util.marshall(response.getBody()));

		return response.getBody();
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId()
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		ResponseEntity<ApiResponse<CivilIdOtpModel>> response = null;
		HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
		String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/send-otp/";
		log.info("calling sendOtpForCivilId api: " + sendOtpUrl);
		response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
				});
		log.info("responce from  sendOtpForCivilId api: " + util.marshall(response.getBody()));

		return response.getBody();
	}

	public ApiResponse<CustomerModel> saveCustomer(String json)
			throws CustomerValidationException, LimitExeededException {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, getHeader());
		String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
		log.info("calling saveCustomer api: " + sendOtpUrl);
		response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
				});
		log.info("responce from  saveCustomer api: " + util.marshall(response.getBody()));
		return response.getBody();
	}

	public ApiResponse<CustomerModel> saveSecurityQuestions(List<SecurityQuestionModel> securityquestions, String otp) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(otp);
			custModel.setSecurityquestions(securityquestions);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(custModel), getHeader());
			String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
			log.info("calling saveSecurityQuestions api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			log.info("responce from  saveSecurityQuestions api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in saveSecurityQuestions ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CustomerModel> savePhishiingImage(String caption, String imageUrl, String otp) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(otp);
			custModel.setCaption(caption);
			custModel.setImageUrl(imageUrl);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(custModel), getHeader());
			String saveCustUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
			log.info("calling savePhishiingImage api: " + saveCustUrl);
			response = restTemplate.exchange(saveCustUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			log.info("responce from  savePhishiingImage api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in savePhishiingImage ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CustomerModel> saveLoginIdAndPassword(String loginId, String password, String otp)
			throws AlreadyExistsException {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		CustomerModel custModel = new CustomerModel();
		custModel.setLoginId(loginId);
		custModel.setPassword(password);
		custModel.setMotp(otp);
		custModel.setCustomerId(jaxMetaInfo.getCustomerId());
		HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(custModel), getHeader());
		String saveCustUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
		log.info("calling saveLoginIdAndPassword api: " + saveCustUrl);
		response = restTemplate.exchange(saveCustUrl, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
				});
		log.info("responce from  saveLoginIdAndPassword api: " + util.marshall(response.getBody()));
		return response.getBody();
	}

	/**
	 * @param size:
	 *            specify how many questions you need
	 */
	public ApiResponse<CustomerModel> fetchRandomQuestoins(int size) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			String randQuestionstUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + customerId
					+ "/random-questions/?size=" + size;
			log.info("calling fetchRandomQuestoins api: " + randQuestionstUrl);
			response = restTemplate.exchange(randQuestionstUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			log.info("responce from  fetchRandomQuestoins api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in fetchRandomQuestoins ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CustomerModel> login(String loginId, String password)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
		String loginCustUrl = baseUrl.toString() + USER_API_ENDPOINT + "/login/?userId=" + loginId + "&password="
				+ password;
		log.info("calling login api: " + loginCustUrl);
		response = restTemplate.exchange(loginCustUrl, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
				});
		log.info("responce from  login api: " + util.marshall(response.getBody()));
		return response.getBody();
	}

	public ApiResponse<CustomerModel> validateSecurityQuestions(List<SecurityQuestionModel> securityquestions)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		CustomerModel custModel = new CustomerModel();
		custModel.setCustomerId(jaxMetaInfo.getCustomerId());
		custModel.setSecurityquestions(securityquestions);
		String validatSecurityQuestionstUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/validate-random-questions/";
		HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
		log.info("calling validateSecurityQuestions api: " + validatSecurityQuestionstUrl);
		response = restTemplate.exchange(validatSecurityQuestionstUrl, HttpMethod.POST, requestEntity,
				new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
				});
		log.info("responce from  validateSecurityQuestions api: " + util.marshall(response.getBody()));
		return response.getBody();
	}

	public ApiResponse<BooleanResponse> updatePassword(String password, String otp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		ResponseEntity<ApiResponse<BooleanResponse>> response = null;
		String endpoint = CUSTOMER_ENDPOINT + UPDATE_CUSTOMER_PASSWORD_ENDPOINT;
		CustomerModel custModel = new CustomerModel();
		custModel.setPassword(password);
		custModel.setMotp(otp);
		String updatePasswordUrl = baseUrl.toString() + endpoint + "?password=" + password;
		HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
		log.info("calling updatePassword api: " + updatePasswordUrl);
		response = restTemplate.exchange(updatePasswordUrl, HttpMethod.PUT, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
				});
		log.info("responce from  updatePassword api: " + util.marshall(response.getBody()));
		return response.getBody();
	}

	public ApiResponse<CustomerDto> getMyProfileInfo() {
		ResponseEntity<ApiResponse<CustomerDto>> response = null;
		try {
			log.info("Bene Clinet to get bene list Input String :");
			String url = baseUrl.toString() + USER_API_ENDPOINT + "/myprofile-info/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerDto>>() {
					});
		} catch (Exception e) {
			log.debug("Bene country list ", e);
		}
		return response.getBody();
	}

	public ApiResponse<BooleanResponse> unLockCustomer() {
		ResponseEntity<ApiResponse<BooleanResponse>> response = null;
		String url = baseUrl.toString() + CUSTOMER_ENDPOINT + "/unlock/";
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
				});
		return response.getBody();
	}

	public ApiResponse<BooleanResponse> deActivateCustomer() {
		ResponseEntity<ApiResponse<BooleanResponse>> response = null;
		String url = baseUrl.toString() + CUSTOMER_ENDPOINT + "/deactivate/";
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
				});
		return response.getBody();
	}

}
