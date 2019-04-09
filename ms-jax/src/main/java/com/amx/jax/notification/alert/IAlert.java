package com.amx.jax.notification.alert;

import java.util.List;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.jax.constants.CommunicationChannel;

/**
 * @author Prashant
 *         <p>
 *         Alert which need to be sent
 *         </p>
 */
public interface IAlert {

	/**
	 * @param ex
	 *            - exception
	 * @param notificationTypes
	 * 
	 */
	void sendAlert(AbstractJaxException ex);

	/**
	 * @param notificationType
	 * @return list of contacts to whom email/sms need to send
	 * 
	 */
	List<String> getAlertContacts(CommunicationChannel notificationType);

	/**
	 * @return list of communucation channel like emai , sms
	 * 
	 */
	List<CommunicationChannel> getCommucationChannels();

	/**
	 * @return whether this alert is enabled or not
	 * 
	 */
	default boolean isEnabled() {
		return true;
	}

}
