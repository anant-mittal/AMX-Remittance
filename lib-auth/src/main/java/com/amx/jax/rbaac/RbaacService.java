package com.amx.jax.rbaac;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.dto.SendOtpModel;

/**
 * @author abhijeet
 *
 */
public interface RbaacService {

	public static class ApiEndPoints {
		
		public static final String INIT_AUTH = "/v1/auth/init_auth/";
		public static final String AUTHORISE = "/v1/auth/authorise/";
		
		public static final String ROLES_GET = "/v1/roles/get/";
		public static final String ROLES_SAVE = "/v1/roles/save/";
		
		public static final String ROLES_ALLOCATE = "/v1/roles/access/allocate/";
		public static final String ROLES_UPDATE = "/v1/roles/access/update/";
		public static final String ROLES_REVOKE = "/v1/roles/access/revoke/";
		
		public static final String UAC_UPDATE = "/v1/user/account/update/";
		
	}
	
	
	/**
	 * Begins process of user Authentication with employee details - empCode, CivilId and Access Terminal IP 
	 * @param empCode
	 * @param identity
	 * @param ipaddress
	 * @return
	 */
	public AmxApiResponse<SendOtpModel, Object> initAuthForEmployee(String empCode, String identity, String ipAddress);
	
}
