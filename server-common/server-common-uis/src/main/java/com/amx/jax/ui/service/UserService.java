package com.amx.jax.ui.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.CustomerProfileClient;
import com.amx.jax.dict.Language;
import com.amx.jax.error.JaxError;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.CivilIdOtpModel;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.ui.WebAppConfig;
import com.amx.jax.ui.UIConstants.Features;
import com.amx.jax.ui.UIConstants.MileStone;
import com.amx.jax.ui.auth.AuthLibContext;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateResponse;
import com.amx.jax.ui.model.UserBean;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.utils.ArgUtil;
import com.amx.utils.AssertUtil;
import com.amx.utils.ListManager;

/**
 * The Class UserService.
 */
@Service
public class UserService {

	/** The log. */
	private Logger LOG = Logger.getLogger(UserService.class);

	/** The user bean. */
	@Autowired
	private UserBean userBean;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/**
	 * Gets the user bean.
	 *
	 * @return the user bean
	 */
	public UserBean getUserBean() {
		return userBean;
	}

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	@Autowired
	AuthLibContext authLibContext;

	/**
	 * Gets the random security question.
	 *
	 * @param customerModel the customer model
	 * @return the random security question
	 */
	public AuthData getRandomSecurityQuestion(CustomerModel customerModel) {
		AuthData loginData = new AuthData();
		ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
				customerModel.getSecurityquestions());

		SecurityQuestionModel answer = listmgr.pickRandom();
		sessionService.getGuestSession().setQuesIndex(listmgr.getIndex());

		List<QuestModelDTO> questModel = jaxService.getMetaClient().getSequrityQuestion().getResults();

