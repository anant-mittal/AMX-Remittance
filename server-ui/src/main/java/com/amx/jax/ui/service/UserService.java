package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.ui.UIConstants.Features;
import com.amx.jax.ui.auth.AuthLibContext;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateResponse;
import com.amx.jax.ui.model.UserBean;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

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
	 * Gets the notify topics.
	 *
	 * @param prefix the prefix
	 * @return the notify topics
	 */
	public List<String> getNotifyTopics(String prefix) {
		CustomerModel customerModel = sessionService.getUserSession().getCustomerModel();
		List<String> topics = new ArrayList<String>();
		topics.add((prefix + String.format(PushMessage.FORMAT_TO_ALL, AppContextUtil.getTenant(),
				customerModel.getPersoninfo().getNationalityId())).toLowerCase());
		topics.add((prefix + String.format(PushMessage.FORMAT_TO_NATIONALITY, AppContextUtil.getTenant(),
				customerModel.getPersoninfo().getNationalityId())).toLowerCase());
		topics.add((prefix + String.format(PushMessage.FORMAT_TO_USER, AppContextUtil.getTenant(),
				ArgUtil.parseAsString(customerModel.getCustomerId(), Constants.BLANK).replaceAll("\\s+", "")))
						.toLowerCase());
		return topics;
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

}
