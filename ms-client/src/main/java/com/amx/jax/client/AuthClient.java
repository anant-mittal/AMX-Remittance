package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.SAVE_PERM_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.SAVE_ROLE_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.SAVE_USER_PERM_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.SAVE_USER_ROLE_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.SYNC_ENUMS_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.VALIDATE_AUTH_USER_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.VALIDATE_USER_DETAIL_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.meta.model.EmployeeDetailsDTO;
import com.amx.amxlib.model.SendOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.util.ConverterUtility;
import com.amx.jax.rest.RestService;

@Component
public class AuthClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = Logger.getLogger(AuthClient.class);

	@Autowired
	private ConverterUtility util;

	@Autowired
	private RestService restService;

	/**
	 * @param save enums
	 * @return
	 */
	public ApiResponse syncPermsMeta() {
		try {
			MultiValueMap<String, String> headers = getHeader();
			LOGGER.info("save enums to database");
			String url = this.getBaseUrl() + SYNC_ENUMS_ENDPOINT;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<Boolean>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in syncPermsMeta : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	/**
	 * @param validate user and generate otp
	 * @return
	 */
	public ApiResponse validateUserGenerateOTP(String empCode,String identity) {
		try {
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			if (empCode != null || identity != null ) {
				sb.append("?");
			}
			if (empCode != null) {
				sb.append("empCode=").append(empCode).append("&");
			}
			if (identity != null) {
				sb.append("identity=").append(identity);
			}
			LOGGER.info("validate user and generate otp");
			String url = this.getBaseUrl() + VALIDATE_AUTH_USER_ENDPOINT + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<SendOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateUserGenerateOTP : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	/**
	 * @param validate user and validate otp
	 * @return
	 */
	public ApiResponse validateUserAndOTP(String empCode,String identity,String mOtp) {
		try {
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			if (empCode != null || identity != null || mOtp != null) {
				sb.append("?");
			}
			if (empCode != null) {
				sb.append("empCode=").append(empCode).append("&");
			}
			if (identity != null) {
				sb.append("identity=").append(identity).append("&");
			}
			if (mOtp != null) {
				sb.append("mOtp=").append(mOtp);
			}
			LOGGER.info("validate user and validate otp");
			String url = this.getBaseUrl() + VALIDATE_USER_DETAIL_ENDPOINT + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<EmployeeDetailsDTO>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateUserAndOTP : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	/**
	 * @param save Role Master
	 * @return
	 */
	public ApiResponse saveRole(String roleTitle) {
		try {
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			sb.append("?roleTitle=").append(roleTitle);
			LOGGER.info("save Role Master");
			String url = this.getBaseUrl() + SAVE_ROLE_ENDPOINT +sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<Boolean>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveRole : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}

	/**
	 * @param Assign perms to Role
	 * @return
	 */
	public ApiResponse savePermissionAndRole(String roleId,String permission,String permScope) {
		try {
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			if (roleId != null || permission != null || permScope != null) {
				sb.append("?");
			}
			if (roleId != null) {
				sb.append("roleId=").append(roleId).append("&");
			}
			if (permission != null) {
				sb.append("permission=").append(permission).append("&");
			}
			if (permScope != null) {
				sb.append("permScope=").append(permScope);
			}
			LOGGER.info("save Assign perms to Role");
			String url = this.getBaseUrl() + SAVE_PERM_ENDPOINT;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<Boolean>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in savePermissionAndRole : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	/**
	 * @param Assign Role to user
	 * @return
	 */
	public ApiResponse saveRoleAndUser(String roleId,String userId) {
		try {
			MultiValueMap<String, String> headers = getHeader();
			
			StringBuffer sb = new StringBuffer();
			if (roleId != null || userId != null) {
				sb.append("?");
			}
			if (roleId != null) {
				sb.append("roleId=").append(roleId).append("&");
			}
			if (userId != null) {
				sb.append("userId=").append(userId);
			}
			
			LOGGER.info("save Assign Role to user");
			String url = this.getBaseUrl() + SAVE_USER_ROLE_ENDPOINT + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<Boolean>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in saveRoleAndUser : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	/**
	 * @param Fetch User permissions
	 * @return
	 */
	public ApiResponse fetchUserPermission(String userId) {
		try {
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			sb.append("?userId=").append(userId);
			LOGGER.info("Fetch User permissions");
			String url = this.getBaseUrl() + SAVE_USER_PERM_ENDPOINT + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			return restService.ajax(url).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<EmployeeDetailsDTO>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in fetchUserPermission : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
}
