package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerCredential;
import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerModelInterface.ICustomerModel;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.PartialRegService;
import com.amx.jax.ui.service.RegistrationService;
import com.amx.jax.ui.session.Transactions;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller responsible for providing online registration of offline user
 * 
 * @author lalittanwar
 *
 */
@RestController
@Api(value = "Registration APIs")
public class RegisterController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	Transactions transactions;

	/**
	 * @param civilid
	 * @return
	 */
	@ApiOperation(value = "Verify KYC and sneds OTP to registered Mobile")
	@RequestMapping(value = "/pub/register/verifyid", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> verifyID(@RequestParam String civilid) {
		return registrationService.validateCustomer(civilid);
	}

	@ApiOperation(value = "Customer Activation", notes = "${RegisterController.verifyCustomer}")
	@RequestMapping(value = "/pub/register/verifycustomer", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> verifyCustomer(@RequestBody AuthData authData) {
		return registrationService.validateCustomer(authData.getIdentity(), authData.getmOtp(), authData.getAnswer());
	}

	/**
	 * Verifies OTP for civilID
	 * 
	 * @param civilid
	 * @param otp
	 * @param request
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/pub/register/verifycuser", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> verifyCUser(@RequestParam String civilid, @RequestParam String mOtp) {
		return registrationService.loginWithOtp(civilid, mOtp);
	}

	@RequestMapping(value = "/pub/register/secques", method = { RequestMethod.GET })
	public ResponseWrapper<UserUpdateData> getSecQues(HttpServletRequest request) {
		return registrationService.getSecQues(true);
	}

	/**
	 * @param securityquestions
	 * @return
	 */
	@RequestMapping(value = "/pub/register/secques", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regSecQues(@RequestBody UserUpdateData userUpdateData) {
		return registrationService.updateSecQues(userUpdateData.getSecQuesAns(), userUpdateData.getmOtp(),
				userUpdateData.geteOtp());
	}

	/**
	 * @param imageUrl
	 * @param caption
	 * @param mOtp
	 * @param eOtp
	 * @return
	 */
	@RequestMapping(value = "/pub/register/phising", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regPhising(@RequestParam String imageUrl, @RequestParam String caption,
			@RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return registrationService.updatePhising(imageUrl, caption, mOtp, eOtp);
	}

	/**
	 * @param loginId
	 * @param password
	 * @param mOtp
	 * @param eOtp
	 * @return
	 */
	@RequestMapping(value = "/pub/register/creds", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Deprecated
	public ResponseWrapper<UserUpdateData> regLoginIdAndPassword(@RequestParam String loginId,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp,
			@RequestParam(required = false) String email) {
		return registrationService.setCredentials(loginId, password, mOtp, eOtp, email, true);
	}

	@RequestMapping(value = "/pub/register/creds/**", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regLoginIdAndPasswordJSON(@RequestBody ICustomerModel customerCredential) {
		return registrationService.setCredentials(customerCredential.getLoginId(), customerCredential.getPassword(),
				customerCredential.getMotp(), customerCredential.getEotp(), customerCredential.getEmail(), true);
	}

	@Autowired
	private PartialRegService partialRegService;

	@RequestMapping(value = "/pub/register/new/init", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> partialReg(@RequestBody CustomerPersonalDetail personalDetail) {
		return transactions.start(partialRegService.newUserRegisterInit(personalDetail));
	}

	@RequestMapping(value = "/pub/register/new/verify", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> partialReg(@RequestParam String mOtp, @RequestParam String eOtp) {
		transactions.track();
		return partialRegService.newUserRegisterValidate(mOtp, eOtp);
	}

	@RequestMapping(value = "/pub/register/new/address", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> saveHomeAddress(@RequestBody CustomerHomeAddress customerHomeAddress) {
		transactions.track();
		return partialRegService.saveHomeAddress(customerHomeAddress);
	}

	@RequestMapping(value = "/pub/register/new/secques", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> regNewSecQues(@RequestBody UserUpdateData userUpdateData) {
		transactions.track();
		return partialRegService.updateSecQues(userUpdateData.getSecQuesAns());
	}

	@RequestMapping(value = "/pub/register/new/phising", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regNewPhising(@RequestParam String imageUrl, @RequestParam String caption) {
		transactions.track();
		return partialRegService.updatePhising(imageUrl, caption);
	}

	@RequestMapping(value = "/pub/register/new/creds", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regNewLoginIdAndPassword(
			@RequestBody CustomerCredential customerCredential) {
		transactions.track();
		return partialRegService.setCredentials(customerCredential);
	}

}
