
package com.amx.jax.ui.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.tunnel.TunnelService;
import com.amx.jax.ui.response.ResponseWrapper;

import io.swagger.annotations.Api;

/**
 * The Class PlaceOrderController.
 */
@RestController
@Api(value = "Ping Apis")
public class PingController {

	@Autowired
	private TunnelService tunnelService;

	/**
	 * List of place orders.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/notify_all", method = { RequestMethod.GET })
	public ResponseWrapper<Map<String, String>> listOfPlaceOrders(@RequestParam(required = false) String sms,
			@RequestParam(required = false) String whatsapp, @RequestParam(required = false) String email,
			@RequestParam(required = false) String customerId, @RequestParam String message,
			@RequestParam AmxTunnelEvents topic, @RequestParam TunnelEventXchange scheme) {
		ResponseWrapper<Map<String, String>> wrapper = new ResponseWrapper<Map<String, String>>(
				new HashMap<String, String>());
		wrapper.getData().put("message", message);
		wrapper.getData().put("sms", sms);
		wrapper.getData().put("whatsapp", whatsapp);
		wrapper.getData().put("email", email);
		wrapper.getData().put("customerId", customerId);

		DBEvent event = new DBEvent();
		event.setEventCode(topic.toString());
		event.setData(wrapper.getData());
		if (scheme == TunnelEventXchange.SEND_LISTNER) {
			tunnelService.send(topic.toString(), event);
		} else if (scheme == TunnelEventXchange.TASK_WORKER) {
			tunnelService.task(topic.toString(), event);
		} else {
			tunnelService.shout(topic.toString(), event);
		}
		return wrapper;
	}

}
