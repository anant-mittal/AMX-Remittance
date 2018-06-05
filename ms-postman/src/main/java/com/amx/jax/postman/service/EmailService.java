package com.amx.jax.postman.service;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.AuditService;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.Utils;

@TenantScoped
@Component
public class EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private TemplateUtils templateUtils;

	@Autowired
	private FileService fileService;

	@TenantValue("${spring.mail.from}")
	private String mailFrom;
	@TenantValue("${spring.mail.host}")
	private String mailHost;
	@TenantValue("${spring.mail.port}")
	private int mailPort;
	@TenantValue("${spring.mail.username}")
	private String mailUsername;
	@TenantValue("${spring.mail.password}")
	private String mailPassword;
	@TenantValue("${spring.mail.properties.mail.smtp.auth}")
	private Boolean mailSmtpAuth;
	@TenantValue("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean mailSmtpTls;
	@TenantValue("${spring.mail.protocol}")
	private String mailProtocol;
	@TenantValue("${spring.mail.defaultEncoding}")
	private String mailDefaultEncoding;

	@Value("${spring.mail.from}")
	private String defaultSender;

	private JavaMailSender mailSender;

	@Autowired
	private JavaMailSender defaultMailSender;

	@Autowired
	private PostManConfig postManConfig;

	@Autowired
	SlackService slackService;

	@Autowired
	AuditService auditService;

	public JavaMailSender getMailSender() {
		if (mailSender == null) {
			if (postManConfig.getTenant() != null) {
				LOGGER.info("Using {} mailSender", postManConfig.getTenant());
				JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
				javaMailSender.setHost(this.mailHost);
				javaMailSender.setPort(this.mailPort);
				javaMailSender.setUsername(this.mailUsername);
				javaMailSender.setPassword(this.mailPassword);
				Properties mailProp = new Properties();
				mailProp.put("mail.smtp.auth", mailSmtpAuth);
				mailProp.put("mail.smtp.starttls.enable", mailSmtpTls);
				javaMailSender.setJavaMailProperties(mailProp);
				javaMailSender.setProtocol(this.mailProtocol);
				javaMailSender.setDefaultEncoding(mailDefaultEncoding);
				this.mailSender = javaMailSender;
			} else {
				LOGGER.info("Using Default mailSender");
				this.mailSender = defaultMailSender;
				this.mailFrom = defaultSender;
			}
		}
		return this.mailSender;
	}

	public Email sendEmail(Email email) throws PostManException {

		String to = null;
		try {
			LOGGER.info("Sending {} Email to {}", email.getTemplate(), Utils.commaConcat(email.getTo()));

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
			} else {
				auditService.fail(new PMGaugeEvent(PMGaugeEvent.Type.EMAIL_SENT_NOT, email));
			}
		} catch (Exception e) {
			auditService.excep(new PMGaugeEvent(PMGaugeEvent.Type.EMAIL_SENT_ERROR, email), LOGGER, e);
			slackService.sendException(to, e);
		}
		return email;
	}

	public Email send(Email eParams) throws MessagingException, IOException {

		if (eParams.isHtml()) {
			sendHtmlMail(eParams);
		} else {
			sendPlainTextMail(eParams);
		}

		return eParams;
	}

	private void sendHtmlMail(Email eParams) throws MessagingException, IOException {

		boolean isHtml = true;

		MimeMessage message = getMailSender().createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		// helper.setReplyTo(eParams.getFrom());

		if (eParams.getFrom() == null || Constants.defaultString.equals(eParams.getFrom())) {
			eParams.setFrom(mailFrom);
		}

		if (eParams.getReplyTo() == null || Constants.defaultString.equals(eParams.getReplyTo())) {
			eParams.setReplyTo(eParams.getFrom());
		}

		helper.setFrom(eParams.getFrom());
		helper.setReplyTo(eParams.getReplyTo());

		String subject = ArgUtil.isEmptyString(eParams.getSubject()) ? "No Subject" : eParams.getSubject();
		helper.setSubject(subject);
		helper.setText(eParams.getMessage(), isHtml);

		if (eParams.getCc().size() > 0) {
			helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}

		String str = eParams.getMessage();
		Pattern p = Pattern.compile("src=\"cid:(.*?)\"");
		Matcher m = p.matcher(str);

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
		auditService.gauge(new PMGaugeEvent(PMGaugeEvent.Type.EMAIL_SENT_SUCCESS, eParams));
	}

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
		auditService.gauge(new PMGaugeEvent(PMGaugeEvent.Type.EMAIL_SENT_SUCCESS, eParams));

	}

}
