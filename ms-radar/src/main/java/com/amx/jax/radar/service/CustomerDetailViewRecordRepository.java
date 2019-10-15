package com.amx.jax.radar.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
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

/**
 * The Class Customer Service.
 */
@Component
public class CustomerDetailViewRecordRepository {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	public GridService gridService;

	public CustomerDetailViewRecord getByIndentity(String indentity) {
		GridQuery gridQuery = new GridQuery();
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());

		// Conditions/Filters
		GridColumn column = new GridColumn();
		column.setKey("identity");
		column.setOperator(FilterOperater.EQ);
		column.setDataType(FilterDataType.STRING);
		column.setValue(indentity);
		column.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column);

		// Sorting
		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.ASC);

		GridViewBuilder<CustomerDetailViewRecord> y = gridService
				.view(GridView.VW_CUSTOMER_KIBANA, gridQuery);
		AmxApiResponse<CustomerDetailViewRecord, GridMeta> x = y.get();
		return x.getResult();
	}

	public CustomerDetailViewRecord getByWhatsApp(String whatsAppNumber) {
		GridQuery gridQuery = new GridQuery();
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());

		// Conditions/Filters
		GridColumn column = new GridColumn();
		column.setKey("whatsapp");
		column.setOperator(FilterOperater.EQ);
		column.setDataType(FilterDataType.NUMBER);
		column.setValue(whatsAppNumber);
		column.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column);

		// Sorting
		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.ASC);

		GridViewBuilder<CustomerDetailViewRecord> y = gridService
				.view(GridView.VW_CUSTOMER_KIBANA, gridQuery);
		AmxApiResponse<CustomerDetailViewRecord, GridMeta> x = y.get();
		return x.getResult();
	}

}
