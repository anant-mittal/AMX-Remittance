package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dbmodel.TransactionLimitCheckView;
import com.amx.jax.dict.ContactType;
import com.amx.jax.manager.DailyPromotionManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CivilIdOtpModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.service.ParameterService;
import com.amx.jax.userservice.service.UserService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceOtpManager {

	private static final String IOS = "IOS";
	private static final String ANDROID = "ANDROID";
	private static final String WEB = "WEB";
	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	RemittanceAdditionalFieldManager remittanceAdditionalFieldManager;

	@Autowired
	private UserService userService;

	@Autowired
	DailyPromotionManager dailyPromotionManager;

	@Autowired
	PartnerTransactionManager partnerTransactionManager;

	@Autowired
	CustomerRepository customerRepository;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	MetaData meta;

	public boolean addOtpOnRemittanceV2(RemittanceTransactionDrRequestModel model) {

		List<TransactionLimitCheckView> trnxLimitList = parameterService.getAllTxnLimits();

		BigDecimal onlineLimit = BigDecimal.ZERO;
		BigDecimal androidLimit = BigDecimal.ZERO;
		BigDecimal iosLimit = BigDecimal.ZERO;

		for (TransactionLimitCheckView view : trnxLimitList) {
			if (JaxChannel.ONLINE.toString().equals(view.getChannel())) {
				onlineLimit = view.getComplianceChkLimit();
			}
			if (ANDROID.equals(view.getChannel())) {
				androidLimit = view.getComplianceChkLimit();
			}
			if (IOS.equals(view.getChannel())) {
				iosLimit = view.getComplianceChkLimit();
			}
		}

		BigDecimal localAmount = (BigDecimal) remitApplParametersMap.get("P_CALCULATED_LC_AMOUNT");
		if (((meta.getChannel().equals(JaxChannel.ONLINE)) && (WEB.equals(meta.getAppType()))
				&& (localAmount.compareTo(onlineLimit) >= 0)) ||

				(IOS.equals(meta.getAppType()) && localAmount.compareTo(iosLimit) >= 0) ||

				(ANDROID.equals(meta.getAppType()) && localAmount.compareTo(androidLimit) >= 0)) {
			return true;
		}

		return false;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CivilIdOtpModel sendOtpOnRemittance() {
		List<ContactType> channel = new ArrayList<>();
		channel.add(ContactType.SMS_EMAIL);
		return (CivilIdOtpModel) userService.sendOtpForCivilId(null, channel, null, null).getData().getValues().get(0);
	}
}
