package com.amx.jax.branch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.GridServiceClient;
import com.amx.jax.grid.GridMeta;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridView;

@RestController
public class GridController {

	@Autowired
	GridServiceClient gridService;

	@RequestMapping(value = "/pub/grid/view/{gridView}", method = { RequestMethod.POST })
	public AmxApiResponse<?, GridMeta> gridView(@PathVariable(value = "gridView") GridView gridView,
			@RequestBody GridQuery gridQuery) {
		return gridService.gridView(gridView, gridQuery);
	}
}
