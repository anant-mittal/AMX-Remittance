package com.amx.jax.worker.tasks.ping;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEvent(topic = AmxTunnelEvents.Names.PING_SHOUT, scheme = TunnelEventXchange.SHOUT_LISTNER)
public class PingShoutListner extends PingCommonListner {

}