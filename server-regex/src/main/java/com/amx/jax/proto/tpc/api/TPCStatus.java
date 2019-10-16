package com.amx.jax.proto.tpc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.proto.tpc.api.TPCStatus.ApiTPCStatus;
import com.amx.jax.proto.tpc.api.TPCStatus.TPCServerCodes;
import com.amx.jax.swagger.IStatusCodeListPlugin;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class TPCStatus extends IStatusCodeListPlugin<TPCServerCodes, ApiTPCStatus> {

	/**
	 * Error Enum Codes for Offsite Server
	 * 
	 * @author lalittanwar
	 *
	 */
	public static enum TPCServerCodes implements IExceptionEnum {
		INVALID_CLIENT_CREDS, INVALID_SESSION_TOKEN, INVALID_CUSTOMER_TOKEN,

		NO_DATA_FOUND, UNKNOWN_ERROR;

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
	public @interface ApiTPCStatus {
		TPCServerCodes[] value() default { TPCServerCodes.UNKNOWN_ERROR };
	}

	/**
	 * 
	 * @author lalittanwar
	 *
	 */
	public static class TPCServerError extends AmxApiException {

		private static final long serialVersionUID = 1L;

		public TPCServerError(AmxApiError error) {
			super(error);
		}

		public TPCServerError() {
			super("Offsite Server error occured");
			this.setError(TPCServerCodes.UNKNOWN_ERROR);
		}

		public TPCServerError(TPCServerCodes statusCode) {
			super(statusCode);
		}

		public TPCServerError(TPCServerCodes statusCode, String message) {
			super(statusCode, message);
		}

		public TPCServerError(Exception e) {
			super(e);
			this.setError(TPCServerCodes.UNKNOWN_ERROR);
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new TPCServerError(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return TPCServerCodes.UNKNOWN_ERROR;
		}

		public static <T> T evaluate(Exception e) {
			throw new TPCServerError(e);
		}

		@Override
		public boolean isReportable() {
			return true;
		}

	}

	@Override
	public Class<ApiTPCStatus> getAnnotionClass() {
		return ApiTPCStatus.class;
	}

	@Override
	public TPCServerCodes[] getValues(ApiTPCStatus annotation) {
		return annotation.value();
	}

}
