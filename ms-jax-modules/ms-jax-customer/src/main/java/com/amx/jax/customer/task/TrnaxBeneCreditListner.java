package com.amx.jax.customer.task;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.client.JaxClientUtil;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.customer.repository.RemittanceRepository;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dbmodel.ReferralDetails;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.userservice.dao.ReferralDetailsDao;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.util.AmxDBConstants;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.TRNX_BENE_CREDIT, scheme = TunnelEventXchange.TASK_WORKER)
public class TrnaxBeneCreditListner implements ITunnelSubscriber<DBEvent> {

	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	private CustomerContactVerificationManager customerContactVerificationManager;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerFlagManager customerFlagManager;

	@Autowired
	ReferralDetailsDao refDao;

	@Autowired
	RemittanceRepository remittanceTransactionRepository;
	
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	@Autowired
	WhatsAppClient whatsAppClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static final String CUST_ID = "CUST_ID";
	private static final String TRANX_ID = "TRANX_ID";
	private static final String TRNXAMT = "TRNXAMT";
	private static final String LOYALTY = "LOYALTY";
	private static final String TRNREF = "TRNREF";
	private static final String TRNDATE = "TRNDATE";
	private static final String LANG_ID = "LANG_ID";
	private static final String CURNAME = "CURNAME";
	private static final String TYPE = "TYPE";

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));

		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		BigDecimal trnxAmount = ArgUtil.parseAsBigDecimal(event.getData().get(TRNXAMT));
		BigDecimal loyality = ArgUtil.parseAsBigDecimal(event.getData().get(LOYALTY));
		String trnxRef = ArgUtil.parseAsString(event.getData().get(TRNREF));
		String trnxDate = ArgUtil.parseAsString(event.getData().get(TRNDATE));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		String curName = ArgUtil.parseAsString(event.getData().get(CURNAME));
		String type = ArgUtil.parseAsString(event.getData().get(TYPE));
		BigDecimal tranxId = ArgUtil.parseAsBigDecimal(event.getData().get(TRANX_ID), new BigDecimal(0));
		Customer c = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");
		String emailId = c.getEmail();
		String smsNo = c.getMobile();

		String custName;
		if (StringUtils.isEmpty(c.getMiddleName())) {
			c.setMiddleName("");
			custName = c.getFirstName() + c.getMiddleName() + ' ' + c.getLastName();
		} else {
			custName = c.getFirstName() + ' ' + c.getMiddleName() + ' ' + c.getLastName();
		}

		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		String trnxAmountval = myFormat.format(trnxAmount);
		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custName);
		modeldata.put("amount", trnxAmountval);
		modeldata.put("loyaltypoints", loyality);
		modeldata.put("refno", trnxRef);
		modeldata.put("date", trnxDate);
		modeldata.put("currency", curName);
		modeldata.put("tranxId", tranxId);
		modeldata.put("verCode", JaxClientUtil.getTransactionVeryCode(tranxId).output());

		wrapper.put("data", modeldata);
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(CommunicationEvents.TRNX_BENE_CREDIT, c);
		TemplatesMX templatesMX=null;
		switch (type) {
		case "CASH":
			templatesMX=TemplatesMX.CASH;
			break;
		case "TT":
			templatesMX=TemplatesMX.TT;
			break;
		case "EFT":
			templatesMX=TemplatesMX.EFT;
			break;
		default:
			break;
		}
		
		if (communicationPrefsResult.isEmail()) {

			if (c.getEmailVerified() != AmxDBConstants.Status.Y) {

				CustomerContactVerification x = null;
				try {
					x = customerContactVerificationManager.create(c, ContactType.EMAIL);
				} catch (GlobalException e) {
					LOGGER.debug(e.getMessage());
				}

				modeldata.put("verifylink", x);

			} else {
				modeldata.put("customer", custName);
				modeldata.put("verifylink", null);
			}

			Email email = new Email();
			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			LOGGER.info("Json value of wrapper is " + JsonUtil.toJson(wrapper));
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setSubject("Transaction Credit Notification"); // changed as per BA
			email.setITemplate(templatesMX);
			postManService.sendEmailAsync(email);
		}

		if (communicationPrefsResult.isSms()) {
			if (c.getMobileVerified() != AmxDBConstants.Status.Y) {
				CustomerContactVerification x = customerContactVerificationManager.create(c, ContactType.SMS);
				modeldata.put("customer", c);
				modeldata.put("verifylink", x);
			} else {
				modeldata.put("customer", null);
				modeldata.put("verifylink", null);
			}
			SMS sms = new SMS();
			if ("2".equals(langId)) {
				sms.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				sms.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			sms.addTo(c.getMobile());
			sms.setModel(wrapper);
			sms.setSubject("Transaction Credit Notification");
			sms.setITemplate(templatesMX);
			postManService.sendSMSAsync(sms);

		}

		List<RemittanceTransaction> remittanceList = remittanceTransactionRepository
				.getTransactionMadeByOnline(String.valueOf(custId));

		if ((remittanceList.size() == 0) || (remittanceList.size() == 1)) {
			ReferralDetails referralDetails = refDao.getReferralByCustomerId(custId);
			if (!ArgUtil.isEmpty(referralDetails)) {
				referralDetails.setIsConsumed("Y");
				refDao.updateReferralCode(referralDetails);

				if (referralDetails.getRefferedByCustomerId() != null) {
					PushMessage pushMessage = new PushMessage();
					/*
					 * pushMessage.setSubject("Refer To Win!"); pushMessage.setMessage(
					 * "Congratulations! Your reference has done the first transaction on AMIEC App! You will get a chance to win from our awesome Referral Program! Keep sharing the links to as many contacts you can and win exciting prices on referral success!"
					 * );
					 */ pushMessage.setITemplate(TemplatesMX.FRIEND_REFERED);
					pushMessage.addToUser(referralDetails.getRefferedByCustomerId());
					pushNotifyClient.send(pushMessage);
				}

				if (referralDetails.getCustomerId() != null) {
					PushMessage pushMessage = new PushMessage();
					/*
					 * pushMessage.setSubject("Refer To Win!"); pushMessage.setMessage(
					 * "Welcome to Al Mulla family! Win a chance to get exciting offers at Al Mulla Exchange by sharing the links to as many contacts as you can."
					 * );
					 */ pushMessage.setITemplate(TemplatesMX.FRIEND_REFER);
					pushMessage.addToUser(referralDetails.getCustomerId());
					pushNotifyClient.send(pushMessage);
				}
			}

		}
		
		if(communicationPrefsResult.isWhatsApp()) {
			WAMessage waMessage = new WAMessage();
			waMessage.addTo(c.getWhatsappPrefix() + c.getWhatsapp());
			waMessage.setModel(wrapper);
			waMessage.setITemplate(templatesMX);
			whatsAppClient.send(waMessage);
		}

		if (!ArgUtil.isEmpty(custId)&&communicationPrefsResult.isPushNotify()) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(templatesMX);
			pushMessage.setId(AmxTunnelEvents.Names.TRNX_BENE_CREDIT + tranxId);
			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}

	}
}
