package com.amx.jax.notification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.dao.CustomerDao;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxNotificationDataManager {

	@Autowired
	MetaData metaData;
	@Autowired
	CustomerDao customerDao;
	@Autowired
	CustomerContactVerificationManager customerContactVerificationManager;

	public Map<String, Object> getTransactionSuccessEmailData() {
		Map<String, Object> map = new HashMap<String, Object>();
		Customer customer = customerDao.getCustById(metaData.getCustomerId());
		CustomerContactVerification cv = customerContactVerificationManager.checkAndCreateVerification(customer, ContactType.EMAIL);
		if (cv != null) {
			map.put("verifylink", cv);
		}
		return map;
	}

}
