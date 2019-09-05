package com.amx.jax.postman.service;

import java.io.IOException;

import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.model.Email;
import com.amx.jax.tunnel.TunnelMessage;
import com.amx.utils.ArgUtil;
import com.amx.utils.TimeUtils;

@Configuration
@EnableScheduling
@Component
@Service
public class EmailServiceTask {

	private static final Logger LOGGER = LoggerService.getLogger(EmailServiceTask.class);
	@Autowired
	RedissonClient redisson;

	@Autowired
	PostManConfig postManConfig;

	@Autowired
	EmailService emailService;

	@Scheduled(fixedDelay = EmailService.RESEND_INTERVAL)
	public void doTask() throws IOException {
		RQueue<TunnelMessage<Email>> emailQueue = redisson
				.getQueue(EmailService.FAILED_EMAIL_QUEUE + "_" +
						TimeUtils.getReverseRotationNumber(EmailService.RESEND_INTERVAL, 0x1)
						+ "_" + postManConfig.getEmailRetryPoll());

		//AppContextUtil.getTraceTime();
		for (int i = 0; i < postManConfig.getEmailRetryBatch(); i++) {
			TunnelMessage<Email> emailtask = emailQueue.poll();
			if (!ArgUtil.isEmpty(emailtask)) {

				AppContextUtil.setContext(emailtask.getContext());
				AppContextUtil.init();

				Email email = emailtask.getData();
				email.setAttempt(email.getAttempt() + 1);
				if (email.getAttempt() < postManConfig.getEmailRetryCount()) {
					emailService.sendEmail(email);
				}

			}
		}

	}

}
