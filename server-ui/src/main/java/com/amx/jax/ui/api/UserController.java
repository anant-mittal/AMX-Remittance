
package com.amx.jax.ui.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.ResponseStatus;
import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.UserMetaData;
import com.amx.jax.ui.response.UserUpdateData;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "User APIs")
public class UserController {

	@Autowired
	private UserSession userSession;

	@Autowired
	private LoginService loginService;

	@Autowired
	private JaxService jaxService;

	/**
	 * Asks for user login and password
	 * 
	 * @param identity
	 * @param password
	 * @return
	 */
	@ApiOperation(value = "Validates login/pass but does not creates session")
	@RequestMapping(value = "/pub/user/login", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> login(@RequestParam String identity, @RequestParam String password) {
		return loginService.login(identity, password);
	}

	@ApiOperation(value = "Logins User & creates session")
	@RequestMapping(value = "/pub/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> loginSecQues(@RequestBody SecurityQuestionModel guestanswer,
			HttpServletRequest request) {
		return loginService.loginSecQues(guestanswer, request);
	}

	@ApiOperation(value = "Sends OTP and resets password")
	@RequestMapping(value = "/pub/user/reset", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> reset(@RequestParam String identity, @RequestParam(required = false) String otp) {
		return loginService.reset(identity, otp);
	}

	@ApiOperation(value = "Logout User & Terminates session")
	@RequestMapping(value = "/pub/user/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		userSession.setValid(false);
		userSession.setCustomerModel(null);
		SecurityContextHolder.getContext().setAuthentication(null);

		wrapper.setMessage(ResponseStatus.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

	@ApiOperation(value = "Get UserMeta Data")
	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> getMeta() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		wrapper.getData().setValid(userSession.isValid());

		if (userSession.getCustomerModel() != null) {
			wrapper.getData().setActive(true);
		}
		return wrapper;
	}

	@ApiOperation(value = "Sends OTP and resets password")
	@RequestMapping(value = "/api/user/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> changePassword(String password) {
		return loginService.updatepwd(password);
	}

	@ApiOperation(value = "Returns transaction history")
	@RequestMapping(value = "/api/user/tranx/history", method = { RequestMethod.POST })
	public ResponseWrapper<List<TransactionHistroyDTO>> tranxhistory(String password) {
		ResponseWrapper<List<TransactionHistroyDTO>> wrapper = new ResponseWrapper<List<TransactionHistroyDTO>>(
				jaxService.setDefaults().getRemitClient().getTransactionHistroy("2017", null, null, null).getResults());
		return wrapper;
	}

}
