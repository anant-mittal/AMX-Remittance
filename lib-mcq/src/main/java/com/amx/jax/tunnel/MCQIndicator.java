package com.amx.jax.tunnel;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.IndicatorListner;

@Component
public class MCQIndicator implements IndicatorListner {

	@Autowired
	TunnelSubscriberFactory tunnelSubscriberFactory;

	@Override
	public Map<String, Object> getIndicators() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tunnel.subs", TunnelSubscriberFactory.getStatus());
		return map;
	}

}
