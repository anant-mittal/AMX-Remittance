package com.amx.jax.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.BranchSearchNotificationModel;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.services.BankService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.service.UserService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxNotificationManager {

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	MetaData metaData;

	@Autowired
	UserService userService;

	@Autowired
	BankService bankSerivce;

	public void sendBranchSearchNotificationToSOA(ApiResponse<BankBranchDto> apiResponse,
			GetBankBranchRequest request) {
		List<BankBranchDto> result = apiResponse.getResults();
		if (result == null || result.isEmpty()) {
			BranchSearchNotificationModel model = new BranchSearchNotificationModel();
			PersonInfo pinfo = userService.getPersonInfo(metaData.getCustomerId());
			String bankFullName = bankSerivce.getBankById(request.getBankId()).getBankFullName();
			model.setCustomerName(pinfo.getFirstName() + " " + pinfo.getLastName());
			model.setIdentityId(pinfo.getIdentityInt());
			model.setCustomerQuery(request);
			model.setBankFullName(bankFullName);
			jaxNotificationService.sendBranchSearchEmailNotification(model, "SOA@almullaexchange.com");
		}
	}
}
