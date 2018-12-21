package com.amx.jax.grid;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.employee.UserSession;
import com.amx.jax.grid.views.UserSessionRecord;

public class GridViewFactory {

	public static Map<GridView, GridInfo<?>> map = new HashMap<GridView, GridInfo<?>>();
	static {
		map.put(GridView.USER_SESSION_RESORD, new GridInfo<UserSessionRecord>(
				"SELECT * FROM USER_SESSION",
				UserSessionRecord.class));
		map.put(GridView.USER_SESSION, new GridInfo<UserSession>(UserSession.class));
		map.put(GridView.DEVICE_CLIENT, new GridInfo<Device>(Device.class));
	}

	public static GridInfo<?> get(GridView gridView) {
		return map.get(gridView);
	}
}
