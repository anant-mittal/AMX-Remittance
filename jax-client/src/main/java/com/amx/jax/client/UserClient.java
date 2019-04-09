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
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.constant.ApiEndpoint;
import com.amx.amxlib.constant.ApiEndpoint.CustomerApi;
import com.amx.amxlib.constant.ApiEndpoint.UserApi;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.amxlib.meta.model.AnnualIncomeRangeDTO;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.service.ICustomerService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.CommunicationChannel;
import com.amx.jax.model.UserDevice;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.customer.CustomerModelSignupResponse;
import com.amx.jax.rest.RestService;

@Component
public class UserClient extends AbstractJaxServiceClient implements ICustomerService {

	private static final Logger LOGGER = Logger.getLogger(UserClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	RestService restService;

	public ApiResponse<CustomerModel> validateOtp(String identityId, String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		try {
			if (StringUtils.isBlank(identityId) || "null".equalsIgnoreCase(identityId)) {
				return validateOtp(mOtp, eOtp);
			}
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	public ApiResponse<CustomerModel> validateOtp(String identityId, String mOtp, String eOtp, String wOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		try {
			if (StringUtils.isBlank(identityId) || "null".equalsIgnoreCase(identityId)) {
				return validateOtp(mOtp, eOtp);
			}
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp).queryParam("wOtp", wOtp);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CustomerModel> validateOtp(String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		try {
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForCivilId : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CivilIdOtpModel> sendResetOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-reset-otp/";
			LOGGER.info("calling sendResetOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendResetOtpForCivilId : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId()
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForCivilId : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveCustomer(String json)
			throws CustomerValidationException, LimitExeededException {
		try {
			HttpEntity<String> requestEntity = new HttpEntity<String>(json, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling saveCustomer api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveSecurityQuestions(List<SecurityQuestionModel> securityquestions, String mOtp,
			String eOtp) {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setSecurityquestions(securityquestions);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling saveSecurityQuestions api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (Exception ae) {
			return JaxSystemError.evaluate(ae);
		}

	}

	public ApiResponse<CustomerModel> savePhishiingImage(String caption, String imageUrl, String mOtp, String eOtp) {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setCaption(caption);
			custModel.setImageUrl(imageUrl);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String saveCustUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling savePhishiingImage api: " + saveCustUrl);
			return restService.ajax(saveCustUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in savePhishiingImage : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * saves customer's login id and password
	 * 
	 * @param email Customer's email id
	 * @param mOtp  mobile otp
	 * @param eOtp  email otp
	 */
	public ApiResponse<CustomerModel> saveCredentials(String loginId, String password, String mOtp, String eOtp,
			String email) throws AlreadyExistsException {
		try {
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
			return restService.ajax(saveCustUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveLoginIdAndPassword : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	/**
	 * @param size: specify how many questions you need
	 */
	public ApiResponse<CustomerModel> fetchRandomQuestoins(int size) {
		try {
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			String randQuestionstUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + customerId
					+ "/random-questions/?size=" + size;
			LOGGER.info("calling fetchRandomQuestoins api: " + randQuestionstUrl);
			return restService.ajax(randQuestionstUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in fetchRandomQuestoins : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

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

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in login : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> login_temp(String loginId, String password)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		try {
			CustomerModel cmodel = new CustomerModel();
			cmodel.setLoginId(loginId);
			cmodel.setPassword(password);
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(cmodel, getHeader());
			String loginCustUrl = this.getBaseUrl() + USER_API_ENDPOINT + "/login/";

			LOGGER.info("calling login api: " + loginCustUrl);
			return restService.ajax(loginCustUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in login : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> validateSecurityQuestions(List<SecurityQuestionModel> securityquestions)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		try {
			CustomerModel custModel = new CustomerModel();
			LOGGER.info("validateSecurityQuestions for customer id " + jaxMetaInfo.getCustomerId());

			custModel.setCustomerId(jaxMetaInfo.getCustomerId());

			custModel.setSecurityquestions(securityquestions);
			String validatSecurityQuestionstUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/validate-random-questions/";
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			LOGGER.info("calling validateSecurityQuestions api: " + validatSecurityQuestionstUrl);
			return restService.ajax(validatSecurityQuestionstUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateSecurityQuestions : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public AmxApiResponse<BoolRespModel, Object> updatePassword(String password, String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		try {
			String endpoint = CUSTOMER_ENDPOINT + UPDATE_CUSTOMER_PASSWORD_ENDPOINT;
			CustomerModel custModel = new CustomerModel();
			custModel.setPassword(password);
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			String updatePasswordUrl = this.getBaseUrl() + endpoint + "?password=" + password;
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			LOGGER.info("calling updatePassword api: " + updatePasswordUrl);
			AmxApiResponse<BoolRespModel, Object> resp = restService.ajax(updatePasswordUrl).put(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
			return resp;
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in updatePassword : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerDto> getMyProfileInfo() {
		try {
			LOGGER.info("Bene Clinet to get bene list Input String :");
			String url = this.getBaseUrl() + USER_API_ENDPOINT + "/myprofile-info/";
			// new HttpHeaders()
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			// HttpEntity<Object> requestEntity = new HttpEntity<Object>(new HttpHeaders());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerDto>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getMyProfileInfo : ", e);
			throw new JaxSystemError(e);
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> unLockCustomer() {
		try {
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/unlock/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in unLockCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> deActivateCustomer() {
		try {
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/deactivate/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
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

			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtp for email update api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
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

			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtp for mobile update api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForMobileUpdate : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> unLockCustomer(String civilId) {
		try {
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/unlock/" + civilId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in unLockCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<BooleanResponse> deActivateCustomer(String civilId) {
		try {
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/deactivate/" + civilId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in deActivateCustomer : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveEmail(String email, String mOtp, String eOtp) {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setEmail(email);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/save";
			LOGGER.info("calling saveEmail api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveEmail : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveMobile(String mobile, String mOtp, String eOtp) {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setMobile(mobile);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/save";
			LOGGER.info("calling saveMobile api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveMobile : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CivilIdOtpModel> initRegistration(String identityId, CommunicationChannel otpCommunicationChannel)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			Boolean initRegistration = new Boolean(true);
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/" + initRegistration
					+ "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.queryParam("otpCommunicationChannel", otpCommunicationChannel)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in initRegistration : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public ApiResponse<QuestModelDTO> getDataVerificationQuestions() {
		try {
			LOGGER.info("Get all the getDataVerificationQuestions");
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/random-data-verification-questions/?size=1";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				LOGGER.error("exception in getDataVerificationQuestions ", e);
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<CustomerModel> validateDataVerificationQuestions(List<SecurityQuestionModel> answers) {
		try {
			LOGGER.info("in the saveDataVerificationQuestions");
			CustomerModel cmodel = new CustomerModel();
			cmodel.setVerificationAnswers(answers);
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/random-data-verification-questions/";
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(cmodel, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (Exception e) {
			if (e instanceof AbstractJaxException) {
				throw e;
			} else {
				LOGGER.error("exception in saveDataVerificationQuestions ", e);
				throw new JaxSystemError();
			}
		}
	}

	public ApiResponse<CustomerModel> customerLoggedIn(UserDevice userDevice) {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());

			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/logged/in/";
			LOGGER.info("calling customer logged in api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).meta(new JaxMetaInfo()).post(custModel)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in customer logged in : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	} // end of customerLoggedIn

	public ApiResponse<CustomerModel> saveEmailNew(String email) {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setEmail(email);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/saveEmailOrMobile";
			LOGGER.info("Calling saveEmailNew API : " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exception in saveEmailNew API : ", e);
			throw new JaxSystemError();
		} // end of try-catch

	}

	public ApiResponse<CustomerModel> saveMobileNew(String mobile) {
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setMobile(mobile);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/saveEmailOrMobile";
			LOGGER.info("Calling saveMobileNew API : " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("Exception in saveMobileNew API : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	public AmxApiResponse<UserFingerprintResponseModel, Object> linkDeviceId(String identityInt) {
		try {
			String url = this.getBaseUrl() + USER_API_ENDPOINT + "/link-deviceid/";
			return restService.ajax(url).queryParam("identityInt", identityInt).meta(new JaxMetaInfo())
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserFingerprintResponseModel, Object>>() {
					});

		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in linkDeviceId : ", e);
			throw new JaxSystemError(e);
		} // end of try-catch
	}

	public AmxApiResponse<UserFingerprintResponseModel, Object> linkDeviceIdLoggedinUser() {
		try {

			return restService.ajax(appConfig.getJaxURL()).path(UserApi.PREFIX + UserApi.LINK_DEVICE_LOGGEDIN_USER)
					.meta(new JaxMetaInfo()).post()

					.as(new ParameterizedTypeReference<AmxApiResponse<UserFingerprintResponseModel, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in linkDeviceloggedinUser : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<CustomerModel, Object> loginUserByFingerprint(String civilId, String password) {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(UserApi.PREFIX + UserApi.LOGIN_CUSTOMER_BY_FINGERPRINT).meta(new JaxMetaInfo()).post()
					.queryParam(UserApi.IDENTITYINT, civilId).queryParam(UserApi.PASSWORD, password).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerModel, Object>>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in loginUserByFingerprint : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public BoolRespModel delinkFingerprint() {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(UserApi.PREFIX + UserApi.DELINK_FINGERPRINT).meta(new JaxMetaInfo())
					.post()
					.as(new ParameterizedTypeReference<BoolRespModel>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in delink fingerprint : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<AnnualIncomeRangeDTO, Object> getIncome() {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(CustomerApi.PREFIX + CustomerApi.GET_ANNUAL_INCOME_RANGE)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<AnnualIncomeRangeDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in Annual Income : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<IncomeDto, Object> saveAnnualIncome(IncomeDto incomeDto) {
		try {
			String url = this.getBaseUrl() + CustomerApi.PREFIX + CustomerApi.SAVE_ANNUAL_INCOME;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(incomeDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<IncomeDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in saveAnnualIncome: ", ae);
			return JaxSystemError.evaluate(ae);
		} // end of try-catch

	}

	public AmxApiResponse<IncomeDto, Object> getAnnualIncomeDetais() {
		try {
			return restService.ajax(appConfig.getJaxURL())
					.path(CustomerApi.PREFIX + CustomerApi.GET_ANNUAL_INCOME_DETAILS)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<IncomeDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in Annual Income details : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public BoolRespModel resetFingerprint(String identity) {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(UserApi.PREFIX + UserApi.RESET_FINGERPRINT).meta(new JaxMetaInfo())
					.queryParam(UserApi.IDENTITYINT, identity)
					.post()
					.as(new ParameterizedTypeReference<BoolRespModel>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in reset fingerprint : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse(String identityInt) {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(ApiEndpoint.CUSTOMER_ENDPOINT + Path.CUSTOMER_MODEL_RESPONSE_BY_IDENTITYINT).meta(new JaxMetaInfo())
					.queryParam(Params.IDENTITY_INT, identityInt)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerModelResponse, Object>>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in get customer response : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}
	@Override
	public AmxApiResponse<BoolRespModel,Object> saveCustomerSecQuestions(List<SecurityQuestionModel> securityQuestion) {
		try {
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustomerApi.PREFIX + CustomerApi.SAVE_SECURITY_QUESTIONS).post(securityQuestion)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in saveSecurityQuestions : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse() {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(ApiEndpoint.CUSTOMER_ENDPOINT + Path.CUSTOMER_MODEL_RESPONSE_GET).meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<CustomerModelResponse, Object>>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in get customer response : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<CustomerModelSignupResponse, Object> getCustomerModelSignupResponse(String identityInt) {
		try {

			return restService.ajax(appConfig.getJaxURL())
					.path(ApiEndpoint.CUSTOMER_ENDPOINT + Path.CUSTOMER_MODEL_SIGNUP_RESPONSE_GET).meta(new JaxMetaInfo())
					.queryParam(Params.IDENTITY_INT, identityInt)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerModelSignupResponse, Object>>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in get customer signup response : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}
}
