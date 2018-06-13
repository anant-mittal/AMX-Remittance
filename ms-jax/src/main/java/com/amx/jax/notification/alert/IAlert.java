package com.amx.jax.notification.alert;

import java.util.List;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.jax.exception.AbstractException;

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
	void sendAlert(AbstractException ex);

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
