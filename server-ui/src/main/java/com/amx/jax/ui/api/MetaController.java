
package com.amx.jax.ui.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.jax.scope.TenantBean;
import com.amx.jax.ui.response.ResponseMeta;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.AppEnvironment;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.session.GuestSession;
import com.hazelcast.core.HazelcastInstance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Meta APIs")
public class MetaController {

	@Autowired
	private JaxService jaxService;

	@Autowired
	AppEnvironment env;

	@Autowired
	TenantBean tenantBean;
	@Autowired
	GuestSession guestSession;

	@Autowired
	HazelcastInstance hazelcastInstance;

	@ApiOperation(value = "List of All Possible Codes")
	@RequestMapping(value = "/pub/meta/status/list", method = { RequestMethod.POST })
	public ResponseWrapper<ResponseMeta> tranxhistory() {
		ResponseWrapper<ResponseMeta> wrapper = new ResponseWrapper<ResponseMeta>(new ResponseMeta());
		return wrapper;
	}

	@ApiOperation(value = "Ping")
	@RequestMapping(value = "/pub/ping", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<Map<String, Object>> status(@RequestParam(required = false) String site,
			HttpSession httpSession) {
		ResponseWrapper<Map<String, Object>> wrapper = new ResponseWrapper<Map<String, Object>>(
				new HashMap<String, Object>());

		if (tenantBean.getName() == null) {
			tenantBean.setName("site=" + site);
		}

		Map<String, Integer> mapCustomers = hazelcastInstance.getMap("test");
		Integer hits = guestSession.hitCounter();

		wrapper.getData().put("debug", env.isDebug());
		wrapper.getData().put("site", tenantBean.getName());
		wrapper.getData().put("id", httpSession.getId());
		wrapper.getData().put("hits-s", hits);
		wrapper.getData().put("hits-h", mapCustomers.get("hits"));

		mapCustomers.put("hits", hits);

		return wrapper;
	}

	@RequestMapping(value = "/api/meta/ccy/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<CurrencyMasterDTO>> ccyList() {
		return new ResponseWrapper<List<CurrencyMasterDTO>>(
				jaxService.setDefaults().getMetaClient().getAllOnlineCurrency().getResults());
	}

	@RequestMapping(value = "/api/meta/income_sources", method = { RequestMethod.POST })
	public ResponseWrapper<List<SourceOfIncomeDto>> fundSources() {
		return new ResponseWrapper<List<SourceOfIncomeDto>>(
				jaxService.setDefaults().getRemitClient().getSourceOfIncome().getResults());
	}

	@RequestMapping(value = "/api/meta/tranx_purpose", method = { RequestMethod.POST })
	public ResponseWrapper<List<SourceOfIncomeDto>> remittPurpose() {
		return new ResponseWrapper<List<SourceOfIncomeDto>>(
				jaxService.setDefaults().getRemitClient().getSourceOfIncome().getResults());
	}

}
