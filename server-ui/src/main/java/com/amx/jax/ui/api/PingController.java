
package com.amx.jax.ui.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.event.Event;
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
	public ResponseWrapper<Map<String, String>> listOfPlaceOrders(@RequestParam String to, @RequestParam String message,
			@RequestParam AmxTunnelEvents topic, @RequestParam TunnelEventXchange scheme) {
		ResponseWrapper<Map<String, String>> wrapper = new ResponseWrapper<Map<String, String>>(
				new HashMap<String, String>());
		wrapper.getData().put("message", message);
		wrapper.getData().put("to", to);
		Event event = new Event();
		event.setEvent_code(topic.toString());
		event.setData(wrapper.getData());
		if (scheme == TunnelEventXchange.SEND_LISTNER) {
			tunnelService.send(topic, event);
		} else {
			tunnelService.shout(topic, event);
		}
		return wrapper;
	}

}
