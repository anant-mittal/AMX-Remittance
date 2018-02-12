package com.amx.jax.userservice.service;

import static com.amx.amxlib.constant.NotificationConstants.REG_SUC;
import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.ChangeType;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxNotificationService {

	@Autowired
	private PostManService postManService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String SUBJECT_ACCOUNT_UPDATE = "Account Update";

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
		logger.info("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getFirstName());
		sendEmail(email);
	}

	// to send profile (password, security question, image, mobile) change
	// notification
	public void sendProfileChangeNotificationEmail(CustomerModel customerModel, PersonInfo pinfo) {

		logger.info("Sending Profile change notification to customer : " + pinfo.getFirstName());

		Email email = new Email();
		Email emailToOld = null;

		if (customerModel.getPassword() != null) {
			email.setSubject("Change Password Success");
			email.getModel().put("change_type", ChangeType.PASSWORD_CHANGE);

		} else if (customerModel.getSecurityquestions() != null) {
			email.setSubject(SUBJECT_ACCOUNT_UPDATE);
			email.getModel().put("change_type", ChangeType.SECURITY_QUESTION_CHANGE);

		} else if (customerModel.getImageUrl() != null) {
			email.setSubject(SUBJECT_ACCOUNT_UPDATE);
			email.getModel().put("change_type", ChangeType.IMAGE_CHANGE);

		} else if (customerModel.getMobile() != null) {
			email.setSubject(SUBJECT_ACCOUNT_UPDATE);
			email.getModel().put("change_type", ChangeType.MOBILE_CHANGE);

		} else if (customerModel.getEmail() != null) {
			email.setSubject(SUBJECT_ACCOUNT_UPDATE);
			email.getModel().put("change_type", ChangeType.EMAIL_CHANGE);

			emailToOld = new Email();
			emailToOld.setSubject(SUBJECT_ACCOUNT_UPDATE);
			emailToOld.getModel().put("change_type", ChangeType.EMAIL_CHANGE);
			emailToOld.addTo(customerModel.getEmail());
			emailToOld.setTemplate(Templates.PROFILE_CHANGE);
			emailToOld.setHtml(true);

			PersonInfo oldPinfo = null;
			try {
				oldPinfo = (PersonInfo) pinfo.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			oldPinfo.setEmail(customerModel.getEmail());
			emailToOld.getModel().put(RESP_DATA_KEY, oldPinfo);
			logger.info("Email change notification to - " + oldPinfo.getFirstName() + " on email id : "
					+ oldPinfo.getEmail());
			sendEmail(emailToOld);
		}

		email.addTo(pinfo.getEmail());
		email.setTemplate(Templates.PROFILE_CHANGE);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);
		logger.info("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getFirstName());
		sendEmail(email);
	} // end of sendProfileChangeNotificationEmail

	public void sendOtpSms(PersonInfo pinfo, CivilIdOtpModel model) {

		logger.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", pinfo.getFirstName(),
				pinfo.getMobile()));

		SMS sms = new SMS();
		sms.addTo(pinfo.getMobile());
		sms.getModel().put(RESP_DATA_KEY, model);
		sms.setTemplate(Templates.RESET_OTP_SMS);

		try {
			postManService.sendSMS(sms);
			sendToSlack("mobile", model.getmOtpPrefix(), model.getmOtp());
		} catch (UnirestException e) {
			logger.error("error in sendOtpSms", e);
		}
	} // end of sendOtpSms

	public void sendOtpEmail(PersonInfo pinfo, CivilIdOtpModel civilIdOtpModel) {

		logger.info("Sending OTP Email to customer : " + pinfo.getFirstName());

		Email email = new Email();
		email.setSubject("OTP Email");
		email.addTo(pinfo.getEmail());
		email.setTemplate(Templates.RESET_OTP);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, civilIdOtpModel);

		logger.info("Email to - " + pinfo.getEmail() + " first name : " + civilIdOtpModel.getFirstName());
		sendEmail(email);
		sendToSlack("email", civilIdOtpModel.geteOtpPrefix(), civilIdOtpModel.geteOtp());

	}// end of sendOtpEmail

	public void sendNewRegistrationSuccessEmailNotification(PersonInfo pinfo, String emailid) {
		Email email = new Email();
		email.setSubject(REG_SUC);
		email.addTo(pinfo.getEmail());
		email.setTemplate(Templates.REG_SUC);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		logger.info("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getFirstName());
		sendEmail(email);
	}

	public void sendToSlack(String channel, String prefix, String otp) {
		Message msg = new Message();
		msg.setMessage(String.format("%s = %s-%s", channel, prefix, otp));
		try {
			postManService.notifySlack(msg);
		} catch (UnirestException e) {
			logger.error("error in SlackNotify", e);
		}
	}

	private void sendEmail(Email email) {
		try {
			postManService.sendEmailAsync(email);
		} catch (UnirestException e) {
			logger.error("error in sendProfileChangedNotification", e);
		}
	}

}
