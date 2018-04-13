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
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.util.ConverterUtility;
import com.amx.jax.rest.RestService;
import com.amx.jax.user.UserDevice;

@Component
public class UserClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(UserClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	public ApiResponse<CustomerModel> validateOtp(String identityId, String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		try {
			if (StringUtils.isBlank(identityId) || "null".equalsIgnoreCase(identityId)) {
				return validateOtp(mOtp, eOtp);
			}
			ResponseEntity<ApiResponse<CustomerModel>> response;
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CustomerModel> validateOtp(String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendOtpForCivilId api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForCivilId : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CivilIdOtpModel> sendResetOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-reset-otp/";
			LOGGER.info("calling sendResetOtpForCivilId api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendResetOtpForCivilId api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendResetOtpForCivilId : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId()
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendOtpForCivilId api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForCivilId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveCustomer(String json)
			throws CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			HttpEntity<String> requestEntity = new HttpEntity<String>(json, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling saveCustomer api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  saveCustomer api: " + util.marshall(response.getBody()));
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveSecurityQuestions(List<SecurityQuestionModel> securityquestions, String mOtp,
			String eOtp) {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;

			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setSecurityquestions(securityquestions);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling saveSecurityQuestions api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  saveSecurityQuestions api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveSecurityQuestions : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> savePhishiingImage(String caption, String imageUrl, String mOtp, String eOtp) {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;

			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setCaption(caption);
			custModel.setImageUrl(imageUrl);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String saveCustUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling savePhishiingImage api: " + saveCustUrl);
			response = restTemplate.exchange(saveCustUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  savePhishiingImage api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in savePhishiingImage : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * saves customer's login id and password
	 * 
	 * @param email
	 *            Customer's email id
	 * @param mOtp
	 *            mobile otp
	 * @param eOtp
	 *            email otp
	 */
	public ApiResponse<CustomerModel> saveCredentials(String loginId, String password, String mOtp, String eOtp,
			String email) throws AlreadyExistsException {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			CustomerModel custModel = new CustomerModel();
			custModel.setRegistrationFlow(true);
			custModel.setLoginId(loginId);
			custModel.setPassword(password);
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setEmail(email);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String saveCustUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling saveLoginIdAndPassword api: " + saveCustUrl);
			response = restTemplate.exchange(saveCustUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  saveLoginIdAndPassword api: " + util.marshall(response.getBody()));
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveLoginIdAndPassword : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * @param size:
	 *            specify how many questions you need
	 */
	public ApiResponse<CustomerModel> fetchRandomQuestoins(int size) {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;

			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			String randQuestionstUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + customerId
					+ "/random-questions/?size=" + size;
			LOGGER.info("calling fetchRandomQuestoins api: " + randQuestionstUrl);
			response = restTemplate.exchange(randQuestionstUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  fetchRandomQuestoins api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in fetchRandomQuestoins : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	@Autowired
	RestService restService;

	public ApiResponse<CustomerModel> login(String loginId, String password)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		try {

			CustomerModel cmodel = new CustomerModel();
			cmodel.setLoginId(loginId);
			cmodel.setPassword(password);
			return restService.ajax(this.getBaseUrl() + USER_API_ENDPOINT + "/login/")
					.post(new HttpEntity<CustomerModel>(cmodel, getHeader()))
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});

		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in login : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> login_temp(String loginId, String password)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			CustomerModel cmodel = new CustomerModel();
			cmodel.setLoginId(loginId);
			cmodel.setPassword(password);
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(cmodel, getHeader());
			String loginCustUrl = this.getBaseUrl() + USER_API_ENDPOINT + "/login/";

			LOGGER.info("calling login api: " + loginCustUrl);
			response = restTemplate.exchange(loginCustUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  login api: " + util.marshall(response.getBody()));
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in login : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> validateSecurityQuestions(List<SecurityQuestionModel> securityquestions)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			CustomerModel custModel = new CustomerModel();
			LOGGER.info("validateSecurityQuestions for customer id " + jaxMetaInfo.getCustomerId());

			custModel.setCustomerId(jaxMetaInfo.getCustomerId());

			custModel.setSecurityquestions(securityquestions);
			String validatSecurityQuestionstUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/validate-random-questions/";
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			LOGGER.info("calling validateSecurityQuestions api: " + validatSecurityQuestionstUrl);
			response = restTemplate.exchange(validatSecurityQuestionstUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  validateSecurityQuestions api: " + util.marshall(response.getBody()));
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateSecurityQuestions : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> updatePassword(String password, String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response;
			String endpoint = CUSTOMER_ENDPOINT + UPDATE_CUSTOMER_PASSWORD_ENDPOINT;
			CustomerModel custModel = new CustomerModel();
			custModel.setPassword(password);
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			String updatePasswordUrl = this.getBaseUrl() + endpoint + "?password=" + password;
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			LOGGER.info("calling updatePassword api: " + updatePasswordUrl);
			response = restTemplate.exchange(updatePasswordUrl, HttpMethod.PUT, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			LOGGER.info("responce from  updatePassword api: " + util.marshall(response.getBody()));
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in updatePassword : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerDto> getMyProfileInfo() {
		try {
			ResponseEntity<ApiResponse<CustomerDto>> response;

			LOGGER.info("Bene Clinet to get bene list Input String :");
			String url = this.getBaseUrl() + USER_API_ENDPOINT + "/myprofile-info/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerDto>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getMyProfileInfo : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> unLockCustomer() {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response;
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/unlock/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in unLockCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> deActivateCustomer() {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response;
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/deactivate/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in deActivateCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CivilIdOtpModel> sendOtpForEmailUpdate(String email)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			custModel.setEmail(email);

			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtp for email update api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendOtpForEmailUpdate api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForEmailUpdate : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CivilIdOtpModel> sendOtpForMobileUpdate(String mobile)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			custModel.setMobile(mobile);

			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtp for mobile update api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendOtpForMobileUpdate api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForMobileUpdate : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> unLockCustomer(String civilId) {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response;
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/unlock/" + civilId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in unLockCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> deActivateCustomer(String civilId) {
		try {
			ResponseEntity<ApiResponse<BooleanResponse>> response;
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/deactivate/" + civilId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in deActivateCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveEmail(String email, String mOtp, String eOtp) {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;

			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setEmail(email);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/save";
			LOGGER.info("calling saveEmail api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  saveEmail api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveEmail : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveMobile(String mobile, String mOtp, String eOtp) {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;

			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setMobile(mobile);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/save";
			LOGGER.info("calling saveMobile api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  saveMobile api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveMobile : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CivilIdOtpModel> initRegistration(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			Boolean initRegistration = new Boolean(true);
			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/" + initRegistration
					+ "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendOtpForCivilId api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in initRegistration : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<QuestModelDTO> getDataVerificationQuestions() {
		ResponseEntity<ApiResponse<QuestModelDTO>> response = null;
		try {
			LOGGER.info("Get all the getDataVerificationQuestions");
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/random-data-verification-questions/?size=1";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				LOGGER.error("exception in getDataVerificationQuestions ", e);
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<CustomerModel> validateDataVerificationQuestions(List<SecurityQuestionModel> answers) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			LOGGER.info("in the saveDataVerificationQuestions");
			CustomerModel cmodel = new CustomerModel();
			cmodel.setVerificationAnswers(answers);
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/random-data-verification-questions/";
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(cmodel, getHeader());
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				LOGGER.error("exception in saveDataVerificationQuestions ", e);
				throw new JaxSystemError();
			}
		}
	}

	public ApiResponse<CustomerModel> customerLoggedIn(UserDevice userDevice) {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());

			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/logged/in/";
			LOGGER.info("calling customer logged in api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			LOGGER.info("responce from  customer logged in  api: " + util.marshall(response.getBody()));
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in customer logged in : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	} // end of customerLoggedIn
}
