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
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.CASH_TRNX_COMM, scheme = TunnelEventXchange.TASK_WORKER)
public class WUNotifyListener implements ITunnelSubscriber<DBEvent> {
	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	private WhatsAppClient whatsAppClient;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerFlagManager customerFlagManager;

	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;

	@Autowired
	CurrencyMasterService currencyMasterService;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;

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
		LOGGER.info("Customer id is " + custId);
		Customer c = customerRepository.getNationalityValue(custId);

		LOGGER.info("Customer object is " + c.toString());
		String emailId = c.getEmail();
		String smsNo = c.getMobile();
		String custName;
		if (StringUtils.isEmpty(c.getMiddleName())) {
			c.setMiddleName("");
			custName = c.getFirstName() + c.getMiddleName() + ' ' + c.getLastName();
		} else {
			custName = c.getFirstName() + ' ' + c.getMiddleName() + ' ' + c.getLastName();
		}

		LOGGER.info("transaction id is  " + tranxId);
		RemittanceTransaction remittanceTransaction = remittanceTransactionRepository.findOne(tranxId);

		CurrencyMasterModel currencyMasterModelLocal = currencyMasterService
				.getCurrencyMasterById(remittanceTransaction.getLocalTranxCurrencyId().getCurrencyId());
		CurrencyMasterModel currencyMasterModelForeign = currencyMasterService
				.getCurrencyMasterById(remittanceTransaction.getForeignCurrencyId().getCurrencyId());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String trnxDate = formatter.format(remittanceTransaction.getCreatedDate());
		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custName);
		modeldata.put("refno", trnxRef);

		modeldata.put("date", trnxDate);

		modeldata.put("localcurcode", currencyMasterModelLocal.getQuoteName());
		modeldata.put("localamount", remittanceTransaction.getLocalTranxAmount());
		modeldata.put("foreigncurcode", currencyMasterModelForeign.getQuoteName());
		modeldata.put("foreignamount", remittanceTransaction.getForeignTranxAmount());

		for (Map.Entry<String, Object> entry : modeldata.entrySet()) {
			LOGGER.info("KeyModel = " + entry.getKey() + ", ValueModel = " + entry.getValue());
		}
		wrapper.put("data", modeldata);
		CommunicationPrefsResult x = communicationPrefsUtil.forCustomer(CommunicationEvents.CASH_PICKUP_WU, c);

		TemplatesMX thisTemplate = null;
		if (notifyType.equalsIgnoreCase(ConstantDocument.WU_PAID)) {
			thisTemplate = TemplatesMX.WU_TRNX_SUCCESS;
		} else if (notifyType.equalsIgnoreCase(ConstantDocument.WU_PICK)) {
			thisTemplate = TemplatesMX.WU_PICKUP_REMINDER;
		} else if (notifyType.equalsIgnoreCase(ConstantDocument.WU_CANC_REM)) {
			thisTemplate = TemplatesMX.WU_CANCEL_REMINDER;
		} else {
			thisTemplate = TemplatesMX.WU_TRNX_CANCELLED;
		}

		LOGGER.debug("Json value of wrapper is " + JsonUtil.toJson(wrapper));
		LOGGER.debug("Wrapper data is {}", wrapper.get("data"));

		if (x.isEmail()) {

			LOGGER.debug("email is  " + emailId);

			Email email = new Email();
			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setITemplate(thisTemplate);

			sendEmail(email);
		}

		if (x.isWhatsApp()) {
			WAMessage waMessage = new WAMessage();
			waMessage.setITemplate(thisTemplate);
			waMessage.setModel(wrapper);
			waMessage.addTo(c.getWhatsappPrefix() + c.getWhatsapp());
			whatsAppClient.send(waMessage);
		}

		if (x.isSms()) {
			SMS smsMessage = new SMS();
			smsMessage.setITemplate(thisTemplate);
			smsMessage.setModel(wrapper);
			smsMessage.addTo(c.getWhatsappPrefix() + c.getWhatsapp());
			postManService.sendSMSAsync(smsMessage);
		}

		if (x.isPushNotify()) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(thisTemplate);
			pushMessage.setModel(wrapper);
			pushMessage.addToUser(custId);
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