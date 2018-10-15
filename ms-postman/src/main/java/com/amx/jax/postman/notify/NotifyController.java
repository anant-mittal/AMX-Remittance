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

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.BranchesBHR;
import com.amx.jax.dict.BranchesKWT;
import com.amx.jax.dict.Nations;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

/**
 * The Class NotifyController.
 */
@RestController
@Api(value = "Push Notifiation APIs")
public class NotifyController {

	/** The logger. */
	private Logger logger = Logger.getLogger(NotifyController.class);

	/** The fb push client. */
	@Autowired
	PushNotifyClient pushNotifyClient;

	/**
	 * List of tenants.
	 *
	 * @return the list
	 * @throws PostManException
	 *             the post man exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ExecutionException
	 *             the execution exception
	 */
	@RequestMapping(value = PostManUrls.LIST_TENANT, method = RequestMethod.POST)
	public List<Tenant> listOfTenants() throws PostManException, InterruptedException, ExecutionException {
		return Arrays.asList(Tenant.values());
	}

	/**
	 * List of nations.
	 *
	 * @return the list
	 * @throws PostManException
	 *             the post man exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ExecutionException
	 *             the execution exception
	 */
	@RequestMapping(value = PostManUrls.LIST_NATIONS, method = RequestMethod.POST)
	public List<Nations> listOfNations() throws PostManException, InterruptedException, ExecutionException {
		return Arrays.asList(Nations.values());
	}

	/**
	 * List of nations.
	 *
	 * @param tenant
	 *            the tenant
	 * @return the list
	 * @throws PostManException
	 *             the post man exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws ExecutionException
	 *             the execution exception
	 */
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

	/**
	 * Notify all.
	 *
	 * @param tenant
	 *            the tenant
	 * @param message
	 *            the message
	 * @param title
	 *            the title
	 * @return the push message
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = "/postman/notify/all", method = RequestMethod.POST)
	public AmxApiResponse<PushMessage, Object> notifyAll(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant,
			@RequestParam String message, @RequestParam String title) throws PostManException {
		PushMessage msg = new PushMessage();
		msg.setMessage(message);
		msg.setSubject(title);
		msg.addTopic(String.format(PushMessage.FORMAT_TO_ALL, tenant.toString().toLowerCase()));
		return pushNotifyClient.sendDirect(msg);
	}

	/**
	 * Notify national.
	 *
	 * @param tenant
	 *            the tenant
	 * @param nationality
	 *            the nationality
	 * @param message
	 *            the message
	 * @param title
	 *            the title
	 * @return the push message
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = "/postman/notify/nationality", method = RequestMethod.POST)
	public AmxApiResponse<PushMessage, Object> notifyNational(
			@ApiParam(required = true, allowableValues = "KWT,BHR", value = "Select Tenant") @RequestParam Tenant tenant,
			@RequestParam Nations nationality, @RequestParam String message, @RequestParam String title)
			throws PostManException {
		PushMessage msg = new PushMessage();
		msg.setMessage(message);
		msg.setSubject(title);

		if (nationality == Nations.ALL) {
			msg.addTopic(String.format(PushMessage.FORMAT_TO_ALL, tenant.toString().toLowerCase()));
		} else {
			msg.addTopic(String.format(PushMessage.FORMAT_TO_NATIONALITY, tenant.toString().toLowerCase(),
					nationality.getCode()));
		}
		return pushNotifyClient.sendDirect(msg);
	}

}
