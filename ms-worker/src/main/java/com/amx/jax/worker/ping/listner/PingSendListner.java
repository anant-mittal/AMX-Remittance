package com.amx.jax.worker.ping.listner;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEvent(topic = AmxTunnelEvents.Names.PING_SINGLE, scheme = TunnelEventXchange.SEND_LISTNER)
public class PingSendListner extends PingCommonListner {

}