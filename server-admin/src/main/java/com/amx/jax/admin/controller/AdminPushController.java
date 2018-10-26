package com.amx.jax.admin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.jax.client.JaxPushNotificationClient;
import com.amx.jax.dict.BranchesBHR;
import com.amx.jax.dict.BranchesKWT;
import com.amx.jax.dict.Nations;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.task.events.PromoNotifyTask;
import com.amx.jax.tunnel.TunnelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "Push Notifiation APIs")
public class AdminPushController {

	private Logger logger = Logger.getLogger(AdminPushController.class);

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@RequestMapping(value = "/pub/list/tenant", method = RequestMethod.POST)
	public List<Tenant> listOfTenants() throws PostManException, InterruptedException, ExecutionException {
		return Arrays.asList(Tenant.values());
	}

	@RequestMapping(value = "/pub/list/nations", method = RequestMethod.POST)
	public List<Nations> listOfNations() throws PostManException, InterruptedException, ExecutionException {
		return Arrays.asList(Nations.values());
	}

	@RequestMapping(value = "/pub/list/branches", method = RequestMethod.POST)
	public List<?> listOfNations(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant)
			throws PostManException, InterruptedException, ExecutionException {
		if (tenant == Tenant.BHR) {
			return Arrays.asList(BranchesBHR.values());
		} else {
			return Arrays.asList(BranchesKWT.values());
		}

	}

	@RequestMapping(value = "/api/notify/all", method = RequestMethod.POST)
	public PromoNotifyTask notifyAll(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant,
			@RequestParam String message, @RequestParam String title) throws PostManException {

		PromoNotifyTask task = new PromoNotifyTask();
		task.setNationality(Nations.ALL);
		task.setTitle(title);
		task.setMessage(message);
		tunnelService.task(task);
		return task;
	}

	@Autowired
	private TunnelService tunnelService;

	@RequestMapping(value = "/api/notify/nationality", method = RequestMethod.POST)
	public PromoNotifyTask notifyNational(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant,
			@RequestParam Nations nationality, @RequestParam String message, @RequestParam String title)
			throws PostManException {

		PromoNotifyTask task = new PromoNotifyTask();
		task.setNationality(nationality);
		task.setTitle(title);
		task.setMessage(message);
		tunnelService.task(task);

		return task;
	}

	@RequestMapping(value = "/api/subscribe/{topic}", method = RequestMethod.POST)
	public String fbPush(@RequestParam String token, @PathVariable String topic)
			throws PostManException, InterruptedException, ExecutionException {
		pushNotifyClient.subscribe(token, topic);
		return topic;
	}

}
