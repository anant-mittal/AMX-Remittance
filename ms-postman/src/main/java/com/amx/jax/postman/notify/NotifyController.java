package com.amx.jax.postman.notify;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.dict.BranchesBHR;
import com.amx.jax.dict.BranchesKWT;
import com.amx.jax.dict.Nations;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.client.FBPushClient;
import com.amx.jax.postman.model.PushMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "Push Notifiation APIs")
public class NotifyController {

	private Logger logger = Logger.getLogger(NotifyController.class);

	@Autowired
	FBPushClient fbPushClient;

	@RequestMapping(value = PostManUrls.LIST_TENANT, method = RequestMethod.POST)
	public List<Tenant> listOfTenants() throws PostManException, InterruptedException, ExecutionException {
		return Arrays.asList(Tenant.values());
	}

	@RequestMapping(value = PostManUrls.LIST_NATIONS, method = RequestMethod.POST)
	public List<Nations> listOfNations() throws PostManException, InterruptedException, ExecutionException {
		return Arrays.asList(Nations.values());
	}

	@RequestMapping(value = PostManUrls.LIST_BRANCHES, method = RequestMethod.POST)
	public List<?> listOfNations(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant)
			throws PostManException, InterruptedException, ExecutionException {
		if (tenant == Tenant.BHR) {
			return Arrays.asList(BranchesBHR.values());
		} else {
			return Arrays.asList(BranchesKWT.values());
		}

	}

	@RequestMapping(value = "/postman/notify/all", method = RequestMethod.POST)
	public PushMessage notifyAll(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant,
			@RequestParam String message, @RequestParam String title) throws PostManException {
		PushMessage msg = new PushMessage();
		msg.setMessage(message);
		msg.setSubject(title);
		msg.addTo(String.format(PushMessage.FORMAT_TO_ALL, tenant.toString().toLowerCase()));
		return fbPushClient.sendDirect(msg);
	}

	@RequestMapping(value = "/postman/notify/nationality", method = RequestMethod.POST)
	public PushMessage notifyNational(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant,
			@RequestParam Nations nationality, @RequestParam String message, @RequestParam String title)
			throws PostManException {
		PushMessage msg = new PushMessage();
		msg.setMessage(message);
		msg.setSubject(title);

		if (nationality == Nations.ALL) {
			msg.addTo(String.format(PushMessage.FORMAT_TO_ALL, tenant.toString().toLowerCase()));
		} else {
			msg.addTo(String.format(PushMessage.FORMAT_TO_NATIONALITY, tenant.toString().toLowerCase(),
					nationality.getCode()));
		}
		return fbPushClient.sendDirect(msg);
	}

}