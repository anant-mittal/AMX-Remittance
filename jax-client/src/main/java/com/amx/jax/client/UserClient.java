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
import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.amxlib.meta.model.AnnualIncomeRangeDTO;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.service.ICustomerService;
import com.amx.amxlib.service.IUserService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.ContactType;
import com.amx.jax.model.CivilIdOtpModel;
import com.amx.jax.model.UserDevice;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.customer.CustomerModelSignupResponse;
import com.amx.jax.rest.RestService;

@Component
public class UserClient extends AbstractJaxServiceClient implements ICustomerService, IUserService {

	private static final Logger LOGGER = Logger.getLogger(UserClient.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	RestService restService;

	public ApiResponse<CustomerModel> validateOtp(String identityId, String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
	
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
		
	}

	public ApiResponse<CustomerModel> validateOtp(String identityId, String mOtp, String eOtp, String wOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		
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
		
	}

	public ApiResponse<CustomerModel> validateOtp(String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		
			LOGGER.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/validate-otp/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		
	}

	public ApiResponse<CivilIdOtpModel> sendResetOtpForCivilId(String identityId)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/send-reset-otp/";
			LOGGER.info("calling sendResetOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId()
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
	
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		

	}

	public ApiResponse<CustomerModel> saveCustomer(String json)
			throws CustomerValidationException, LimitExeededException {
		
			HttpEntity<String> requestEntity = new HttpEntity<String>(json, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling saveCustomer api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});

	}

	public ApiResponse<CustomerModel> saveSecurityQuestions(List<SecurityQuestionModel> securityquestions, String mOtp,
			String eOtp) {
		
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
		

	}

	public ApiResponse<CustomerModel> savePhishiingImage(String caption, String imageUrl, String mOtp, String eOtp) {
		
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
		

	}

