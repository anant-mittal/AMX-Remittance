package com.amx.jax.services;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Templates;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxNotificationService {

	@Autowired
	private PostManService postManService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Async
	public void sendTransactionNotification(RemittanceReceiptSubreport remittanceReceiptSubreport, Customer customer) {

		logger.info("Sending txn notification to customer");
		PersonInfo pinfo = new PersonInfo();
		try {
			BeanUtils.copyProperties(pinfo, customer);
		} catch (Exception e1) {
		}
		Email email = new Email();
		email.setSubject("Your transaction on AMX is successful");
		email.addTo(customer.getEmail());
		email.setTemplate(Templates.TXN_CRT_SUCC);
		email.setHtml(true);
		email.getModel().put("data", pinfo);

		File file = new File();
		file.setTemplate(Templates.REMIT_RECEIPT);
		file.setType(File.Type.PDF);
		file.getModel().put("data", remittanceReceiptSubreport);

		email.addFile(file);

		try {
			postManService.sendEmail(email);
		} catch (UnirestException e) {
			logger.error("error in sendTransactionNotification", e);
		}
	}

}
