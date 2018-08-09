package com.amx.jax.rbaac;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;

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

		private static final String SERVICE_PREFIX = "/rbaac";
		private static final String API_VERSION_V1 = "/v1";

		/** The Constant INIT_AUTH. */
		public static final String INIT_AUTH = SERVICE_PREFIX + API_VERSION_V1 + "/auth/init-auth";

		/** The Constant AUTHORISE. */
		public static final String AUTHORISE = SERVICE_PREFIX + API_VERSION_V1 + "/auth/authorise";

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

	}

	/**
	 * Begins process of user Authentication with employee details - empCode,
	 * CivilId and Access Terminal IP .
	 *
	 * @param employeeNo
	 *            the employee no
	 * @param identity
	 *            the identity
	 * @param ipAddress
	 *            the ip address
	 * @return the amx api response
	 */
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO);

	/**
	 * Authorise user.
	 *
	 * @param employeeNo
	 *            the employee no
	 * @param mOtpHash
	 *            the m otp hash
	 * @param eOtpHash
	 *            the e otp hash
	 * @param ipAddress
	 *            the ip address
	 * @return the amx api response
	 */
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDto);

}
