package com.amx.jax.swagger;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.AmxStatus;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.swagger.ApiStatusBuilder.ApiStatus;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class ApiStatusBuilder extends IStatusCodeListPlugin<AmxStatus, ApiStatus> {

	@ApiStatusService
	public @interface ApiStatus {
		AmxStatus[] value() default { AmxStatus.SUCCESS };

		Class<? extends IExceptionEnum> enumClass() default AmxStatus.class;
	}

	@Override
	public Class<ApiStatus> getAnnotionClass() {
		return ApiStatus.class;
	}

	@Override
	public AmxStatus[] getValues(ApiStatus annotation) {
		return annotation.value();
	}

}
