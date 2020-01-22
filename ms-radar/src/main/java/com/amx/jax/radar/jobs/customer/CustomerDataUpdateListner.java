package com.amx.jax.radar.jobs.customer;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.AppConfig;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.grid.GridService;
import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncIndex;
import com.amx.jax.radar.service.CustomerDetailViewRecordManager;
import com.amx.jax.radar.snap.SnapQueryService.BulkRequestSnapBuilder;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.DATAUPD_CUSTOMER, scheme = TunnelEventXchange.TASK_WORKER)
public class CustomerDataUpdateListner implements ITunnelSubscriber<DBEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String CUST_ID = "CUST_ID";

	@Autowired
	public AppConfig appConfig;

	@Autowired
	public ESRepository esRepository;

	@Autowired
	public GridService gridService;

	@Autowired
	public OracleVarsCache oracleVarsCache;

	@Autowired
	CustomerDetailViewRecordManager customerDetailViewRecordManager;

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		String custId = ArgUtil.parseAsString(event.getData().get(CUST_ID));
		BulkRequestSnapBuilder builder = new BulkRequestSnapBuilder();
		try {
			CustomerDetailViewRecord record = customerDetailViewRecordManager.queryByCustomerId(custId);
			OracleViewDocument document = new OracleViewDocument(record);
			document.setTimestamp(new Date(System.currentTimeMillis()));
			builder.update(DBSyncIndex.CUSTOMER_JOB.getIndexName(), document);
		} catch (Exception e) {
			LOGGER.error("CustomerViewTask Excep", e);
		}
		esRepository.bulk(builder.build());

	}
}
