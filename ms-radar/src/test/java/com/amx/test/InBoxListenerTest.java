package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.WAMessage;
import com.amx.utils.ArgUtil;
import com.amx.utils.StringUtils.StringMatcher;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class InBoxListenerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(InBoxListenerTest.class);

	private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	public static final Pattern LINK_CIVIL_ID = Pattern.compile("^LINK([ ]*)(\\d{3,15})$");
	public static final Pattern PING = Pattern.compile("^PING$");

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

	public static void main(String[] args) throws URISyntaxException, IOException, NumberParseException {
		System.out.println(
				onMessageResponse(
						new UserInboxEvent().from("+919930104050").message("link-12345672328"), true, true, true)
								.getMessage());
	}

	public static WAMessage onMessageResponse(UserInboxEvent event, boolean civilIdExists,
			boolean civilIdExistsWAMatch,
			boolean civilIdExistsWAMatchVerified) throws NumberParseException {

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
				System.out.println("Message :" + matcher.group(1) + ":" + matcher.group(2));
				String civilId = matcher.group(2);
				if (!civilIdExists) { // Customer no Found
					replyMessage = FOUND_NOT;
				} else if (!civilIdExistsWAMatch) { // Customer number
					replyMessage = FOUND_MATCH_NOT;
				} else if (civilIdExistsWAMatchVerified) { // Already Verified so
					replyMessage = NO_ACTION;
				} else { // Found and matched
							// customer.setWhatsAppVerified(AmxDBConstants.Status.Y);
					// customerRepository.save(customer);
					replyMessage = FOUND_MATCHED;
				}

			} catch (Exception e) {
				replyMessage = SOME_ERROR;
			}

		}

		return event.replyWAMessage(replyMessage);

	}
}
