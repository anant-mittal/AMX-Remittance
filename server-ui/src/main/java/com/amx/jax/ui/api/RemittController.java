
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.ui.model.LoginData;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "User APIs")
public class RemittController {

	@Autowired
	private UserSession userSession;

	@Autowired
	private JaxService jaxService;

	@ApiOperation(value = "Validates login/pass but does not creates session")
	@RequestMapping(value = "/api/remot/xrate", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> login(@RequestParam String forCur, @RequestParam(required = false) String banBank,
			@RequestParam(required = false) BigDecimal domAmount) {
		return loginService.login(identity, password);
	}

}
