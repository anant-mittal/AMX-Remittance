package com.amx.jax.services;

import static com.amx.amxlib.constant.NotificationConstants.BRANCH_SEARCH;
import static com.amx.amxlib.constant.NotificationConstants.REG_SUC;
import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.model.BranchSearchNotificationModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.EmployeeInfo;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.notification.RemittanceTransactionFailureAlertModel;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.dict.Tenant;
import com.amx.jax.model.response.fx.FxDeliveryDetailNotificationDto;
import com.amx.jax.model.response.fx.FxOrderDetailNotificationDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.ChangeType;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.CollectionUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxNotificationService {

	@Autowired
	private PostManService postManService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String SUBJECT_ACCOUNT_UPDATE = "Account Update";
	private final String SUBJECT_EMAIL_CHANGE = "Al Mulla Exchange Account - Email ID Change";
	private final String SUBJECT_PHONE_CHANGE = "Al Mulla Exchange Account - Phone Number Change";

	public void sendTransactionNotification(RemittanceReceiptSubreport remittanceReceiptSubreport, PersonInfo pinfo) {

		logger.debug("Sending txn notification to customer");
		Email email = new Email();

		if (TenantContextHolder.currentSite().equals(Tenant.KWT)) {
			email.setSubject("Your transaction on AMX is successful");
		} else if (TenantContextHolder.currentSite().equals(Tenant.BHR)) {
			email.setSubject("Your transaction on MEC is successful");
		}else if (TenantContextHolder.currentSite().equals(Tenant.OMN)) {
		    email.setSubject("Your transaction on Modern Exchange - Oman is successful");
		}

		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.TXN_CRT_SUCC);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		File file = new File();
		file.setITemplate(TemplatesMX.REMIT_RECEIPT_JASPER);
		file.setName("TransactionReceipt");
		file.setType(File.Type.PDF);
		file.getModel().put(RESP_DATA_KEY, remittanceReceiptSubreport);

		email.addFile(file);
		logger.debug("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getFirstName());
		sendEmail(email);
	}

	public void sendTransactionNotification(FxOrderReportResponseDto remittanceReceiptSubreport,
			FxOrderDetailNotificationDto pinfo) {

		logger.debug("Sending txn notification to customer");
		Email email = new Email();

		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.FC_KNET_SUCCESS);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		File file = new File();
		file.setITemplate(TemplatesMX.FXO_RECEIPT);
		file.setType(File.Type.PDF);
		file.getModel().put(Message.RESULTS_KEY, CollectionUtil.getList(remittanceReceiptSubreport));

		email.addFile(file);
		logger.debug("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getCustomerName());
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
			email.setSubject(SUBJECT_PHONE_CHANGE);
			email.getModel().put("change_type", ChangeType.MOBILE_CHANGE);

		} else if (customerModel.getEmail() != null) {
			email.setSubject(SUBJECT_EMAIL_CHANGE);
			email.getModel().put("change_type", ChangeType.EMAIL_CHANGE);

			emailToOld = new Email();
			emailToOld.setSubject(SUBJECT_EMAIL_CHANGE);
			emailToOld.getModel().put("change_type", ChangeType.EMAIL_CHANGE);
			emailToOld.addTo(customerModel.getEmail());
			emailToOld.setITemplate(TemplatesMX.EMAIL_CHANGE_OLD_EMAIL);
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
		email.setITemplate(TemplatesMX.PROFILE_CHANGE);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);
		logger.info("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getFirstName());
		sendEmail(email);
	} // end of sendProfileChangeNotificationEmail
	
	public void sendProfileChangeNotificationMobile(CustomerModel customerModel, PersonInfo personinfo, String oldMobile) {
		if (customerModel.getMobile() != null) {
			SMS smsOld = new SMS();
			// to new and old
			smsOld.addTo(oldMobile);
			smsOld.getModel().put(RESP_DATA_KEY, personinfo);
			smsOld.setITemplate(TemplatesMX.PROFILE_CHANGE_SMS);

			try {
				postManService.sendSMSAsync(smsOld);
			} catch (PostManException e) {
				logger.error("error in sendProfileChangeNotificationMobile", e);
			}

			SMS smsNew = new SMS();
			// to new and old
			smsNew.addTo(customerModel.getMobile());
			smsNew.getModel().put(RESP_DATA_KEY, personinfo);
			smsNew.setITemplate(TemplatesMX.PROFILE_CHANGE_SMS);

			try {
				postManService.sendSMSAsync(smsNew);
			} catch (PostManException e) {
				logger.error("error in sendProfileChangeNotificationMobile", e);
			}
		}
	}

	public void sendOtpSms(PersonInfo pinfo, CivilIdOtpModel model) {
		sendOtpSms(pinfo, model, TemplatesMX.RESET_OTP_SMS);
	}

	public void sendOtpSms(PersonInfo pinfo, CivilIdOtpModel model, TemplatesMX templateMX) {

		logger.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", pinfo.getFirstName(),
				pinfo.getMobile()));

		SMS sms = new SMS();
		sms.addTo(pinfo.getMobile());
		sms.getModel().put(RESP_DATA_KEY, model);
		sms.setITemplate(templateMX);

		try {
			postManService.sendSMSAsync(sms);
		} catch (PostManException e) {
			logger.error("error in sendOtpSms", e);
		}
	} // end of sendOtpSms

	public void sendOtpSms(String mobile, FxDeliveryDetailNotificationDto model) {
		SMS sms = new SMS();
		sms.addTo(mobile);
		sms.getModel().put(RESP_DATA_KEY, model);
		sms.setITemplate(TemplatesMX.FC_DELIVER_SMS_OTP);

		try {
			postManService.sendSMSAsync(sms);
		} catch (PostManException e) {
			logger.error("error in sendOtpSms", e);
		}
	}

	public void sendOtpEmail(PersonInfo pinfo, CivilIdOtpModel civilIdOtpModel) {

		logger.info("Sending OTP Email to customer : " + pinfo.getFirstName());

		Email email = new Email();
		email.setSubject("OTP Email");
		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.RESET_OTP);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, civilIdOtpModel);

		logger.info("Email to - " + pinfo.getEmail() + " first name : " + civilIdOtpModel.getFirstName());
		sendEmail(email);

	}// end of sendOtpEmail

	public void sendNewRegistrationSuccessEmailNotification(PersonInfo pinfo, String emailid) {
		Email email = new Email();
		email.setSubject(REG_SUC);
		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.REG_SUC);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		logger.info("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getFirstName());
		sendEmail(email);
	}
	
	@Async(ExecutorConfig.EXECUTER_TASK)
	public void sendEmail(Email email) {
		try {
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
			logger.error("error in sendProfileChangedNotification", e);
		}
	}

	public void sendBranchSearchEmailNotification(BranchSearchNotificationModel model, String emailid) {
		Email email = new Email();
		email.setSubject(BRANCH_SEARCH);
		email.addTo(emailid);
		email.setITemplate(TemplatesMX.BRANCH_SEARCH_EMPTY);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, model);

		logger.info("Email to - " + emailid + " first name : " + model.getCustomerName());
		sendEmail(email);
	}

	public void sendErrorEmail(RemittanceTransactionFailureAlertModel model,
			List<ExEmailNotification> emailNotification) {
		try {
			for (ExEmailNotification emailNot : emailNotification) {
				String emailid = emailNot.getEmailId();
				Email email = new Email();
				email.setSubject(BRANCH_SEARCH);
				email.addTo(emailid);
				email.setITemplate(TemplatesMX.TRANSACTION_FAILURE);
				email.setHtml(true);
				email.getModel().put(RESP_DATA_KEY, model);
				sendEmail(email);
			}
		} catch (Exception e) {
			logger.error("error in sendErrormail", e);
		}
	}

	public void sendPartialRegistraionMail(PersonInfo personinfo, ApplicationSetup applicationSetup) {

		logger.info("Sending Email to Team Of The Customer : " + personinfo.getFirstName());
		try {
			Email email = new Email();
			email.setSubject("New Customer Registration");
			email.addTo(applicationSetup.getEmailId());
			email.setITemplate(TemplatesMX.PARTIAL_REGISTRATION_EMAIL);
			email.setHtml(true);
			email.getModel().put(RESP_DATA_KEY, personinfo);
			sendEmail(email);
		} catch (Exception e) {
			logger.error("error in sendErrormail", e);
		}

	}

	// employee otp to login
	public void sendOtpSms(EmployeeInfo einfo, CivilIdOtpModel model) {

		logger.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", einfo.getEmployeeName(),
				einfo.getTelephoneNumber()));

		SMS sms = new SMS();
		sms.addTo(einfo.getTelephoneNumber());
		sms.getModel().put(RESP_DATA_KEY, model);
		sms.setITemplate(TemplatesMX.RESET_OTP_SMS);

		try {
			postManService.sendSMSAsync(sms);
		} catch (PostManException e) {
			logger.error("error in sendOtpSms", e);
		}
	} // end of sendOtpSms

}
