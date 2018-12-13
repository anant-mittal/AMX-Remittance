package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerVerificationRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.Random;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerDataVerificationService extends AbstractService {

	Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private CustomerDao custDao;

	@Autowired
	BeneficiaryService beneService;
	
	@Autowired
	TransactionHistroyService txnhistoryService;

	@Autowired
	UserService userService;

	@Autowired
	MetaData metaData;

	@Autowired
	CustomerVerificationRepository customerVerificationRepository;

	@Autowired
	JaxUtil util;

	@Autowired
	JaxNotificationService jaxNotificationService;

	public void setAdditionalData(List<QuestModelDTO> list) {

		beneService.setBeneDataVerificationQuestion(list);
	}

	public ApiResponse saveVerificationData(CustomerModel model) {
		if (metaData.getCustomerId() == null) {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID.getStatusKey(), "Null customer id passed ");
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(metaData.getCustomerId());
		ApiResponse response = getBlackApiResponse();
		saveVerificationData(model.getVerificationAnswers());
		CustomerModel responseModel = userService.convert(onlineCustomer);

		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	private void saveVerificationData(List<SecurityQuestionModel> verificationAnswers) {
		CustomerVerification cv = customerVerificationRepository
				.findBycustomerIdAndVerificationType(metaData.getCustomerId(), "EMAIL");
		verificationAnswers.forEach(i -> {
			boolean verificationDone = false;
			if (i.getQuestionSrNo().equals(BigDecimal.ONE)) {
				Customer customerInfo= custDao.getCustById(metaData.getCustomerId());
				String mobileNumber = customerInfo.getMobile();
				String actualAnswer = mobileNumber.substring(mobileNumber.length() - 4);
				String givenAnswer = i.getAnswer();
				logger.info(
						"Q1: in saveVerificationData. actualAnswer: " + actualAnswer + " givenAnswer: " + givenAnswer);
				if (actualAnswer.equalsIgnoreCase(givenAnswer)) {
					verificationDone = true;
				}
			}
			
			if (i.getQuestionSrNo().equals(new BigDecimal(2))) {
				Customer customerInfo= custDao.getCustById(metaData.getCustomerId());
				Date identityExpiry = customerInfo.getIdentityExpiredDate();
				
				String givenAnswer = i.getAnswer();
				Date givenDate = com.amx.jax.util.DateUtil.convertStringToDate(givenAnswer);
				logger.info(
						"Q2: in saveVerificationData. identityExpiry: " + identityExpiry + " givenDate: " + givenDate);
				
				if (DateUtils.isSameDay(identityExpiry, givenDate)) {
					verificationDone = true;
				}
			}
			
			if (verificationDone) {
				if(StringUtils.isNotBlank(cv.getFieldValue())) {
					userService.updateEmail(metaData.getCustomerId(), cv.getFieldValue());
				}
				updateCustomerVerification(cv);
			}
		});
		if (ConstantDocument.No.equals(cv.getVerificationStatus())) {
			// sendTpinTocustomer(cv);
		}
	}

	private void sendTpinTocustomer(CustomerVerification cv) {
		String tpin = Random.randomNumeric(6);
		logger.info("generated tpin/otp for customer data verification is: " + tpin);
		PersonInfo pinfo = userService.getPersonInfo(metaData.getCustomerId());
		CivilIdOtpModel model = new CivilIdOtpModel();
		model.setmOtp(tpin);
		model.setmOtpPrefix("");
		jaxNotificationService.sendOtpSms(pinfo, model);
		customerVerificationRepository.save(cv);
	}

	private void updateCustomerVerification(CustomerVerification cv) {
		cv.setVerificationBy("JOMAX_USER");
		cv.setVerificationDate(new Date());
		cv.setVerificationStatus("Y");
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
