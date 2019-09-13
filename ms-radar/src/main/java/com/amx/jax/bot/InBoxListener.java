package com.amx.jax.bot;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.client.CustomerProfileClient;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.ContactType;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.radar.EsConfig;
import com.amx.jax.radar.RadarConfig;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.radar.service.SnapDocumentRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.util.AmxDBConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;
import com.amx.utils.StringUtils.StringMatcher;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

@TunnelEventMapping(byEvent = UserInboxEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class InBoxListener implements ITunnelSubscriber<UserInboxEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InBoxListener.class);
	private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	public static final Pattern LINK_CIVIL_ID = Pattern.compile("^LINK (.*)$");
	public static final Pattern PING = Pattern.compile("^PING$");

	@Autowired
	private WhatsAppClient whatsAppClient;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerProfileClient customerProfileClient;

	@Autowired
	SnapDocumentRepository snapApiService;

	@Autowired
	RadarConfig radarConfig;

	@Autowired
	EsConfig esConfig;

	public static final String FOUND_MATCHED = "Thank you for verification. Your account is now linked to this whatsApp number.";
	public static final String FOUND_MATCH_NOT = "This WhatsApp number is not linked to the {companyIDType} entered. "
			+ "Please recheck. In case the {companyIDType} is correct, please go to the branch and update your"
			+ " WhatsApp number with any of our counter staff.";
	public static final String FOUND_NOT = "We cannot find an account with this {companyIDType}. "
			+ "Kindly check if {companyIDType} is correct or visit branch to create a new account. "
			+ "You can also register online on https://{companyWebSiteUrl}";
	public static final String ANY_TEXT = "We cannot find any account linked with this WhatsApp number. "
			+ "Please send {companyIDType} to link your account. Eg : LINK 123456789987";
	public static final String NO_ACTION = "Your number is verified. Visit https://{companyWebSiteUrl} or download our app, "
			+ "register yourself and see exchange rates, view past transactions, place orders for targeted exchange rates, "
			+ "do remittances and order Foreign Exchange to be delivered to you.";
	public static final String SOME_ERROR = "There is some issue while verifying your WhatsApp number."
			+ "Please recheck. In case the {companyIDType} is correct, please go to the branch and update your"
			+ " WhatsApp number with any of our counter staff.";

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

			if (matcher.isMatch(PING)) {
				replyMessage = "PING";
			} else if (matcher.isMatch(LINK_CIVIL_ID)) {

				try {
					String civilId = matcher.group(1);
					Customer customer = CollectionUtil.getOne(customerRepository.findActiveCustomers(civilId));

					if (ArgUtil.isEmpty(customer)) { // Customer no Found
						replyMessage = FOUND_NOT;
					} else if (!swissNumberProtoString.equalsIgnoreCase(customer.getWhatsapp())) { // Customer number
																									// does
						replyMessage = FOUND_MATCH_NOT;
					} else if (AmxDBConstants.Status.Y.equals(customer.getWhatsAppVerified())) { // Already Verified so
						replyMessage = NO_ACTION;
					} else { // Found and matched
								// customer.setWhatsAppVerified(AmxDBConstants.Status.Y);
						customerProfileClient.verifyLinkByContact(civilId, ContactType.WHATSAPP,
								swissISDProtoString + swissNumberProtoString);
						// customerRepository.save(customer);
						replyMessage = FOUND_MATCHED;

					}

				} catch (Exception e) {
					replyMessage = SOME_ERROR;
					LOGGER.error("SOME_ERROR", e);
				}

			} else if (esConfig.isEnabled()) {
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
			} else {
				replyMessage = ANY_TEXT;
			}

			return event.replyWAMessage(replyMessage.replace("{companyName}", radarConfig.getCompanyName())
					.replace("{companyWebSiteUrl}", radarConfig.getCompanyWebSiteUrl())
					.replace("{companyIDType}", radarConfig.getCompanyIDType())

			);
		} else {
			return event.replyWAMessage(null);
		}
	}

}
