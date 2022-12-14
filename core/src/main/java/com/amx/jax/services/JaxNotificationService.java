package com.amx.jax.services;

import static com.amx.amxlib.constant.NotificationConstants.BRANCH_SEARCH;
import static com.amx.amxlib.constant.NotificationConstants.REG_SUC;
import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;
import static com.amx.amxlib.constant.NotificationConstants.RESP_TRANSACTION_DATA_KEY;
import static com.amx.amxlib.constant.NotificationConstants.SERVICE_PROVIDER_RESPONSE;
import static com.amx.amxlib.constant.NotificationConstants.TRANSACTION_FAIL;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.notification.RemittanceTransactionFailureAlertModel;
import com.amx.jax.AppContextUtil;
import com.amx.jax.customer.service.CustomerCommunicationService;
import com.amx.jax.db.utils.EntityDtoUtil;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.Tenant;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.CivilIdOtpModel;
import com.amx.jax.model.request.fx.FcSaleOrderFailReportDTO;
import com.amx.jax.model.request.partner.TransactionFailReportDTO;
import com.amx.jax.model.response.customer.CustomerDto;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.fx.FxDeliveryDetailNotificationDto;
import com.amx.jax.model.response.fx.FxOrderDetailNotificationDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.ChangeType;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.utils.CollectionUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxNotificationService {

	@Autowired
	private PostManService postManService;

	@Autowired
	private WhatsAppClient whatsAppClient;

	@Autowired
	JaxEmailNotificationService jaxEmailNotificationService;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MetaData metaData;

	@Autowired
	private CustomerCommunicationService customerCommunicationService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String SUBJECT_ACCOUNT_UPDATE = "Account Update";

	public void sendTransactionNotification(RemittanceReceiptSubreport remittanceReceiptSubreport, PersonInfo pinfo,
			Map<String, Object> emailData) {
		sendTransactionNotification(remittanceReceiptSubreport, pinfo, emailData, true);
	}

	public void sendTransactionNotification(RemittanceReceiptSubreport remittanceReceiptSubreport, PersonInfo pinfo,
			Map<String, Object> emailData, boolean sendReceiptFile) {

		logger.debug("Sending txn notification to customer");
		Email email = new Email();

		if (TenantContextHolder.currentSite().equals(Tenant.KWT)) {
			email.setSubject("Your transaction on AMX is successful");
		} else if (TenantContextHolder.currentSite().equals(Tenant.BHR)) {
			email.setSubject("Your transaction on MEC is successful");
		} else if (TenantContextHolder.currentSite().equals(Tenant.OMN)) {
			email.setSubject("Your transaction on Modern Exchange - Oman is successful");
		}

		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.TXN_CRT_SUCC);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);
		email.setLang(AppContextUtil.getTenant().defaultLang());

		if (remittanceReceiptSubreport != null) {
			email.getModel().put(RESP_TRANSACTION_DATA_KEY, remittanceReceiptSubreport);
		} else {
			email.getModel().put(RESP_TRANSACTION_DATA_KEY, new RemittanceReceiptSubreport());
		}

		if (sendReceiptFile) {
			File file = new File();
			file.setITemplate(TemplatesMX.REMIT_RECEIPT_JASPER);
			file.setName("TransactionReceipt");
			file.setType(File.Type.PDF);
			file.getModel().put(RESP_DATA_KEY, remittanceReceiptSubreport);
			file.setPassword(pinfo.getIdentityInt());
			file.setLang(AppContextUtil.getTenant().defaultLang());
			email.addFile(file);
		}
		
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
		file.setLang(AppContextUtil.getTenant().defaultLang());

		email.addFile(file);
		logger.debug("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getCustomerName());
		sendEmail(email);
	}

	// to send profile (password, security question, image, mobile) change
	// notification
	public void sendProfileChangeNotificationEmail(CustomerModel customerModel, PersonInfo pinfo) {
		Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(customerModel.getCustomerId(), "Y");
		// CommunicationPrefsResult communicationPrefsResult =
		// communicationPrefsUtil.forCustomer(CommunicationEvents.MY_PROFILE, customer);

		logger.info("Sending Profile change notification to customer : " + pinfo.getFirstName());
		// if(customer.canSendEmail()) {
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

			email.getModel().put("change_type", ChangeType.MOBILE_CHANGE);

		} else if (customerModel.getEmail() != null) {
			// Customer model has old email and Person info has new email
			if (!customerModel.getEmail().equals(pinfo.getEmail())) {

				email.getModel().put("change_type", ChangeType.EMAIL_CHANGE);

				emailToOld = new Email();

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
			email.getModel().put("change_type", ChangeType.EMAIL_CHANGE);
		}

		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.PROFILE_CHANGE);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);
		logger.info("Email to - " + pinfo.getEmail() + " first name : " + pinfo.getFirstName());
		sendEmail(email);
		// }

	} // end of sendProfileChangeNotificationEmail

	public void sendProfileChangeNotificationMobile(CustomerModel customerModel, PersonInfo personinfo,
			String oldMobile) {
		Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(customerModel.getCustomerId(), "Y");
		if (customer.canSendMobile()) {
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

	public void sendOtpWhatsApp(PersonInfo pinfo, CivilIdOtpModel model) {
		sendOtpWhatsApp(pinfo, model, TemplatesMX.RESET_OTP_SMS);
	}

	public void sendOtpWhatsApp(PersonInfo pinfo, CivilIdOtpModel model, TemplatesMX templateMX) {

		logger.debug(String.format("Sending wOTP to customer :%s on whatsapp_no :%s  ", pinfo.getFirstName(),
				pinfo.getWhatsappPrefixCode() + pinfo.getWhatsAppNumber()));

		WAMessage sms = new WAMessage();
		sms.addTo(pinfo.getWhatsappPrefixCode() + pinfo.getWhatsAppNumber());
		sms.getModel().put(RESP_DATA_KEY, model);
		sms.setITemplate(templateMX);

		try {
			whatsAppClient.send(sms);
		} catch (PostManException e) {
			logger.error("error in sendOtpSms", e);
		}
	} // end of sendOtpSms

	public void sendOtpSms(PersonInfo pinfo, CivilIdOtpModel model, TemplatesMX templateMX) {

		logger.info(String.format("Sending mOTP SMS to customer :%s on mobile_no :%s  ", pinfo.getFirstName(),
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

	@Async
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
				email.setSubject(TRANSACTION_FAIL);
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

	public String sendEmailChangeSubject() {

		if (TenantContextHolder.currentSite().equals(Tenant.KWT)) {
			return "Almulla Exchange Account - Email ID Change";
		} else if (TenantContextHolder.currentSite().equals(Tenant.BHR)) {
			return "Modern Exchange Account - Email ID Change";
		} else if (TenantContextHolder.currentSite().equals(Tenant.OMN)) {
			return "Almulla Exchange Account - Email ID Change";
		}
		return "Almulla Exchange Account - Email ID Change";
	}

	public String sendMobileNumberChangeSubject() {

		if (TenantContextHolder.currentSite().equals(Tenant.KWT)) {
			return "Almulla Exchange Account - Phone number Change";
		} else if (TenantContextHolder.currentSite().equals(Tenant.BHR)) {
			return "Modern Exchange Account - Phone number Change";
		} else if (TenantContextHolder.currentSite().equals(Tenant.OMN)) {
			return "Almulla Exchange Account - Phone number Change";
		}
		return "Almulla Exchange Account - Phone number Change";
	}

	public void sendTransactionErrorAlertEmail(String errorMsg, String subject, PaymentResponseDto paymentResponse) {
		String[] receiverList = jaxEmailNotificationService.getBeneCreationErrorEmailList();
		if (StringUtils.isNotBlank(errorMsg) && receiverList != null && receiverList.length > 0) {
			StringBuffer message = new StringBuffer();
			message.append("errorMsg: ").append(errorMsg);
			message.append("paymentResponse: ").append(paymentResponse);
			Email email = new Email();
			email.setSubject(subject);
			email.addTo(receiverList);
			email.setMessage(message.toString());
			sendEmail(email);
		}
	}

	public void sendCustomerVerificationNotification(List<CustomerContactVerification> cvs, Customer c) {

		cvs.forEach(i -> {
			if (ContactType.EMAIL.equals(i.getContactType())) {
				Email email = new Email();
				email.addTo(c.getEmail());
				email.setITemplate(TemplatesMX.CONTACT_VERIFICATION_EMAIL);
				email.getModel().put("customer", EntityDtoUtil.entityToDto(c, new CustomerDto()));
				email.getModel().put("link", i);
				postManService.sendEmailAsync(email);
			} else if (ContactType.SMS.equals(i.getContactType())) {
				SMS sms = new SMS();
				sms.addTo(c.getMobile());
				sms.setITemplate(TemplatesMX.CONTACT_VERIFICATION_SMS);

				sms.getModel().put("customer", EntityDtoUtil.entityToDto(c, new CustomerDto()));
				sms.getModel().put("link", i);
				postManService.sendSMSAsync(sms);
			} else if (ContactType.WHATSAPP.equals(i.getContactType())) {

			}
		});

	}

	public void sendSPErrorEmail(TransactionFailReportDTO model,
			List<ExEmailNotification> emailNotification) {
		try {
			for (ExEmailNotification emailNot : emailNotification) {
				String emailid = emailNot.getEmailId();
				Email email = new Email();
				email.setSubject(SERVICE_PROVIDER_RESPONSE);
				email.addTo(emailid);
				email.setITemplate(TemplatesMX.HOMESEND_TRANSACTION_FAILAURE);
				email.setHtml(true);
				email.getModel().put(RESP_DATA_KEY, model);
				sendEmail(email);
			}
		} catch (Exception e) {
			logger.error("error in sendErrormail", e);
		}
	}

	public void sendTransactionNotificationDL(PersonInfo pinfo) {
		logger.debug("Sending txn notification to customer");
		Email email = new Email();

		email.setSubject("Your transaction on AMX is successful");
		email.addTo(pinfo.getEmail());
		email.setITemplate(TemplatesMX.TXN_CRT_SUCC);

		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, pinfo);

		logger.debug("Email to DL - " + pinfo.getEmail());
		sendEmail(email);
	}

	public void sendFCSaleSupportErrorEmail(FcSaleOrderFailReportDTO model,
			List<ExEmailNotification> emailNotification) {
		try {
			for (ExEmailNotification emailNot : emailNotification) {
				String emailid = emailNot.getEmailId();
				Email email = new Email();
				// email.setSubject(FC_OUTOF_STOCK_SUPPORT);
				email.addTo(emailid);
				email.setITemplate(TemplatesMX.FC_OUTOF_STOCK_SUPPORT);
				email.setHtml(true);
				email.getModel().put(RESP_DATA_KEY, model);
				sendEmail(email);
			}
		} catch (Exception e) {
			logger.error("error in sendErrormail", e);
		}
	}

	public void sendFCSaleCustomerErrorEmail(FcSaleOrderFailReportDTO model,
			String emailid) {
		try {
			Email email = new Email();
			// email.setSubject(FC_OUTOF_STOCK_CUSTOMER);
			email.addTo(emailid);
			email.setITemplate(TemplatesMX.FC_OUTOF_STOCK_CUSTOMER);
			email.setHtml(true);
			email.getModel().put(RESP_DATA_KEY, model);
			sendEmail(email);
		} catch (Exception e) {
			logger.error("error in sendErrormail", e);
		}
	}
}
