package com.amx.jax.metrics;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import com.amx.jax.def.IndicatorListner;
import com.amx.utils.ArgUtil;

@Component
public class AmxHealthIndicator extends AbstractHealthIndicator {

	@Autowired(required = false)
	List<IndicatorListner> listners;

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		// Use the builder to build the health status details that should be reported.
		// If you throw an exception, the status will be DOWN with the exception
		// message.
		builder.up();

		if (!ArgUtil.isEmpty(listners)) {
			for (IndicatorListner eachListner : listners) {
				Map<String, Object> x = eachListner.getIndicators();
				for (Entry<String, Object> indicatorListner : x.entrySet()) {
					builder.withDetail(indicatorListner.getKey(), indicatorListner.getValue());
				}
			}
		}
	}
}
