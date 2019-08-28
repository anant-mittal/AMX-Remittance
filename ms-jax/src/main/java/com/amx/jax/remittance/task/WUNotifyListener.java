package com.amx.jax.remittance.task;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
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
import com.amx.jax.repository.RemittanceTransactionRepository;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.CASH_TRNX_COMM, scheme = TunnelEventXchange.TASK_WORKER)
public class WUNotifyListener implements ITunnelSubscriber<DBEvent> {
	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;


	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CustomerFlagManager customerFlagManager;
	
	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;
	
	@Autowired
	CurrencyMasterService currencyMasterService;
	

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static final String CUST_ID = "CUST_ID";
	private static final String TRANX_ID = "TRANX_ID";
	
	private static final String TRNREF = "TRNREF";
	
	private static final String LANG_ID = "LANG_ID";
	private static final String PARTNER = "PARTNER";
	private static final String NOTIF_TYPE = "NOTIF_TYPE";
	
	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		
		String trnxRef = ArgUtil.parseAsString(event.getData().get(TRNREF));
		String partner = ArgUtil.parseAsString(event.getData().get(PARTNER));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		String notifyType = ArgUtil.parseAsString(event.getData().get(NOTIF_TYPE));
		
		
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
		RemittanceTransaction remittanceTransaction = remittanceTransactionRepository.findOne(tranxId);
		
		CurrencyMasterModel currencyMasterModelLocal=currencyMasterService.getCurrencyMasterById(remittanceTransaction.getLocalTranxCurrencyId().getCurrencyId());
		CurrencyMasterModel currencyMasterModelForeign = currencyMasterService.getCurrencyMasterById(remittanceTransaction.getForeignCurrencyId().getCurrencyId());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
		String trnxDate = formatter.format(remittanceTransaction.getCreatedDate()) ;
		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custName);
		modeldata.put("refno", trnxRef);
		
		modeldata.put("date", trnxDate);
		
		modeldata.put("localcurcode", currencyMasterModelLocal.getQuoteName());
		modeldata.put("localamount", remittanceTransaction.getLocalNetTranxAmount());
		modeldata.put("foreigncurcode",currencyMasterModelForeign.getQuoteName());
		modeldata.put("foreignamount", remittanceTransaction.getForeignTranxAmount());
		
		
		for (Map.Entry<String, Object> entry : modeldata.entrySet()) {
			LOGGER.info("KeyModel = " + entry.getKey() + ", ValueModel = " + entry.getValue());
		}
		wrapper.put("data", modeldata);
		if(!ArgUtil.isEmpty(emailId)&&c.canSendEmail()) {
			

			
			LOGGER.info("email is  "+emailId);
		
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
			if(notifyType.equalsIgnoreCase(ConstantDocument.WU_PAID)) {
				email.setITemplate(TemplatesMX.WU_TRNX_SUCCESS);
			}else if(notifyType.equalsIgnoreCase(ConstantDocument.WU_PICK)) {
				email.setITemplate(TemplatesMX.WU_PICKUP_REMINDER);
			}else if(notifyType.equalsIgnoreCase(ConstantDocument.WU_CANC_REM)) {
				email.setITemplate(TemplatesMX.WU_CANCEL_REMINDER);
			}else {
				email.setITemplate(TemplatesMX.WU_TRNX_CANCELLED);
			}
			
			sendEmail(email);
	}	

		if (!ArgUtil.isEmpty(smsNo)&&c.canSendMobile()) {
			
				
			SMS sms = new SMS();
			if ("2".equals(langId)) {
				sms.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				sms.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			LOGGER.info("Json value of wrapper is "+JsonUtil.toJson(wrapper));
			LOGGER.info("Wrapper data is {}", wrapper.get("data"));
			sms.addTo(c.getMobile());
			sms.setModel(wrapper);
			
			
			if(notifyType.equalsIgnoreCase(ConstantDocument.WU_PICK)) {
				sms.setITemplate(TemplatesMX.WU_PICKUP_REMINDER);
			}else if(notifyType.equalsIgnoreCase(ConstantDocument.WU_CANC_REM)) {
				sms.setITemplate(TemplatesMX.WU_CANCEL_REMINDER);
			}else if(notifyType.equalsIgnoreCase(ConstantDocument.WU_CANCELLED)){
				sms.setITemplate(TemplatesMX.WU_TRNX_CANCELLED);
			}
			postManService.sendSMSAsync(sms);

		}

		if (!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();
			
			
			if(notifyType.equalsIgnoreCase(ConstantDocument.WU_PICK)) {
				pushMessage.setITemplate(TemplatesMX.WU_PICKUP_REMINDER);
			}else if(notifyType.equalsIgnoreCase(ConstantDocument.WU_CANC_REM)) {
				pushMessage.setITemplate(TemplatesMX.WU_CANCEL_REMINDER);
			}else if(notifyType.equalsIgnoreCase(ConstantDocument.WU_CANCELLED)){
				pushMessage.setITemplate(TemplatesMX.WU_TRNX_CANCELLED);
			}
			LOGGER.info("Json value of wrapper is "+JsonUtil.toJson(wrapper));
			LOGGER.info("Wrapper data is {}", wrapper.get("data"));
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
			
		}
	}
	

}