package com.amx.jax.branch.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.jax.branch.common.OffsiteStatus.ApiOffisteStatus;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.swagger.IStatusCodeListPlugin;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class OffsiteStatus extends IStatusCodeListPlugin<OffsiteServerCodes, ApiOffisteStatus> {

	/**
	 * Error Enum Codes for Offsite Server
	 * 
	 * @author lalittanwar
	 *
	 */
	public static enum OffsiteServerCodes implements IExceptionEnum {
		CLIENT_UNKNOWN,

		TERMINAL_UNKNOWN,

		CLIENT_CREDS_MISSING, INVALID_CLIENT_SESSION, INVALID_CLIENT_REQUEST,

		OFFSITE_SERVER_ERROR, DOTP_REQUIRED, MOTP_REQUIRED, EOTP_REQUIRED;

		@Override
		public String getStatusKey() {
			return this.toString();
		}

		@Override
		public int getStatusCode() {
			return 8000 + this.ordinal();
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
	public @interface ApiOffisteStatus {
		OffsiteServerCodes[] value() default { OffsiteServerCodes.OFFSITE_SERVER_ERROR };
	}

	/**
	 * 
	 * @author lalittanwar
	 *
	 */
	public static class OffsiteServerError extends AmxApiException {

		private static final long serialVersionUID = 1L;

		public OffsiteServerError(AmxApiError error) {
			super(error);
		}

		public OffsiteServerError() {
			super("Offsite Server error occured");
			this.setError(OffsiteServerCodes.OFFSITE_SERVER_ERROR);
		}

		public OffsiteServerError(OffsiteServerCodes statusCode) {
			super(statusCode);
		}

		public OffsiteServerError(OffsiteServerCodes statusCode, String message) {
			super(statusCode, message);
		}

		public OffsiteServerError(Exception e) {
			super(e);
			this.setError(OffsiteServerCodes.OFFSITE_SERVER_ERROR);
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new OffsiteServerError(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return OffsiteServerCodes.OFFSITE_SERVER_ERROR;
		}

		public static <T> T evaluate(Exception e) {
			if (e instanceof JaxSystemError) {
				throw (JaxSystemError) e;
			} else if (e instanceof AbstractJaxException) {
				throw (AbstractJaxException) e;
			} else {
				throw new OffsiteServerError(e);
			}
		}

		@Override
		public boolean isReportable() {
			return false;
		}

	}

	@Override
	public Class<ApiOffisteStatus> getAnnotionClass() {
		return ApiOffisteStatus.class;
	}

	@Override
	public OffsiteServerCodes[] getValues(ApiOffisteStatus annotation) {
		return annotation.value();
	}

}
