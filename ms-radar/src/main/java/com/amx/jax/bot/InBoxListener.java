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
						MapModel customer = x.getHits().getHits().get(0).getSource().getMap("customer");
						replyMessage = String.format(
								"Hello, Your request has been recieved to link CivilID %s with WhatsApp %s. "
										+ "Please visit branch or online to get it verified.",
								customer.getString("name"), civilId, swissNumberProto.getNationalNumber());
					} else {
						replyMessage = "We cannot find any account linked with this civild id, Kindyl visit branch or online to create new account";
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
						replyMessage = "We cannot find any account linked with this number please enter "
								+ "your civil id in format LINK <CIVILID> to link this WhatsApp number to your civilid. ie:- LINK 285030905179";
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
