package com.amx.jax.rbaac.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.IRbaacService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.dto.request.EmployeeDetailsRequestDTO;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.request.UserRoleMappingsRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleMappingForEmployee;
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
public class RbaacServiceControllerTest implements IRbaacService {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(RbaacServiceControllerTest.class);

	/** The rbaac service client. */
	@Autowired
	RbaacServiceClient rbaacServiceClient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#initAuthForUser(com.amx.jax.rbaac.dto.request.
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
	 * com.amx.jax.rbaac.IRbaacService#authoriseUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthorisationReqDTO)
	 */
	@Override
	@ApiOperation("User Authorisation")
	@RequestMapping(value = ApiEndPoints.AUTHORIZE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDto) {
		return rbaacServiceClient.authoriseUser(reqDto);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#getAllPermissions(java.lang.String,
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
	 * @see com.amx.jax.rbaac.IRbaacService#getAllRoles(java.lang.String,
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
	 * @see com.amx.jax.rbaac.IRbaacService#saveRole(com.amx.jax.rbaac.dto.request.
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
	 * @see com.amx.jax.rbaac.IRbaacService#getUserRoleMappingsForBranch(java.math.
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
	 * com.amx.jax.rbaac.IRbaacService#updateUserRoleMappings(com.amx.jax.rbaac.dto.
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
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#updateEmployeeAccountDetails(com.amx.jax.rbaac
	 * .dto.request.EmployeeDetailsRequestDTO)
	 */
	@Override
	@ApiOperation("User Account Update / (In)Activate / (Un)Lock")
	@RequestMapping(value = ApiEndPoints.UAC_UPDATE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> updateEmployeeAccountDetails(
			EmployeeDetailsRequestDTO edRequestDTO) {
		return rbaacServiceClient.updateEmployeeAccountDetails(edRequestDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#testGet()
	 */
	@Override
	public AmxApiResponse<String, Object> testGet() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#testPost()
	 */
	@Override
	public AmxApiResponse<String, Object> testPost() {
		return null;
	}

	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(Integer deviceRegId, String mOtp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> deactivateDevice(Integer deviceRegId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> createDeviceSession(Integer deviceRegId, String paireToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> pairDeviceSession(ClientType deviceType,
			Integer countryBranchSystemInventoryId, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> validateDeviceSessionToken(BigDecimal deviceRegId,
			String deviceSessionToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<BigDecimal, Object> getDeviceRegIdByBranchInventoryId(ClientType deviceClientType,
			BigDecimal countryBranchSystemInventoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<RoleMappingForEmployee, Object> getRoleMappingsForEmployee(BigDecimal employeeId,
			String ipAddr, String deviceId, Boolean filterRole) {
		// TODO Auto-generated method stub
		return null;
	}

}
