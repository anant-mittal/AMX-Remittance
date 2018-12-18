package com.amx.jax.grid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;

@RestController
public class GridViewController {

	@Autowired
	GridService gridService;

	@RequestMapping(value = "/grid/view/USER_SESSION", method = { RequestMethod.GET, RequestMethod.POST })
	public AmxApiResponse<UserSessionRecord, DataTableMeta> listUsersPaginatedForOracle(HttpServletRequest request,
			HttpServletResponse response, Model model, @RequestBody GridQuery gridQuery) {
		return gridService.getView("SELECT * FROM USER_SESSION", gridQuery,
				UserSessionRecord.class);
	}
}
