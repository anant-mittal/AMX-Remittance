package com.amx.jax.swagger;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.AmxStatus;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.swagger.ApiStatusBuilder.ApiStatus;
import com.google.common.base.Optional;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class ApiStatusBuilder extends IStatusCodeListPlugin<AmxStatus, ApiStatus> {

	@Target({ ElementType.METHOD, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ApiStatus {

		AmxStatus[] value() default { AmxStatus.SUCCESS };

		Class<? extends IExceptionEnum> enumClass() default AmxStatus.class;
	}

	@Override
	public Class<? extends Annotation> getAnnotionClass() {
		return ApiStatus.class;
	}

	@Override
	public AmxStatus[] getValues(Optional<ApiStatus> annotation) {
		return annotation.get().value();
	}

}
