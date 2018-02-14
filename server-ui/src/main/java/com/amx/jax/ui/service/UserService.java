package com.amx.jax.ui.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.model.UserBean;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;

@Service
public class UserService {

	private Logger LOG = Logger.getLogger(UserService.class);

	@Autowired
	private UserBean userBean;

	public UserBean getUserBean() {
		return userBean;
	}

	@Autowired
	private PostManService postManService;

	@Autowired
	private JaxService jaxService;

	public ResponseWrapper<CustomerDto> getProfileDetails() {
		return new ResponseWrapper<CustomerDto>(
				jaxService.setDefaults().getUserclient().getMyProfileInfo().getResult());
	}

	public ResponseWrapper<UserUpdateData> updateEmail(String email, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		try {
			if (mOtp == null) {
				CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendOtpForEmailUpdate(email)
						.getResult();
				wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
				wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
				wrapper.setMessage(ResponseStatus.USER_UPDATE_INIT, "OTP Sent for mobile update");
			} else {
				jaxService.setDefaults().getUserclient().saveEmail(email, mOtp, eOtp).getResult();
				wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Email Updated");
			}
		} catch (AbstractException e) {
			wrapper.setMessage(ResponseStatus.USER_UPDATE_FAILED, e);
		}
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatePhone(String phone, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		try {
			if (mOtp == null) {
				CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendOtpForMobileUpdate(phone)
						.getResult();
				wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
				wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
				wrapper.setMessage(ResponseStatus.USER_UPDATE_INIT, "OTP Sent for email update");
			} else {
				jaxService.setDefaults().getUserclient().saveMobile(phone, mOtp, eOtp).getResult();
				wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Mobile Updated");
			}
		} catch (AbstractException e) {
			wrapper.setMessage(ResponseStatus.USER_UPDATE_FAILED, e);
		}
		return wrapper;
	}

	@Async
	public void notifyResetOTP(CivilIdOtpModel model) {

		Message msg = new Message();
		msg.setMessage("Your OTP for Reset is " + model.getmOtp());
		try {
			postManService.notifySlack(msg);
		} catch (PostManException e1) {
			LOG.error("Error while sending SlackMS", e1);
		}

		SMS sms = new SMS();
		try {
			String phoneNo = "7710072192";
			phoneNo = model.getMobile();
			sms.addTo(phoneNo);
			sms.setMessage("Your OTP for Reset is " + model.getmOtp());
			sms.setTemplate(Templates.RESET_OTP_SMS);
			sms.getModel().put("data", model);
			postManService.sendSMS(sms);
			LOG.info("Message Sent : OTP SMS to " + phoneNo);
		} catch (Exception e) {
			LOG.error("Error while sending OTP SMS to 7710072192", e);
		}

		Email email = new Email();
		email.setSubject("Verify Your Account");
		if (model.getEmail() != null && !UIConstants.EMPTY.equals(model.getEmail())) {
			email.addTo(model.getEmail());
		} else {
			email.addTo("riddhi.madhu@almullagroup.com");
		}
		email.setTemplate(Templates.RESET_OTP);
		email.setHtml(true);
		email.getModel().put(UIConstants.RESP_DATA_KEY, model);

		try {
			postManService.sendEmail(email);
		} catch (Exception e) {
			LOG.error("Error while sending OTP Email to" + model.getEmail(), e);
		}

	}

}
