package com.amx.jax.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.MapModel;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.radar.snap.SnapQueryService;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
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
	private SnapQueryService snapQueryService;

	public static final String FOUND_MATCHED = "Thank you for verification. Your account is now linked to this whatsApp number.";
	public static final String FOUND_MATCH_NOT = "Kindly visit branch to update your whatsapp communication number. "
			+ "Retry sending the same WhatsApp message, after the number has been updated at our branch.";
	public static final String FOUND_NOT = "We cannot find an account with this Civil ID. "
			+ "Kindly check if civil id is correct or visit branch to create new account."
			+ " You can also register online on https://www.almullaexchange.com";
	public static final String ANY_TEXT = "We cannot find any account linked with this WhatsApp number."
			+ " Please send Civili Id to link your account. Eg : LINK 123456789987";

	@Override
	public void onMessage(String channel, UserInboxEvent event) {
		if (!ArgUtil.isEmpty(event.getWaChannel())) {
			try {
				PhoneNumber swissNumberProto = phoneUtil.parse("+" + event.getFrom(), "IN");
				LOGGER.info("Recieved {} {} ", swissNumberProto.getNationalNumber(), event.getMessage());
				String replyMessage = "";

				StringMatcher matcher = new StringMatcher(event.getMessage().toUpperCase());

				if (matcher.match(LINK_CIVIL_ID) && matcher.find()) {
					String civilId = matcher.group(1);
					Map<String, Object> query = new HashMap<String, Object>();
					query.put("gte", "now-20y");
					query.put("lte", "now");
					query.put("searchKey", "customer.identity");
					query.put("searchValue", civilId);
					SnapModelWrapper x = snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_PROFILE, query);

					if (x.getHits().getTotal() > 0) {
						// MapModel customer =
						// x.getHits().getHits().get(0).getSource().getMap("customer");
						replyMessage = FOUND_MATCHED;
					} else {
						replyMessage = FOUND_NOT;
					}
				} else {

					Map<String, Object> query = new HashMap<String, Object>();
					query.put("gte", "now-20y");
					query.put("lte", "now");
					query.put("searchKey", "customer.mobile");
					query.put("searchValue", swissNumberProto.getNationalNumber());

					SnapModelWrapper x = snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_PROFILE, query);

					if (x.getHits().getTotal() > 0) {
						MapModel customer = x.getHits().getHits().get(0).getSource().getMap("customer");
						replyMessage = String.format(
								"Hello %s, You account is linked with this whatsapp account, thanx.",
								customer.getString("name"));
					} else {
						replyMessage = ANY_TEXT;
					}
				}

				WAMessage reply = event.replyWAMessage(replyMessage);
				whatsAppClient.send(reply);
			} catch (NumberParseException e) {
				LOGGER.error("BOT EXCEPTION", e);
			}

		}
	}

}
