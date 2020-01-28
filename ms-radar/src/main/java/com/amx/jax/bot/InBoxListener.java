package com.amx.jax.bot;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ShellProperties.Telnet;
import org.springframework.scheduling.annotation.Scheduled;

import com.amx.jax.AppContextUtil;
import com.amx.jax.client.CustomerProfileClient;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.logger.AuditActor.ActorType;
import com.amx.jax.mcq.shedlock.SchedulerLock;
import com.amx.jax.mcq.shedlock.SchedulerLock.LockContext;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.MessageBox;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.radar.EsConfig;
import com.amx.jax.radar.RadarConfig;
import com.amx.jax.radar.service.SnapDocumentRepository;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.repository.CustomerContactVerificationRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.util.AmxDBConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;
import com.amx.utils.StringUtils.StringMatcher;
import com.amx.utils.TimeUtils;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

@TunnelEventMapping(byEvent = UserInboxEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class InBoxListener implements ITunnelSubscriber<UserInboxEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InBoxListener.class);
	private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	public static final Pattern LINK_CIVIL_ID = Pattern.compile("^LINK([ ]*)(\\d{3,20})([ ]*)$");
	public static final Pattern JUST_CIVIL_ID = Pattern.compile("^([ ]*)(\\d{3,20})([ ]*)$");
	public static final Pattern PING = Pattern.compile("^PING$");

	@Autowired
	private WhatsAppClient whatsAppClient;

	@Autowired
	private PostManClient postManClient;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerProfileClient customerProfileClient;

	@Autowired
	CustomerContactVerificationRepository customerContactVerificationRepository;

	@Autowired
	SnapDocumentRepository snapApiService;

	@Autowired
	RadarConfig radarConfig;

	@Autowired
	EsConfig esConfig;

	@Autowired
	Chatbot chatbot;

	public static final String VERIFICATION_DONE = "Thank you for verification. Your account is now linked to this whatsApp number.";
	public static final String NO_LINK_FOUND = "This WhatsApp number is not linked to the {companyIDType} entered. "
			+ "Please recheck. In case the {companyIDType} is correct, please go to the branch and update your"
			+ " WhatsApp number with any of our counter staff.";
	public static final String NO_ACCOUNT_FOUND = "We cannot find an account with this {companyIDType}. "
			+ "Kindly check if {companyIDType} is correct or visit branch to create a new account. "
			+ "You can also register online on https://{companyWebSiteUrl}";
	public static final String NO_ACCOUNT_FOUND_FOR_WHATSAPP = "We cannot find any account linked with this WhatsApp number. "
			+ "Please send {companyIDType} to link your account. Eg : LINK 123456789987";
	public static final String ALREADY_VERIFIED = "Your number is verified. Visit https://{companyWebSiteUrl} or download our app, "
			+ "register yourself and see exchange rates, view past transactions, place orders for targeted exchange rates, "
			+ "do remittances and order Foreign Exchange to be delivered to you.";
	public static final String SOME_ERROR = "There is some issue while verifying your WhatsApp number."
			+ "Please recheck. In case the {companyIDType} is correct, please go to the branch and update your"
			+ " WhatsApp number with any of our counter staff. ReasonCode : {errorCode}";

	public static final String RESEND_LINK = "There was a failure to verify your WhatsApp number. "
			+ "Please resend the original message LINK {identity} once again. Apologies for the inconvenience.";

	@Override
	public void onMessage(String channel, UserInboxEvent event) {
		this.onMessageReply(event);
	}

	public Message onMessageReply(UserInboxEvent event) {
		Message reply = null;
		try {
			reply = this.onMessageResponse(event);
			if (!ArgUtil.isEmpty(reply.getMessage())) {
				MessageBox mb = new MessageBox();
				mb.push(reply);
				postManClient.send(mb);
			}
		} catch (NumberParseException e) {
			LOGGER.error("BOT EXCEPTION from:" + event.getFrom(), e);
		}
		return reply;
	}

	public Message onMessageResponse(UserInboxEvent event) throws NumberParseException {

		if (ArgUtil.is(event.getWaChannel())
				|| ArgUtil.is(event.getTgChannel())) {

			PhoneNumber swissNumberProto = phoneUtil.parse("+" + event.getFrom(), "IN");
			LOGGER.info("Recieved +{} {} {} ", swissNumberProto.getCountryCode(), swissNumberProto.getNationalNumber(),
					event.getMessage());
			String replyMessage = "";
			String swissNumberProtoString = ArgUtil.parseAsString(swissNumberProto.getNationalNumber());
			String swissISDProtoString = ArgUtil.parseAsString(swissNumberProto.getCountryCode());

			StringMatcher matcher = new StringMatcher(event.getMessage().toUpperCase());

			String errorCode = "TECHNICAL_ERROR";
			String whatsAppNUmber = swissISDProtoString + swissNumberProtoString;

			if (matcher.isMatch(PING)) {
				replyMessage = "PING";
			} else if (matcher.isMatch(LINK_CIVIL_ID) || matcher.isMatch(JUST_CIVIL_ID)) {
				try {
					AppContextUtil
							.setActorId(new AuditActor(ActorType.W, whatsAppNUmber));
					String civilId = matcher.group(2);
					Customer customer = CollectionUtil.getOne(customerRepository.findActiveCustomers(civilId));

					if (ArgUtil.isEmpty(customer)) { // Customer no Found
						replyMessage = chatbot.response(whatsAppNUmber, "NO ACCOUNT FOUND", NO_ACCOUNT_FOUND);
					} else if (!swissNumberProtoString.equalsIgnoreCase(customer.getWhatsapp())) { // Customer number
																									// does
						replyMessage = chatbot.response(whatsAppNUmber, "NO LINK FOUND", NO_LINK_FOUND);
					} else if (customer.hasVerified(ContactType.WHATSAPP)) { // Already Verified so
						replyMessage = chatbot.response(whatsAppNUmber, "ALREADY VERIFIED", ALREADY_VERIFIED);
					} else { // Found and matched
								// customer.setWhatsAppVerified(AmxDBConstants.Status.Y);
						customerProfileClient.verifyLinkByContact(civilId, ContactType.WHATSAPP,
								swissISDProtoString + swissNumberProtoString);
						// customerRepository.save(customer);
						replyMessage = chatbot.response(whatsAppNUmber, "VERIFICATION DONE", VERIFICATION_DONE);
					}
				} catch (AmxApiException e) {
					errorCode = e.getErrorKey();
					LOGGER.error("SOME_AmxApiException", e);
				} catch (Exception e) {
					replyMessage = chatbot.response(whatsAppNUmber, "SOME ERROR", SOME_ERROR);
					LOGGER.error("SOME_Exception", e);
				}

			} else {
				replyMessage = chatbot.response(whatsAppNUmber, event.getMessage().toUpperCase(), "NO_RESPONSE");
			}
			/*
			 * else if (esConfig.isEnabled()) { OracleViewDocument doc =
			 * snapApiService.getCustomerByWhatsApp(swissISDProtoString,
			 * swissNumberProtoString); if (!ArgUtil.isEmpty(doc)) { if
			 * (AmxDBConstants.Yes.equalsIgnoreCase(doc.getCustomer().getWhatsAppVerified())
			 * ) { replyMessage = NO_ACTION; } else { replyMessage = ANY_TEXT; } } else {
			 * replyMessage = ANY_TEXT; } } else { replyMessage = ANY_TEXT; }
			 */

			return event.replyMessage(replyMessage.replace("{companyName}", radarConfig.getCompanyName())
					.replace("{companyWebSiteUrl}", radarConfig.getCompanyWebSiteUrl())
					.replace("{companyIDType}", radarConfig.getCompanyIDType()).replace("{errorCode}", errorCode));
		} else {
			return event.replyMessage(null);
		}
	}

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_HRS * 13, context = LockContext.BY_METHOD)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_HRS * 12)
	public void doTaskModeDay() {
		if (TimeUtils.inHourSlot(4, 0) && radarConfig.isJobWAFailRetryEnabled()) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());
			List<CustomerContactVerification> links = customerContactVerificationRepository
					.getExpiredLinks(ContactType.WHATSAPP, oneDay);

			for (CustomerContactVerification link : links) {
				Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(link.getCustomerId());
				if (ArgUtil.is(customer) && !(AmxDBConstants.Status.Y.equals(customer.getWhatsAppVerified()))) {
					link.setSendDate(new Date());
					customerContactVerificationRepository.save(link);
					WAMessage reply = new WAMessage();
					reply.addTo(link.getContactValue());
					reply.setMessage(RESEND_LINK.replace("{identity}", customer.getIdentityInt()));
					whatsAppClient.send(reply);
//					customerProfileClient.resendLink(customer.getIdentityInt(), link.getId(),
//							link.getVerificationCode());
				} else {
					link.setIsActive(AmxDBConstants.Status.E);
					customerContactVerificationRepository.save(link);
				}
			}
		}
	}

}
