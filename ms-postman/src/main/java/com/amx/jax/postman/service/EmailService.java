package com.amx.jax.postman.service;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.AuditService;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.scope.TenantScoped;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.Utils;

/**
 * The Class EmailService.
 */
@TenantScoped
@Component
public class EmailService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	public static final Pattern pattern = Pattern.compile("^(.*)<(.*)>$");
	public static final Pattern PATTERN_CID = Pattern.compile("src=\"cid:(.*?)\"");
	public static final Pattern PATTERN_SUBJECT = Pattern.compile("<title>(.*?)</title>");

	/** The template utils. */
	@Autowired
	private TemplateUtils templateUtils;

	/** The file service. */
	@Autowired
	private FileService fileService;

	/** The mail sender. */
	private JavaMailSender mailSender;

	/** The default mail sender. */
	@Autowired
	private JavaMailSender defaultMailSender;

	/** The post man config. */
	@Autowired
	private PostManConfig postManConfig;

	@Autowired
	ContactCleanerService contactService;

	/** The slack service. */
	@Autowired
	SlackService slackService;

	/** The audit service. */
	@Autowired
	AuditService auditService;

	/**
	 * Gets the mail sender.
	 *
	 * @return the mail sender
	 */
	public JavaMailSender getMailSender() {
		if (mailSender == null) {
			if (postManConfig.getTenant() != null) {
				LOGGER.info("Using {} mailSender", postManConfig.getTenant());
				JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
				javaMailSender.setHost(postManConfig.getMailHost());
				javaMailSender.setPort(postManConfig.getMailPort());
				javaMailSender.setUsername(postManConfig.getMailUsername());
				javaMailSender.setPassword(postManConfig.getMailPassword());
				Properties mailProp = new Properties();
				mailProp.put("mail.smtp.auth", postManConfig.isMailSmtpAuth());
				mailProp.put("mail.smtp.starttls.enable", postManConfig.isMailSmtpTls());
				javaMailSender.setJavaMailProperties(mailProp);
				javaMailSender.setProtocol(postManConfig.getMailProtocol());
				javaMailSender.setDefaultEncoding(postManConfig.getMailDefaultEncoding());
				this.mailSender = javaMailSender;
			} else {
				LOGGER.info("Using Default mailSender");
				this.mailSender = defaultMailSender;
				// this.mailFrom = postManConfig.getMailDefaultSender();
			}
		}
		return this.mailSender;
	}

	/**
	 * Send email.
	 *
	 * @param email
	 *            the email
	 * @return the email
	 * @throws PostManException
	 *             the post man exception
	 */
	@Async(ExecutorConfig.EXECUTER_GOLD)
	public Email sendEmail(Email email) throws PostManException {

		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(PMGaugeEvent.Type.SEND_EMAIL);
		String to = null;
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Sending {} Email to {}", email.getTemplate(), Utils.commaConcat(email.getTo()));
			}

			to = email.getTo() != null ? email.getTo().get(0) : null;

			if (email.getTemplate() != null) {
				File file = new File();
				file.setTemplate(email.getTemplate());
				file.setModel(email.getModel());
				file.setLang(email.getLang());

				email.setMessage(fileService.create(file).getContent());

				if (ArgUtil.isEmptyString(email.getSubject())) {
					email.setSubject(file.getTitle());
				}
			}

			if (email.getFiles() != null && email.getFiles().size() > 0) {
				for (File file : email.getFiles()) {
					if (file.getLang() == null) {
						file.setLang(email.getLang());
					}
					fileService.create(file);
				}
			}
			if (!ArgUtil.isEmpty(to)) {
				this.send(email);
				auditService.log(pMGaugeEvent.set(AuditEvent.Result.DONE).set(email));
			} else {
				auditService.log(pMGaugeEvent.set(AuditEvent.Result.FAIL).set(email));
			}
		} catch (Exception e) {
			auditService.excep(pMGaugeEvent.set(email), LOGGER, e);
			slackService.sendException(to, e);
		}
		return email;
	}

	/**
	 * Send.
	 *
	 * @param eParams
	 *            the e params
	 * @return the email
	 * @throws MessagingException
	 *             the messaging exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Email send(Email eParams) throws MessagingException, IOException {
		if (eParams.isHtml()) {
			sendHtmlMail(eParams);
		} else {
			sendPlainTextMail(eParams);
		}
		return eParams;
	}

	/**
	 * Send html mail.
	 *
	 * @param eParams
	 *            the e params
	 * @throws MessagingException
	 *             the messaging exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void sendHtmlMail(Email eParams) throws MessagingException, IOException {

		boolean isHtml = true;

		MimeMessage message = getMailSender().createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(contactService.getEmail(eParams.getTo()));
		// helper.setTo(emailsTo.toArray(new String[emailsTo.size()]));
		// helper.setReplyTo(eParams.getFrom());

		if (eParams.getFrom() == null || Constants.DEFAULT_STRING.equals(eParams.getFrom())) {
			eParams.setFrom(postManConfig.getMailFrom());
		}

		String fromEmail = null;
		String fromTitle = null;

		Matcher matcher = pattern.matcher(eParams.getFrom());
		if (matcher.find()) {
			fromEmail = matcher.group(2);
			fromTitle = matcher.group(1);
		} else {
			fromEmail = eParams.getFrom();
			fromTitle = eParams.getFrom();
		}

		if (eParams.getReplyTo() == null || Constants.DEFAULT_STRING.equals(eParams.getReplyTo())) {
			eParams.setReplyTo(eParams.getFrom());
		}

		helper.setFrom(new InternetAddress(fromEmail, fromTitle));
		helper.setReplyTo(eParams.getReplyTo());

		String messageStr = eParams.getMessage();

		String subject = eParams.getSubject();
		if (ArgUtil.isEmptyString(subject)) {
			Matcher mtitle = PATTERN_SUBJECT.matcher(messageStr);

			if (mtitle.find()) {
				subject = mtitle.group(1);
			}
			subject = ArgUtil.isEmptyString(subject) ? "No Subject" : subject;
		}
		helper.setSubject(subject);
		helper.setText(eParams.getMessage(), isHtml);

		if (eParams.getCc().size() > 0) {
			helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}

		Matcher m = PATTERN_CID.matcher(messageStr);
		while (m.find()) {
			String contentId = m.group(1);
			helper.addInline(contentId, templateUtils.readAsResource(contentId));
		}

		if (eParams.getFiles() != null && eParams.getFiles().size() > 0) {
			for (File file : eParams.getFiles()) {
				helper.addAttachment(file.getName(), fileService.toDataSource(file));
			}
		}

		getMailSender().send(message);
	}

	/**
	 * Send plain text mail.
	 *
	 * @param eParams
	 *            the e params
	 */
	private void sendPlainTextMail(Email eParams) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		eParams.getTo().toArray(new String[eParams.getTo().size()]);
		mailMessage.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		mailMessage.setReplyTo(eParams.getFrom());
		mailMessage.setFrom(eParams.getFrom());
		mailMessage.setSubject(eParams.getSubject());
		mailMessage.setText(eParams.getMessage());

		if (eParams.getCc().size() > 0) {
			mailMessage.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}

		getMailSender().send(mailMessage);

	}

}
