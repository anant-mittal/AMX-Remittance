package com.amx.jax.ui.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.postman.PostManService;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.jax.ui.config.HttpUnauthorizedException;
import com.amx.jax.ui.model.UserBean;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;

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
				wrapper.setMessage(WebResponseStatus.USER_UPDATE_INIT, "OTP Sent for mobile update");
			} else {
				jaxService.setDefaults().getUserclient().saveEmail(email, mOtp, eOtp).getResult();
				wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Email Updated");
			}
		} catch (AbstractException e) {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_FAILED, e);
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
				wrapper.setMessage(WebResponseStatus.USER_UPDATE_INIT, "OTP Sent for email update");
			} else {
				jaxService.setDefaults().getUserclient().saveMobile(phone, mOtp, eOtp).getResult();
				wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Mobile Updated");
			}
		} catch (AbstractException e) {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_FAILED, e);
		}
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updateSecQues(List<SecurityQuestionModel> securityquestions, String mOtp,
			String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		CustomerModel customerModel = jaxService.setDefaults().getUserclient()
				.saveSecurityQuestions(securityquestions, mOtp, eOtp).getResult();

		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updateSecQues(String phone, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		try {
			if (mOtp == null) {
				CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendOtpForMobileUpdate(phone)
						.getResult();
				wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
				wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
				wrapper.setMessage(WebResponseStatus.USER_UPDATE_INIT, "OTP Sent for email update");
			} else {
				jaxService.setDefaults().getUserclient().saveMobile(phone, mOtp, eOtp).getResult();
				wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Mobile Updated");
			}
		} catch (AbstractException e) {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_FAILED, e);
		}
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatepwd(String password, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		BooleanResponse model = jaxService.setDefaults().getUserclient().updatePassword(password, mOtp, eOtp)
				.getResult();
		if (model.isSuccess()) {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Password Updated Succesfully");
		}
		return wrapper;
	}

}
