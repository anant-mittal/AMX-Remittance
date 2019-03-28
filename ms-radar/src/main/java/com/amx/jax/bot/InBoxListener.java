package com.amx.jax.bot;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.radar.service.SnapDocumentRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.util.AmxDBConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.StringUtils.StringMatcher;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

@TunnelEventMapping(byEvent = UserInboxEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class InBoxListener implements ITunnelSubscriber<UserInboxEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InBoxListener.class);
	private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	public static final Pattern LINK_CIVIL_ID = Pattern.compile("^LINK (.*)$");

	@Autowired
	WhatsAppClient whatsAppClient;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	SnapDocumentRepository snapApiService;

	public static final String FOUND_MATCHED = "Thank you for verification. Your account is now linked to this whatsApp number.";
	public static final String FOUND_MATCH_NOT = "Kindly visit branch to update your whatsapp communication number. "
			+ "Retry sending the same WhatsApp message, after the number has been updated at our branch.";
	public static final String FOUND_NOT = "We cannot find an account with this Civil ID. "
			+ "Kindly check if civil id is correct or visit branch to create new account."
			+ " You can also register online on https://www.almullaexchange.com";
	public static final String ANY_TEXT = "We cannot find any account linked with this WhatsApp number."
			+ " Please send Civili Id to link your account. Eg : LINK 123456789987";
	public static final String NO_ACTION = "Visit https://www.almullaexchange.com or download our app to view your transactions.";

	@Override
	public void onMessage(String channel, UserInboxEvent event) {
		this.onMessageReply(event);
	}

	public WAMessage onMessageReply(UserInboxEvent event) {
		WAMessage reply = null;
		try {
			reply = this.onMessageResponse(event);
			if (!ArgUtil.isEmpty(reply.getMessage())) {
				whatsAppClient.send(reply);
			}
		} catch (NumberParseException e) {
			LOGGER.error("BOT EXCEPTION", e);
		}
		return reply;
	}

	public WAMessage onMessageResponse(UserInboxEvent event) throws NumberParseException {
		if (!ArgUtil.isEmpty(event.getWaChannel())) {
			PhoneNumber swissNumberProto = phoneUtil.parse("+" + event.getFrom(), "IN");
			LOGGER.info("Recieved {} {} ", swissNumberProto.getNationalNumber(), event.getMessage());
			String replyMessage = "";
			String swissNumberProtoString = ArgUtil.parseAsString(swissNumberProto.getNationalNumber());
			String swissISDProtoString = ArgUtil.parseAsString(swissNumberProto.getCountryCode());

			StringMatcher matcher = new StringMatcher(event.getMessage().toUpperCase());

			if (matcher.match(LINK_CIVIL_ID) && matcher.find()) {
				String civilId = matcher.group(1);

				Customer customer = customerRepository.getCustomerOneByIdentityInt(civilId);

				if (ArgUtil.isEmpty(customer)) { // Customer no Found
					replyMessage = FOUND_NOT;
				} else if (!swissNumberProtoString.equalsIgnoreCase(customer.getWhatsapp())) { // Customer number does
					replyMessage = FOUND_MATCH_NOT;
				} else if (AmxDBConstants.Yes.equalsIgnoreCase(customer.getWhatsAppVerified())) { // Already Verified so
					replyMessage = NO_ACTION;
				} else { // Found and matched
					customer.setWhatsAppVerified(AmxDBConstants.Yes);
					customerRepository.save(customer);
					replyMessage = FOUND_MATCHED;
				}

			} else {
				OracleViewDocument doc = snapApiService.getCustomerByWhatsApp(swissISDProtoString,
						swissNumberProtoString);
				if (!ArgUtil.isEmpty(doc)) {
					if (AmxDBConstants.Yes.equalsIgnoreCase(doc.getCustomer().getWhatsAppVerified())) {
						replyMessage = NO_ACTION;
					} else {
						replyMessage = ANY_TEXT;
					}
				} else {
					replyMessage = ANY_TEXT;
				}
			}
			return event.replyWAMessage(replyMessage);
		} else {
			return event.replyWAMessage(null);
		}
	}

}
