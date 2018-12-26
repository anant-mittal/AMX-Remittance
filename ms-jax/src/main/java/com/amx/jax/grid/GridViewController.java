package com.amx.jax.grid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;

@RestController
public class GridViewController {

	Logger LOGGER = LoggerService.getLogger(getClass());

	@Autowired
	GridService gridService;

	@RequestMapping(value = "/grid/view/{gridView}", method = { RequestMethod.POST })
	public AmxApiResponse<?, GridMeta> gridView(@PathVariable(value = "gridView") GridView gridView,
			@RequestBody GridQuery gridQuery) {
		return gridService.view(gridView, gridQuery).get();
	}

}
