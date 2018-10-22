package com.amx.jax.manager;

import static com.amx.amxlib.constant.NotificationConstants.BRANCH_SEARCH;
import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.PlaceOrderNotificationDTO;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.RoundUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IPlaceOrderDao placeOrderDao;
	@Autowired
	CustomerDao customerDao;
	@Autowired
	JaxNotificationService jaxNotificationService;
	@Autowired
	CurrencyMasterService currencyMasterService;

	@Autowired
	PostManService postManService;
	@Autowired
	PushNotifyClient pushNotifyClient;

	/**
	 * method will be called after successful remittance
	 * 
	 * @param remittanceTransaction
	 * 
	 */
	@Async
	public void afterRemittanceSteps(RemittanceTransaction remittanceTransaction) {
		PlaceOrder placeorder = placeOrderDao
				.getPlaceOrderForRemittanceTransactionId(remittanceTransaction.getRemittanceTransactionId());
		if (placeorder != null) {
			logger.info("sending email and push notification for place order id {}",
					placeorder.getOnlinePlaceOrderId());
			NumberFormat myFormat = NumberFormat.getInstance();

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
			String date = simpleDateFormat.format(new Date());

			Customer cusotmer = customerDao.getCustById(placeorder.getCustomerId());
			PlaceOrderNotificationDTO placeorderNotDTO = new PlaceOrderNotificationDTO();
			placeorderNotDTO.setFirstName(cusotmer.getFirstName());
			placeorderNotDTO.setMiddleName(cusotmer.getMiddleName());
			placeorderNotDTO.setLastName(cusotmer.getLastName());
			placeorderNotDTO.setEmail(cusotmer.getEmail());
			placeorderNotDTO.setInputAmount(myFormat.format(placeorder.getPayAmount()));
			placeorderNotDTO.setOutputAmount(myFormat.format(placeorder.getReceiveAmount()));
			placeorderNotDTO.setInputCur(placeorder.getBaseCurrencyQuote());
			placeorderNotDTO.setOutputCur(placeorder.getForeignCurrencyQuote());
			BigDecimal dn = currencyMasterService.getCurrencyMasterById(placeorder.getCurrencyId()).getDecinalNumber();
			BigDecimal inverseRate = RoundUtil.roundBigDecimal(
					BigDecimal.ONE.divide(remittanceTransaction.getExchangeRateApplied(), 10, RoundingMode.HALF_UP),
					dn.intValue());
			placeorderNotDTO.setRate(inverseRate);
			placeorderNotDTO.setDate(date);
			placeorderNotDTO.setOnlinePlaceOrderId(placeorder.getOnlinePlaceOrderId());

			sendPaceorderNotification(placeorderNotDTO);
		}
	}

	public void sendPaceorderNotification(PlaceOrderNotificationDTO model) {
		try {
			String emailid = model.getEmail();
			logger.info("Email send to--" + model.getEmail());
			Email email = new Email();
			email.setSubject(BRANCH_SEARCH);
			email.addTo(emailid);
			email.setITemplate(TemplatesMX.RATE_ALERT);
			email.setHtml(true);
			email.getModel().put(RESP_DATA_KEY, model);
			postManService.sendEmailAsync(email);
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.RATE_ALERT);
			pushMessage.addToUser(model.getCustomerId());
			pushMessage.getModel().put(RESP_DATA_KEY, model);
			pushNotifyClient.send(pushMessage);
		} catch (Exception e) {
			logger.error("error in sendPaceorderNotification", e);
		}
	}
}
