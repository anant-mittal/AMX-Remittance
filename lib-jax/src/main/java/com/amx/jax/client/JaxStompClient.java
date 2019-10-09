package com.amx.jax.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.AmxEnums.FxOrderStatus;
import com.amx.jax.logger.AuditActor.ActorType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.stomp.StompTunnelService;

@Component
public class JaxStompClient implements IJaxStompService {

	private static final Logger LOGGER = LoggerService.getLogger(JaxStompClient.class);

	@Autowired
	StompTunnelService stompTunnelService;

	@Override
	public void publishFxOrderStatusChange(BigDecimal collectionDocumentNo, BigDecimal collectionDocumentYear,
			FxOrderStatus status) {
		try {
			LOGGER.debug("in publishFxOrderStatusChange");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("status", status.toString());
			stompTunnelService
					.sendToAll("/fx/order/" + collectionDocumentNo + "/" + collectionDocumentYear + "/onupdate", data);
			stompTunnelService.sendToAll("/fx/order/list/onupdate", data);
		} catch (Exception e) {
			LOGGER.error("exception in publishFxOrderStatusChange : ", e);
		}
	}

	@Override
	public void publishOnCallCustomerStatus(BigDecimal empId, BigDecimal customerid) {
		try {
			LOGGER.debug("in publishOnCallCustomerStatus");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("customerid", customerid);
			stompTunnelService.sendTo(ActorType.E.getId(empId), "/branch-user/customer-call-session", data);
			stompTunnelService
					.sendToAll("/branch-user/customer-call-session/" + empId, data);
		} catch (Exception e) {
			LOGGER.error("exception in publishOnCallCustomerStatus : ", e);
		}
	}
}