package com.amx.jax.rbaac.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.RbaacService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.request.UserRoleMappingsRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingsResponseDTO;

import io.swagger.annotations.ApiOperation;

/**
 * The Class RbaacServiceControllerTest.
 */
@RestController
@RequestMapping("test/")
public class RbaacServiceControllerTest implements RbaacService {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(RbaacServiceControllerTest.class);

	/** The rbaac service client. */
	@Autowired
	RbaacServiceClient rbaacServiceClient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.RbaacService#initAuthForUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthInitReqDTO)
	 */
	@Override
	@ApiOperation("User Auth")
	@RequestMapping(value = ApiEndPoints.INIT_AUTH, method = RequestMethod.POST)
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO) {
		return rbaacServiceClient.initAuthForUser(userAuthInitReqDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.RbaacService#authoriseUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthorisationReqDTO)
	 */
	@Override
	@ApiOperation("User Authorisation")
	@RequestMapping(value = ApiEndPoints.AUTHORISE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDto) {
		return rbaacServiceClient.authoriseUser(reqDto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getAllPermissions(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@ApiOperation("User Permissions Get")
	@RequestMapping(value = ApiEndPoints.PERMS_GET, method = RequestMethod.POST)
	public AmxApiResponse<PermissionResposeDTO, Object> getAllPermissions(String ipAddr, String deviceId) {
		return rbaacServiceClient.getAllPermissions(ipAddr, deviceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getAllRoles(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@ApiOperation("User Roles Get")
	@RequestMapping(value = ApiEndPoints.ROLES_GET, method = RequestMethod.POST)
	public AmxApiResponse<RoleResponseDTO, Object> getAllRoles(String ipAddr, String deviceId) {
		return rbaacServiceClient.getAllRoles(ipAddr, deviceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#saveRole(com.amx.jax.rbaac.dto.request.
	 * RoleRequestDTO)
	 */
	@Override
	@ApiOperation("User Roles Save")
	@RequestMapping(value = ApiEndPoints.ROLES_SAVE, method = RequestMethod.POST)
	public AmxApiResponse<RoleResponseDTO, Object> saveRole(RoleRequestDTO roleRequestDTO) {
		return rbaacServiceClient.saveRole(roleRequestDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getUserRoleMappingsForBranch(java.math.
	 * BigDecimal, java.lang.String, java.lang.String)
	 */
	@Override
	@ApiOperation("User Roles Allocations Get for Branch")
	@RequestMapping(value = ApiEndPoints.RA_GET_FOR_BRANCH, method = RequestMethod.POST)
	public AmxApiResponse<UserRoleMappingsResponseDTO, Object> getUserRoleMappingsForBranch(BigDecimal countryBranchId,
			String ipAddr, String deviceId) {

		return rbaacServiceClient.getUserRoleMappingsForBranch(countryBranchId, ipAddr, deviceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.RbaacService#updateUserRoleMappings(com.amx.jax.rbaac.dto.
	 * request.UserRoleMappingsRequestDTO)
	 */
	@Override
	@ApiOperation("User Roles Mapping Update / Delete")
	@RequestMapping(value = ApiEndPoints.RA_UPDATE, method = RequestMethod.POST)
	public AmxApiResponse<UserRoleMappingDTO, Object> updateUserRoleMappings(UserRoleMappingsRequestDTO urmRequestDTO) {

		return rbaacServiceClient.updateUserRoleMappings(urmRequestDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#testGet()
	 */
	@Override
	public AmxApiResponse<String, Object> testGet() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#testPost()
	 */
	@Override
	public AmxApiResponse<String, Object> testPost() {
		return null;
	}

}
