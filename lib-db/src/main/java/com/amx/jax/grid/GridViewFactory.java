package com.amx.jax.grid;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.FeedbackStarRating;
import com.amx.jax.dbmodel.employee.UserSession;
import com.amx.jax.grid.views.CustomerDetailViewRecord;
import com.amx.jax.grid.views.EmployeeDetailViewRecord;
import com.amx.jax.grid.views.TranxViewRecord;
import com.amx.jax.grid.views.TranxViewRecordByBene;
import com.amx.jax.grid.views.UserSessionRecord;
import com.amx.jax.grid.views.XRateViewRecord;

public class GridViewFactory {

	public static Map<GridView, GridInfo<?>> map = new HashMap<GridView, GridInfo<?>>();
	static {
		map.put(GridView.USER_SESSION_RECORD_CUSTOM, new GridInfo<UserSessionRecord>(
				"SELECT * FROM USER_SESSION",
				UserSessionRecord.class));
		map.put(GridView.USER_SESSION_RECORD, new GridInfo<UserSessionRecord>(
				UserSessionRecord.class));
		map.put(GridView.USER_SESSION, new GridInfo<UserSession>(UserSession.class));

		map.put(GridView.VW_FS_EMPLOYEE, new GridInfo<EmployeeDetailViewRecord>(EmployeeDetailViewRecord.class));
		map.put(GridView.EX_BRANCH_SYSTEM_INVENTORY, new GridInfo<BranchSystemDetail>(BranchSystemDetail.class));

		map.put(GridView.DEVICE_CLIENT, new GridInfo<Device>(Device.class));
		map.put(GridView.VW_CUSTOMER_KIBANA, new GridInfo<CustomerDetailViewRecord>(CustomerDetailViewRecord.class));
		map.put(GridView.VW_KIBANA_TRNX, new GridInfo<TranxViewRecord>(TranxViewRecord.class));
		map.put(GridView.VW_KIBANA_TRNX_BY_BENE, new GridInfo<TranxViewRecordByBene>(
				TranxViewRecordByBene.class));
		map.put(GridView.EX_V_RATE_PATTERN, new GridInfo<XRateViewRecord>(XRateViewRecord.class));
		map.put(GridView.VW_TRNX_FEEDBACK, new GridInfo<FeedbackStarRating>(FeedbackStarRating.class));

	}

	public static GridInfo<?> get(GridView gridView) {
		return map.get(gridView);
	}

}
