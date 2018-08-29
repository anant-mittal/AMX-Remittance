package com.amx.jax.rbaac;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;

// TODO: Auto-generated Javadoc
/**
 * The Interface RbaacService.
 *
 * @author abhijeet
 */
public interface RbaacService {

	/**
	 * The Class ApiEndPoints.
	 */
	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		private static final String SERVICE_PREFIX = "/rbaac";

		/** The Constant API_VERSION_V1. */
		private static final String API_VERSION_V1 = "/v1";

		/** The Constant INIT_AUTH. */
		public static final String INIT_AUTH = SERVICE_PREFIX + API_VERSION_V1 + "/auth/init-auth";

		/** The Constant AUTHORISE. */
		public static final String AUTHORISE = SERVICE_PREFIX + API_VERSION_V1 + "/auth/authorise";
		
		/** The Constant PERMS_GET. */
		public static final String PERMS_GET = SERVICE_PREFIX + API_VERSION_V1 + "/perms/get";

		/** The Constant ROLES_GET. */
		public static final String ROLES_GET = SERVICE_PREFIX + API_VERSION_V1 + "/roles/get";

		/** The Constant ROLES_SAVE. */
		public static final String ROLES_SAVE = SERVICE_PREFIX + API_VERSION_V1 + "/roles/save";

		/** The Constant ROLES_ALLOCATE. */
		public static final String ROLES_ALLOCATE = SERVICE_PREFIX + API_VERSION_V1 + "/roles/access/allocate";

		/** The Constant ROLES_UPDATE. */
		public static final String ROLES_UPDATE = SERVICE_PREFIX + API_VERSION_V1 + "/roles/access/update";

		/** The Constant ROLES_REVOKE. */
		public static final String ROLES_REVOKE = SERVICE_PREFIX + API_VERSION_V1 + "/roles/access/revoke";

		/** The Constant UAC_UPDATE. */
		public static final String UAC_UPDATE = SERVICE_PREFIX + API_VERSION_V1 + "/user/account/update";

		/** The Constant TEST_GET. */
		public static final String TEST_GET = SERVICE_PREFIX + API_VERSION_V1 + "/test/get";

		/** The Constant TEST_POST. */
		public static final String TEST_POST = SERVICE_PREFIX + API_VERSION_V1 + "/test/post";

	}

	/**
	 * Begins process of user Authentication with employee details - empCode,
	 * CivilId and Access Terminal IP .
	 *
	 * @param userAuthInitReqDTO
	 *            the user auth init req DTO
	 * @return the amx api response
	 */
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO);

	/**
	 * Authorise user.
	 *
	 * @param reqDto
	 *            the req dto
	 * @return the amx api response
	 */
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDto);

	/**
	 * Gets the all permissions.
	 *
	 * @param ipAddr the ip addr
	 * @param deviceId the device id
	 * @return the all permissions
	 */
	public AmxApiResponse<PermissionResposeDTO, Object> getAllPermissions(String ipAddr, String deviceId);
	

	/**
	 * Gets the all roles.
	 *
	 * @param ipAddr the ip addr
	 * @param deviceId the device id
	 * @return the all roles
	 */
	public AmxApiResponse<RoleResponseDTO, Object> getAllRoles(String ipAddr, String deviceId);

	/**
	 * Test get.
	 *
	 * @return the amx api response
	 */
	public AmxApiResponse<String, Object> testGet();

	/**
	 * Test post.
	 *
	 * @return the amx api response
	 */
	public AmxApiResponse<String, Object> testPost();

}
