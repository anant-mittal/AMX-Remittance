package com.amx.jax.sso;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.AmxApiError;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.sso.SSOStatus.ApiSSOStatus;
import com.amx.jax.sso.SSOStatus.SSOServerCodes;
import com.amx.jax.swagger.IStatusCodeListPlugin;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class SSOStatus extends IStatusCodeListPlugin<SSOServerCodes, ApiSSOStatus> {

	/**
	 * Error Enum Codes for Offsite Server
	 * 
	 * @author lalittanwar
	 *
	 */
	public static enum SSOServerCodes implements IExceptionEnum {
		AUTH_REQUIRED, OTP_REQUIRED, DOTP_REQUIRED, MOTP_REQUIRED, EOTP_REQUIRED, AUTH_DONE,

		NO_TERMINAL_SESSION, NO_TERMINAL_CARD, ERROR_TERMINAL_CARD_DATA;

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
	public @interface ApiSSOStatus {
		SSOServerCodes[] value() default { SSOServerCodes.AUTH_REQUIRED };
	}

	/**
	 * 
	 * @author lalittanwar
	 *
	 */
	public static class SSOServerError extends AmxApiException {

		private static final long serialVersionUID = 1L;

		public SSOServerError(AmxApiError error) {
			super(error);
		}

		public SSOServerError() {
			super("Offsite Server error occured");
			this.setError(SSOServerCodes.AUTH_REQUIRED);
		}

		public SSOServerError(Exception e) {
			super(e);
			this.setError(SSOServerCodes.AUTH_REQUIRED);
		}

		public SSOServerError(SSOServerCodes error, String errorMessage) {
			super(error, errorMessage);
		}

		@Override
		public AmxApiException getInstance(AmxApiError apiError) {
			return new SSOServerError(apiError);
		}

		@Override
		public IExceptionEnum getErrorIdEnum(String errorId) {
			return SSOServerCodes.AUTH_REQUIRED;
		}

		public static <T> T evaluate(Exception e) {
			if (e instanceof SSOServerError) {
				throw (SSOServerError) e;
			} else {
				throw new SSOServerError(e);
			}
		}

		@Override
		public boolean isReportable() {
			return true;
		}

	}

	@Override
	public Class<ApiSSOStatus> getAnnotionClass() {
		return ApiSSOStatus.class;
	}

	@Override
	public SSOServerCodes[] getValues(ApiSSOStatus annotation) {
		return annotation.value();
	}

}
