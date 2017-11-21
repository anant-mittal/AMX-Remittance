package com.amx.jax.userservice.controller;

import static com.amx.jax.constant.ApiEndpoint.USER_API_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.model.UserModel;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(USER_API_ENDPOINT)
public class UserController {

	@Autowired
	private MetaData metaData;

	@Autowired
	private ConverterUtil converterUtil;
	
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse registerUser(@RequestBody String json) {

		UserModel userModel = (UserModel) converterUtil.unmarshall(json, userService.getModelClass());
		ApiResponse response = userService.registerUser(userModel);

		return response;
	}

	/*@RequestMapping(value = "/{user-id}", method = RequestMethod.GET)
	public ApiResponse getUser(@PathVariable("user-id") Long userId) {

		ApiResponse response = metaData.getServiceFactory().getUserService().getUser(userId);
		return response;
	}
*/
	/*@RequestMapping(value = "/{user-id}", method = RequestMethod.PUT)
	public ApiResponse editUser(@PathVariable("user-id") Long userId, @RequestBody String json) {

		AbstractUserService userService = (AbstractUserService) metaData.getServiceFactory().getUserService();
		AbstractUserModel userModel = (AbstractUserModel) converterUtil.unmarshall(json, userService.getModelClass());
		userModel.setId(userId);
		ApiResponse response = metaData.getServiceFactory().getUserService().editUser(userModel);
		return response;
	}*/

}
