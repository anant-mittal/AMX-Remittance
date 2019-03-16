package com.amx.jax.notification.alert;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.userservice.dao.CustomerDao;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FingerPrintLoginIncorrectAttempt implements IAlert {
	Logger logger = Logger.getLogger(FingerPrintLoginIncorrectAttempt.class);
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerDao custDao;
	@Autowired
	private PostManService postManService;
	
	@Override
	public void sendAlert(AbstractJaxException ex) {
		if (ex.getErrorKey() != null && ex.getErrorKey().startsWith(JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.toString())) {
			logger.debug("setting details for mail");
			CustomerOnlineRegistration customerOnlineRegistration = custDao.getOnlineCustByCustomerId(metaData.getCustomerId());
			logger.debug("Email to - " + customerOnlineRegistration.getEmail());
			Customer customer = custDao.getCustById(customerOnlineRegistration.getCustomerId());
			logger.debug("Email to him - " + customer.getEmail());
			PersonInfo personinfo = new PersonInfo();
			personinfo.setFirstName(customer.getFirstName());
			personinfo.setMiddleName(customer.getMiddleName());
			personinfo.setLastName(customer.getLastName());
			Email email = new Email();

			email.addTo(customerOnlineRegistration.getEmail());
			logger.debug("setting to");
			email.setITemplate(TemplatesMX.FINGERPRINT_DELINKED_ATTEMP_SUCCESS);
			logger.debug("setting template");
			email.setHtml(true);
			logger.debug("setting html");
			email.getModel().put(RESP_DATA_KEY, personinfo);
			logger.debug("setting data");

			logger.debug("Email to - " + customerOnlineRegistration.getEmail());
			sendEmail(email);
		}
	}
	public void sendEmail(Email email) {
		try {
			logger.debug("email sent");
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
			logger.debug("email exception");
			logger.error("error in incorrect attempts to link fingerprint", e);
		}
	}

	@Override
	public List<String> getAlertContacts(CommunicationChannel notificationType) {
		
		return null;
	}

	@Override
	public List<CommunicationChannel> getCommucationChannels() {
		
		return null;
	}

}
