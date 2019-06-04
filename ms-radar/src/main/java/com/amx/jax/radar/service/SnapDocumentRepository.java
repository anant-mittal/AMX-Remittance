package com.amx.jax.radar.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants.SnapIndexName;
import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.dict.ContactType;
import com.amx.jax.grid.views.ContactVerificationRecord;
import com.amx.jax.model.MapModel;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.radar.snap.SnapQueryService;

@Component
public class SnapDocumentRepository {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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

	public OracleViewDocument getCustomerById(String id) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("_id", id);
		SnapModelWrapper x = snapQueryService.execute(SnapQueryTemplate.FIND_DOC_BY_ID, SnapIndexName.CUSTOMER, query);
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

	public OracleViewDocument getCustomerVerificationLink(String verificationId) {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("_id", verificationId);
		SnapModelWrapper x = snapQueryService.execute(SnapQueryTemplate.FIND_DOC_BY_ID, SnapIndexName.VERIFY, query);
		if (x.getHits().getTotal() > 0) {
			return x.getHits().getHits().get(0).getSource(OracleViewDocument.class);
		}
		return null;
	}

	public OracleViewDocument createCustomerVerificationLink(ContactType type, BigDecimal custmerId) {
		ContactVerificationRecord link = new ContactVerificationRecord();
		link.setCustomerId(custmerId);
		link.setContactType(type);
		link.setLinkDate(new Date(System.currentTimeMillis()));
		OracleViewDocument doc = new OracleViewDocument(link);
		Map<String, Object> x = snapQueryService.save(SnapIndexName.VERIFY, doc);
		return new MapModel(x).as(OracleViewDocument.class);
	}
}
