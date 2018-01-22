package com.amx.jax.userservice.service;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import static com.amx.amxlib.constant.NotificationConstants.*;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.ChangeType;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxNotificationService {

	@Autowired
	private PostManService postManService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Async
	public void sendTransactionNotification(RemittanceReceiptSubreport remittanceReceiptSubreport, PersonInfo pinfo) {

		logger.info("Sending txn notification to customer");
		Email email = new Email();
		email.setSubject("Your transaction on AMX is successful");
		email.addTo(pinfo.getEmail());
		email.setTemplate(Templates.TXN_CRT_SUCC);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		File file = new File();
		file.setTemplate(Templates.REMIT_RECEIPT);
		file.setType(File.Type.PDF);
		file.getModel().put(RESP_DATA_KEY, remittanceReceiptSubreport);

		email.addFile(file);

		try {
			postManService.sendEmail(email);
		} catch (UnirestException e) {
			logger.error("error in sendTransactionNotification", e);
		}
	}

	// to send profile (password, security question, image, mobile) change
	// notification
	@Async
	public void sendProfileChangeNotificationEmail(CustomerModel customerModel, PersonInfo pinfo) {

		logger.info("Sending Profile change notification to customer : " + pinfo.getFirstName());

		Email email = new Email();

		if (customerModel.getPassword() != null) {
			email.setSubject("Change Password Success");
			email.getModel().put("change_type", ChangeType.PASSWORD_CHANGE);

		} else if (customerModel.getSecurityquestions() != null) {
			email.setSubject("Update Security Credentials Success");
			email.getModel().put("change_type", ChangeType.SECURITY_QUESTION_CHANGE);

		} else if (customerModel.getImageUrl() != null) {
			email.setSubject("Update Security Credentials Success");
			email.getModel().put("change_type", ChangeType.IMAGE_CHANGE);

		} else if (customerModel.getMobile() != null) {
			email.setSubject("Change mobile Success");
			email.getModel().put("change_type", ChangeType.MOBILE_CHANGE);
			
		}else if (customerModel.getEmail() != null) {
			email.setSubject("Change email Success");
			email.getModel().put("change_type", ChangeType.EMAIL_CHANGE);
		}

		email.addTo(pinfo.getEmail());
		email.setTemplate(Templates.PROFILE_CHANGE);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		try {
			postManService.sendEmail(email);
		} catch (UnirestException e) {
			logger.error("error in sendProfileChangedNotification", e);
		}
	} // end of sendProfileChangeNotificationEmail

	@Async
	public void sendOtpSms(PersonInfo pinfo, CivilIdOtpModel model) {

		logger.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", pinfo.getFirstName(),
				pinfo.getMobile()));

		SMS sms = new SMS();
		sms.addTo(pinfo.getMobile());
		sms.getModel().put(RESP_DATA_KEY, model);
		sms.setTemplate(Templates.RESET_OTP_SMS);

		try {
			postManService.sendSMS(sms);
			sms.setMessage(String.format("%s-%s  %s-%s", model.getmOtpPrefix(), model.getmOtp(), model.geteOtpPrefix(),
					model.geteOtp()));
			postManService.notifySlack(sms);
		} catch (UnirestException e) {
			logger.error("error in sendOtpSms", e);
		}
	} // end of sendOtpSms

	@Async
	public void sendOtpEmail(PersonInfo pinfo, CivilIdOtpModel civilIdOtpModel) {
	
		logger.info("Sending OTP Email to customer : " + pinfo.getFirstName());

		Email email = new Email();
		email.setSubject("OTP Email");
		email.addTo(pinfo.getEmail());
		email.setTemplate(Templates.PROFILE_CHANGE);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		try {
			postManService.sendEmail(email);
		} catch (UnirestException e) {
			logger.error("error in sendOtpEmail", e);
		}		
	}// end of sendOtpEmail

	@Async
	public void sendNewRegistrationSuccessEmailNotification(PersonInfo pinfo, String emailid) {
		Email email = new Email();
		email.setSubject(REG_SUC);
		email.addTo(emailid);
		email.setTemplate(Templates.REG_SUC);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);
	}
}