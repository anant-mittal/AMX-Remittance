package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.jax.CustomerCredential;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.dict.ContactType;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.config.UIServerError;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequest;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.PartialRegService;
import com.amx.jax.ui.service.RegistrationService;
import com.amx.jax.ui.session.Transactions;
import com.amx.libjax.model.CustomerModelInterface.ICustomerModel;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller responsible for providing online registration of offline user.
 *
 * @author lalittanwar
 */
@RestController
@Api(value = "Registration APIs")
public class RegisterController {

	/** The registration service. */
	@Autowired
	private RegistrationService registrationService;

	/** The transactions. */
	@Autowired
	Transactions transactions;

	@Autowired
	private JaxService jaxService;

	/**
	 * Verify ID.
	 *
	 * @param civilid the civilid
	 * @return the response wrapper
	 */
	@Deprecated
	@ApiOperation(value = "Verify KYC and sneds OTP to registered Mobile")
	@RequestMapping(value = "/pub/register/verifyid", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> verifyID(@RequestParam String civilid) {
		return registrationService.validateCustomer(civilid);
	}

	@ApiOperation(value = "Verify KYC and sneds OTP to registered Mobile")
	@RequestMapping(value = "/pub/register/verifyid/v2", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> verifyID(@RequestParam String identity,
			@RequestParam(required = false) ContactType contactType) {
		String otp = JaxAuthContext.getOtp();

		if (ArgUtil.isEmpty(contactType)) {
			AmxApiError amxApiError = new AmxApiError(OWAStatusStatusCodes.CONTACT_TYPE_REQUIRED);
			amxApiError.setMeta(
					jaxService.setDefaults().getUserclient().getCustomerModelSignupResponse(identity).getResult());
			throw new UIServerError(amxApiError);
		} else if (ArgUtil.isEmpty(otp)) {
			AuthData x = registrationService.validateCustomerInit(identity, contactType).getData();
			AmxApiError amxApiError = new AmxApiError(OWAStatusStatusCodes.OTP_REQUIRED);
			amxApiError.setMeta(x);
			throw new UIServerError(amxApiError);
		} else {
			return registrationService.validateCustomer(identity, otp, contactType);
		}
	}

	/**
	 * Verify customer.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@Deprecated
	@ApiOperation(value = "Customer Activation", notes = "${RegisterController.verifyCustomer}")
	@RequestMapping(value = "/pub/register/verifycustomer", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> verifyCustomer(@RequestBody AuthData authData) {
		return registrationService.validateCustomer(authData.getIdentity(), authData.getmOtp(), authData.getAnswer());
	}

	/**
	 * Verify customer.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@Deprecated
	@ApiOperation(value = "Customer Activation", notes = "${RegisterController.verifyCustomer}")
	@RequestMapping(value = "/pub/register/verifycustomer/v2", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> verifyCustomer(@RequestParam String identity, @RequestParam String otp,
			@RequestParam ContactType communicationChannel) {
		return registrationService.validateCustomer(identity, otp, communicationChannel);
	}

	/**
	 * Verifies OTP for civilID.
	 *
	 * @param civilid the civilid
	 * @param mOtp    the m otp
	 * @return the response wrapper
	 */
	@Deprecated
	@RequestMapping(value = "/pub/register/verifycuser", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> verifyCUser(@RequestParam String civilid, @RequestParam String mOtp) {
		return registrationService.loginWithOtp(civilid, mOtp);
	}

	/**
	 * Gets the sec ques.
	 *
	 * @param request the request
	 * @return the sec ques
	 */
	@RequestMapping(value = "/pub/register/secques", method = { RequestMethod.GET })
	public ResponseWrapper<UserUpdateData> getSecQues(HttpServletRequest request) {
		return registrationService.getSecQues(true);
	}

	/**
	 * Reg sec ques.
	 *
	 * @param userUpdateData the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/secques", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regSecQues(@RequestBody UserUpdateData userUpdateData) {
		return registrationService.updateSecQues(userUpdateData.getSecQuesAns(), userUpdateData.getmOtp(),
				userUpdateData.geteOtp());
	}

	/**
	 * Reg phising.
	 *
	 * @param imageUrl the image url
	 * @param caption  the caption
	 * @param mOtp     the m otp
	 * @param eOtp     the e otp
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/phising", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> regPhising(@RequestParam String imageUrl, @RequestParam String caption,
			@RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return registrationService.updatePhising(imageUrl, caption, mOtp, eOtp);
	}

	/**
	 * Reg login id and password.
	 *
	 * @param loginId  the login id
	 * @param password the password
	 * @param mOtp     the m otp
	 * @param eOtp     the e otp
	 * @param email    the email
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/creds", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Deprecated
	public ResponseWrapper<UserUpdateData> regLoginIdAndPassword(@RequestParam String loginId,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp,
			@RequestParam(required = false) String email) {
		return registrationService.setCredentials(loginId, password, mOtp, eOtp, email, true);
	}

	/**
	 * Reg login id and password JSON.
	 *
	 * @param customerCredential the customer credential
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/creds/**", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regLoginIdAndPasswordJSON(@RequestBody ICustomerModel customerCredential) {
		return registrationService.setCredentials(customerCredential.getLoginId(), customerCredential.getPassword(),
				customerCredential.getMotp(), customerCredential.getEotp(), customerCredential.getEmail(), true);
	}

	/** The partial reg service. */
	@Autowired
	private PartialRegService partialRegService;

	/**
	 * Partial reg.
	 *
	 * @param personalDetail the personal detail
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/new/init", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> partialReg(@RequestBody CustomerPersonalDetail personalDetail) {
		return transactions.start(partialRegService.newUserRegisterInit(personalDetail));
	}

	/**
	 * Partial reg.
	 *
	 * @param mOtp the m otp
	 * @param eOtp the e otp
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/new/verify", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> partialReg(@RequestParam String mOtp, @RequestParam String eOtp) {
		transactions.track();
		return partialRegService.newUserRegisterValidate(mOtp, eOtp);
	}

	/**
	 * Save home address.
	 *
	 * @param customerHomeAddress the customer home address
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/new/address", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> saveHomeAddress(@RequestBody CustomerHomeAddress customerHomeAddress) {
		transactions.track();
		return partialRegService.saveHomeAddress(customerHomeAddress);
	}

	/**
	 * Reg new sec ques.
	 *
	 * @param userUpdateData the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/new/secques", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> regNewSecQues(@RequestBody UserUpdateData userUpdateData) {
		transactions.track();
		return partialRegService.updateSecQues(userUpdateData.getSecQuesAns());
	}

	/**
	 * Reg new phising.
	 *
	 * @param imageUrl the image url
	 * @param caption  the caption
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/new/phising", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> regNewPhising(@RequestParam String imageUrl, @RequestParam String caption) {
		transactions.track();
		return partialRegService.updatePhising(imageUrl, caption);
	}

	/**
	 * Reg new login id and password.
	 *
	 * @param customerCredential the customer credential
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/register/new/creds", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> regNewLoginIdAndPassword(
			@RequestBody CustomerCredential customerCredential) {
		transactions.track();
		return partialRegService.setCredentials(customerCredential);
	}

}
