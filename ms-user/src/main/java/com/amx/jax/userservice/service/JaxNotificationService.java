package com.amx.jax.userservice.service;

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
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.ChangeType;
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

	// to send profile (password, security question, image, mobile) change notification
	@Async
	public void sendProfileChangedNotification(CustomerModel customerModel, Customer customer) {

		logger.info("Sending Profile change notification to customer : "+customer.getFirstName());
		PersonInfo pinfo = new PersonInfo();
		try {
			BeanUtils.copyProperties(pinfo, customer);
		} catch (Exception e1) {
			logger.error("Error while copying Customer to PersonInfo.");
			e1.printStackTrace();
		}
		Email email = new Email();
		
		if ( customerModel.getPassword() != null ) {
			email.setSubject("Change Password Success");
			email.getModel().put("change_type", ChangeType.PASSWORD_CHANGE);
			
		}else if( customerModel.getSecurityquestions() != null ) {
			email.setSubject("Update Security Credentials Success");
			email.getModel().put("change_type", ChangeType.SECURITY_QUESTION_CHANGE);
			
		}else if(customerModel.getImageUrl() != null ) {
			email.setSubject("Update Security Credentials Success");
			email.getModel().put("change_type", ChangeType.IMAGE_CHANGE);
			
		}else if(customerModel.getMobile() != null ) {
			email.setSubject("Your password changed successfuly");
			email.getModel().put("change_type", ChangeType.MOBILE_CHANGE);
		}
		
		email.addTo(customer.getEmail());
		email.setTemplate(Templates.PROFILE_CHANGE);
		email.setHtml(true);
		email.getModel().put("pinfo", pinfo);
		

		try {
			postManService.sendEmail(email);
		} catch (UnirestException e) {
			logger.error("error in sendProfileChangedNotification", e);
		}
	}

}
