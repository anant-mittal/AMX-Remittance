package com.amx.jax.postman.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class SupportService {

	@TenantValue("${support.contact.to}")
	private String supportContactTo;
	@TenantValue("${support.contact.from}")
	private String supportContactFrom;

	public Email createContactUsEmail(SupportEmail email) throws PostManException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", email.getVisitorName());
		map.put("cphone", email.getVisitorPhone());
		map.put("cemail", email.getVisitorEmail());
		map.put("message", email.getVisitorMessage());
		email.setFrom(this.supportContactFrom);
		email.setReplyTo(email.getVisitorEmail());
		email.addAllTo(supportContactTo);
		email.getModel().put("data", map);
		email.setSubject("Inquiry");
		email.setTemplate(Templates.CONTACT_US);
		email.setHtml(true);
		return email;
	}

}
