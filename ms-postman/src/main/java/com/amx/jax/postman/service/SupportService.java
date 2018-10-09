package com.amx.jax.postman.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.MessageType;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.utils.Utils;

/**
 * The Class SupportService.
 */
@Component
@TenantScoped
public class SupportService {

	/** The support contact to. */
	@TenantValue("${support.contact.to}")
	private String supportContactTo;

	/** The support contact from. */
	@TenantValue("${support.contact.from}")
	private String supportContactFrom;

	/** The support contact subject. */
	@TenantValue("${support.contact.subject}")
	private String supportContactSubject;

	/** The support SAO subject. */
	@TenantValue("${support.soa.to}")
	private String supportSAOSubject;

	/** The support IT subject. */
	@TenantValue("${support.it.to}")
	private String supportITSubject;

	/**
	 * Creates the contact us email.
	 *
	 * @param email
	 *            the email
	 * @return the email
	 * @throws PostManException
	 *             the post man exception
	 */
	public Email createContactUsEmail(SupportEmail email) throws PostManException {

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", email.getVisitorName());
		map.put("cphone", email.getVisitorPhone());
		map.put("cemail", email.getVisitorEmail());
		map.put("message", email.getVisitorMessage());
		map.put("identity", email.getIdentity());
		map.put("lines", Utils.concatenate(email.getLines(), " \n "));

		email.setFrom(this.supportContactFrom);
		email.setReplyTo(email.getVisitorEmail());
		email.addAllTo(supportContactTo);
		email.getModel().put("data", map);
		email.setSubject(supportContactSubject);
		email.setITemplate(TemplatesMX.CONTACT_US);
		email.setHtml(true);

		return email;
	}

	/**
	 * Filter message type.
	 *
	 * @param email
	 *            the email
	 * @return the email
	 */
	public Email filterMessageType(Email email) {
		if (email.getMessageType() == null) {
		} else if (email.getMessageType() == MessageType.SOA) {
			email.addAllTo(supportSAOSubject);
		} else if (email.getMessageType() == MessageType.IT) {
			email.addAllTo(supportITSubject);
		}
		return email;
	}

}
