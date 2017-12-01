package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_ENDPOINT;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.util.ConverterUtility;

@Component
public class UserClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private ConverterUtility util;

	public ApiResponse<CustomerModel> validateOtp(String civilId, String otp) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			log.info("calling validateOtp api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + civilId + "/validate-otp/?otp="
					+ otp;
			response = restTemplate.exchange(validateOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
		} catch (Exception e) {
			log.error("exception in validateOtp ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CivilIdOtpModel> sendOtpForCivilId(String civilId) {
		ResponseEntity<ApiResponse<CivilIdOtpModel>> response = null;
		try {
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT + "/" + civilId + "/send-otp/";
			log.info("calling sendOtpForCivilId api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			log.info("responce from  sendOtpForCivilId api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in sendOtpForCivilId ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CustomerModel> saveCustomer(String json) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			HttpEntity<String> requestEntity = new HttpEntity<String>(json, getHeader());
			String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
			log.info("calling saveCustomer api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			log.info("responce from  saveCustomer api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in saveCustomer ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CustomerModel> saveSecurityQuestions(List<SecurityQuestionModel> securityquestions) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setSecurityquestions(securityquestions);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(custModel), getHeader());
			String sendOtpUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
			log.info("calling saveSecurityQuestions api: " + sendOtpUrl);
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			log.info("responce from  saveSecurityQuestions api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in saveSecurityQuestions ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CustomerModel> savePhishiingImage(String caption, String imageUrl) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setCaption(caption);
			custModel.setImageUrl(imageUrl);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(custModel), getHeader());
			String saveCustUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
			log.info("calling savePhishiingImage api: " + saveCustUrl);
			response = restTemplate.exchange(saveCustUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			log.info("responce from  savePhishiingImage api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in savePhishiingImage ", e);
		}
		return response.getBody();
	}

	public ApiResponse<CustomerModel> saveLoginIdAndPassword(String loginId, String password) {
		ResponseEntity<ApiResponse<CustomerModel>> response = null;
		try {
			CustomerModel custModel = new CustomerModel();
			custModel.setLoginId(loginId);
			custModel.setPassword(password);
			custModel.setCustomerId(jaxMetaInfo.getCustomerId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(custModel), getHeader());
			String saveCustUrl = baseUrl.toString() + CUSTOMER_ENDPOINT;
			log.info("calling saveLoginIdAndPassword api: " + saveCustUrl);
			response = restTemplate.exchange(saveCustUrl, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			log.info("responce from  saveLoginIdAndPassword api: " + util.marshall(response.getBody()));
		} catch (Exception e) {
			log.error("exception in saveLoginIdAndPassword ", e);
		}
		return response.getBody();
	}

}
