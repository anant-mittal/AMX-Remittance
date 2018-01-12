package com.amx.jax.postman.service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.Email;
import com.bootloaderjs.Constants;
import com.bootloaderjs.Utils;

@Component
public class EmailService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private TemplateService templateService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.from}")
	private String defaultSender;

	public Email send(Email eParams) {

		if (eParams.isHtml()) {
			try {
				sendHtmlMail(eParams);
			} catch (MessagingException | IOException e) {
				log.error("Could not send email to : " + Utils.concatenate(eParams.getTo(), ",") + " Error = {}", e);
			}
		} else {
			sendPlainTextMail(eParams);
		}
		return eParams;
	}

	private void sendHtmlMail(Email eParams) throws MessagingException, IOException {

		boolean isHtml = true;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		//helper.setReplyTo(eParams.getFrom());

		if (eParams.getFrom() != null && !Constants.defaultString.equals(eParams.getFrom())) {
			helper.setFrom(eParams.getFrom());
			helper.setReplyTo(eParams.getFrom());
		} else {
			helper.setFrom(defaultSender);
			helper.setReplyTo(defaultSender);
		}

		helper.setSubject(eParams.getSubject());
		helper.setText(eParams.getMessage(), isHtml);

		if (eParams.getCc().size() > 0) {
			helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}

		String str = eParams.getMessage();
		Pattern p = Pattern.compile("src=\"cid:(.*?)\"");
		Matcher m = p.matcher(str);
		while (m.find()) {
			String contentId = m.group(1);
			helper.addInline(contentId, templateService.readAsResource(contentId));
		}

		mailSender.send(message);
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

		mailSender.send(mailMessage);

	}

}