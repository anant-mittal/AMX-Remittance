package com.amx.jax.radar.jobs.customer;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.grid.GridColumn;
import com.amx.jax.grid.GridConstants.FilterDataType;
import com.amx.jax.grid.GridConstants.FilterOperater;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridService;
import com.amx.jax.grid.GridService.GridViewBuilder;
import com.amx.jax.grid.GridView;
import com.amx.jax.grid.SortOrder;
import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncIndex;
import com.amx.jax.radar.snap.SnapQueryService.BulkRequestSnapBuilder;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

//@TunnelEventMapping(topic = AmxTunnelEvents.Names.DATAUPD_TRNX, scheme = TunnelEventXchange.TASK_WORKER)
@TunnelEventMapping(topic = AmxTunnelEvents.Names.TRNX_STATUS_UPDATE, scheme = TunnelEventXchange.TASK_WORKER)
public class TranxViewUpdateListner implements ITunnelSubscriber<DBEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String APP_ID = "APP_ID";
	private static final String TRANX_ID = "TRANX_ID";

	@Autowired
	public AppConfig appConfig;

	@Autowired
	public ESRepository esRepository;

	@Autowired
	public GridService gridService;

	@Autowired
	public OracleVarsCache oracleVarsCache;

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));

		String tranxId = ArgUtil.parseAsString(event.getData().get(TRANX_ID));
		String appId = ArgUtil.parseAsString(event.getData().get(APP_ID));

		if (ArgUtil.isEmpty(tranxId) && ArgUtil.isEmpty(appId)) {
			LOGGER.info("Nothing to Do as Transaction/Application Id is Empty");
			return;
		}

		// Query
		GridQuery gridQuery = new GridQuery();
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());

		// Conditions/Filters
		GridColumn column = new GridColumn();
		column.setOperator(FilterOperater.EQ);
		column.setDataType(FilterDataType.NUMBER);

		if (ArgUtil.is(tranxId)) {
			column.setKey("trnxId");
			column.setValue(tranxId);
		} else {
			column.setKey("id");
			column.setValue(appId);
		}

		column.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column);

		// Sorting
		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.ASC);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX, gridQuery);
		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestSnapBuilder builder = new BulkRequestSnapBuilder();
		for (TranxViewRecord record : x.getResults()) {
			try {
				OracleViewDocument document = new OracleViewDocument(record);
				document.setTimestamp(new Date(System.currentTimeMillis()));
				builder.update(DBSyncIndex.TRANSACTION_JOB.getIndexName(), document);
			} catch (Exception e) {
				LOGGER.error("TranxViewTask Excep", e);
			}
		}
		esRepository.bulk(builder.build());

	}
}
