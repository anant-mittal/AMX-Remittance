package com.amx.jax.rbaac.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.rbaac.error.RbaacApiStatusBuilder.RbaacApiStatus;
import com.amx.jax.swagger.IStatusCodeListPlugin;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 500)
public class RbaacApiStatusBuilder extends IStatusCodeListPlugin<RbaacServiceError, RbaacApiStatus> {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RbaacApiStatus {
		RbaacServiceError[] value() default { RbaacServiceError.UNKNOWN_EXCEPTION };
	}

	@Override
	public Class<RbaacApiStatus> getAnnotionClass() {
		return RbaacApiStatus.class;
	}

	@Override
	public RbaacServiceError[] getValues(RbaacApiStatus annotation) {
		return annotation.value();
	}

}
