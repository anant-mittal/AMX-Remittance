package com.amx.jax.radar.logger;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amx.common.HandlerBeanFactory.HandlerMapping;
import com.amx.jax.client.snap.SnapConstants.SnapIndexName;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.logger.AuditHandler;
import com.amx.jax.logger.AuditMapModel;
import com.amx.jax.mcq.shedlock.SchedulerLock;
import com.amx.jax.mcq.shedlock.SchedulerLock.LockContext;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.radar.snap.SnapQueryService;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.repository.CustomerContactVerificationRepository;
import com.amx.utils.ArgUtil;

@Component
@HandlerMapping("CONTACT_VERF")
public class ContactAuditHandler implements AuditHandler {

	@Autowired
	private SnapQueryService snapQueryService;

	@Autowired
	private CustomerContactVerificationRepository customerContactVerificationRepository;

	@Override
	public void doHandle(AuditMapModel event) {

		BigDecimal verificationId = event.getTargetId();
		if (ArgUtil.is(verificationId)) {
			customerContactVerificationRepository.findById(verificationId);
		}

	}

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_HRS * 24, context = LockContext.BY_METHOD)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_HRS * 24, initialDelay = AmxCurConstants.INTERVAL_SEC * 60)
	public void syncContactVerf() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		java.util.Date oneDay = new java.util.Date(cal.getTimeInMillis());
		List<CustomerContactVerification> x = customerContactVerificationRepository
				.getByContactsByEmployee(oneDay);
		for (CustomerContactVerification customerContactVerification : x) {
			OracleViewDocument doc = new OracleViewDocument(customerContactVerification);
			snapQueryService.save(SnapIndexName.VERIFY, doc);
		}
	}

}
