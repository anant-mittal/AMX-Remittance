
package com.amx.jax.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.ITunnelEvent;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.tunnel.TunnelService;

/**
 * The Class PlaceOrderController.
 */
@RestController
public class BrokerController {

	@Autowired
	private TunnelService tunnelService;

	/**
	 * List of place orders.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/task/{scheme}/{topic}", method = { RequestMethod.POST })
	public AmxApiResponse<ITunnelEvent, ?> initTask(@RequestBody ITunnelEvent event,
			@PathVariable AmxTunnelEvents topic, @PathVariable TunnelEventXchange scheme) {
		event.setEventCode(topic.toString());
		if (scheme == TunnelEventXchange.SEND_LISTNER) {
			tunnelService.send(topic.toString(), event);
		} else if (scheme == TunnelEventXchange.TASK_WORKER) {
			tunnelService.task(topic.toString(), event);
		} else {
			tunnelService.shout(topic.toString(), event);
		}
		return AmxApiResponse.build(event);
	}

}
