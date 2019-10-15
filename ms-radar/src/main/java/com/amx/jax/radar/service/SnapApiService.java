package com.amx.jax.radar.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.radar.snap.SnapQueryService;

@Component
public class SnapApiService {

	@Autowired
	private SnapQueryService snapQueryService;

	public OracleViewDocument getCustomerByIdentity(String identity) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("gte", "now-25y");
		query.put("lte", "now");
		query.put("searchKey", "customer.identity");
		query.put("searchValue", identity);
		SnapModelWrapper x = snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_PROFILE, query);
		if (x.getHits().getTotal() > 0) {
			return x.getHits().getHits().get(0).getSource(OracleViewDocument.class);

		}
		return null;
	}

	public OracleViewDocument getCustomerByWhatsApp(String whatsAppPrefix, String whatsAppNo) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("gte", "now-25y");
		query.put("lte", "now");
		query.put("searchKey", "customer.mobile");
		query.put("searchValue", whatsAppNo);
		SnapModelWrapper x = snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_PROFILE, query);
		if (x.getHits().getTotal() > 0) {
			return x.getHits().getHits().get(0).getSource(OracleViewDocument.class);

		}
		return null;
	}

}
