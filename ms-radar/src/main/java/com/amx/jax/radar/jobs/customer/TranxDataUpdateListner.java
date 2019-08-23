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
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

//@TunnelEventMapping(topic = AmxTunnelEvents.Names.DATAUPD_TRNX, scheme = TunnelEventXchange.TASK_WORKER)
@TunnelEventMapping(topic = AmxTunnelEvents.Names.TRNX_BENE_CREDIT, scheme = TunnelEventXchange.TASK_LISTNER)
public class TranxDataUpdateListner implements ITunnelSubscriber<DBEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String ID = "APP_ID";
	private static final String CUST_ID = "CUST_ID";
	private static final String TRANX_ID = "TRANX_ID";
	private static final String TRNXAMT = "TRNXAMT";
	private static final String LOYALTY = "LOYALTY";
	private static final String TRNREF = "TRNREF";
	private static final String TRNDATE = "TRNDATE";
	private static final String LANG_ID = "LANG_ID";
	private static final String TENANT = "TENANT";
	private static final String CURNAME = "CURNAME";
	private static final String TYPE = "TYPE";

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
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));

		String custId = ArgUtil.parseAsString(event.getData().get(CUST_ID));
		String trnxRef = ArgUtil.parseAsString(event.getData().get(TRNREF));
		String trnxDate = ArgUtil.parseAsString(event.getData().get(TRNDATE));
		String tranxId = ArgUtil.parseAsString(event.getData().get(TRANX_ID));

		if (ArgUtil.isEmpty(tranxId)) {
			LOGGER.info("Nothing to Do as Transaction Id is Empty");
			return;
		}

		// Query
		GridQuery gridQuery = new GridQuery();
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());

		// Conditions/Filters
		GridColumn column = new GridColumn();
		column.setKey("trnxId");
		column.setOperator(FilterOperater.EQ);
		column.setDataType(FilterDataType.NUMBER);
		column.setValue(tranxId);
		column.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column);

		// Sorting
		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.ASC);

		GridViewBuilder<TranxViewRecord> y = gridService
				.view(GridView.VW_KIBANA_TRNX, gridQuery);
		AmxApiResponse<TranxViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();
		for (TranxViewRecord record : x.getResults()) {
			try {
				OracleViewDocument document = new OracleViewDocument(record);
				document.setTimestamp(new Date(System.currentTimeMillis()));
				builder.update(oracleVarsCache.getTranxIndex(), document);
			} catch (Exception e) {
				LOGGER.error("TranxViewTask Excep", e);
			}
		}
		esRepository.bulk(builder.build());

	}
}
