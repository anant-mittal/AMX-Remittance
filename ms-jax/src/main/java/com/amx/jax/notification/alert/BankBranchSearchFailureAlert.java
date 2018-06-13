package com.amx.jax.notification.alert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.model.BranchSearchNotificationModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.CompanyService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BankBranchSearchFailureAlert implements IAlert {

	@Autowired
	JaxNotificationService jaxNotificationService;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;
	@Autowired
	BankService bankSerivce;
	@Autowired
	CompanyService companySerivce;
	@Autowired
	JaxProperties jaxProperties;

	@Override
	public void sendAlert(AbstractJaxException ex) {
		GetBankBranchRequest request = (GetBankBranchRequest) JaxContextUtil.getRequestModel();
		BranchSearchNotificationModel model = new BranchSearchNotificationModel();
		PersonInfo pinfo = userService.getPersonInfo(metaData.getCustomerId());
		String bankFullName = bankSerivce.getBankById(request.getBankId()).getBankFullName();
		model.setCustomerName(pinfo.getFirstName() + " " + pinfo.getLastName());
		model.setIdentityId(pinfo.getIdentityInt());
		model.setCustomerQuery(request);
		model.setBankFullName(bankFullName);
		getAlertContacts(CommunicationChannel.EMAIL)
				.forEach(i -> jaxNotificationService.sendBranchSearchEmailNotification(model, i));
	}

	@Override
	public List<String> getAlertContacts(CommunicationChannel notificationType) {
		return jaxProperties.getSupportSoaEmail();
	}

	@Override
	public List<CommunicationChannel> getCommucationChannels() {
		List<CommunicationChannel> channels = new ArrayList<>();
		channels.add(CommunicationChannel.EMAIL);
		return channels;
	}

}
