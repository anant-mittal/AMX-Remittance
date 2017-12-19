
package com.amx.jax.ui.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.scope.TenantBean;
import com.amx.jax.ui.response.ResponseMeta;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.AppEnvironment;
import com.amx.jax.ui.service.JaxService;

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

	@ApiOperation(value = "List of All Possible Codes")
	@RequestMapping(value = "/pub/meta/status/list", method = { RequestMethod.POST })
	public ResponseWrapper<ResponseMeta> tranxhistory() {
		ResponseWrapper<ResponseMeta> wrapper = new ResponseWrapper<ResponseMeta>(new ResponseMeta());
		return wrapper;
	}

	@ApiOperation(value = "Ping")
	@RequestMapping(value = "/pub/ping", method = { RequestMethod.POST })
	public ResponseWrapper<Map<String, Object>> status(@RequestParam(required = false) String site) {
		ResponseWrapper<Map<String, Object>> wrapper = new ResponseWrapper<Map<String, Object>>(
				new HashMap<String, Object>());

		if (tenantBean.getName() == null) {
			tenantBean.setName("site=" + site);
		}

		wrapper.getData().put("debug", env.isDebug());
		wrapper.getData().put("appDebug", env.getAppDebug());
		wrapper.getData().put("site", tenantBean.getName());
		return wrapper;
	}

}
