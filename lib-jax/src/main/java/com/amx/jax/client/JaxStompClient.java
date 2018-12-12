package com.amx.jax.client;

import org.slf4j.Logger;

import java.math.BigDecimal;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.stomp.StompTunnelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JaxStompClient implements IJaxStompService {
    
    private static final Logger LOGGER = LoggerService.getLogger(JaxStompClient.class);
    
    @Autowired
	StompTunnelService stompTunnelService;
    
    @Override
    public void publishFxOrderStatusChange(BigDecimal orderNumber, BigDecimal orderYear, Object updateData) {
        try {
            LOGGER.debug("in publishFxOrderStatusChange");
            stompTunnelService.sendToAll("/fx/order/"+orderNumber+"/"+orderYear +"/onupdate", updateData);
            stompTunnelService.sendToAll("/fx/order/list/onupdate", updateData);
        } catch (Exception e) {
            LOGGER.error("exception in getStatus : ", e);
        }
        
    }
    
}