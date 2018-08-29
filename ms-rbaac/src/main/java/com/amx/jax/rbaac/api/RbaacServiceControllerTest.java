package com.amx.jax.rbaac.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.RbaacService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionsResposeDTO;
import com.amx.jax.rbaac.dto.response.RolesResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("test/")
public class RbaacServiceControllerTest implements RbaacService {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(RbaacServiceControllerTest.class);

	@Autowired
	RbaacServiceClient rbaacServiceClient;

	@Override
	@ApiOperation("User Auth")
	@RequestMapping(value = ApiEndPoints.INIT_AUTH, method = RequestMethod.POST)
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO) {
		return rbaacServiceClient.initAuthForUser(userAuthInitReqDTO);
	}

	@Override
	@ApiOperation("User Authorisation")
	@RequestMapping(value = ApiEndPoints.AUTHORISE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDto) {
		return rbaacServiceClient.authoriseUser(reqDto);
	}

	@Override
	@ApiOperation("User Permissions Get")
	@RequestMapping(value = ApiEndPoints.PERMS_GET, method = RequestMethod.POST)
	public AmxApiResponse<PermissionsResposeDTO, Object> getAllPermissions(String ipAddr, String deviceId) {
		return rbaacServiceClient.getAllPermissions(ipAddr, deviceId);
	}

	@Override
	@ApiOperation("User Roles Get")
	@RequestMapping(value = ApiEndPoints.ROLES_GET, method = RequestMethod.POST)
	public AmxApiResponse<RolesResponseDTO, Object> getAllRoles(String ipAddr, String deviceId) {
		return rbaacServiceClient.getAllRoles(ipAddr, deviceId);
	}

	@Override
	public AmxApiResponse<String, Object> testGet() {
		return null;
	}

	@Override
	public AmxApiResponse<String, Object> testPost() {
		// TODO Auto-generated method stub
		return null;
	}

}
