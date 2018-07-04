package com.amx.jax.auth.client;

import static com.amx.jax.auth.AuthConstants.ApiEndPoints.ROLE;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.ROLE_PERM;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.USER_PERMS;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.USER_ROLE;
import static com.amx.jax.auth.AuthConstants.ApiEndPoints.USER_VALID;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.SendOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.AppConfig;
import com.amx.jax.auth.AuthConstants;
import com.amx.jax.auth.dto.EmployeeDetailsDTO;
import com.amx.jax.auth.models.PermScope;
import com.amx.jax.auth.models.Permission;
import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.rest.RestService;

@Component
public class AuthClient extends AbstractJaxServiceClient {

	private static final Logger LOGGER = LoggerService.getLogger(AuthClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	// sync enum data with database
	public boolean syncPermsMeta() throws PostManException {
		return restService.ajax(appConfig.getAuthURL()).path(AuthConstants.ApiEndPoints.SYNC_PERMS).post()
				.as(Boolean.class);
	}
	
	// generate otp with employee details matching
	public ApiResponse<SendOtpModel> validateUser(String empCode, String identity, String ipaddress){
		try {
			LOGGER.info("calling validateUser api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateUserUrl = this.getBaseUrl() + USER_VALID;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateUserUrl).queryParam("empCode", empCode).queryParam("identity", identity).queryParam("ipaddress", ipaddress);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<SendOtpModel>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateUser : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	public ApiResponse<EmployeeDetailsDTO> authUser(String empCode, String identity, String mOtp, String ipaddress) {
		try {
			LOGGER.info("calling authUser api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateUserUrl = this.getBaseUrl() + USER_VALID;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateUserUrl).queryParam("empCode", empCode).queryParam("identity", identity).queryParam("mOtp", mOtp).queryParam("ipaddress", ipaddress);
			return restService.ajax(builder.build().encode().toUri()).get(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<EmployeeDetailsDTO>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in authUser : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	public ApiResponse<BooleanResponse> createRole(String roleTitle) {
		try {
			LOGGER.info("calling createRole api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + ROLE ;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("roleTitle", roleTitle);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in createRole : ", e);
			throw new JaxSystemError(e);
		}
	}
	
	public ApiResponse<BooleanResponse> assinPerm(BigDecimal roleId, Permission permission, PermScope permScope,String admin) {
		try {
			LOGGER.info("calling assinPerm api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + ROLE_PERM ;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("roleId", roleId).queryParam("permission", permission).queryParam("permScope", permScope).queryParam("admin", admin);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in assinPerm : ", e);
			throw new JaxSystemError(e);
		}
	}
	
	public ApiResponse<BooleanResponse> assinRole(BigDecimal roleId, BigDecimal userId) {
		try {
			LOGGER.info("calling assinRole api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + USER_ROLE ;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("roleId", roleId).queryParam("userId", userId);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in assinRole : ", e);
			throw new JaxSystemError(e);
		}
	}
	
	public ApiResponse<BooleanResponse> getUserPerms(BigDecimal userId) {
		try {
			LOGGER.info("calling getUserPerms api: ");
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + USER_PERMS ;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("userId", userId);
			return restService.ajax(builder.build().encode().toUri()).post(requestEntity)
					.as(new ParameterizedTypeReference<ApiResponse<BooleanResponse>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getUserPerms : ", e);
			throw new JaxSystemError(e);
		}
	}
}
