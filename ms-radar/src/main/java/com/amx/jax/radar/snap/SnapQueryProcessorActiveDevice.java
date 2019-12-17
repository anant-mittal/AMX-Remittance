package com.amx.jax.radar.snap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.def.AbstractQueryFactory.QueryProcessor;
import com.amx.jax.device.DeviceBox;
import com.amx.jax.device.DeviceData;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQuery;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQueryParams;

@Component
@SnapQuery(SnapQueryTemplate.ACTIVE_DEVICE_REPORT)
public class SnapQueryProcessorActiveDevice implements QueryProcessor<DeviceData, SnapQueryParams> {

	public static final Logger LOGGER = LoggerService.getLogger(SnapQueryProcessorActiveDevice.class);

	@Autowired
	DeviceBox deviceBox;

	@Override
	public List<DeviceData> process(SnapQueryParams params) {

		List<DeviceData> devices = new ArrayList<DeviceData>();

		Set<Entry<String, DeviceData>> x = deviceBox.map().entrySet();
		try {

			for (Entry<String, DeviceData> entry : x) {
				try {
					devices.add(entry.getValue());
				} catch (Exception e) {
					LOGGER.error("LOCAL===   {}" + e.getMessage());
				}
			}
		} catch (Exception e2) {
			LOGGER.error("GLOBAL===   {}" + e2.getMessage());
		}

		return devices;

	}

}
