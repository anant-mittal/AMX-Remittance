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
import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.ESRepository;
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

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		String custId = ArgUtil.parseAsString(event.getData().get(CUST_ID));

		// Query
		GridQuery gridQuery = new GridQuery();
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());

		// Conditions/Filters
		GridColumn column = new GridColumn();
		column.setKey("id");
		column.setOperator(FilterOperater.EQ);
		column.setDataType(FilterDataType.NUMBER);
		column.setValue(custId);
		column.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column);

		// Sorting
		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.ASC);

		GridViewBuilder<CustomerDetailViewRecord> y = gridService
				.view(GridView.VW_CUSTOMER_KIBANA, gridQuery);
		AmxApiResponse<CustomerDetailViewRecord, GridMeta> x = y.get();

		BulkRequestBuilder builder = new BulkRequestBuilder();
		for (CustomerDetailViewRecord record : x.getResults()) {
			try {
				OracleViewDocument document = new OracleViewDocument(record);
				document.setTimestamp(new Date(System.currentTimeMillis()));
				builder.update(oracleVarsCache.getCustomerIndex(), document);
			} catch (Exception e) {
				LOGGER.error("CustomerViewTask Excep", e);
			}
		}
		esRepository.bulk(builder.build());

	}
}