		for (QuestModelDTO questModelDTO : questModel) {
			if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
				loginData.setQuestion(questModelDTO.getDescription()); // TODO:- TO be removed
				loginData.setQues(questModelDTO);
			}
		}

		loginData.setImageId(customerModel.getImageUrl());
		loginData.setImageCaption(customerModel.getCaption());
		return loginData;
	}

	/**
	 * Gets the notify topics.
	 *
	 * @param prefix the prefix
	 * @param lang
	 * @return the notify topics
	 */
	public List<String> getNotifyTopics(String prefix, Language lang) {
		CustomerModel customerModel = sessionService.getUserSession().getCustomerModel();
		AssertUtil.asNotNull(customerModel, "Customer Model is empty from UseerSession");
		
		PushMessage msg = new PushMessage();
		msg.addToTenant(AppContextUtil.getTenant());
		msg.addToCountry(customerModel.getPersoninfo().getNationalityId());
		msg.addToUser(customerModel.getCustomerId());

		// With Language
		msg.addToTenant(AppContextUtil.getTenant(), lang);
		msg.addToCountry(customerModel.getPersoninfo().getNationalityId(), lang);
		msg.addToUser(customerModel.getCustomerId(), lang);

		Date dob = customerModel.getPersoninfo().getDateOfBirth();
		if(ArgUtil.is(dob)) {
			msg.addToDate("dob", dob);			
		}
		return msg.getTo();
	}

	public AmxApiResponse<CustomerFlags, Object> checkModule(Features feature) {
		try {
			sessionService.getGuestSession().initFlow(AuthState.AuthFlow.PERMS, AuthState.AuthStep.CHECK);
			return ResponseWrapper.buildData(authLibContext.get()
					.checkModule(sessionService.getGuestSession().getState(),
							sessionService.getUserSession().getCustomerModel().getFlags(), feature));
		} catch (GlobalException ex) {
			if (ex.getError().equals(JaxError.SQA_REQUIRED)) {
				AuthData authData = getRandomSecurityQuestion(sessionService.getUserSession().getCustomerModel());
				ex.setMeta(authData.toJaxAuthMetaResp());
			}
			throw ex;
		}
	}

	/**
	 * Gets the profile details.
	 *
	 * @return the profile details
	 */
	public ResponseWrapper<CustomerDto> getProfileDetails() {
		return new ResponseWrapper<CustomerDto>(
				jaxService.setDefaults().getUserclient().getMyProfileInfo().getResult());
	}

	public void updateCustoemrModel() {
		String identity = sessionService.getUserSession().getCustomerModel().getIdentityId();
		AmxApiResponse<CustomerModelResponse, Object> x = jaxService.setDefaults().getUserclient()
				.getCustomerModelResponse(identity);
		sessionService.getUserSession().getCustomerModel().setFlags(x.getResult().getCustomerFlags());
		sessionService.getUserSession().getCustomerModel().setPersoninfo(x.getResult().getPersonInfo());
		sessionService.getUserSession().getCustomerModel().setSecurityquestions(x.getResult().getSecurityquestions());
		// sessionService.getGuestSession().setLanguage(x.getResult().getPersonInfo().getLang());
	}

	/**
	 * Update email.
	 *
	 * @param email the email
	 * @param mOtp  the m otp
	 * @param eOtp  the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateResponse> updateEmail(String email, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateResponse> wrapper = new ResponseWrapper<UserUpdateResponse>(new UserUpdateData());
		if (mOtp == null) {
			CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendOtpForEmailUpdate(email).getResult();
			wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
			wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
			wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_INIT, "OTP Sent for mobile update");
		} else {
			CustomerModel model = jaxService.setDefaults().getUserclient().saveEmail(email, mOtp, eOtp).getResult();
			sessionService.getUserSession().getCustomerModel().setEmail(model.getEmail());
			sessionService.getUserSession().getCustomerModel().getPersoninfo().setEmail(model.getEmail());
			wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "Email Updated");
			updateCustoemrModel();
		}
		return wrapper;
	}

	/**
	 * Update phone.
	 *
	 * @param phone the phone
	 * @param mOtp  the m otp
	 * @param eOtp  the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateResponse> updatePhone(String phone, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateResponse> wrapper = new ResponseWrapper<UserUpdateResponse>(new UserUpdateData());
		if (mOtp == null) {
			CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendOtpForMobileUpdate(phone).getResult();
			wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
			wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
			wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_INIT, "OTP Sent for email update");
		} else {
			CustomerModel model = jaxService.setDefaults().getUserclient().saveMobile(phone, mOtp, eOtp).getResult();
			sessionService.getUserSession().getCustomerModel().setMobile(model.getMobile());
			sessionService.getUserSession().getCustomerModel().getPersoninfo().setMobile(model.getMobile());
			wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "Mobile Updated");
		}
		return wrapper;
	}

	/**
	 * Update sec ques.
	 *
	 * @param securityquestions the securityquestions
	 * @param mOtp              the m otp
	 * @param eOtp              the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateResponse> updateSecQues(List<SecurityQuestionModel> securityquestions, String mOtp,
			String eOtp) {
		ResponseWrapper<UserUpdateResponse> wrapper = new ResponseWrapper<UserUpdateResponse>(new UserUpdateData());
		jaxService.setDefaults().getUserclient().saveSecurityQuestions(securityquestions, mOtp, eOtp).getResult();
		wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");
		return wrapper;
	}

	public AmxApiResponse<BoolRespModel, Object> updateSecQues(List<SecurityQuestionModel> securityquestions) {
		AmxApiResponse<BoolRespModel, Object> x = jaxService.getUserclient()
				.saveCustomerSecQuestions(securityquestions);
		sessionService.getGuestSession().getState().setValidSecQues(true);
		updateCustoemrModel();
		return x;
	}

	/**
	 * Update phising.
	 *
	 * @param imageUrl the image url
	 * @param caption  the caption
	 * @param mOtp     the m otp
	 * @param eOtp     the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> updatePhising(String imageUrl, String caption, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		jaxService.setDefaults().getUserclient().savePhishiingImage(caption, imageUrl, mOtp, eOtp).getResult();
		wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "Phishing Image Updated");
		return wrapper;
	}

	/**
	 * Updatepwd.
	 *
	 * @param password the password
	 * @param mOtp     the m otp
	 * @param eOtp     the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateResponse> updatepwd(String password, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateResponse> wrapper = new ResponseWrapper<UserUpdateResponse>(new UserUpdateData());
		BoolRespModel model = jaxService.setDefaults().getUserclient().updatePassword(password, mOtp, eOtp).getResult();
		if (model.isSuccess()) {
			wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "Password Updated Succesfully");
		}
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> getSecQues() {
		List<QuestModelDTO> questModel = jaxService.setDefaults().getMetaClient().getSequrityQuestion().getResults();
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		wrapper.getData().setSecQuesMeta(questModel);
		if (sessionService.getUserSession().getCustomerModel() != null) {
			wrapper.getData().setSecQuesAns(sessionService.getUserSession().getCustomerModel().getSecurityquestions());
		} else if (sessionService.getGuestSession().getCustomerModel() != null) {
			wrapper.getData().setSecQuesAns(sessionService.getGuestSession().getCustomerModel().getSecurityquestions());
		}
		return wrapper;
	}

	/** The http service. */
	@Autowired
	private CommonHttpRequest httpService;

	/** The tenant context. */
	@Autowired
	private TenantService tenantContext;

	/** The app config. */
	@Autowired
	private AppConfig appConfig;

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	@Autowired
	private CustomerProfileClient customerProfileClient;

	public ResponseWrapper<UserMetaData> getMeta(boolean refresh, boolean validate, Language lang) {

		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		lang = httpService.getLanguage();
		Language sessionLang = sessionService.getGuestSession().getLanguage();
		boolean isLangChange = ArgUtil.is(lang) && !lang.equals(sessionLang);

		wrapper.getData().setTenant(AppContextUtil.getTenant());
		wrapper.getData().setTenantCode(AppContextUtil.getTenant().getCode());
		wrapper.getData().setCdnUrl(appConfig.getCdnURL());

		wrapper.getData().setDevice(sessionService.getAppDevice().getUserDevice().toSanitized());
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		wrapper.getData().setValidSession(sessionService.getUserSession().isValid());

		CustomerModel customer = sessionService.getUserSession().getCustomerModel();

		if (customer != null) {
			wrapper.getData().setActive(true);
			wrapper.getData().setCustomerId(sessionService.getUserSession().getCustomerModel().getCustomerId());
			wrapper.getData().setInfo(sessionService.getUserSession().getCustomerModel().getPersoninfo());

			Language profileLang = customer.getPersoninfo().getLang();

			if (false
					/**
					 * This is language Change request after Login
					 */
					|| isLangChange
					/**
					 * profile language is empty
					 */
					|| (ArgUtil.isEmpty(profileLang) && !Language.EN.equals(lang))
					/**
					 * Client language is NON-English and different than profile Language
					 */
					|| (!Language.EN.equals(lang) && !lang.equals(profileLang))

			) {
				customerProfileClient.saveLanguage(customer.getCustomerId(), lang.getBDCode());
				refresh = true;
			} else {
				lang = profileLang;
			}

			if (refresh) {
				this.updateCustoemrModel();
			}
			CustomerFlags customerFlags = sessionService.getUserSession().getCustomerModel().getFlags();

			if (validate) {
				customerFlags = authLibContext.get().checkUserMeta(sessionService.getGuestSession().getState(),
						customerFlags);
			}

			wrapper.getData().setFlags(customerFlags);

			if (!ArgUtil.isEmpty(customerFlags) && customerFlags.getAnnualIncomeExpired()) {
				wrapper.setStatusEnum(OWAStatusStatusCodes.INCOME_UPDATE_REQUIRED);
			}

			wrapper.getData().setDomCurrency(tenantContext.getDomCurrency());
			wrapper.getData().setConfig(jaxService.setDefaults().getMetaClient().getJaxMetaParameter().getResult());
			wrapper.getData().getSubscriptions().addAll(this.getNotifyTopics("/topics/", lang));
			wrapper.getData().setReturnUrl(sessionService.getGuestSession().getReturnUrl());

			wrapper.getData().setFeatures(
					authLibContext.get().filterFeatures(sessionService.getGuestSession().getState(), customerFlags,
							webAppConfig.getFeaturesList()));
		} else {
			wrapper.getData().setFeatures(webAppConfig.getFeaturesList());
		}

		/**
		 * Language Changed - Change it in session, cookie and meta
		 */
		sessionService.getGuestSession().setLanguage(lang);
		wrapper.getData().setLang(sessionService.getGuestSession().getLanguage());
		httpService.setCookie("lang", lang.toString(), 30 * 60 * 60 * 2);

		wrapper.getData().setMileStones(MileStone.LIST);
		wrapper.getData().setNotifyRangeShort(webAppConfig.getNotifyRangeShort());
		wrapper.getData().setNotifyRangeLong(webAppConfig.getNotifyRangeLong());
		wrapper.getData().setNotificationGap(webAppConfig.getNotificationGap());

		return wrapper;
	}

}
