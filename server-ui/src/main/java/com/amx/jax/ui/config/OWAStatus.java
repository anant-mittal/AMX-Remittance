package com.amx.jax.ui.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.swagger.IStatusCodeListPlugin;
import com.amx.jax.ui.config.OWAStatus.ApiOWAStatus;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class OWAStatus extends IStatusCodeListPlugin<OWAStatusStatusCodes, ApiOWAStatus> {

	/**
	 * Error Enum Codes for Offsite Server
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum OWAStatusStatusCodes implements IExceptionEnum {

		UI_SERVER_ERROR("000"),
		/** The already active. */
		// Registration - CIVIL ID validation
		ALREADY_ACTIVE("302"),
		DEVICE_LOCKED("302"),
		/** The invalid id. */
		INVALID_ID("200"),
		/** The otp sent. */
		OTP_SENT("200"),

		/** The active session. */
		// Registration - OTP validation
		ACTIVE_SESSION("302"),
		/** The verify success. */
		VERIFY_SUCCESS("200"),
		/** The verify failed. */
		VERIFY_FAILED("401"),

		/** The user update init. */
		// User Updates
		USER_UPDATE_INIT("200"),
		/** The user update success. */
		USER_UPDATE_SUCCESS("200"),
		/** The logout done. */
		LOGOUT_DONE("200"),
		/** The user update failed. */
		USER_UPDATE_FAILED("300"),

		/** The auth failed. */
		// Auth Reponses
		AUTH_FAILED("302"),
		/** The auth ok. */
		AUTH_OK("200"),
		/** The auth done. */
		AUTH_DONE("200"),
		/** The auth blocked temp. */
		AUTH_BLOCKED_TEMP("200"),
		/** The auth blocked. */
		AUTH_BLOCKED("200"),
		/** The unauthorized. */
		UNAUTHORIZED("401"),

		/** The dotp required. */
		// Info Required
		DOTP_REQUIRED("300"),
		MOTP_REQUIRED("300"),
		OTP_REQUIRED("300"),

		INCOME_UPDATE_REQUIRED("200"),

		/** The unknown jax error. */
		UNKNOWN_JAX_ERROR("500"),

		/** The bad input. */
		// Registration - END ERROR
		BAD_INPUT("400"),
		/** The success. */
		SUCCESS("200"),
		/** The error. */
		ERROR("500"),

		/** The redirection. */
		REDIRECTION("3xx"),
		/** The server error. */
		SERVER_ERROR("4xx"),
		/** The client error. */
		CLIENT_ERROR("5xx");

		/** The code. */
		private final String code;

		/**
		 * Gets the code.
		 *
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * Instantiates a new web response status.
		 *
		 * @param code the code
		 */
		OWAStatusStatusCodes(String code) {
			this.code = code;
		}

		OWAStatusStatusCodes() {
			this.code = "300";
		}

		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return this.ordinal();
		}

	}

	/**
	 * 
	 * API Status Enum for Offiste Server APis
	 * 
	 * @author lalittanwar
	 *
	 */
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ApiOWAStatus {
		OWAStatusStatusCodes[] value() default { OWAStatusStatusCodes.UI_SERVER_ERROR };
	}

	/**
	 * 
	 * @author lalittanwar
	 *
	 */
	public static class UIServerError extends AmxApiException {

		private static final long serialVersionUID = 1L;

		private boolean reportable = false;

		public UIServerError(AmxApiError error) {
			super(error);
		}

		public UIServerError() {
			super("UI Server error occured");
			this.setError(OWAStatusStatusCodes.UI_SERVER_ERROR);
		}

		public UIServerError(Exception e) {
			super(e);
			reportable = true;
			this.setError(OWAStatusStatusCodes.UI_SERVER_ERROR);
		}

		public UIServerError(OWAStatusStatusCodes error) {
			super("UI Server error occured");
			this.setError(error);
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new UIServerError(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return OWAStatusStatusCodes.valueOf(errorId);
		}

		public static <T> T evaluate(Exception e) {
			if (e instanceof JaxSystemError) {
				throw (JaxSystemError) e;
			} else if (e instanceof AbstractJaxException) {
				throw (AbstractJaxException) e;
			} else {
				throw new UIServerError(e);
			}
		}

		@Override
		public boolean isReportable() {
			return reportable;
		}

	}

	@Override
	public Class<ApiOWAStatus> getAnnotionClass() {
		return ApiOWAStatus.class;
	}

	@Override
	public OWAStatusStatusCodes[] getValues(ApiOWAStatus annotation) {
		return annotation.value();
	}

}
