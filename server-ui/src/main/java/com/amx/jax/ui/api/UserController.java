package com.amx.jax.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.UserMetaData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "User APIs")
public class UserController {

	@Autowired
	private UserSession userSession;

	@ApiOperation(value = "Get UserMeta Data")
	@RequestMapping(value = "/api/user/meta", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> verifyID(@RequestParam String civilid) {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		wrapper.getData().setValid(userSession.isValid());

		if (userSession.getCustomerModel() != null) {
			wrapper.getData().setActive(true);
		}
		return wrapper;
	}
}
