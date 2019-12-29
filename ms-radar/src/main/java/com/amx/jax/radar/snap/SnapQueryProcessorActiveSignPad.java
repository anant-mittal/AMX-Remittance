package com.amx.jax.radar.snap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapQueryParams;
import com.amx.jax.def.AbstractQueryFactory.QueryProcessor;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQuery;
import com.amx.jax.signpad.SignPadBox;
import com.amx.jax.signpad.SignPadData;

@Component
@SnapQuery(SnapQueryTemplate.ACTIVE_SIGNPAD_REPORT)
public class SnapQueryProcessorActiveSignPad implements QueryProcessor<SignPadData, SnapQueryParams> {

	public static final Logger LOGGER = LoggerService.getLogger(SnapQueryProcessorActiveSignPad.class);

	@Autowired
	SignPadBox signPadBox;

	@Override
	public List<SignPadData> process(SnapQueryParams params) {

		List<SignPadData> devices = new ArrayList<SignPadData>();

		Set<Entry<String, SignPadData>> x = signPadBox.map().entrySet();
		try {

			for (Entry<String, SignPadData> entry : x) {
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
