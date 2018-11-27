
package com.amx.jax.broker;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.tunnel.TunnelEvent;
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
	public AmxApiResponse<TunnelEvent, ?> initTask(@RequestBody TunnelEvent event,
			@PathVariable String topic,
			@PathVariable TunnelEventXchange scheme) {
		event.setEventCode(topic);
		if (scheme == TunnelEventXchange.SEND_LISTNER) {
			tunnelService.send(topic, event);
		} else if (scheme == TunnelEventXchange.TASK_WORKER) {
			tunnelService.task(topic, event);
		} else {
			tunnelService.shout(topic, event);
		}
		return AmxApiResponse.build(event);
	}

	@Autowired
	private BrokerService brokerService;

	@ApiRequest(type = RequestType.POLL)
	@RequestMapping(value = "/pub/task/{scheme}/{topic}", method = { RequestMethod.POST })
	public AmxApiResponse<Object, Object> pollEvents(BigDecimal eventId) {
		brokerService.pushNewEventNotifications();
		return AmxApiResponse.build();
	}

}
