package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.UPDATE_CUSTOMER_PASSWORD_ENDPOINT;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.services.CustomerDataVerificationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(CUSTOMER_ENDPOINT)
@SuppressWarnings("rawtypes")
public class CustomerController {

	@Autowired
	private ConverterUtil converterUtil;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerDataVerificationService customerDataVerificationService;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
   @RequestMapping(value = "/logged/in/", method = RequestMethod.POST)
    public ApiResponse loginUser(@RequestBody CustomerModel customerModel) {
        logger.info("loginUser Request: usreid: " + customerModel.getLoginId());
        ApiResponse response = userService.customerLoggedIn(customerModel);
        return response;
    }

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse saveCust(@RequestBody CustomerModel customerModel) {
		logger.debug("saveCust Request:" + customerModel);
		ApiResponse response = userService.saveCustomer(customerModel);
		return response;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ApiResponse save(@RequestBody CustomerModel customerModel) {
		logger.debug("saveCust Request:" + customerModel.toString());
		ApiResponse response = userService.saveCustomer(customerModel);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/send-otp/", method = RequestMethod.GET)
	public ApiResponse sendOtp(@PathVariable("civil-id") String civilId) {
		logger.debug("verifyCivilId Request:civilId" + civilId);
		ApiResponse response = userService.sendOtpForCivilId(civilId);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/send-reset-otp/", method = RequestMethod.GET)
	public ApiResponse sendResetCredentialsOtp(@PathVariable("civil-id") String civilId) {
		logger.debug("send Request:civilId" + civilId);
		List<CommunicationChannel> channel = new ArrayList<>();
		channel.add(CommunicationChannel.EMAIL);
		channel.add(CommunicationChannel.MOBILE);
		ApiResponse response = userService.sendOtpForCivilId(civilId, channel, null, null);
		return response;
	}

	@RequestMapping(value = "/send-otp/", method = RequestMethod.GET)
	public ApiResponse sendOtp() {
		logger.debug("in sendOtp Request");
		ApiResponse response = userService.sendOtpForCivilId(null);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/validate-otp/", method = RequestMethod.GET)
	public ApiResponse validateOtp(@PathVariable("civil-id") String civilId, @RequestParam("mOtp") String mOtp,
			@RequestParam(name = "eOtp", required = false) String eOtp) {
		logger.debug("validateOtp Request:civilId" + civilId + " mOtp:" + mOtp + " eOtp:" + eOtp);
		ApiResponse response = userService.validateOtp(civilId, mOtp, eOtp);
		return response;
	}

	@RequestMapping(value = "/validate-otp/", method = RequestMethod.GET)
	public ApiResponse validateOtp(@RequestParam("mOtp") String mOtp,
			@RequestParam(name = "eOtp", required = false) String eOtp) {
		logger.debug("validateOtp Request:" + " mOtp:" + mOtp + " eOtp:" + eOtp);
		ApiResponse response = userService.validateOtp(null, mOtp, eOtp);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/checklist/", method = RequestMethod.GET)
	public ApiResponse getCheckList(@PathVariable("civil-id") String civilId) {
		logger.debug("getCheckList Request:civilId" + civilId);
		ApiResponse response = userService.getUserCheckList(civilId);
		return response;
	}

	@RequestMapping(value = "{customer-id}/random-questions/", method = RequestMethod.GET)
	public ApiResponse getRandomQuestion(@RequestParam Integer size, @PathVariable("customer-id") Integer customerId) {
		logger.debug("getCheckList Request: customerId" + customerId + " size= " + size);
		ApiResponse response = userService.generateRandomQuestions(size, customerId);
		return response;
	}

	@RequestMapping(value = "/validate-random-questions/", method = RequestMethod.POST)
	public ApiResponse validateCustomerData(@RequestBody CustomerModel model) {
		String json = converterUtil.marshall(model);
		logger.debug("validateCustomerData Request:" + json);
		ApiResponse response = userService.validateCustomerData(model);
		return response;
	}

	@RequestMapping(value = UPDATE_CUSTOMER_PASSWORD_ENDPOINT, method = RequestMethod.PUT)
	public ApiResponse updatePassword(@RequestBody CustomerModel model) {
		logger.debug("updatePassword Request: " + model.toString());
		ApiResponse response = userService.updatePassword(model);
		return response;
	}

	@RequestMapping(value = "/unlock/", method = RequestMethod.GET)
	public ApiResponse unlockCustomer() {
		logger.debug("in unlockCustomer Request ");
		ApiResponse response = userService.unlockCustomer();
		return response;
	}

	@RequestMapping(value = "/deactivate/", method = RequestMethod.GET)
	public ApiResponse deActivateCustomer() {
		logger.debug("in deActivateCustomer Request ");
		ApiResponse response = userService.deactivateCustomer();
		return response;
	}

	@RequestMapping(value = "/send-otp/", method = RequestMethod.POST)
	public ApiResponse sendResetEmailCredentialsOtp(@RequestBody CustomerModel custModel) {
		logger.debug("send Request:civilId" + custModel.toString());
		List<CommunicationChannel> channel = new ArrayList<>();
		channel.add(CommunicationChannel.EMAIL);
		channel.add(CommunicationChannel.MOBILE);

		if (custModel.getMobile() != null) {
			logger.info("Validating mobile for client id : " + custModel.getCustomerId());
			userService.validateMobile(custModel);
		}

		ApiResponse response = userService.sendOtpForCivilId(custModel.getIdentityId(), channel, custModel, null);
		return response;
	}

	@RequestMapping(value = "/unlock/{civilid}", method = RequestMethod.GET)
	public ApiResponse unlockCustomer(@PathVariable("civilid") String civilid) {
		logger.debug("in unlockCustomer Request ");
		ApiResponse response = userService.unlockCustomer(civilid);
		return response;
	}

	@RequestMapping(value = "/deactivate/{civilid}", method = RequestMethod.GET)
	public ApiResponse deActivateCustomer(@PathVariable("civilid") String civilid) {
		logger.debug("in deActivateCustomer Request ");
		ApiResponse response = userService.deactivateCustomer(civilid);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/{init-registration}/send-otp/", method = RequestMethod.GET)
	public ApiResponse initRegistrationSendOtp(@PathVariable("civil-id") String civilId,
			@PathVariable("init-registration") Boolean init) {
		logger.debug("initRegistrationSendOtp Request:civilId" + civilId);
		ApiResponse response = userService.sendOtpForCivilId(civilId, null, null, init);
		return response;
	}

	@RequestMapping(value = "/random-data-verification-questions/", method = RequestMethod.GET)
	public ApiResponse getCustomerDataValidationQeustions(@RequestParam Integer size) {
		logger.debug("getCustomerDataValidationQeustions Request:");
		ApiResponse<QuestModelDTO> response = userService.getDataVerificationRandomQuestions(size);
		customerDataVerificationService.setAdditionalData(response.getResults());
		return response;
	}
	
	@RequestMapping(value = "/random-data-verification-questions/", method = RequestMethod.POST)
	public ApiResponse saveDataVerificationQuestions(@RequestBody CustomerModel model) {
		logger.debug("in saveDataVerificationQuestions ");
		ApiResponse response = customerDataVerificationService.saveVerificationData(model);
		return response;
	}
}
