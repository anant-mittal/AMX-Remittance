package com.amx.jax.rbaac;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.dto.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.UserAuthInitResponseDTO;

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

		/** The Constant INIT_AUTH. */
		public static final String INIT_AUTH = "/v1/auth/init-auth";

		/** The Constant AUTHORISE. */
		public static final String AUTHORISE = "/v1/auth/authorise";

		/** The Constant ROLES_GET. */
		public static final String ROLES_GET = "/v1/roles/get";

		/** The Constant ROLES_SAVE. */
		public static final String ROLES_SAVE = "/v1/roles/save";

		/** The Constant ROLES_ALLOCATE. */
		public static final String ROLES_ALLOCATE = "/v1/roles/access/allocate";

		/** The Constant ROLES_UPDATE. */
		public static final String ROLES_UPDATE = "/v1/roles/access/update";

		/** The Constant ROLES_REVOKE. */
		public static final String ROLES_REVOKE = "/v1/roles/access/revoke";

		/** The Constant UAC_UPDATE. */
		public static final String UAC_UPDATE = "/v1/user/account/update";

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
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(String employeeNo, String identity,
			String ipAddress);

	/**
	 * Authorise user.
	 *
	 * @param employeeNo the employee no	
	 * @param mOtpHash the m otp hash
	 * @param eOtpHash the e otp hash
	 * @param ipAddress the ip address
	 * @return the amx api response
	 */
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(String employeeNo, String mOtpHash, String eOtpHash,
			String ipAddress);

}
