package com.amx.jax.customer.service;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.ContactType;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;

public interface CustomerCommunicationService {

	boolean sendMessage(CommunicationEvents communicationEvent, Customer commCustomer, Object model,
			TemplatesMX templateMX, ContactType... contactTypes);

	boolean sendMessage(CommunicationEvents communicationEvent, Customer commCustomer, Email email);

}