	/**
	 * saves customer's login id and password
	 * 
	 * @param email Customer's email id
	 * @param mOtp  mobile otp
	 * @param eOtp  email otp
	 */
	public ApiResponse<CustomerModel> saveCredentials(String loginId, String password, String mOtp, String eOtp,
			String email,String referralCode) throws AlreadyExistsException {
		
			CustomerModel custModel = new CustomerModel();
			custModel.setRegistrationFlow(true);
			custModel.setLoginId(loginId);
			custModel.setPassword(password);
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			custModel.setEmail(email);
			custModel.setReferralCode(referralCode);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String saveCustUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT;
			LOGGER.info("calling saveLoginIdAndPassword api: " + saveCustUrl);
			return restService.ajax(saveCustUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		

	}

	/**
	 * @param size: specify how many questions you need
	 */
	public ApiResponse<CustomerModel> fetchRandomQuestoins(int size) {
		
			BigDecimal customerId = jaxMetaInfo.getCustomerId();
			HttpEntity<String> requestEntity = new HttpEntity<String>(getHeader());
			String randQuestionstUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + customerId
					+ "/random-questions/?size=" + size;
			LOGGER.info("calling fetchRandomQuestoins api: " + randQuestionstUrl);
			return restService.ajax(randQuestionstUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		

	}

	public ApiResponse<CustomerModel> login(String loginId, String password)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		
			CustomerModel cmodel = new CustomerModel();
			cmodel.setLoginId(loginId);
			cmodel.setPassword(password);
			return restService.ajax(this.getBaseUrl() + USER_API_ENDPOINT + "/login/")
					.post(new HttpEntity<CustomerModel>(cmodel, getHeader()))
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});


	}

	public ApiResponse<CustomerModel> login_temp(String loginId, String password)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		
			CustomerModel cmodel = new CustomerModel();
			cmodel.setLoginId(loginId);
			cmodel.setPassword(password);
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(cmodel, getHeader());
			String loginCustUrl = this.getBaseUrl() + USER_API_ENDPOINT + "/login/";

			LOGGER.info("calling login api: " + loginCustUrl);
			return restService.ajax(loginCustUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		

	}

	public ApiResponse<CustomerModel> validateSecurityQuestions(List<SecurityQuestionModel> securityquestions)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		
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

	}

	public AmxApiResponse<BoolRespModel, Object> updatePassword(String password, String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException {
		
			String endpoint = CUSTOMER_ENDPOINT + UPDATE_CUSTOMER_PASSWORD_ENDPOINT;
			CustomerModel custModel = new CustomerModel();
			custModel.setPassword(password);
			custModel.setMotp(mOtp);
			custModel.setEotp(eOtp);
			String updatePasswordUrl = this.getBaseUrl() + endpoint;
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			LOGGER.info("calling updatePassword api: " + updatePasswordUrl);
			AmxApiResponse<BoolRespModel, Object> resp = restService.ajax(updatePasswordUrl).put(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
			return resp;
		

	}

	public ApiResponse<CustomerDto> getMyProfileInfo() {
		
			LOGGER.info("Bene Clinet to get bene list Input String :");
			String url = this.getBaseUrl() + USER_API_ENDPOINT + "/myprofile-info/";
			// new HttpHeaders()
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			// HttpEntity<Object> requestEntity = new HttpEntity<Object>(new HttpHeaders());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerDto>>() {
					});
		
	}

	public ApiResponse<BooleanResponse> unLockCustomer() {
		
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/unlock/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		

	}

	public ApiResponse<BooleanResponse> deActivateCustomer() {
		
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/deactivate/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		

	}

	public ApiResponse<CivilIdOtpModel> sendOtpForEmailUpdate(String email)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			custModel.setEmail(email);

			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtp for email update api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		

	}

	public ApiResponse<CivilIdOtpModel> sendOtpForMobileUpdate(String mobile)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			custModel.setMobile(mobile);

			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(custModel, getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/send-otp/";
			LOGGER.info("calling sendOtp for mobile update api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		

	}

	public ApiResponse<BooleanResponse> unLockCustomer(String civilId) {
		
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/unlock/" + civilId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		

	}

	public ApiResponse<BooleanResponse> deActivateCustomer(String civilId) {
		
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/deactivate/" + civilId;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		
	}

	public ApiResponse<CustomerModel> saveEmail(String email, String mOtp, String eOtp) {
		
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
		
	}

	public ApiResponse<CustomerModel> saveMobile(String mobile, String mOtp, String eOtp) {
		
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
		
	}

	public ApiResponse<CivilIdOtpModel> initRegistration(String identityId, ContactType contactType)
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		
			Boolean initRegistration = new Boolean(true);
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/" + identityId + "/" + initRegistration
					+ "/send-otp/";
			LOGGER.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).get(requestEntity)
					.queryParam("contactType", contactType)
					.as(new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
		
	}

	public ApiResponse<QuestModelDTO> getDataVerificationQuestions() {
		
			LOGGER.info("Get all the getDataVerificationQuestions");
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/random-data-verification-questions/?size=1";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<QuestModelDTO>>() {
					});
		
	}

	public ApiResponse<CustomerModel> validateDataVerificationQuestions(List<SecurityQuestionModel> answers) {
		
			LOGGER.info("in the saveDataVerificationQuestions");
			CustomerModel cmodel = new CustomerModel();
			cmodel.setVerificationAnswers(answers);
			String url = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/random-data-verification-questions/";
			HttpEntity<CustomerModel> requestEntity = new HttpEntity<CustomerModel>(cmodel, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		
	}

	public ApiResponse<CustomerModel> customerLoggedIn(UserDevice userDevice) {
		
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());

			String sendOtpUrl = this.getBaseUrl() + CUSTOMER_ENDPOINT + "/logged/in/";
			LOGGER.info("calling customer logged in api: " + sendOtpUrl);
			return restService.ajax(sendOtpUrl).meta(new JaxMetaInfo()).post(custModel)
					.as(new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		
	} // end of customerLoggedIn

	public AmxApiResponse<UserFingerprintResponseModel, Object> linkDeviceId(String identityInt) {
		
			String url = this.getBaseUrl() + USER_API_ENDPOINT + "/link-deviceid/";
			return restService.ajax(url).queryParam("identityInt", identityInt).meta(new JaxMetaInfo())
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserFingerprintResponseModel, Object>>() {
					});

		
	}

	public AmxApiResponse<UserFingerprintResponseModel, Object> linkDeviceIdLoggedinUser() {
		

			return restService.ajax(appConfig.getJaxURL()).path(UserApi.PREFIX + UserApi.LINK_DEVICE_LOGGEDIN_USER)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserFingerprintResponseModel, Object>>() {
					});
		
	}

	public AmxApiResponse<CustomerModel, Object> loginUserByFingerprint(String civilId, String password) {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(UserApi.PREFIX + UserApi.LOGIN_CUSTOMER_BY_FINGERPRINT).meta(new JaxMetaInfo())
					.field(UserApi.IDENTITYINT, civilId).field(UserApi.PASSWORD, password).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerModel, Object>>() {
					});
		
	}

	public BoolRespModel delinkFingerprint() {
		

			return restService.ajax(appConfig.getJaxURL())
					.path(UserApi.PREFIX + UserApi.DELINK_FINGERPRINT).meta(new JaxMetaInfo())
					.post()
					.as(new ParameterizedTypeReference<BoolRespModel>() {
					});
		
	}

	public AmxApiResponse<AnnualIncomeRangeDTO, Object> getIncome() {
		

			return restService.ajax(appConfig.getJaxURL())
					.path(CustomerApi.PREFIX + CustomerApi.GET_ANNUAL_INCOME_RANGE)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<AnnualIncomeRangeDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<IncomeDto, Object> saveAnnualIncome(IncomeDto incomeDto) {
		
			String url = this.getBaseUrl() + CustomerApi.PREFIX + CustomerApi.SAVE_ANNUAL_INCOME;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(incomeDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<IncomeDto, Object>>() {
					});
		
	}

	public AmxApiResponse<IncomeDto, Object> getAnnualIncomeDetais() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(CustomerApi.PREFIX + CustomerApi.GET_ANNUAL_INCOME_DETAILS)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<IncomeDto, Object>>() {
					});
		
	}

	public BoolRespModel resetFingerprint(String identity) {
		

			return restService.ajax(appConfig.getJaxURL())
					.path(UserApi.PREFIX + UserApi.RESET_FINGERPRINT).meta(new JaxMetaInfo())
					.queryParam(UserApi.IDENTITYINT, identity)
					.post()
					.as(new ParameterizedTypeReference<BoolRespModel>() {
					});
		
	}

	@Override
	public AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse(String identityInt) {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(ApiEndpoint.CUSTOMER_ENDPOINT + Path.CUSTOMER_MODEL_RESPONSE_BY_IDENTITYINT)
					.meta(new JaxMetaInfo())
					.queryParam(Params.IDENTITY_INT, identityInt)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerModelResponse, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> saveCustomerSecQuestions(
			List<SecurityQuestionModel> securityQuestion) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustomerApi.PREFIX + CustomerApi.SAVE_SECURITY_QUESTIONS).post(securityQuestion)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<CustomerModelResponse, Object> getCustomerModelResponse() {
		

			return restService.ajax(appConfig.getJaxURL())
					.path(ApiEndpoint.CUSTOMER_ENDPOINT + Path.CUSTOMER_MODEL_RESPONSE_GET).meta(new JaxMetaInfo())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<CustomerModelResponse, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<CustomerModelSignupResponse, Object> getCustomerModelSignupResponse(String identityInt) {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(ApiEndpoint.CUSTOMER_ENDPOINT + Path.CUSTOMER_MODEL_SIGNUP_RESPONSE_GET)
					.meta(new JaxMetaInfo())
					.queryParam(Params.IDENTITY_INT, identityInt)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerModelSignupResponse, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<AnnualIncomeRangeDTO, Object> getAnnualTransactionLimitRange() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(CustomerApi.PREFIX + Path.ANNUAL_TRANSACTION_LIMIT_RANGE)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<AnnualIncomeRangeDTO, Object>>() {
					});
		
	}

	public AmxApiResponse<BoolRespModel, Object> saveAnnualTransactionLimit(IncomeDto incomeDto) {
		
			String url = this.getBaseUrl() + CustomerApi.PREFIX + Path.SAVE_ANNUAL_TRANSACTION_LIMIT;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(incomeDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		

	}

	public AmxApiResponse<AnnualIncomeRangeDTO, Object> getAnnualTransactionLimit() {
		
			return restService.ajax(appConfig.getJaxURL())
					.path(CustomerApi.PREFIX + Path.GET_ANNUAL_TRANSACTION_LIMIT)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<AnnualIncomeRangeDTO, Object>>() {
					});
		
	}

	
	public AmxApiResponse<BoolRespModel, Object> sendEmailOnLogin(CustomerModel customerModel) {
		
			String url = this.getBaseUrl() + UserApi.PREFIX + Path.RESEND_EMAIL_LOGIN;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(customerModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updatePasswordCustomer(String identityInt, String resetPassword) {
			CustomerModel customerModel = new CustomerModel();
			customerModel.setIdentityId(identityInt);
			customerModel.setPassword(resetPassword);
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(CustomerApi.PREFIX + CustomerApi.UPDATE_PASSWORD_CUSTOMER_V2)
					.post(customerModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	/**
	 * To Validate Customer with OTP
	 */
	@Override
	public AmxApiResponse<CustomerModel, Object> validateCustomerLoginOtp(String identityInt) {
		
			return restService.ajax(appConfig.getJaxURL()).meta(new JaxMetaInfo())
					.path(UserApi.PREFIX + UserApi.VALIDATE_CUSTOMER_LOGIN_OTP)
					.queryParam("identityInt", identityInt)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerModel, Object>>() {
					});
	}
}
