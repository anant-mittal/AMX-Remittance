package com.amx.jax.notification.alert;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.jax.exception.AbstractException;
import com.amx.jax.util.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceCreationFailureAlert implements IAlert {

	@Override
	public List<String> getAlertContacts(CommunicationChannel notificationType) {
		// TODO fetch alert contacts
		return null;
	}

	@Override
	public void sendAlert(AbstractException ex, CommunicationChannel... notificationType) {

		PaymentResponseDto model = (PaymentResponseDto) JaxContextUtil.getRequestModel();
		// TODO fetch bene and customer details
		// TODO fill data in RemittanceTransactionFailureAlertModel
	}

}
