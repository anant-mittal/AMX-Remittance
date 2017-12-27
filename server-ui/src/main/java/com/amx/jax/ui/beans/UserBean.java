package com.amx.jax.ui.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.ui.Constants;
import com.amx.jax.ui.session.UserSession;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(UserBean.class);

	@Autowired
	private PostManService postManService;

	@Autowired
	private UserSession userSession;

	@Autowired
	private TenantBean tenantBean;

	CurrencyMasterDTO defaultForCurrency;

	public CurrencyMasterDTO getDefaultForCurrency() {
		if (defaultForCurrency == null) {
			BigDecimal nationalityId = userSession.getCustomerModel().getPersoninfo().getNationalityId();
			if (nationalityId == null) {
				defaultForCurrency = tenantBean.getOnlineCurrencies().get(0);
			} else {
				for (CurrencyMasterDTO currency : tenantBean.getOnlineCurrencies()) {
					if (nationalityId.equals(currency.getCountryId())) {
						defaultForCurrency = currency;
						break;
					}
				}
			}
		}
		return defaultForCurrency;
	}

	@Async
	public void notifyResetOTP(CivilIdOtpModel model) {

		SMS sms = new SMS();

		try {
			sms.setTo("7710072192");
			sms.setMessage("Your OTP for Reset is " + model.getOtp());
			sms.setTemplate(Templates.RESET_OTP_SMS);
			sms.getModel().put("data", model);
			postManService.sendSMS(sms);
		} catch (Exception e) {
			log.error("Error while sending OTP SMS to 7710072192", e);
		}

		Email email = new Email();
		email.setSubject("Verify Your Account");
		email.setFrom("amxjax@gmail.com");
		if (model.getEmail() != null && !Constants.EMPTY.equals(model.getEmail())) {
			email.setTo(model.getEmail());
		} else {
			email.setTo("riddhi.madhu@almullagroup.com");
		}
		email.setTemplate(Templates.RESET_OTP);
		email.setHtml(true);
		email.getModel().put("data", model);

		try {
			postManService.sendEmail(email);
		} catch (Exception e) {
			log.error("Error while sending OTP Email to" + model.getEmail(), e);
		}

	}

}
