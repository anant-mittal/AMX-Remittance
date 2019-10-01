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
import org.springframework.scheduling.annotation.Async;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.client.JaxClientUtil;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.customer.repository.RemittanceRepository;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dbmodel.ReferralDetails;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.userservice.dao.ReferralDetailsDao;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.util.AmxDBConstants;
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
	

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	// EMAIL:MOBILE:CUST_NAME:TRNXAMT:LOYALTY:TRNREF:TRNDATE:LANG_ID:TNT
	//private static final String EMAIL = "EMAIL";
	//private static final String MOBILE = "MOBILE";
	//private static final String CUST_NAME = "CUST_NAME";
	private static final String CUST_ID = "CUST_ID";
	private static final String TRANX_ID = "TRANX_ID";
	private static final String TRNXAMT = "TRNXAMT";
	private static final String LOYALTY = "LOYALTY";
	private static final String TRNREF = "TRNREF";
	private static final String TRNDATE = "TRNDATE";
	private static final String LANG_ID = "LANG_ID";
	private static final String TENANT = "TENANT";
	private static final String CURNAME = "CURNAME";
	private static final String TYPE = "TYPE";

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		//String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		//String smsNo = ArgUtil.parseAsString(event.getData().get(MOBILE));
		//String custNname = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		BigDecimal trnxAmount = ArgUtil.parseAsBigDecimal(event.getData().get(TRNXAMT));
		BigDecimal loyality = ArgUtil.parseAsBigDecimal(event.getData().get(LOYALTY));
		String trnxRef = ArgUtil.parseAsString(event.getData().get(TRNREF));
		String trnxDate = ArgUtil.parseAsString(event.getData().get(TRNDATE));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		String curName = ArgUtil.parseAsString(event.getData().get(CURNAME));
		String type = ArgUtil.parseAsString(event.getData().get(TYPE));
		BigDecimal tranxId = ArgUtil.parseAsBigDecimal(event.getData().get(TRANX_ID), new BigDecimal(0));
		LOGGER.info("Customer id is "+custId);
		Customer c = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");
		LOGGER.info("Customer object is "+c.toString());
		String emailId = c.getEmail();
		String smsNo = c.getMobile();
		
		String custName;
		if(StringUtils.isEmpty(c.getMiddleName())) {
			c.setMiddleName("");
			custName=c.getFirstName()+c.getMiddleName() + ' '+c.getLastName();
		}else {
			custName=c.getFirstName()+' '+c.getMiddleName() + ' '+c.getLastName();
		}
		
		 
		LOGGER.info("transaction id is  "+tranxId);
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		String trnxAmountval = myFormat.format(trnxAmount);

		
		
		
		Boolean isOnlineCustomer=false;
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
		modeldata.put("verCode",
				JaxClientUtil.getTransactionVeryCode(tranxId).output());

		for (Map.Entry<String, Object> entry : modeldata.entrySet()) {
			LOGGER.info("KeyModel = " + entry.getKey() + ", ValueModel = " + entry.getValue());
		}

		wrapper.put("data", modeldata);
		LOGGER.info("email is is "+emailId);
		if (!ArgUtil.isEmpty(emailId)) {
			
			LOGGER.info("email verified is "+c.getEmailVerified());
			if (c.getEmailVerified() != AmxDBConstants.Status.Y) {
				LOGGER.info("email value is "+c.getEmailVerified());
				CustomerContactVerification x = customerContactVerificationManager.create(c, ContactType.EMAIL);
				LOGGER.info("value of x is "+x.toString());
				//modeldata.put("customer", c);
				modeldata.put("verifylink", x);
				for (Map.Entry<String, Object> entry : modeldata.entrySet()) {
					LOGGER.info("KeyModel2 = " + entry.getKey() + ", ValueModel2 = " + entry.getValue());
				}
				LOGGER.debug("Model data is {}", modeldata.get("verifylink"));
				//LOGGER.debug("Customer value is ", modeldata.get("customer"));

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
			for (Map.Entry<String, Object> entry : wrapper.entrySet()) {
				LOGGER.info("KeyModelWrap = " + entry.getKey() + ", ValueModelWrap = " + entry.getValue());
			}
			LOGGER.info("Json value of wrapper is "+JsonUtil.toJson(wrapper));
			LOGGER.info("Wrapper data is {}", wrapper.get("data"));
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setSubject("Transaction Credit Notification"); // changed as per BA
			switch (type) {
			case "CASH":
				email.setITemplate(TemplatesMX.CASH);
				break;
			case "TT":
				email.setITemplate(TemplatesMX.TT);
				break;
			case "EFT":
				email.setITemplate(TemplatesMX.EFT);
				break;
			default:
				break;
			}
			sendEmail(email);
		}

		if (!ArgUtil.isEmpty(smsNo)&&isOnlineCustomer) {
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
			switch (type) {
			case "CASH":
				sms.setITemplate(TemplatesMX.CASH);
				break;
			case "TT":
				sms.setITemplate(TemplatesMX.TT);
				break;
			case "EFT":
				sms.setITemplate(TemplatesMX.EFT);
				break;
			default:
				break;
			}
			postManService.sendSMSAsync(sms);

		}
		
		List<RemittanceTransaction> remittanceList = remittanceTransactionRepository.getTransactionMadeByOnline(String.valueOf(custId));
		System.out.println("RemittanceCount : "+remittanceList.size());
		
		
		if((remittanceList.size() == 0) || (remittanceList.size() == 1)) {
			ReferralDetails referralDetails = refDao.getReferralByCustomerId(custId);
			System.out.println("Referrer Customer Id"+referralDetails.getCustomerId());
			System.out.println("Referree Customer Id"+referralDetails.getRefferedByCustomerId());
			referralDetails.setIsConsumed("Y");
			refDao.updateReferralCode(referralDetails);
			
			if (referralDetails.getRefferedByCustomerId() != null) {
				PushMessage pushMessage = new PushMessage();
				pushMessage.setSubject("Refer To Win!");
				pushMessage.setMessage(
						"Congratulations! Your reference has done the first transaction on AMIEC App! You will get a chance to win from our awesome Referral Program! Keep sharing the links to as many contacts you can and win exciting prices on referral success!");
				pushMessage.addToUser(referralDetails.getRefferedByCustomerId());
				pushNotifyClient.send(pushMessage);
			}
			
			if(referralDetails.getCustomerId() != null) {
				PushMessage pushMessage = new PushMessage();
				pushMessage.setSubject("Refer To Win!");
				pushMessage.setMessage(
						"Welcome to Al Mulla family! Win a chance to get exciting offers at Al Mulla Exchange by sharing the links to as many contacts as you can.");
				pushMessage.addToUser(referralDetails.getCustomerId());
				pushNotifyClient.send(pushMessage);	
			}
		}

		if (!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();

			switch (type) {
			case "CASH":
				pushMessage.setITemplate(TemplatesMX.CASH);
				break;
			case "TT":
				pushMessage.setITemplate(TemplatesMX.TT);
				break;
			case "EFT":
				pushMessage.setITemplate(TemplatesMX.EFT);
				break;
			default:
				break;
			}
			pushMessage.setModel(wrapper);
			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}

	}

	@Async(ExecutorConfig.DEFAULT)
	public void sendEmail(Email email) {
		try {
			LOGGER.info("email sent");
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
			LOGGER.info("email exception");
			LOGGER.error("error in link fingerprint", e);
		}
	}

}
