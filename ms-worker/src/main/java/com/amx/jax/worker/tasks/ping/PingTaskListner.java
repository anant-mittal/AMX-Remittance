package com.amx.jax.worker.tasks.ping;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.PING_TASK, scheme = TunnelEventXchange.TASK_WORKER)
public class PingTaskListner extends PingCommonListner {

}