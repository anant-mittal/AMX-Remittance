package com.amx.jax.radar.snap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bytedeco.javacpp.opencv_videostab.RansacParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.def.AbstarctQueryFactory.QueryProcessor;
import com.amx.jax.device.DeviceBox;
import com.amx.jax.device.DeviceData;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQuery;
import com.amx.utils.Random;

@Component
@SnapQuery(SnapQueryTemplate.SIGNATURE_PADS_REPORT)
public class SnapQuerySignPadProcessor implements QueryProcessor<DeviceData> {

	@Autowired
	DeviceBox deviceBox;

	@Override
	public List<DeviceData> process() {

		List<DeviceData> devices = new ArrayList<DeviceData>();
		/*
		Set<String> y = deviceBox.map().keySet();
		try {
			for (String string : y) {
				try {
					DeviceData d = new DeviceData();
					d.setRegId(string);
					devices.add(d);
					deviceBox.fastRemove(string);
				} catch (Exception e) {
					System.out.println("LOCAL===   " + e.getMessage());
				}
			}
		} catch (Exception e2) {
			System.out.println("GLOBAL===   " + e2.getMessage());
		}
		DeviceData d = new DeviceData();
		d.setRegId("ddddddddd");
		deviceBox.put("ddd", d);
		return devices;
		*/

		Set<Entry<String, DeviceData>> x = deviceBox.map().entrySet();
		try {

			for (Entry<String, DeviceData> entry : x) {
				try {
					devices.add(entry.getValue());
				} catch (Exception e) {
					System.out.println("LOCAL===   " + e.getMessage());
				}
			}
		} catch (Exception e2) {
			System.out.println("GLOBAL===   " + e2.getMessage());
		}
		
		DeviceData d = new DeviceData();
		d.setRegId("ddddddddd");
		deviceBox.put(Random.randomNumeric(3), d);
		
		return devices;

	}

}
