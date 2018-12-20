package com.amx.jax.client;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridView;

public interface IGridService {
	public static class Path {
		public static final String PREFIX = "/grid";
		public static final String GRID_VIEW = PREFIX + "/view/{gridView}";
	}

	public static class Params {
		public static final String GRID_VIEW = "gridView";
	}

	AmxApiResponse<?, GridMeta> gridView(GridView gridView, GridQuery gridQuery);
}
